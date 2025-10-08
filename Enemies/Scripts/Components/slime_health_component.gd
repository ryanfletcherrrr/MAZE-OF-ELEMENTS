extends Node
class_name SlimeHealthComponent

var enemy: Slime = null
var health_bar: ProgressBar = null

func initialize(owner_enemy: Slime) -> void:
	enemy = owner_enemy

	# Find health bar
	health_bar = enemy.get_node_or_null("HealthBar")
	if health_bar:
		health_bar.max_value = enemy.max_health
		health_bar.value = enemy.current_health

func take_damage(damage: int) -> void:
	if not enemy.is_alive:
		return

	enemy.current_health = maxi(0, enemy.current_health - damage)
	update_health_bar()
	flash_damage()

	if not enemy.is_alive:
		die()

func flash_damage() -> void:
	if not enemy.enemy_animation:
		return
	enemy.enemy_animation.modulate = Color.RED
	var tween = enemy.create_tween()
	tween.tween_property(enemy.enemy_animation, "modulate", Color.WHITE, 0.2)

func die() -> void:
	enemy.player_chase = false
	enemy.velocity = Vector2.ZERO
	enemy.is_dying = true

	if enemy.state_machine:
		enemy.state_machine.process_mode = Node.PROCESS_MODE_DISABLED

	# Disable collision and processing
	enemy.set_physics_process(false)
	enemy.collision_layer = 0
	enemy.collision_mask = 0

	play_death_animation()
	create_safety_timer()

func play_death_animation() -> void:
	if not enemy.enemy_animation:
		return

	var death_anim = "death"
	if abs(enemy.last_direction.y) > abs(enemy.last_direction.x):
		death_anim = "death_up" if enemy.last_direction.y < 0 else "death_down"
	else:
		death_anim = "death_left" if enemy.last_direction.x < 0 else "death_right"

	# Play death animation (ensure it doesn't loop)
	if enemy.enemy_animation.sprite_frames.has_animation(death_anim):
		enemy.enemy_animation.play(death_anim)
	elif enemy.enemy_animation.sprite_frames.has_animation("death"):
		enemy.enemy_animation.play("death")

	# Ensure animation doesn't loop
	if enemy.enemy_animation.sprite_frames:
		var current_anim = enemy.enemy_animation.animation
		enemy.enemy_animation.sprite_frames.set_animation_loop(current_anim, false)

func create_safety_timer() -> void:
	var safety_timer = Timer.new()
	safety_timer.wait_time = 2.0
	safety_timer.one_shot = true
	safety_timer.timeout.connect(_on_safety_timer_timeout)
	enemy.add_child(safety_timer)
	safety_timer.start()

func _on_safety_timer_timeout() -> void:
	if is_instance_valid(enemy):
		enemy.queue_free()

func update_health_bar() -> void:
	if not health_bar:
		return

	health_bar.value = enemy.current_health

	# Change color based on health percentage
	var health_percent = float(enemy.current_health) / float(enemy.max_health)
	if health_percent > 0.6:
		health_bar.modulate = Color.GREEN
	elif health_percent > 0.3:
		health_bar.modulate = Color.YELLOW
	else:
		health_bar.modulate = Color.RED
