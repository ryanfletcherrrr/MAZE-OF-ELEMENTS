extends Node
class_name PlayerCombatComponent

# Handles player attack logic, hitboxes, and combat interactions

var player: CharacterBody2D = null
var attack_hitbox: Area2D = null
var player_hitbox: Area2D = null

func initialize(owner_player: CharacterBody2D) -> void:
	player = owner_player

	setup_attack_hitbox()
	setup_player_hitbox()

func setup_attack_hitbox() -> void:
	attack_hitbox = player.get_node_or_null("AttackHitbox")
	if attack_hitbox:
		# Initially disable the attack hitbox
		attack_hitbox.monitoring = false
		attack_hitbox.visible = false

		# Connect both body_entered (for CharacterBody2D/StaticBody2D) and area_entered (for Area2D)
		attack_hitbox.body_entered.connect(_on_attack_hitbox_body_entered)
		attack_hitbox.area_entered.connect(_on_attack_hitbox_area_entered)
	else:
		print("AttackHitbox not found - add Area2D named 'AttackHitbox' to player scene")

func setup_player_hitbox() -> void:
	player_hitbox = player.get_node_or_null("PlayerHitbox")
	if not player_hitbox:
		# Create PlayerHitbox programmatically
		player_hitbox = Area2D.new()
		player_hitbox.name = "PlayerHitbox"
		player.add_child(player_hitbox)

		# Add collision shape
		var collision_shape = CollisionShape2D.new()
		var circle_shape = CircleShape2D.new()
		circle_shape.radius = 12.0
		collision_shape.shape = circle_shape
		player_hitbox.add_child(collision_shape)

	# Connect PlayerHitbox signals
	player_hitbox.body_entered.connect(_on_player_hitbox_body_entered)
	player_hitbox.body_exited.connect(_on_player_hitbox_body_exited)

func perform_attack() -> void:
	if not player.can_attack():
		return

	player.is_attacking = true

	# Trigger attack state
	var state_machine = player.get_node_or_null("StateMachine")
	if state_machine:
		var attack_state = state_machine.get_state("Attack")
		if attack_state:
			state_machine.change_state(attack_state)

	# Enable attack hitbox
	activate_attack_hitbox()

func activate_attack_hitbox() -> void:
	if not attack_hitbox:
		return

	# Position the attack hitbox in front of the player
	var attack_direction = player.last_direction.normalized()
	var hitbox_offset = attack_direction * 32  # 32 pixels in front
	attack_hitbox.position = hitbox_offset
	attack_hitbox.rotation = attack_direction.angle()

	# Enable the hitbox
	attack_hitbox.monitoring = true
	attack_hitbox.visible = true

	# Disable after attack duration
	await player.get_tree().create_timer(0.3).timeout

	if is_instance_valid(attack_hitbox):
		attack_hitbox.monitoring = false
		attack_hitbox.visible = false
		player.is_attacking = false

func _on_attack_hitbox_body_entered(body: Node2D) -> void:
	if not player.is_alive:
		return

	# Only damage enemies (CharacterBody2D enemies)
	if body != player and body.has_method("take_damage"):
		body.call("take_damage", player.attack_damage)

		# Visual feedback
		create_attack_effect(body.global_position)

func _on_attack_hitbox_area_entered(area: Area2D) -> void:
	if not player.is_alive:
		return

	# Check if the area's parent is an enemy (for Area2D-based enemies)
	var enemy = area.get_parent()
	if enemy and enemy != player and enemy.has_method("take_damage"):
		enemy.call("take_damage", player.attack_damage)

		# Visual feedback
		create_attack_effect(enemy.global_position)

func _on_player_hitbox_body_entered(_body: Node2D) -> void:
	# Detection only - damage comes from enemy attacks
	pass

func _on_player_hitbox_body_exited(_body: Node2D) -> void:
	pass

func create_attack_effect(_position: Vector2) -> void:
	# Placeholder for attack visual effects
	pass
