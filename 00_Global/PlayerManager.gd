extends Node

signal interact_pressed

@onready var PLAYER = preload("res://Player/player.tscn")
const INVENTORY_DATA : InventoryData = preload("res://GUI/pause_menu/inventory/player_inventory.tres")



var player_spawned : bool = false
var player : Player


func _ready() -> void:
	print("Loaded Player Manager")
	add_player_instance()
	await get_tree().create_timer(0.2).timeout
	player_spawned = true


func _unhandled_input(event: InputEvent) -> void:
	if event.is_action_pressed("interact"):
		print("DEBUG PlayerManager: E key pressed, emitting interact_pressed signal")
		interact_pressed.emit()
		get_viewport().set_input_as_handled()


func update_hp( _amount : int ) -> void:
	player.full_heal()
	pass

func add_player_instance() -> void:
	player = PLAYER.instantiate()
	add_child( player )
	pass


func set_player_position( _new_pos : Vector2 ) -> void:
	player.global_position = _new_pos
	pass

func set_as_parent( _p : Node2D ) -> void:
	if player.get_parent():
		player.get_parent().remove_child( player )
	_p.add_child( player )

func unparent_player( _p : Node2D ) -> void:
	if player.get_parent() == _p:
		_p.remove_child( player )

