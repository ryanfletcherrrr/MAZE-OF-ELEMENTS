extends Node
class_name SlimeAnimationComponent

var enemy: Slime = null
var enemy_animation: AnimatedSprite2D = null
var current_animation: String = ""
var idle_timer: Timer = null

func initialize(owner_enemy: Slime) -> void:
	enemy = owner_enemy
	enemy_animation = enemy.get_node_or_null("Enemy")
	idle_timer = enemy.get_node_or_null("Timer")

	if idle_timer:
		idle_timer.timeout.connect(_on_idle_timer_timeout)

	if not enemy_animation:
		push_warning("Enemy AnimatedSprite2D not found")
		return

	# Connect to animation finished signal
	enemy_animation.animation_finished.connect(_on_animation_finished)

	# Set sprite immediately
	if enemy.slime_image:
		enemy_animation.sprite_frames = enemy.slime_image
		play_idle_animation()
		enemy_animation.play()

	# Start idle timer
	if idle_timer:
		idle_timer.start()

func play_idle_animation() -> void:
	if not enemy_animation:
		return

	# Always play idle, don't call change_animation to avoid recursion
	enemy_animation.play("idle_down")
	current_animation = "idle"

func change_animation(direction: Vector2) -> void:
	if not enemy_animation:
		return

	# Only change animation if player is being chased
	if not enemy.player_chase:
		if current_animation != "idle":
			# Directly play idle without calling play_idle_animation to avoid recursion
			enemy_animation.play("idle_down")
			current_animation = "idle"
		return

	var new_animation = ""

	# Determine which walk animation to play
	if abs(direction.y) > abs(direction.x):
		new_animation = "walk_up" if direction.y < 0 else "walk_down"
	else:
		new_animation = "walk_left" if direction.x < 0 else "walk_right"

	# Only change if different
	if new_animation != current_animation:
		enemy_animation.play(new_animation)
		current_animation = new_animation

func _on_idle_timer_timeout() -> void:
	# Only play idle animations when not chasing
	if not enemy.player_chase:
		play_idle_animation()
	if idle_timer:
		idle_timer.start()

func _on_animation_finished() -> void:
	# Only remove if dying and animation is a death animation
	if enemy.is_dying and enemy_animation:
		var anim = enemy_animation.animation
		if anim.begins_with("death") and is_instance_valid(enemy):
			enemy.queue_free()
