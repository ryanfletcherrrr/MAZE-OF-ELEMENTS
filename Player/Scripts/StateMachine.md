# Player State Machine Documentation

## Overview

The PlayerStateMachine is a modular, node-based state management system for controlling player behavior in Godot. It enables clean separation of logic for different player states (e.g., idle, walking, jumping) and provides a robust framework for handling transitions, input, and updates.

---

## Architecture

### Node Structure
- The PlayerStateMachine is a Node that acts as a parent to multiple child nodes, each representing a distinct player state.
- Each child state node must implement the required state interface (methods: `enter`, `exit`, `update`, `physics_update`, `handle_input`).
- The state machine itself does not handle player movement or physics directly; it delegates these responsibilities to the active state.

### Key Properties
- `state_list: Array[Node]` — Holds all valid state nodes discovered at initialization.
- `previous_state: Node` — Tracks the last active state before a transition.
- `current_state: Node` — The currently active state node.

---

## Initialization

### Method: `initialize(player: CharacterBody2D)`
- Must be called with a valid player reference (usually from the player's `_ready()` method).
- Discovers all child nodes with an `enter` method (duck typing) and adds them to `state_list`.
- Assigns the `player` and `state_machine` references to each state for context.
- Activates the first state in the list and enables processing.
- Prevents re-initialization if already initialized.

#### Why Not Use `_ready()` Directly?
- Delayed initialization ensures the player node is fully set up before states are activated.
- Prevents premature execution and null reference errors.

---

## State Transition Logic

### Method: `change_state(new_state: Node)`
- Validates the new state (not null, not the same as current).
- Calls `exit()` on the current state (if any).
- Updates `previous_state` and `current_state` references.
- Calls `enter()` on the new state.
- Ensures clean transitions and proper resource management.

---

## Update Loop Integration

The state machine delegates engine callbacks to the active state:

- `_process(delta)` — Calls `current_state.update(delta)`. Used for frame-based logic.
- `_physics_process(delta)` — Calls `current_state.physics_update(delta)`. Used for physics and movement.
- `_unhandled_input(event)` — Calls `current_state.handle_input(event)`. Used for input event handling.

Each callback checks for a valid `current_state` before delegating. If the state returns a different state node, a transition is triggered.

---

## State Lookup Methods

- `get_state(state_name: String)` — Finds a state by its node name.
- `get_state_by_type(state_type: String)` — Finds a state by its script filename (without extension).

These methods allow states to request transitions to other states by name or type, supporting flexible and readable code.

---

## State Node Contract

Each state node must implement the following methods:

- `enter()` — Called when the state becomes active. Initialize variables, start animations, etc.
- `exit()` — Called when the state is deactivated. Clean up resources, stop timers, etc.
- `update(delta)` — Frame update logic. Return a state node to transition, or null/self to remain.
- `physics_update(delta)` — Physics update logic. Same return contract as `update`.
- `handle_input(event)` — Input event handling. Same return contract as above.

States should use the `player` and `state_machine` references for context and transitions.

---

## Best Practices

- Always null-check state lookups before transitioning.
- Keep state logic independent; avoid direct references to other states except via state machine methods.
- Use `physics_update` for movement and collision logic.
- Log transitions during development for debugging.
- Validate the player reference before accessing it in states.

---

## Example State Implementation

```gdscript
extends Node

var player
var state_machine

func enter():
    # Initialize state
    pass

func exit():
    # Clean up
    pass

func update(delta):
    # Frame logic
    return null

func physics_update(delta):
    # Physics logic
    return null

func handle_input(event):
    # Input logic
    return null
```

---

## Common Patterns

### Conditional Transitions
```gdscript
func physics_update(delta):
    if player.is_on_floor():
        return state_machine.get_state("Idle")
    return null
```

### Multi-Condition State Selection
```gdscript
func update(delta):
    if Input.is_action_just_pressed("jump"):
        return state_machine.get_state("Jump")
    if Input.get_vector("left", "right", "up", "down").length() > 0:
        return state_machine.get_state("Walk")
    return state_machine.get_state("Idle")
```

### Context-Aware Behavior
```gdscript
func enter():
    if state_machine.previous_state.name == "Jump":
        play_landing_animation()
    else:
        play_idle_animation()
```

---

## Debugging Tips

- Add print statements in `change_state()` to track transitions.
- Use Godot's remote inspector to view current and previous state nodes.
- Ensure all states are added as children of the state machine node in the scene tree.

---

## Extending the State Machine

- Add new states by creating new child nodes with the required interface.
- Use `get_state_by_type` for robust transitions based on script type.
- States can store additional context or timers as needed.

---

## Summary

The PlayerStateMachine provides a clean, extensible framework for managing complex player behavior in Godot. By separating logic into distinct state nodes and handling transitions centrally, it enables maintainable, scalable code for any game project.

