extends Node

# Signals
signal tilemap_bounds_changed(bounds: Array[Vector2])
signal level_load_started
signal level_load_finished

# State
var current_tilemap_bounds: Array[Vector2] = []

# Called when the node enters the scene tree for the first time.
func _ready() -> void:
	print("Global Camera Manager Loaded")

# Camera bounds API
func change_tilemap_bounds(bounds: Array[Vector2]) -> void:
	current_tilemap_bounds = bounds
	tilemap_bounds_changed.emit(bounds)

# Level load lifecycle API (emit these from your scene switching code)
func start_level_load() -> void:
	level_load_started.emit()

func finish_level_load() -> void:
	level_load_finished.emit()
