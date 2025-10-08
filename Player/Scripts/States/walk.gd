extends Node

var animator: AnimatedSprite2D = null
var attack_triggered: bool = false

# Reference to the player owning this state
var player: CharacterBody2D = null

# Back-reference to the state machine for convenience
var state_machine: Node = null

func enter() -> void:
	if not animator:
		animator = player.get_node_or_null("AnimatedSprite2D")

func exit() -> void:
	attack_triggered = false

	if not animator:
		return

	var idle = "idle_down"
	var face = player.last_direction

	if face == Vector2.UP:
		idle = "idle_up"
	elif face == Vector2.DOWN:
		idle = "idle_down"
	elif face == Vector2.LEFT:
		idle = "idle_left"
	elif face == Vector2.RIGHT:
		idle = "idle_right"

	if animator.animation != idle:
		animator.play(idle)

func physics_update(_delta: float):
	# Check for attack input (X key just pressed)
	if Input.is_key_pressed(KEY_X) and not attack_triggered:
		attack_triggered = true
		return state_machine.get_state("Attack")

	var horizontal = Input.get_axis("ui_left", "ui_right")
	var vertical = Input.get_axis("ui_up", "ui_down")

	# Prevent diagonals
	if horizontal != 0 and vertical != 0:
		vertical = 0

	var dir = Vector2(horizontal, vertical)
	player.direction = dir

	if dir != Vector2.ZERO:
		player.last_direction = dir

	if dir == Vector2.ZERO:
		return state_machine.get_state("Idle")

	if animator:
		var anim = "walk_down"

		if dir == Vector2.UP:
			anim = "walk_up"
		elif dir == Vector2.DOWN:
			anim = "walk_down"
		elif dir == Vector2.LEFT:
			anim = "walk_left"
		elif dir == Vector2.RIGHT:
			anim = "walk_right"

		if animator.animation != anim:
			animator.play(anim)

	return null

func update(_delta: float):
	return null

func handle_input(_event: InputEvent):
	return null
