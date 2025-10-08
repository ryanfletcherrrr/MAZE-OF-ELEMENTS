extends Node
class_name PlayerHealthComponent

# Handles player health, damage, healing, and death

var player: CharacterBody2D = null
var health_bar: ProgressBar = null

func initialize(owner_player: CharacterBody2D) -> void:
	player = owner_player

	# Find health bar
	health_bar = player.get_node_or_null("HealthBar")
	if health_bar:
		health_bar.max_value = player.max_health
		health_bar.value = player.current_health

func take_damage(damage: int) -> void:
	if not player.is_alive:
		return

	player.current_health = maxi(0, player.current_health - damage)

	update_health_bar()

	if not player.is_alive:
		die()

func heal(amount: int) -> void:
	if not player.is_alive:
		return

	player.current_health = mini(player.max_health, player.current_health + amount)

	update_health_bar()

func die() -> void:
	# Handle death (could emit signal, trigger game over, etc.)

	# For now, respawn with full health
	player.current_health = player.max_health
	update_health_bar()

func update_health_bar() -> void:
	if not health_bar:
		return

	health_bar.value = player.current_health

	# Change color based on health percentage
	var health_percent = player.get_health_percent()
	if health_percent > 0.6:
		health_bar.modulate = Color.GREEN
	elif health_percent > 0.3:
		health_bar.modulate = Color.YELLOW
	else:
		health_bar.modulate = Color.RED

func reset_health() -> void:
	player.current_health = player.max_health
	update_health_bar()
