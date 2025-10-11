extends "res://Enemies/Scripts/States/slime_state.gd"

func enter() -> void:
	if not enemy:
		return
	enemy.player_chase = true

func physics_update(delta: float):
	if not enemy:
		return null
	if not state_machine:
		return null

	if not enemy.has_target():
		return state_machine.get_state("Idle")

	var distance: float = enemy.distance_to_player()
	var direction = (enemy.player.position - enemy.position).normalized()
	enemy.last_direction = direction

	if distance <= 16.0 and enemy.is_attack_ready():
		return state_machine.get_state("Attack")

	var target_velocity = direction * enemy.speed
	enemy.velocity = enemy.velocity.lerp(target_velocity, 10.0 * delta)
	enemy.move_and_slide()
	enemy.change_animation(direction)

	return null
