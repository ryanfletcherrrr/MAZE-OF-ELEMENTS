class_name StateBase extends Node

# Abstract class - blueprint for all states
# Each state knows which Player it belongs to
# Each State can access the state_machine used to get other States

# Reference to the player owning this state
var player: CharacterBody2D = null

# Back-reference to the state machine for convenience
var state_machine: Node = null

# Called when the state becomes active
func enter() -> void:
	pass

# Called when the state is about to be replaced
func exit() -> void:
	pass

# Per-frame (non-physics) update. Return a different State to transition, or null to stay.
func update(_delta: float):
	return null

# Physics update. Return next state or null to stay.
func physics_update(_delta: float):
	return null

# Input handling. Return next state or null.
func handle_input(_event: InputEvent):
	return null
