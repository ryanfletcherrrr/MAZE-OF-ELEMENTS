using Godot;

public partial class MapBoundaryManager : Node2D
{
    [Export] public Node2D mapScene; // the whole map scene
    [Export] public CharacterBody2D player; // reference to the player
    [Export] public string tileMapLayerName = ""; // name of the specific layer to use (leave empty to use first/biggest)
    [Export] public int tilePadding = 0; // extra tiles of padding if needed

    private TileMapLayer currentMap;
    private Camera2D playerCamera;

    public override void _Ready()
    {
        // get camera from player's children
        if (player != null)
        {
            playerCamera = player.GetNode<Camera2D>("PlayerCamera");
            if (playerCamera == null)
            {
                // try finding any camera in player's children
                foreach (Node child in player.GetChildren())
                {
                    if (child is Camera2D cam)
                    {
                        playerCamera = cam;
                        break;
                    }
                }
            }
        }

        if (mapScene != null)
        {
            FindAndSetMap(mapScene);
        }
    }

    private void FindAndSetMap(Node2D scene)
    {
        // if a specific layer name is given, find that one
        if (!string.IsNullOrEmpty(tileMapLayerName))
        {
            foreach (Node child in scene.GetChildren())
            {
                if (child is TileMapLayer layer && child.Name == tileMapLayerName)
                {
                    currentMap = layer;
                    break;
                }
            }
        }
        else
        {
            // otherwise find the biggest layer (most tiles)
            TileMapLayer biggestLayer = null;
            int biggestArea = 0;

            foreach (Node child in scene.GetChildren())
            {
                if (child is TileMapLayer layer)
                {
                    Rect2I usedRect = layer.GetUsedRect();
                    int area = usedRect.Size.X * usedRect.Size.Y;

                    if (area > biggestArea)
                    {
                        biggestArea = area;
                        biggestLayer = layer;
                    }
                }
            }

            currentMap = biggestLayer;
        }

        if (currentMap != null)
        {
            SetMapBoundaries();
            GD.Print($"Using TileMapLayer: {currentMap.Name}");
        }
        else
        {
            GD.PrintErr("No TileMapLayer found in map scene!");
        }
    }

    public void ChangeMap(Node2D newMapScene)
    {
        mapScene = newMapScene;
        FindAndSetMap(newMapScene);
    }

    private void SetMapBoundaries()
    {
        if (currentMap == null)
        {
            GD.PrintErr("currentMap is null!");
            return;
        }

        if (playerCamera == null)
        {
            GD.PrintErr("playerCamera is null! Make sure it's assigned in the Inspector.");
            return;
        }

        // get the actual used area of your tilemap
        Rect2I usedRect = currentMap.GetUsedRect();

        // Handle missing TileSet (common with Tiled imports)
        Vector2I tileSize;
        if (currentMap.TileSet != null)
        {
            tileSize = currentMap.TileSet.TileSize;
        }
        else
        {
            // Default to 8x8 for Tiled imports
            tileSize = new Vector2I(8, 8);
            GD.Print("TileSet is null, using default 8x8 tile size for Tiled import");
        }

        // calculate pixel boundaries
        int minX = usedRect.Position.X * tileSize.X - (tilePadding * tileSize.X);
        int minY = usedRect.Position.Y * tileSize.Y - (tilePadding * tileSize.Y);
        int maxX = (usedRect.Position.X + usedRect.Size.X) * tileSize.X + (tilePadding * tileSize.X);
        int maxY = (usedRect.Position.Y + usedRect.Size.Y) * tileSize.Y + (tilePadding * tileSize.Y);

        GD.Print($"TileMap UsedRect: Pos=({usedRect.Position.X}, {usedRect.Position.Y}), Size=({usedRect.Size.X}, {usedRect.Size.Y})");
        GD.Print($"Tile Size: {tileSize.X}x{tileSize.Y}");

        // set camera limits
        playerCamera.LimitLeft = minX;
        playerCamera.LimitTop = minY;
        playerCamera.LimitRight = maxX;
        playerCamera.LimitBottom = maxY;

        // enable the limits
        playerCamera.LimitSmoothed = true;

        GD.Print($"Map boundaries set: ({minX}, {minY}) to ({maxX}, {maxY})");
        GD.Print($"Camera limits updated: Left={playerCamera.LimitLeft}, Right={playerCamera.LimitRight}");
    }

    // Method for Player to request camera update
    public void UpdatePlayerCamera(CharacterBody2D newPlayer)
    {
        if (newPlayer == null)
        {
            GD.PrintErr("UpdatePlayerCamera: Player is null");
            return;
        }

        // Update player reference
        player = newPlayer;

        // Find the camera in the player
        playerCamera = player.GetNode<Camera2D>("PlayerCamera");
        if (playerCamera == null)
        {
            // Try the exported camera field from Player script
            if (newPlayer is Player playerScript && playerScript.camera != null)
            {
                playerCamera = playerScript.camera;
                GD.Print("MapBoundaryManager: Using Player's exported camera");
            }
            else
            {
                // Try finding any camera in player's children
                foreach (Node child in player.GetChildren())
                {
                    if (child is Camera2D cam)
                    {
                        playerCamera = cam;
                        GD.Print($"MapBoundaryManager: Found camera in player children: {cam.Name}");
                        break;
                    }
                }
            }
        }
        else
        {
            GD.Print("MapBoundaryManager: Found PlayerCamera node");
        }

        if (playerCamera != null && currentMap != null)
        {
            SetMapBoundaries();
        }
        else
        {
            GD.PrintErr($"UpdatePlayerCamera: Missing camera ({playerCamera != null}) or map ({currentMap != null})");
        }
    }
}
