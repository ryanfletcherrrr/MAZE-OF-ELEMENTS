extends CharacterBody2D
class_name Player

# ========== EXPORTS ==========

@export_category("Core Player Mechanics")
@export var speed: float = 60.0
@export var level: int = 0

@export_category("Player Stats")
@export var max_health: int = 100
@export var current_health: int = 100
@export var attack_damage: int = 25

@export_category("Character Sprite (Level 1-9)")
@export var character_sprite: SpriteFrames

# ========== PUBLIC PROPERTIES ==========
var direction: Vector2 = Vector2.ZERO
var last_direction: Vector2 = Vector2.DOWN
var is_attacking: bool = false
var state_name: String = "Idle"

# ========== COMPONENTS ==========
var state_machine: Node
var health_component: Node
var combat_component: Node
var animator: AnimatedSprite2D

# ========== COMPUTED PROPERTIES ==========

var is_alive: bool:
	get:
		return current_health > 0

# ========== GODOT LIFECYCLE ==========
func _ready() -> void:
	initialize_components()
	initialize_state_machine()
	initialize_animator()

func _physics_process(_delta: float) -> void:
	# Don't move while attacking
	if state_machine and state_machine.current_state and state_machine.current_state.name == "Attack":
		velocity = Vector2.ZERO
		move_and_slide()
		return

	# Apply movement from state machine
	velocity = direction * speed
	move_and_slide()

func _input(event: InputEvent) -> void:
	if not event is InputEventKey:
		return

	var key_event = event as InputEventKey
	if not key_event.pressed:
		return

	# Attack with X key
	if key_event.keycode == KEY_X:
		perform_attack()

	# Debug damage with Z key
	if key_event.keycode == KEY_Z:
		take_damage(10)

# ========== INITIALIZATION ==========
func initialize_components() -> void:
	# Health component
	health_component = load("res://Player/Scripts/Components/player_health_component.gd").new()
	add_child(health_component)
	health_component.initialize(self)

	# Combat component
	combat_component = load("res://Player/Scripts/Components/player_combat_component.gd").new()
	add_child(combat_component)
	combat_component.initialize(self)

func initialize_state_machine() -> void:
	state_machine = get_node_or_null("StateMachine")
	if state_machine:
		state_machine.initialize(self)
	else:
		print("PlayerStateMachine not found in scene tree")

func initialize_animator() -> void:
	animator = get_node_or_null("AnimatedSprite2D")
	if not animator:
		print("No AnimatedSprite2D detected")
		return

	if character_sprite:
		animator.sprite_frames = character_sprite

# ========== PUBLIC API ==========
# These methods are exposed for other scripts and state machine
func take_damage(damage: int) -> void:
	if health_component:
		health_component.take_damage(damage)

func heal(amount: int) -> void:
	if health_component:
		health_component.heal(amount)

func perform_attack() -> void:
	if combat_component:
		combat_component.perform_attack()

func full_heal() -> void:
	if health_component:
		health_component.reset_health()

func can_attack() -> bool:
	return is_alive and not is_attacking

func get_health_percent() -> float:
	if max_health > 0:
		return float(current_health) / float(max_health)
	return 0.0
