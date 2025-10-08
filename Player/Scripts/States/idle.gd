extends Node

var animator: AnimatedSprite2D = null
var attack_triggered: bool = false

# Reference to the player owning this state
var player: CharacterBody2D = null

# Back-reference to the state machine for convenience
var state_machine: Node = null

func enter() -> void:
	player.direction = Vector2.ZERO

	if not animator:
		animator = player.get_node_or_null("AnimatedSprite2D")

	if animator:
		var anim = "idle_down"

		if player.last_direction == Vector2.UP:
			anim = "idle_up"
		elif player.last_direction == Vector2.DOWN:
			anim = "idle_down"
		elif player.last_direction == Vector2.LEFT:
			anim = "idle_left"
		elif player.last_direction == Vector2.RIGHT:
			anim = "idle_right"

		if animator.animation == "":
			animator.play("attack_down")

		if animator.animation != anim:
			animator.play(anim)

func exit() -> void:
	attack_triggered = false

func handle_input(event: InputEvent):
	if event is InputEventKey and event.pressed:
		if Input.is_action_pressed("ui_left") or Input.is_action_pressed("ui_right") or \
		   Input.is_action_pressed("ui_up") or Input.is_action_pressed("ui_down"):
			return state_machine.get_state("Walk")
	return null

func physics_update(_delta: float):
	# Check for attack input (X key just pressed)
	if Input.is_key_pressed(KEY_X) and not attack_triggered:
		attack_triggered = true
		return state_machine.get_state("Attack")

	var horizontal = Input.get_axis("ui_left", "ui_right")
	var vertical = Input.get_axis("ui_up", "ui_down")

	if horizontal != 0 or vertical != 0:
		return state_machine.get_state("Walk")

	if animator:
		var anim = "idle_down"

		if player.last_direction == Vector2.UP:
			anim = "idle_up"
		elif player.last_direction == Vector2.DOWN:
			anim = "idle_down"
		elif player.last_direction == Vector2.LEFT:
			anim = "idle_left"
		elif player.last_direction == Vector2.RIGHT:
			anim = "idle_right"

		if animator.animation != anim:
			animator.play(anim)

	return null

func update(_delta: float):
	return null
