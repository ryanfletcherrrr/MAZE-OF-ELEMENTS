let createEmptyLayers = tiled.registerAction('CreateEmptyLayers', function(action) {
    const map = tiled.activeAsset

    if (map.isTileMap) {
        map.addLayer(new TileLayer("WalkBehind"));
        map.addLayer(new TileLayer("Grass"));
        map.addLayer(new TileLayer("CollisionMain"));

        map.addLayer(new ObjectGroup("Warp"));
        map.addLayer(new ObjectGroup("TriggerLayer"));
        map.addLayer(new ObjectGroup("LightLayer"));
        map.addLayer(new ObjectGroup("ParticleLayer"));
    }
});

let createEmptyLayersIndoor = tiled.registerAction('CreateEmptyLayersIndoorMap', function(action) {
    const map = tiled.activeAsset

    if (map.isTileMap) {
        map.addLayer(new TileLayer("WalkBehind"));
        map.addLayer(new TileLayer("Grass"));
        map.addLayer(new TileLayer("CollisionMain"));

        map.addLayer(new ObjectGroup("Warp"));
        map.addLayer(new ObjectGroup("TriggerLayer"));
        map.addLayer(new ObjectGroup("ParticleLayer"));

        map.setProperty("indoor", "true");
    }
});

createEmptyLayers.text = "Create Empty Layers";
createEmptyLayersIndoor.text = "Create Empty Indoor Layers";

tiled.extendMenu("Map", [
    { separator: true },
    { action: "CreateEmptyLayers" },
])

tiled.extendMenu("Map", [
    { separator: true },
    { action: "CreateEmptyLayersIndoorMap" },
])

