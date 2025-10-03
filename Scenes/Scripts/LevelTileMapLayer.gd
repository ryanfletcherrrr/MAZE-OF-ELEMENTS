class_name LevelTileMap extends TileMapLayer

# Fallback tile size (pixels). Will auto-detect from TileSet when possible.
@export var tile_size: float = 32
@export var update_bounds: bool = true

# Naming patterns to classify child TileMapLayers into base vs overlay.
# Any child name that matches a base keyword gets z_index=0 (below player),
# overlay keyword gets z_index=2 (above player). Player stays at z_index=1.
@export var base_keywords: Array[String] = [
	"ground", "main", "base", "floor", "grass", "shadow",
	"water", "background", "lighting", "under", "elevated_space",
	"main space", "ground shadow", "grass shadow"
]
@export var overlay_keywords: Array[String] = [
	"column", "columns", "object", "objects", "over", "overlay",
	"canopy", "tops", "details", "decor", "tree"
]

# Called when the node enters the scene tree for the first time.
func _ready():
	if update_bounds:
		# Defer one frame so child TileMapLayers are initialized and used_rect is valid
		call_deferred("_emit_bounds")

# Determine tile pixel size from TileSetAtlasSource if available
func _get_tile_px(ts: TileSet = null) -> Vector2:
	var set_to_use := ts if ts != null else tile_set
	if set_to_use:
		var count := set_to_use.get_source_count()
		for i in range(count):
			var sid = set_to_use.get_source_id(i)
			var src = set_to_use.get_source(sid)
			if src is TileSetAtlasSource:
				return Vector2(src.texture_region_size)
	return Vector2(tile_size, tile_size)

func _emit_bounds() -> void:
	var bounds = _get_tilemap_bounds()
	print("LevelTileMap [", name, "]: Updating bounds to: ", bounds)
	# Emit via LevelManager autoload (present in project.godot)
	LevelManager.change_tilemap_bounds(bounds)

func _get_tilemap_bounds() -> Array[Vector2]:
	var min_x := INF
	var min_y := INF
	var max_x := -INF
	var max_y := -INF
	var found := false

	# Use this layer's rect
	var px := _get_tile_px()
	var used_self := get_used_rect()
	if used_self.size.x > 0 and used_self.size.y > 0:
		var tl = (Vector2(used_self.position) * px) + global_position
		var br = (Vector2(used_self.end) * px) + global_position
		min_x = min(min_x, tl.x); min_y = min(min_y, tl.y)
		max_x = max(max_x, br.x); max_y = max(max_y, br.y)
		found = true

	# Include all child TileMapLayers (actual painted layers)
	for child in get_children():
		if child is TileMapLayer:
			var child_rect = child.get_used_rect()
			if child_rect.size.x > 0 and child_rect.size.y > 0:
				var cpx := _get_tile_px(child.tile_set)
				var ctl = (Vector2(child_rect.position) * cpx) + child.global_position
				var cbr = (Vector2(child_rect.end) * cpx) + child.global_position
				min_x = min(min_x, ctl.x); min_y = min(min_y, ctl.y)
				max_x = max(max_x, cbr.x); max_y = max(max_y, cbr.y)
				found = true

	var result: Array[Vector2] = []
	if found:
		result.append(Vector2(min_x, min_y))
		result.append(Vector2(max_x, max_y))
	else:
		# Safe default small bounds to avoid crashes
		result.append(Vector2(0, 0))
		result.append(Vector2(320, 180))
	return result
