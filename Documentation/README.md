MazeOfElement Project Documentation

Overview
This project is a top down grid based character controller built with Godot 4 and C#. The player moves one cell at a time on a grid. Input is buffered so a short tap causes the player to turn and a held press starts walking. Movement, animation, and input are separate small scripts plugged together in the Player scene.

Core Concepts
Player scene composition
Player2D (CharacterBody2D) root
  Input (PlayerInput script) handles reading actions and emitting Walk and Turn signals
  Movement (CharacterMovement script) consumes those signals and moves the character by grid cells
  AnimatedSprite2D (CharacterAnimation script) listens to movement to play correct animations
  Collider (CollisionShape2D) defines hit box
  Camera2D follows player

Grid system
Globals singleton exports GRID_SIZE (default 16). All movement snaps to this size. A move sets a target position that is exactly one grid cell away from the current position in the pressed direction. When reaching it the position is snapped cleanly back to grid to eliminate drift.

Input flow
PlayerInput polls Godot actions ui_up ui_down ui_left ui_right. Direction is updated to the last pressed direction. When a direction key is initially pressed HoldTime starts at zero. If the key is released before HoldThreshold (default 0.08 seconds) a Turn signal is emitted which only updates facing direction and idle animation. If the key remains held past the threshold a Walk signal is emitted and Movement begins a step. While a step is in progress additional walk emissions are blocked until the character arrives.

Movement logic
CharacterMovement receives Walk and Turn signals. On Walk it sets TargetPosition = current position + Direction * GRID_SIZE. Each frame in _Process it moves the character toward TargetPosition using MoveToward with a speed of GRID_SIZE * MoveSpeed. When distance to TargetPosition < 1 it calls StopWalking which snaps to the nearest grid cell and sets IsWalking false then emits idle animation if not already emitted. Turn simply triggers a turn animation without displacement.

Animation system
CharacterAnimation keeps an enum of animation states (idle walk turn in four directions). It listens to the Movement Animation signal events (walk turn idle) and picks the specific directional variant based on the current Direction from CharacterInput. Only when the enum changes does it call Play to avoid restarting the same animation every frame.

Collision detection
Currently movement is optimistic: it checks a CollisionDetected flag before starting a move. RayCast2D logic (CharacterCollisionRayCast) can set CollisionDetected or you can expand with Godot physics queries. If a collision is detected before starting a step the Walk emission is ignored.

Signals
CharacterInput emits Walk Turn
CharacterMovement emits Animation (string) values: walk turn idle
Other systems listen to Animation to sync visuals.

Extensibility suggestions
Add interaction: another signal from Input for Interact and a system to query overlapping areas
Add state abstraction later only if multiple locomotion modes are needed
Add pathfinding: feed a queue of Directions into the same movement API
Add diagonal or smooth movement by removing grid snap and using velocity instead of MoveToward

Adding a new skin
Provide a new SpriteFrames resource with same animation names (idle_down walk_left etc). Assign it to the AnimatedSprite2D in the Player scene or swap at runtime by setting AnimatedSprite2D.SpriteFrames.

Typical frame
1 Input updates Direction and decides to emit Walk or Turn
2 Movement consumes Walk sets TargetPosition starts moving
3 Animation receives Animation signal chooses directional variant and plays it
4 Movement finishes step snaps to grid emits idle

Troubleshooting
Player does not move: ensure Input node script still emits Walk (HoldTime must exceed threshold) and Movement Character property points to Player2D
Animation stuck idle: verify Animation signal connections and that Direction updates
Stops short: adjust MoveSpeed or ensure GRID_SIZE matches intended tiles and snapping threshold (<1) is appropriate

File summary
Scripts/Core/Globals.cs global GRID_SIZE singleton
Scripts/gameplay/Player/CharacterInput.cs base input abstraction
Scripts/gameplay/Player/PlayerInput.cs concrete input with hold logic
Scripts/gameplay/Player/CharacterMovement.cs grid step movement and animation signal emission
Scripts/gameplay/Player/CharacterAnimation.cs chooses and plays animations based on movement + direction
Scenes/Player/player.tscn assembles the components

How to change grid size
Open Globals node in the scene tree. Adjust GRID_SIZE export. Movement snapping and step distance will adapt automatically. Adjust animation speed if pacing feels off.

How to add a new action
1 Define action in Project Settings Input Map
2 Read it in PlayerInput similarly to movement keys
3 Emit a new signal (export/define) to other components

Design philosophy
Keep components small single purpose
Use signals to decouple (input does not directly call movement methods)
Data like grid size lives in a single authoritative place (Globals)
Avoid premature abstraction (state machine removed until really needed)

Next improvements (optional)
Refactor collision to use CharacterBody2D.MoveAndCollide for exact contacts
Centralize animation names in a static class or enum to avoid typos
Unit test input timing logic by separating pure logic from Node code

End
