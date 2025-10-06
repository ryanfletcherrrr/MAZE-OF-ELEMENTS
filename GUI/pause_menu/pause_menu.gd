extends CanvasLayer

@onready var button_save: Button = $Control/HBoxContainer/Button_Save
@onready var button_load: Button = $Control/HBoxContainer/Button_Load
@onready var item_description: Label = $Control/ItemDescription

signal shown
signal hidden


var is_paused: bool = false

func _ready() -> void:
	# Allow this node to receive input even when the game is paused
	process_mode = Node.PROCESS_MODE_ALWAYS
	hide_pause_menu()

func _unhandled_input(event: InputEvent) -> void:
	if event.is_action_pressed("Pause"):
		# Fixed logic: show when NOT paused, hide when paused
		if is_paused:
			hide_pause_menu()
			print("Unpaused")
		else:
			show_pause_menu()
			print("Paused")
		get_viewport().set_input_as_handled()

func show_pause_menu() -> void:
	get_tree().paused = true
	visible = true
	is_paused = true
	shown.emit()

func hide_pause_menu() -> void:
	get_tree().paused = false
	visible = false
	is_paused = false
	hidden.emit()
	
func update_item_description( new_text : String ) -> void:
	item_description.text = new_text
