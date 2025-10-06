extends Node2D


func _ready() -> void:
	visible = false
	
	# Wait one frame to let all nodes initialize
	await get_tree().process_frame
	
	# Check if we're entering from a level transition or testing a scene directly
	var is_from_transition = false
	
	if LevelManager.target_transition != "" and LevelManager.target_transition != null:
		# Look for a LevelTransition node with the matching name in the current scene
		var current_scene = get_tree().current_scene
		if current_scene:
			var transition_node = current_scene.find_child(LevelManager.target_transition, true, false)
			if transition_node != null:
				is_from_transition = true
				print("Entering from level transition: ", LevelManager.target_transition)
	
	# If not from a valid transition, spawn player here
	if not is_from_transition:
		print("No valid transition found, spawning player at spawn point")
		_spawn_player_here()


func _spawn_player_here() -> void:
	# Clear any transition target to prevent interference
	LevelManager.target_transition = ""
	LevelManager.position_offset = Vector2.ZERO
	
	# Position the player at this spawn point
	PlayerManager.set_player_position(global_position)
	PlayerManager.player_spawned = true
	print("Player spawned at spawn point: ", global_position)