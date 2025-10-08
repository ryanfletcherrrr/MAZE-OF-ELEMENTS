extends Node

var animator: AnimatedSprite2D = null
var attack_timer: Timer = null

# Reference to the player owning this state
var player: CharacterBody2D = null

# Back-reference to the state machine for convenience
var state_machine: Node = null

func enter() -> void:
	# Get animator
	animator = player.get_node_or_null("AnimatedSprite2D")

	if animator:
		# Play attack animation
		var anim_name = get_attack_animation(player.last_direction)
		if animator.sprite_frames.has_animation(anim_name):
			animator.play(anim_name)

			# Start timer for attack duration (adjust time as needed)
			attack_timer = Timer.new()
			attack_timer.wait_time = 0.5  # 0.5 seconds attack duration
			attack_timer.one_shot = true
			attack_timer.timeout.connect(_on_attack_finished)
			add_child(attack_timer)
			attack_timer.start()
		else:
			# No animation, go back immediately
			state_machine.change_state(state_machine.get_state("Walk"))

func exit() -> void:
	# Clean up timer
	if attack_timer:
		attack_timer.queue_free()
		attack_timer = null

func _on_attack_finished() -> void:
	# Attack done, go back to walking
	state_machine.change_state(state_machine.get_state("Walk"))

func get_attack_animation(direction: Vector2) -> String:
	if direction == Vector2.UP:
		return "attack_up"
	if direction == Vector2.DOWN:
		return "attack_down"
	if direction == Vector2.LEFT:
		return "attack_left"
	if direction == Vector2.RIGHT:
		return "attack_right"
	return "attack_down"

func update(_delta: float):
	return null

func physics_update(_delta: float):
	return null

func handle_input(_event: InputEvent):
	return null
