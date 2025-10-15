extends Node

# Author Scara
# Global Level manager to handle level transactions and set player camera limit for each level

# Signals
signal tilemap_bounds_changed(bounds: Array[Vector2])
signal level_load_started
signal level_loaded
signal level_load_finished


# Variables
var current_tilemap_bounds: Array[Vector2] = []
var target_transition : String
var position_offset : Vector2


# Called when the node enters the scene tree for the first time.
func _ready() -> void:
	await get_tree().process_frame
	level_loaded.emit()
	print("Global Camera Manager Loaded")

# Camera bounds API
func change_tilemap_bounds(bounds: Array[Vector2]) -> void:
	current_tilemap_bounds = bounds
	tilemap_bounds_changed.emit(bounds)



func load_new_level(
		level_path : String,
		_target_transition : String,
		_position_offset : Vector2
) -> void:
	get_tree().paused = true
	target_transition = _target_transition
	position_offset = _position_offset

	level_load_started.emit()

	SceneTransition.fade_out()
	await get_tree().process_frame

	get_tree().change_scene_to_file(level_path)
	SceneTransition.fade_in()

	get_tree().paused = false

	await get_tree().process_frame

	level_loaded.emit()
	pass

	# Level load lifecycle API (emit t hese from your scene switching code)
func start_level_load() -> void:
	level_load_started.emit()

func finish_level_load() -> void: 	level_load_finished.emit()
