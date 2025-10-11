extends Node



var current_save : Dictionary = {
	scene_path = "",
	player = {
		hp = 1,
		max_hp = 1,
		pos_x = 0,
		pos_y = 0
	},
	items = [],
	persistence = [],
	quests = [],
}



func _ready() -> void:
	print("Save manager will be worked on at the very end of development.")
	pass