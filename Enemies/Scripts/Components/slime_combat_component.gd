extends Node
class_name SlimeCombatComponent

var enemy: Slime = null
var contact_damage_timer: float = 0.0

func initialize(owner_enemy: Slime) -> void:
	enemy = owner_enemy

func update(delta: float) -> void:
	if contact_damage_timer > 0:
		contact_damage_timer -= delta

func is_attack_ready() -> bool:
	return contact_damage_timer <= 0.0

func perform_contact_attack() -> void:
	if is_attack_ready():
		deal_contact_damage()

func deal_contact_damage() -> void:
	if not enemy.player or not enemy.player.has_method("take_damage"):
		return

	# Deal contact damage
	enemy.player.call("take_damage", enemy.attack_damage)

	# Visual feedback
	flash_attack()
	flash_player_hurt()

	# Reset cooldown
	contact_damage_timer = enemy.contact_damage_cooldown

func flash_attack() -> void:
	if not enemy.enemy_animation:
		return
	enemy.enemy_animation.modulate = Color.CYAN
	var tween = enemy.create_tween()
	tween.tween_property(enemy.enemy_animation, "modulate", Color.WHITE, 0.3)

func flash_player_hurt() -> void:
	if not enemy.player:
		return
	var player_sprite = enemy.player.get_node_or_null("AnimatedSprite2D")
	if player_sprite:
		player_sprite.modulate = Color.RED
		var tween = enemy.create_tween()
		tween.tween_property(player_sprite, "modulate", Color.WHITE, 0.3)
