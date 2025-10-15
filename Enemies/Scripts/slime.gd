extends CharacterBody2D
class_name Slime

# ========== EXPORTS ==========

@export_category("Slime Settings")
@export var slime_image: SpriteFrames
@export var speed: float = 50.0
@export var follow_distance: float = 80.0
@export var contact_damage_cooldown: float = 1.0

@export_category("Health System")
@export var max_health: int = 50
@export var attack_damage: int = 15

# ========== PUBLIC VARIABLES ==========
var player: CharacterBody2D = null
var last_direction: Vector2 = Vector2.DOWN
var player_chase: bool = false
var current_health: int = 0
var is_dying: bool = false

# ========== COMPONENTS ==========
var health_component: Node = null
var combat_component: Node = null
var animation_component: Node = null
var state_machine: Node = null

# ========== REFERENCES ==========
var enemy_animation: AnimatedSprite2D = null

# ========== COMPUTED PROPERTIES ==========
var is_alive: bool:
	get:
		return current_health > 0

# ========== GODOT LIFECYCLE ==========

func _ready() -> void:
	add_to_group("enemies")
	current_health = max_health

	initialize_components()
	initialize_state_machine()

func _physics_process(delta: float) -> void:
	if not is_alive:
		return

	# Update combat cooldown
	if combat_component:
		combat_component.update(delta)

	# Use state machine or legacy behavior
	if not state_machine:
		legacy_physics_process(delta)

# ========== INITIALIZATION ==========

func initialize_components() -> void:
	# Health component
	health_component = load("res://Enemies/Scripts/Components/slime_health_component.gd").new()
	add_child(health_component)
	health_component.initialize(self)

	# Combat component
	combat_component = load("res://Enemies/Scripts/Components/slime_combat_component.gd").new()
	add_child(combat_component)
	combat_component.initialize(self)

	# Animation component
	animation_component = load("res://Enemies/Scripts/Components/slime_animation_component.gd").new()
	add_child(animation_component)
	animation_component.initialize(self)

	# Cache animation reference
	enemy_animation = get_node_or_null("Enemy")

func initialize_state_machine() -> void:
	state_machine = get_node_or_null("StateMachine")
	if state_machine and state_machine.has_method("initialize"):
		state_machine.initialize(self)

# ========== LEGACY BEHAVIOR (Fallback) ==========

func legacy_physics_process(delta: float) -> void:
	if player_chase and player:
		var dist_to_player = position.distance_to(player.position)
		var direction_to_player = (player.position - position).normalized()
		last_direction = direction_to_player

		# Stop at attack range, don't stick to player
		if dist_to_player <= 20.0:
			velocity = velocity.lerp(Vector2.ZERO, 15.0 * delta)
		else:
			var target_velocity = direction_to_player * speed
			velocity = velocity.lerp(target_velocity, 10.0 * delta)

		move_and_slide()
		if animation_component:
			animation_component.change_animation(direction_to_player)

		# Contact damage if ready and in range
		if dist_to_player <= 20.0 and combat_component and combat_component.is_attack_ready():
			combat_component.deal_contact_damage()
	else:
		velocity = velocity.lerp(Vector2.ZERO, 8.0 * delta)
		if velocity.length() > 0.1:
			move_and_slide()
		if animation_component:
			animation_component.play_idle_animation()

# ========== DETECTION HANDLERS ==========

func body_entered(body: Node2D) -> void:
	if body is CharacterBody2D and body.name.contains("Player"):
		player = body
		player_chase = true
		if state_machine:
			state_machine.change_state_by_name("Chase")

func body_exit(body: Node2D) -> void:
	if body == player:
		player = null
		player_chase = false
		if state_machine:
			state_machine.change_state_by_name("Idle")

func _on_detection_body_entered(body: Node2D) -> void:
	body_entered(body)

func _on_detection_body_exited(body: Node2D) -> void:
	body_exit(body)

# ========== PUBLIC API FOR STATES ==========

func has_target() -> bool:
	return player != null and is_instance_valid(player)

func distance_to_player() -> float:
	if not has_target():
		return INF
	return position.distance_to(player.position)

func is_attack_ready() -> bool:
	return combat_component and combat_component.is_attack_ready()

func perform_contact_attack() -> void:
	if combat_component:
		combat_component.perform_contact_attack()

func change_animation(direction: Vector2) -> void:
	if animation_component:
		animation_component.change_animation(direction)

func play_idle_animation() -> void:
	if animation_component:
		animation_component.play_idle_animation()

# ========== PUBLIC API FOR DAMAGE ==========

func take_damage(damage: int) -> void:
	if health_component:
		health_component.take_damage(damage)

# ========== MARKER METHOD ==========

func enemy() -> void:
	pass
