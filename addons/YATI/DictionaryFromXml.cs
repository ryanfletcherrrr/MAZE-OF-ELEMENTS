// MIT License
//
// Copyright (c) 2023-2025 Roland Helmerichs
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.

#if TOOLS
using System;
using System.Globalization;
using System.Linq;
using Godot;
using Godot.Collections;
using Array = Godot.Collections.Array;

namespace YATI;

[Tool]
public class DictionaryFromXml
{
    private XmlParserCtrl xmlParser;
    private string currentElement;
    private int currentGroupLevel;
    private readonly Dictionary resultDictionary = new Dictionary();
    private Dictionary currentDictionary;
    private Array currentArray;
    private readonly CultureInfo cultureInfo = (CultureInfo)CultureInfo.CurrentCulture.Clone();
    private bool csvEncoded = true;
    private bool isMap;
    private bool inTileset;

    public Dictionary Create(byte[] tiledFileContent, string sourceFileName)
    {
        cultureInfo.NumberFormat.NumberDecimalSeparator = ".";

        xmlParser = new XmlParserCtrl();

        var err = xmlParser.Open(tiledFileContent, sourceFileName);
        if (err != Error.Ok) return null;

        currentElement = xmlParser.NextElement();
        currentDictionary = resultDictionary;
        var baseAttributes = xmlParser.GetAttributes();

        currentDictionary.Add("type", currentElement);
        InsertAttributes(currentDictionary, baseAttributes);
        isMap = currentElement == "map";

        var baseElement = currentElement;
        var baseGroupLevel = currentGroupLevel;
        while ((err == Error.Ok) && ((!xmlParser.IsEnd() || (currentElement != baseElement) ||
                                      (baseElement == "group" && currentElement == "group" && currentGroupLevel == baseGroupLevel))))
        {
            currentElement = xmlParser.NextElement();
            if (currentElement == null) { err = Error.ParseError; break; }
            if (xmlParser.IsEnd())
            {
                if (currentElement == "group")
                    currentGroupLevel--;
                continue;
            }
            if (currentElement == "group" && !xmlParser.IsEmpty())
                currentGroupLevel++;
            var cAttributes = xmlParser.GetAttributes();
            var dictionaryBookmark = currentDictionary;
            err = xmlParser.IsEmpty() ? SimpleElement(currentElement, cAttributes) : NestedElement(currentElement, cAttributes);
            currentDictionary = dictionaryBookmark;
        }

        if (err == Error.Ok) return resultDictionary;
        GD.PrintErr($"Import aborted with {err} error.");
        return null;
    }

    private Error SimpleElement(string elementName, Dictionary<string, string> attributes)
    {
        switch (elementName)
        {
            case "image":
                currentDictionary.Add("image", attributes["source"]);
                if (attributes.TryGetValue("width", out var value))
                    currentDictionary.Add("imagewidth", int.Parse(value));
                if (attributes.TryGetValue("height", out value))
                    currentDictionary.Add("imageheight", int.Parse(value));
                if (attributes.TryGetValue("trans", out value))
                    currentDictionary.Add("transparentcolor", value);
                return Error.Ok;
            case "wangcolor":
                elementName = "color";
                break;
            case "point":
                currentDictionary.Add("point", true);
                return Error.Ok;
            case "ellipse":
                currentDictionary.Add("ellipse", true);
                return Error.Ok;
        }

        var dictKey = elementName;
        if ((elementName == "objectgroup" && (!isMap || inTileset)) || elementName is "text" or "tileoffset" or "grid")
        {
            // Create a single dictionary, not an array.
            currentDictionary[dictKey] = new Dictionary();
            currentDictionary = (Dictionary)currentDictionary[dictKey];
            if (attributes.Count > 0)
                InsertAttributes(currentDictionary, attributes);
        }
        else switch (dictKey)
        {
            case "polygon" or "polyline":
            {
                var arr = new Array();
                foreach (var pt in attributes["points"].Split(' '))
                {
                    var dict = new Dictionary();
                    var x = float.Parse(pt.Split(',')[0], NumberStyles.Any, cultureInfo);
                    var y = float.Parse(pt.Split(',')[1], NumberStyles.Any, cultureInfo);
                    dict.Add("x", x);
                    dict.Add("y", y);
                    arr.Add(dict);
                }
                currentDictionary.Add(dictKey,arr);
                break;
            }
            case "frame" or "property":
            {
                // i.e. will be part of the superior array (animation or properties)
                var dict = new Dictionary();
                InsertAttributes(dict, attributes);
                currentArray.Add(dict);
                break;
            }
            default:
            {
                if (new [] {"objectgroup", "imagelayer"}.Contains(dictKey))
                {
                    // to be later added to the layer attributes (by InsertAttributes)
                    attributes.Add("type", dictKey);
                    dictKey = "layer";
                }

                if (dictKey == "group")
                {
                    // Add nested "layers" array
                    attributes.Add("type", "group");
                    if (currentDictionary.TryGetValue("layers", out var layersVal))
                        currentArray = (Array)layersVal;
                    else
                    {
                        currentArray = new Array();
                        currentDictionary["layers"] = currentArray;
                    }
                    dictKey = "layer";
                }

                if ((dictKey != "animation") && (dictKey != "properties"))
                    dictKey += "s";
                if (currentDictionary.TryGetValue(dictKey, out var dictVal))
                {
                    currentArray = (Array)dictVal;
                }
                else
                {
                    currentArray = new Array();
                    currentDictionary[dictKey] = currentArray;
                }

                if ((dictKey != "animation") && (dictKey != "properties"))
                {
                    currentDictionary = new Dictionary();
                    currentArray.Add(currentDictionary);
                }

                if (dictKey == "wangtiles")
                {
                    currentDictionary.Add("tileid", int.Parse(attributes["tileid"]));
                    var arr = new Array();
                    foreach (var s in attributes["wangid"].Split(','))
                        arr.Add(int.Parse(s));
                    currentDictionary.Add("wangid", arr);
                }
                else if (attributes.Count > 0)
                    InsertAttributes(currentDictionary, attributes);

                break;
            }
        }

        return Error.Ok;
    }

