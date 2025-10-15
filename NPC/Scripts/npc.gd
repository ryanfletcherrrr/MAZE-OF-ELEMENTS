@tool
@icon( "res://npc/icons/npc.svg" )
class_name NPC extends CharacterBody2D

signal do_behavior_enabled

var state : String = "idle"
var direction : Vector2 = Vector2.DOWN
var direction_name : String = "down"
var do_behavior : bool = true

@export var npc_resource : NPCResource : set = _set_npc_resource

@onready var animated_sprite: AnimatedSprite2D = $AnimatedSprite2D


func _ready() -> void:
	setup_npc()
	if Engine.is_editor_hint():
		return
	do_behavior_enabled.emit()
	pass



func _physics_process(_delta: float) -> void:
	move_and_slide()


func update_animation() -> void:
	if animated_sprite:
		var anim_name = state + "_" + direction_name
		animated_sprite.play(anim_name)



func update_direction( target_position : Vector2 ) -> void:
	direction = global_position.direction_to( target_position )
	update_direction_name()
	# No need to flip - we have separate left/right animations



func update_direction_name() -> void:
	# Use the strongest direction component
	if abs(direction.x) > abs(direction.y):
		# Horizontal movement is stronger
		if direction.x > 0:
			direction_name = "right"
		else:
			direction_name = "left"
	else:
		# Vertical movement is stronger
		if direction.y > 0:
			direction_name = "down"
		else:
			direction_name = "up"



func setup_npc() -> void:
	# AnimatedSprite2D already has animations configured in the scene
	# Just ensure the animated_sprite is ready
	if animated_sprite:
		animated_sprite.play(state + "_" + direction_name)
	pass



func _set_npc_resource( _npc : NPCResource ) -> void:
	npc_resource = _npc
	setup_npc()
