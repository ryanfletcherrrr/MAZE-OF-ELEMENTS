extends Node
class_name PlayerStateMachine

# Array of States a player can have
var state_list: Array[Node] = []

# Previous state of the player
var previous_state: Node = null

# Current state of the player
var current_state: Node = null

func _ready() -> void:
	process_mode = Node.PROCESS_MODE_DISABLED

func _process(delta: float) -> void:
	if not current_state:
		return

	var next = current_state.update(delta)
	if next and next != current_state:
		change_state(next)

func _physics_process(delta: float) -> void:
	if not current_state:
		return

	var next = current_state.physics_update(delta)
	if next and next != current_state:
		change_state(next)

func _unhandled_input(event: InputEvent) -> void:
	if not current_state:
		return

	var next = current_state.handle_input(event)
	if next and next != current_state:
		change_state(next)

func initialize(player: CharacterBody2D) -> void:
	if not player:
		print("Initialize called with null player")
		return

	if current_state:
		return

	state_list.clear()
	for child in get_children():
		if child.has_method("enter"):  # Duck typing for State
			state_list.append(child)

	if state_list.is_empty():
		print("PlayerStateMachine has no child State nodes.")
		return

	# Attach references to all states
	for s in state_list:
		s.player = player
		s.state_machine = self

	change_state(state_list[0])
	process_mode = Node.PROCESS_MODE_INHERIT  # enable processing

func change_state(new_state: Node) -> void:
	# Check if the states are valid if not exit
	if not new_state:
		print("Attempted to change to null state")
		return

	if new_state == current_state:
		return

	if current_state:
		current_state.exit()

	previous_state = current_state
	current_state = new_state
	current_state.enter()

# Generic helper for states to request another state instance
func get_state(state_name: String) -> Node:
	for state in state_list:
		if state.name == state_name:
			return state
	return null

func get_state_by_type(state_type: String) -> Node:
	for state in state_list:
		if state.get_script().resource_path.get_file().get_basename() == state_type:
			return state
	return null
