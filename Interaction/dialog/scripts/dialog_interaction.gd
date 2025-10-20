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
	print("DEBUG: Collected ", dialog_items.size(), " dialog items")
	pass



func player_interact() -> void:
	print("DEBUG: player_interact called, player_in_range: ", player_in_range, ", is_interacting: ", is_interacting)

	if not player_in_range:
		print("DEBUG: Player not in range, aborting")
		return

	if is_interacting:
		print("DEBUG: Already interacting, aborting")
		return

	# Re-collect dialog items in case they changed
	_collect_dialog_items()

	if dialog_items.size() == 0:
		print("DEBUG: No dialog items found, aborting")
		return

	is_interacting = true
	player_interacted.emit()

	print("DEBUG: Connecting to Dialog.finished")
	# Only connect if not already connected
	if not Dialog.finished.is_connected( _on_dialog_finished ):
		Dialog.finished.connect( _on_dialog_finished )

	print("DEBUG: Showing dialog with ", dialog_items.size(), " items")
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
		print("DEBUG: No dialog items, skipping interaction setup")
		return

	print("DEBUG: Player entered interaction area - Instance: ", get_instance_id())
	player_in_range = true
	animation_player.play("show")

	# Only connect if not already connected
	if not PlayerManager.interact_pressed.is_connected( player_interact ):
		print("DEBUG: Connecting to PlayerManager.interact_pressed - Instance: ", get_instance_id())
		PlayerManager.interact_pressed.connect( player_interact )
	else:
		print("DEBUG: Already connected to PlayerManager.interact_pressed - Instance: ", get_instance_id())
	pass


func _on_body_exit( body : Node2D) -> void:
	if body is not Player:
		return

	print("DEBUG: Player EXIT interaction area - Instance: ", get_instance_id(), ", is_interacting: ", is_interacting)

	# Don't process exit if we're currently in a dialog
	if is_interacting:
		print("DEBUG: Ignoring exit because dialog is active")
		return

	player_in_range = false
	animation_player.play("hide")

	# Disconnect the signal
	if PlayerManager.interact_pressed.is_connected( player_interact ):
		print("DEBUG: Disconnecting from PlayerManager.interact_pressed - Instance: ", get_instance_id())
		PlayerManager.interact_pressed.disconnect( player_interact )
	pass


func _on_dialog_finished() -> void:
	print("DEBUG: Dialog finished callback")

	# Disconnect the signal
	if Dialog.finished.is_connected( _on_dialog_finished ):
		Dialog.finished.disconnect( _on_dialog_finished )
		print("DEBUG: Disconnected from Dialog.finished")

	is_interacting = false
	print("DEBUG: is_interacting set to false, player_in_range: ", player_in_range)

	# Hide prompt if player is no longer in range
	if not player_in_range:
		print("DEBUG: Player not in range, hiding prompt")
		animation_player.play("hide")
		if PlayerManager.interact_pressed.is_connected( player_interact ):
			PlayerManager.interact_pressed.disconnect( player_interact )
	else:
		# Player is still in range, show the prompt again for another interaction
		print("DEBUG: Player still in range, showing prompt again")
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
