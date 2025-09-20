# Player System Documentation

## Table of Contents
1. [Introduction](#introduction)
2. [State Machine Overview](#state-machine-overview)
3. [StateMachine.cs Detailed Explanation](#statemachinecs-detailed-explanation)
   - [Class Structure](#class-structure)
   - [Initialization](#initialization)
   - [State Transitions](#state-transitions)
   - [Processing Methods](#processing-methods)
   - [Helper Methods](#helper-methods)
4. [Attack State (Attack.cs)](#attack-state-attackcs)
   - [Purpose](#purpose)
   - [Key Methods](#key-methods)
   - [Animation Handling](#animation-handling)
   - [State Transitions](#state-transitions-1)
5. [Idle State (Idle.cs)](#idle-state-idlecs)
   - [Purpose](#purpose-1)
   - [Key Methods](#key-methods-1)
   - [Input Handling](#input-handling)
   - [Animation Logic](#animation-logic)
6. [Walk State (Walk.cs)](#walk-state-walkcs)
   - [Purpose](#purpose-2)
   - [Key Methods](#key-methods-2)
   - [Movement Logic](#movement-logic)
   - [Animation Updates](#animation-updates)
7. [System Interaction and Flow](#system-interaction-and-flow)
   - [State Flow Diagram](#state-flow-diagram)
   - [Input Processing](#input-processing)
   - [Animation Coordination](#animation-coordination)
8. [Best Practices and Tips](#best-practices-and-tips)
9. [Troubleshooting](#troubleshooting)
10. [Conclusion](#conclusion)

## Introduction

The Player system in this Godot project implements a robust state-based architecture for character movement and actions. At its core is a finite state machine that manages different player states: Idle, Walk, and Attack. This design allows for clean separation of concerns, easy maintenance, and extensible behavior.

The system handles:
- Directional movement with keyboard input
- Attack animations triggered by mouse clicks
- Smooth state transitions
- Animation synchronization
- Input processing and validation

All components work together to create a responsive and intuitive player character that can move, idle, and attack seamlessly.

## State Machine Overview

A state machine is a behavioral design pattern that allows an object to alter its behavior when its internal state changes. In this implementation:

- **States** represent different modes of player behavior (Idle, Walk, Attack)
- **Transitions** occur based on input, conditions, or events
- **State Machine** manages the current state and handles transitions

### Benefits of This Approach:
1. **Modularity**: Each state encapsulates its own logic
2. **Maintainability**: Easy to add new states or modify existing ones
3. **Readability**: Clear separation of different behaviors
4. **Testability**: Individual states can be tested in isolation
5. **Extensibility**: New features can be added as new states

### Key Components:
- `PlayerStateMachine.cs`: The central controller
- `State.cs`: Abstract base class for all states
- `Idle.cs`, `Walk.cs`, `Attack.cs`: Concrete state implementations

## StateMachine.cs Detailed Explanation

The `PlayerStateMachine` class is the heart of the player system. It manages state transitions, handles processing, and coordinates between different states.

### Class Structure

```csharp
public partial class PlayerStateMachine : Node
{
    [Export] private NodePath _initialState;
    private Dictionary<string, State> _states;
    public State CurrentState { get; private set; }
    // ... methods
}
```

- `_initialState`: Exported NodePath to set the starting state in the Godot editor
- `_states`: Dictionary mapping state names to State instances
- `CurrentState`: Property holding the currently active state

### Initialization

The state machine initializes in the `_Ready()` method:

```csharp
public override void _Ready()
{
    _states = GetChildren().OfType<State>().ToDictionary(s => s.Name.ToString());

    foreach (var state in _states.Values)
    {
        state.Initialize(GetParent<Player>(), this);
    }

    CurrentState = GetNode<State>(_initialState);
    CurrentState.Enter();
}
```

1. **Collect States**: Finds all child nodes that inherit from `State`
2. **Initialize States**: Calls `Initialize()` on each state, passing the Player and StateMachine references
3. **Set Initial State**: Retrieves the initial state node and calls its `Enter()` method

### State Transitions

Transitions are handled by the `TransitionTo()` method:

```csharp
public void TransitionTo(string stateName)
{
    if (!_states.ContainsKey(stateName) || _states[stateName] == CurrentState)
    {
        return;
    }

    CurrentState.Exit();
    CurrentState = _states[stateName];
    CurrentState.Enter();
}
```

1. **Validation**: Checks if the target state exists and isn't already current
2. **Exit Current**: Calls `Exit()` on the current state
3. **Switch State**: Updates `CurrentState` to the new state
4. **Enter New**: Calls `Enter()` on the new state

### Processing Methods

The state machine delegates processing to the current state:

```csharp
public override void _Process(double delta)
{
    CurrentState?.Update(delta);
}

public override void _PhysicsProcess(double delta)
{
    CurrentState?.PhysicsUpdate(delta);
}
```

- `_Process()`: Called every frame for non-physics updates
- `_PhysicsProcess()`: Called every physics frame for movement and physics-related logic

### Helper Methods

States can request other states using `GetState<T>()`:

```csharp
public T GetState<T>() where T : State
{
    return _states.Values.OfType<T>().FirstOrDefault();
}
```

This allows states to transition to other states by type rather than name.

## Attack State (Attack.cs)

The Attack state handles the player's attack animation and ensures it plays once per trigger.

### Purpose

- Play directional attack animations based on player's facing direction
- Prevent movement interruption during attack
- Automatically return to walking state after animation completes
- Handle animation setup and cleanup

### Key Methods

#### Enter()
```csharp
public override void Enter()
{
    // Get animator reference
    // Determine attack animation name
    // Disable looping
    // Play animation
    // Connect to finished signal
}
```

1. **Setup Animator**: Gets reference to AnimatedSprite2D
2. **Animation Selection**: Chooses animation based on `Player.LastDirection`
3. **Loop Prevention**: Calls `SetAnimationLoop(false)` to ensure single play
4. **Playback**: Plays the animation
5. **Signal Connection**: Connects to `animation_finished` signal

#### Exit()
```csharp
public override void Exit()
{
    // Disconnect signal to prevent memory leaks
}
```

Cleans up signal connections when leaving the state.

#### OnAttackFinished()
```csharp
private void OnAttackFinished()
{
    Machine.ChangeState(Machine.GetState<Walk>());
}
```

Called when animation completes, transitions back to Walk state.

### Animation Handling

The `GetAttackAnimation()` method maps directions to animation names:

```csharp
private string GetAttackAnimation(Vector2 direction)
{
    if (direction == Vector2.Up) return "attack_up";
    if (direction == Vector2.Down) return "attack_down";
    if (direction == Vector2.Left) return "attack_left";
    if (direction == Vector2.Right) return "attack_right";
    return "attack_down"; // Default
}
```

### State Transitions

- **Entry**: Triggered by mouse click in Walk or Idle states
- **Exit**: Automatic after animation finishes
- **Next State**: Always returns to Walk state

## Idle State (Idle.cs)

The Idle state manages the player's stationary behavior and handles transitions to movement or attack.

### Purpose

- Display idle animations based on last facing direction
- Monitor for input to transition to movement
- Handle attack input from idle position
- Maintain player's last direction for animation consistency

### Key Methods

#### Enter()
```csharp
public override void Enter()
{
    Player.Direction = Vector2.Zero; // Stop movement
    // Setup animator
    // Play appropriate idle animation
}
```

1. **Stop Movement**: Sets player direction to zero
2. **Animation Setup**: Gets animator reference
3. **Idle Animation**: Plays idle animation matching last direction

#### PhysicsUpdate()
```csharp
public override State PhysicsUpdate(double delta)
{
    // Check for attack input
    if (Input.IsMouseButtonPressed(MouseButton.Left) && !_attackTriggered)
    {
        _attackTriggered = true;
        return Machine.GetState<Attack>();
    }

    // Check for movement input
    float horizontal = Input.GetAxis("ui_left", "ui_right");
    float vertical = Input.GetAxis("ui_up", "ui_down");

    if (horizontal != 0 || vertical != 0)
    {
        return Machine.GetState<Walk>();
    }

    // Update idle animation if needed
    // ... animation logic
}
```

1. **Attack Check**: Detects mouse click to trigger attack
2. **Movement Check**: Detects arrow key input to transition to Walk
3. **Animation Update**: Ensures correct idle animation is playing

#### Exit()
```csharp
public override void Exit()
{
    _attackTriggered = false; // Reset flag
}
```

Resets the attack trigger flag for next entry.

### Input Handling

The Idle state uses a trigger flag to prevent multiple attack triggers:

```csharp
private bool _attackTriggered = false;
```

- Set to `true` when attack is triggered
- Reset to `false` when exiting the state
- Prevents attack spam while mouse is held

### Animation Logic

Idle animations follow the same naming convention as walk animations:

```csharp
private string GetIdleAnimation(Vector2 direction)
{
    if (direction == Vector2.Up) return "idle_up";
    if (direction == Vector2.Down) return "idle_down";
    if (direction == Vector2.Left) return "idle_left";
    if (direction == Vector2.Right) return "idle_right";
    return "idle_down";
}
```

## Walk State (Walk.cs)

The Walk state handles player movement and walking animations.

### Purpose

- Process directional input from arrow keys
- Update player position based on input
- Play appropriate walking animations
- Handle attack input during movement
- Transition to idle when movement stops

### Key Methods

#### Enter()
```csharp
public override void Enter()
{
    // Setup animator reference
}
```

Simple setup of animator reference.

#### PhysicsUpdate()
```csharp
public override State PhysicsUpdate(double delta)
{
    // Check for attack
    if (Input.IsMouseButtonPressed(MouseButton.Left) && !_attackTriggered)
    {
        _attackTriggered = true;
        return Machine.GetState<Attack>();
    }

    // Process movement input
    float horizontal = Input.GetAxis("ui_left", "ui_right");
    float vertical = Input.GetAxis("ui_up", "ui_down");

    // Prevent diagonal movement
    if (horizontal != 0 && vertical != 0)
    {
        vertical = 0;
    }

    Vector2 dir = new Vector2(horizontal, vertical);
    Player.Direction = dir;

    // Update last direction
    if (dir != Vector2.Zero)
    {
        Player.LastDirection = dir;
    }

    // Transition to idle if no movement
    if (dir == Vector2.Zero)
    {
        return Machine.GetState<Idle>();
    }

    // Update walking animation
    UpdateAnimation();
}
```

1. **Attack Detection**: Checks for mouse click
2. **Input Processing**: Gets axis input and prevents diagonals
3. **Direction Update**: Sets player direction and last direction
4. **State Transition**: Returns to Idle if no input
5. **Animation**: Updates walking animation

#### Exit()
```csharp
public override void Exit()
{
    _attackTriggered = false;
    // Play idle animation for last direction
}
```

Resets trigger flag and transitions to idle animation.

### Movement Logic

The Walk state implements diagonal prevention:

```csharp
if (horizontal != 0 && vertical != 0)
{
    vertical = 0; // Prioritize horizontal
}
```

This ensures the player moves in cardinal directions only.

### Animation Updates

Walking animations are selected based on movement direction:

```csharp
private void UpdateAnimation()
{
    string anim = "walk_down";
    if (Player.Direction == Vector2.Up) anim = "walk_up";
    else if (Player.Direction == Vector2.Down) anim = "walk_down";
    else if (Player.Direction == Vector2.Left) anim = "walk_left";
    else if (Player.Direction == Vector2.Right) anim = "walk_right";

    if (animator.Animation != anim)
    {
        animator.Play(anim);
    }
}
```

## System Interaction and Flow

### State Flow Diagram

```
Idle <--> Walk
  ^         ^
  |         |
   --> Attack
```

- **Idle**: Stationary state, monitors for input
- **Walk**: Movement state, processes directional input
- **Attack**: Action state, plays animation and returns

### Input Processing

1. **Idle State**:
   - Checks for movement input → Transition to Walk
   - Checks for mouse click → Transition to Attack

2. **Walk State**:
   - Processes arrow key input → Updates direction
   - Checks for mouse click → Transition to Attack
   - No input → Transition to Idle

3. **Attack State**:
   - No input processing (locked during animation)
   - Animation finished → Transition to Walk

### Animation Coordination

- **Direction Tracking**: `Player.LastDirection` maintains facing direction
- **State-Specific Animations**: Each state manages its own animation set
- **Smooth Transitions**: Exit methods ensure proper animation handover

## Best Practices and Tips

1. **Animation Naming**: Consistent naming convention (idle/walk/attack + direction)
2. **State Isolation**: Keep state logic contained within state classes
3. **Input Validation**: Use flags to prevent input spam
4. **Resource Management**: Properly disconnect signals in Exit methods
5. **Error Handling**: Check for null references and missing animations

## Troubleshooting

### Common Issues:

1. **Stuck in Attack State**:
   - Check if attack animations have Loop disabled
   - Verify animation names match exactly
   - Ensure SpriteFrames has the animations

2. **No Movement**:
   - Check if Direction is being set correctly
   - Verify Player script is attached and running
   - Ensure no other scripts are overriding movement

3. **Animation Not Playing**:
   - Confirm AnimatedSprite2D is properly configured
   - Check animation names in SpriteFrames
   - Verify animator reference is not null

4. **Input Not Responding**:
   - Check input action names in project settings
   - Verify mouse button detection
   - Ensure no UI elements are capturing input

### Debug Tips:
- Add print statements in Enter/Exit methods
- Check Godot's debugger for state transitions
- Monitor Player.Direction and LastDirection values

## Conclusion

This player system demonstrates a clean, modular approach to character control using the state machine pattern. The separation of concerns allows for easy maintenance and extension. Each state handles its specific responsibilities while the state machine coordinates their interactions.

Key takeaways:
- State machines provide excellent organization for complex behaviors
- Proper signal management prevents memory leaks
- Input validation and flags prevent unwanted behavior
- Animation coordination requires careful state management

This architecture can be easily extended with new states for abilities like jumping, dashing, or special attacks by following the established patterns.