    private Error NestedElement(string elementName, Dictionary<string, string> attributes)
    {
        switch (elementName)
        {
            case "wangsets":
                return Error.Ok;
            case "data":
                currentDictionary.Add("type", "tilelayer");
                if (attributes.TryGetValue("encoding", out var encoding))
                {
                    currentDictionary.Add("encoding", encoding);
                    csvEncoded = attributes["encoding"] == "csv";
                }
                if (attributes.TryGetValue("compression", out var attribute))
                    currentDictionary.Add("compression", attribute);

                return Error.Ok;
            case "tileset":
                inTileset = true;
                break;
        }

        var dictionaryBookmark1 = currentDictionary;
        var arrayBookmark1 = currentArray;
        var err = SimpleElement(elementName, attributes);
        var baseElement = currentElement;
        var baseGroupLevel = currentGroupLevel;
        while ((err == Error.Ok) && ((!xmlParser.IsEnd() || (currentElement != baseElement) ||
                                      (baseElement == "group" && currentElement == "group" && currentGroupLevel == baseGroupLevel))))
        {
            currentElement = xmlParser.NextElement();
            if (currentElement == null) return Error.ParseError;
            if (xmlParser.IsEnd())
            {
                if (currentElement == "group")
                    currentGroupLevel--;
                continue;
            }
            switch (currentElement)
            {
                case "group" when !xmlParser.IsEmpty():
                    currentGroupLevel++;
                    break;
                case "<data>":
                {
                    Variant data = xmlParser.GetData();
                    switch (baseElement)
                    {
                        case "text":
                            currentDictionary.Add("text", ((string)data).Replace("\r", ""));
                            break;
                        case "property":
                            ((Dictionary)currentArray[^1]).Add("value", ((string)data).Replace("\r", ""));
                            break;
                        default:
                        {
                            data = ((string)data).Trim();
                            if (csvEncoded)
                            {
                                var arr = new Array();
                                foreach (var s in ((string)data).Split(',',StringSplitOptions.TrimEntries))
                                    arr.Add(uint.Parse(s));
                                data = arr;
                            }
                            ((Dictionary)currentArray[^1]).Add("data", data);
                            break;
                        }
                    }
                    continue;
                }
            }

            var cAttributes = xmlParser.GetAttributes();
            var dictionaryBookmark2 = currentDictionary;
            var arrayBookmark2 = currentArray;
            err = xmlParser.IsEmpty() ? SimpleElement(currentElement, cAttributes) : NestedElement(currentElement, cAttributes);
            currentDictionary = dictionaryBookmark2;
            currentArray = arrayBookmark2;
        }

        currentDictionary = dictionaryBookmark1;
        currentArray = arrayBookmark1;
        if (baseElement == "tileset")
            inTileset = false;
        return err;
    }

    private void InsertAttributes(Dictionary targetDictionary, Dictionary<string, string> attributes)
    {
        foreach (var (key, value) in attributes)
        {
            var val = key switch
            {
                "infinite" => value == "1",
                "visible" => value == "1",
                "wrap" => value == "1",
                _ => (Variant)value
            };

            if (!key.Contains("version"))
            {
                if (int.TryParse((string)val, out var iTmp))
                    val = iTmp;
                else if (uint.TryParse((string)val, out var uiTmp))
                    val = uiTmp;
                else if (float.TryParse((string)val, NumberStyles.Float, cultureInfo, out var fTmp))
                    val = fTmp;
            }
            targetDictionary.Add(key, val);
        }
    }
}
#endif
