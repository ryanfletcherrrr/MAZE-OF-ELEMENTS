class_name  PlayerCamera extends Camera2D



func _ready():
	# Ensure this camera becomes active and centers on the player
	make_current()
	LevelManager.tilemap_bounds_changed.connect(_update_limits)
	# Apply existing bounds right away if available
	if LevelManager.current_tilemap_bounds.size() >= 2:
		_update_limits(LevelManager.current_tilemap_bounds)
	# Snap to the correct scroll immediately (avoid visible half-map on first frame)
	if has_method("force_update_scroll"):
		force_update_scroll()

func _update_limits( bounds : Array[ Vector2 ] ) -> void:
	if bounds == []:
		return
	limit_left = int( bounds[0].x )
	limit_top = int( bounds[0].y )
	limit_right = int( bounds[1].x )
	limit_bottom = int( bounds[1].y )
	pass
