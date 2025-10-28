## Manages player state transitions using the State pattern.
## Coordinates between different states (Idle, Walk, Attack) by calling
## their lifecycle methods and handling transitions.
class_name PlayerStateMachine extends Node

var state_list: Array[Node] = []
var previous_state: Node = null
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

## Initializes the state machine with the player reference.
## Discovers child state nodes, attaches player to each, and starts first state.
func initialize(player: CharacterBody2D) -> void:
	if not player:
		push_error("PlayerStateMachine.initialize() called with null player")
		return

	if current_state:
		return

	state_list.clear()
	for child in get_children():
		if child.has_method("enter"):
			state_list.append(child)

	if state_list.is_empty():
		push_error("PlayerStateMachine has no child State nodes")
		return

	for state in state_list:
		state.player = player
		state.state_machine = self

	change_state(state_list[0])
	process_mode = Node.PROCESS_MODE_INHERIT

## Transitions to a new state with validation.
## Calls exit() on current state, then enter() on new state.
func change_state(new_state: Node) -> void:
	if not new_state:
		push_error("Attempted to change to null state")
		return

	if not new_state in state_list:
		push_error("State %s is not registered in state_list" % new_state.name)
		return

	if new_state == current_state:
		return

	if current_state:
		current_state.exit()

	previous_state = current_state
	current_state = new_state
	current_state.enter()

## Returns state instance by name for state transitions.
func get_state(state_name: String) -> Node:
	for state in state_list:
		if state.name == state_name:
			return state
	return null
