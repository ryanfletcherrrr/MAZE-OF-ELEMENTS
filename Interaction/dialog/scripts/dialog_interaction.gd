@tool
@icon( "res://GUI/dialog_system/icons/chat_bubbles.svg" )
class_name DialogInteraction extends Area2D

signal player_interacted
signal finished

@export var enabled : bool = true

var dialog_items : Array[ DialogItem ]
var player_in_range : bool = false
var is_interacting : bool = false

@onready var animation_player: AnimationPlayer = $AnimationPlayer


func _ready() -> void:
	if Engine.is_editor_hint():
		return

	body_entered.connect( _on_body_enter )
	body_exited.connect( _on_body_exit )

	# Collect dialog items
	_collect_dialog_items()
	pass


func _collect_dialog_items() -> void:
	dialog_items.clear()
	for c in get_children():
		if c is DialogItem:
			dialog_items.append( c )
	pass



func player_interact() -> void:
	if not player_in_range:
		return

	if is_interacting:
		return

	# Re-collect dialog items in case they changed
	_collect_dialog_items()

	if dialog_items.size() == 0:
		return

	is_interacting = true
	player_interacted.emit()

	# Only connect if not already connected
	if not Dialog.finished.is_connected( _on_dialog_finished ):
		Dialog.finished.connect( _on_dialog_finished )

	Dialog.show_dialog( dialog_items )
	pass



func _on_body_enter( body : Node2D) -> void:
	if body is not Player:
		return
	if enabled == false:
		return

	# Collect dialog items to check if we have any
	_collect_dialog_items()

	if dialog_items.size() == 0:
		return

	player_in_range = true
	animation_player.play("show")

	# Only connect if not already connected
	if not PlayerManager.interact_pressed.is_connected( player_interact ):
		PlayerManager.interact_pressed.connect( player_interact )
	pass


func _on_body_exit( body : Node2D) -> void:
	if body is not Player:
		return

	# Don't process exit if we're currently in a dialog
	if is_interacting:
		return

	player_in_range = false
	animation_player.play("hide")

	# Disconnect the signal
	if PlayerManager.interact_pressed.is_connected( player_interact ):
		PlayerManager.interact_pressed.disconnect( player_interact )
	pass


func _on_dialog_finished() -> void:
	# Disconnect the signal
	if Dialog.finished.is_connected( _on_dialog_finished ):
		Dialog.finished.disconnect( _on_dialog_finished )

	is_interacting = false

	# Hide prompt if player is no longer in range
	if not player_in_range:
		animation_player.play("hide")
		if PlayerManager.interact_pressed.is_connected( player_interact ):
			PlayerManager.interact_pressed.disconnect( player_interact )
	else:
		# Player is still in range, show the prompt again for another interaction
		animation_player.play("show")

	finished.emit()


func _get_configuration_warnings() -> PackedStringArray:
	if _check_for_dialog_items() == false:
		return [ "Requires at least one DialogItem node." ]
	else:
		return []
	pass


func _check_for_dialog_items() -> bool:
	for c in get_children():
		if c is DialogItem:
			return true
	return false
