extends "res://Enemies/Scripts/States/slime_state.gd"

var attack_duration: float = 0.35
var elapsed: float = 0.0

func enter() -> void:
	elapsed = 0.0
	if not enemy:
		return
	enemy.velocity = Vector2.ZERO
	enemy.player_chase = true
	enemy.perform_contact_attack()

func physics_update(delta: float):
	if not enemy:
		return null
	if not state_machine:
		return null

	elapsed += delta
	if elapsed >= attack_duration:
		if enemy.has_target():
			return state_machine.get_state("Chase")
		return state_machine.get_state("Idle")

	enemy.move_and_slide()
	return null
