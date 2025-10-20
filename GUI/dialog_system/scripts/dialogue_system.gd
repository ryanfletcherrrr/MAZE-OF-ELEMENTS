@tool
@icon("res://GUI/dialog_system/icons/star_bubble.svg")
class_name DialogSystemNode extends CanvasLayer

signal finished
signal choice_selected(choice_index: int)

var is_active : bool = false
var current_dialog_items : Array[DialogItem] = []
var current_item_index : int = 0
var is_text_complete : bool = false
var current_text : String = ""
var target_text : String = ""
var text_speed : float = 0.05

@onready var dialog_ui : Control = $DialogUI
@onready var text_label : RichTextLabel = $DialogUI/PanelContainer/RichTextLabel
@onready var name_label : Label = $DialogUI/NameLabel
@onready var timer : Timer = $DialogUI/Timer
@onready var audio_player : AudioStreamPlayer = $DialogUI/AudioStreamPlayer
@onready var progress_indicator : PanelContainer = $DialogUI/DialogProgressIndicator


func _ready() -> void:
	if Engine.is_editor_hint():
		if get_viewport() is Window:
			get_parent().remove_child( self )
			return
		return
	hide_dialog()
	timer.timeout.connect(_on_timer_timeout)
	pass


func _unhandled_input(event: InputEvent) -> void:
	if is_active == false:
		return

	# Advance dialog on interact key
	if event.is_action_pressed("ui_accept") or event.is_action_pressed("interact"):
		if is_text_complete:
			next_dialog()
		else:
			complete_text_immediately()
		get_viewport().set_input_as_handled()
	pass


func show_dialog(dialog_items: Array[DialogItem] = []) -> void:
	if dialog_items.size() == 0:
		return

	current_dialog_items = dialog_items
	current_item_index = 0
	is_active = true
	dialog_ui.visible = true
	dialog_ui.process_mode = Node.PROCESS_MODE_ALWAYS
	get_tree().paused = true

	display_current_item()
	pass


func hide_dialog() -> void:
	is_active = false
	dialog_ui.visible = false
	dialog_ui.process_mode = Node.PROCESS_MODE_DISABLED
	get_tree().paused = false
	current_dialog_items.clear()
	current_item_index = 0
	timer.stop()
	pass


func display_current_item() -> void:
	if current_item_index >= current_dialog_items.size():
		end_dialog()
		return

	var item = current_dialog_items[current_item_index]

	# Set NPC name
	if item.npc_info:
		name_label.text = item.npc_info.npc_name if item.npc_info.npc_name else "???"
		name_label.visible = true
	else:
		name_label.visible = false

	if item is DialogText:
		display_text(item.text)
	elif item is DialogChoice:
		display_choice(item)
	pass


func display_text(text: String) -> void:
	target_text = text
	current_text = ""
	is_text_complete = false
	progress_indicator.visible = false
	text_label.text = ""

	# Start typing effect
	timer.wait_time = text_speed
	timer.start()
	pass


func display_choice(choice_item: DialogChoice) -> void:
	# For now, just display the first choice text
	# You can expand this to show actual choice buttons
	var choice_text = "Choose:\n"
	for i in choice_item.choices.size():
		choice_text += str(i + 1) + ". " + choice_item.choices[i] + "\n"

	display_text(choice_text)
	pass


func _on_timer_timeout() -> void:
	if current_text.length() < target_text.length():
		current_text += target_text[current_text.length()]
		text_label.text = current_text

		# Play typing sound (optional)
		# if audio_player.stream:
		#     audio_player.play()
	else:
		complete_text_immediately()
	pass


func complete_text_immediately() -> void:
	timer.stop()
	current_text = target_text
	text_label.text = current_text
	is_text_complete = true
	progress_indicator.visible = true
	pass


func next_dialog() -> void:
	current_item_index += 1
	display_current_item()
	pass


func end_dialog() -> void:
	hide_dialog()
	finished.emit()
	pass
