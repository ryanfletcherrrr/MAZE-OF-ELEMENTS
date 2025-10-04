extends CanvasLayer

@onready var animation_player: AnimationPlayer = $Control/AnimationPlayer

@export var target_duration: float = 1.0  # seconds for each fade

func _play_norm(anim_name: String) -> void:
	if not animation_player.has_animation(anim_name):
		push_warning("SceneTransition: Missing animation %s" % anim_name)
		return
	var length = animation_player.get_animation(anim_name).length
	# Adjust speed_scale so it lasts target_duration
	if length > 0:
		animation_player.speed_scale = length / target_duration
	animation_player.play(anim_name)


func fade_out() -> bool:
	_play_norm("fade_out")
	await animation_player.animation_finished
	animation_player.speed_scale = 1.0
	return true


func fade_in() -> bool:
	_play_norm("fade_in")
	await animation_player.animation_finished
	animation_player.speed_scale = 1.0
	return true
