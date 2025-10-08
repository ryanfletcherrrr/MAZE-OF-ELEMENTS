extends Node
class_name SlimeStateMachine

var enemy: Slime = null
var states: Array[Node] = []
var current_state: Node = null
var previous_state: Node = null

func _ready() -> void:
	process_mode = Node.PROCESS_MODE_DISABLED

func initialize(slime_owner: Slime) -> void:
	enemy = slime_owner
	states.clear()
	for child in get_children():
		if child.has_method("enter"):
			states.append(child)

	if states.is_empty():
		push_warning("SlimeStateMachine has no state children")
		return

	for state in states:
		state.enemy = enemy
		state.state_machine = self

	change_state(states[0])
	process_mode = Node.PROCESS_MODE_INHERIT

func _process(delta: float) -> void:
	if not current_state:
		return
	var next_state = current_state.update(delta)
	if next_state and next_state != current_state:
		change_state(next_state)

func _physics_process(delta: float) -> void:
	if not current_state:
		return
	var next_state = current_state.physics_update(delta)
	if next_state and next_state != current_state:
		change_state(next_state)

func change_state(new_state: Node) -> void:
	if not new_state:
		push_warning("Attempted to change to null state")
		return
	if new_state == current_state:
		return

	if current_state and current_state.has_method("exit"):
		current_state.exit()

	previous_state = current_state
	current_state = new_state
	if current_state and current_state.has_method("enter"):
		current_state.enter()

func get_state(state_name: String) -> Node:
	for state in states:
		if state.name == state_name:
			return state
	return null

func change_state_by_name(state_name: String) -> void:
	var target = get_state(state_name)
	if target:
		change_state(target)
