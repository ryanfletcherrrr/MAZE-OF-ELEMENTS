extends "res://Enemies/Scripts/States/slime_state.gd"

func enter() -> void:
	if not enemy:
		return
	enemy.player_chase = false
	enemy.velocity = enemy.velocity.lerp(Vector2.ZERO, 1.0)
	enemy.play_idle_animation()

func physics_update(delta: float):
	if not enemy:
		return null
	if not state_machine:
		return null
	enemy.velocity = enemy.velocity.lerp(Vector2.ZERO, 8.0 * delta)
	if enemy.velocity.length() > 0.01:
		enemy.move_and_slide()

	if enemy.has_target() and enemy.distance_to_player() <= enemy.follow_distance:
		return state_machine.get_state("Chase")
	return null
