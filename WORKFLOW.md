# Maze Of Elements - Workflow & Architecture Documentation

## Table of Contents
1. [Project Overview](#project-overview)
2. [Directory Structure](#directory-structure)
3. [Core Systems](#core-systems)
4. [Scripting Patterns](#scripting-patterns)
5. [Scene Hierarchy](#scene-hierarchy)
6. [Signal Flow](#signal-flow)
7. [State Management](#state-management)
8. [Common Workflows](#common-workflows)

---

## Project Overview

**Maze Of Elements** is a Godot-based game project that appears to involve maze navigation with elemental mechanics.

### Technology Stack
- **Engine**: Godot 4.x (GDScript)
- **Language**: GDScript (migrated from C#)
- **Architecture**: Node-based with Autoload singletons

---

## Directory Structure

```
Maze Of Elements/
├── 00_Global/              # Global autoload scripts and managers
│   ├── game_logger.gd      # Logging system (autoload)
│   └── game_logger.gd.uid  # Godot resource identifier
├── 01_Scenes/              # Game scenes (likely)
├── 02_Scripts/             # Gameplay scripts
├── 03_Assets/              # Art, audio, textures
├── 04_Resources/           # Custom resources (materials, themes)
├── 05_Prefabs/             # Reusable scene templates
└── project.godot           # Project configuration
```

### Directory Naming Convention
- **00_**: Global/Core systems (highest priority, loaded first)
- **01_**: Scenes (main game levels/menus)
- **02_**: Scripts (component behaviors)
- **03_**: Assets (media files)
- **04_**: Resources (Godot resource files)
- **05_**: Prefabs (reusable scene instances)

---

## Core Systems

### 1. GameLogger (Autoload Singleton)

**Location**: `00_Global/game_logger.gd`  
**Autoload Name**: `GameLogger` (accessible globally)

#### Purpose
Centralized logging system that provides consistent debug output across the entire game.

#### Features
- **4 Log Levels**: DEBUG, INFO, WARNING, ERROR
- **Timestamp Support**: All logs include HH:MM:SS timestamps
- **Godot Integration**: Warnings/errors use push_warning/push_error for editor highlighting
- **Runtime Control**: Adjustable log level to filter output

#### Usage Examples
```gdscript
# In any script:
GameLogger.debug("Player position: %s" % position)
GameLogger.info("Level loaded: %s" % level_name)
GameLogger.warning("Low health: %d" % health)
GameLogger.error("Failed to load save file!")
```

#### Log Level Filtering
```gdscript
# Set minimum log level (higher levels are silenced)
GameLogger.current_log_level = GameLogger.LogLevel.WARNING
# Now only WARNING and ERROR messages will print
```

#### Implementation Details
```gdscript
enum LogLevel { DEBUG, INFO, WARNING, ERROR }
var current_log_level: LogLevel = LogLevel.DEBUG

func _get_timestamp() -> String:
    # Returns current system time formatted as HH:MM:SS
    
func debug/info/warning/error(message: String) -> void:
    # Each method checks current_log_level before printing
    # WARNING and ERROR also call push_warning/push_error
```

---

## Scripting Patterns

### Autoload Pattern (Singleton)

**What it is**: Global scripts accessible from any scene/script without instantiation.

**How to create**:
1. Create a script extending Node
2. Go to Project Settings → Autoload
3. Add the script with a name (e.g., "GameLogger")

**When to use**:
- Game managers (AudioManager, SaveManager)
- Global state (score, inventory)
- Utility systems (logging, localization)

**Example Structure**:
```gdscript
# game_manager.gd (autoload as "GameManager")
extends Node

var score: int = 0
var current_level: int = 1

func _ready() -> void:
    GameLogger.info("GameManager initialized")

func add_score(points: int) -> void:
    score += points
    GameLogger.debug("Score increased by %d. Total: %d" % [points, score])
```

**Access from any script**:
```gdscript
# player.gd
extends CharacterBody2D

func collect_coin() -> void:
    GameManager.add_score(10)
```

---

### Signal-Based Communication

**What it is**: Godot's event system for decoupled communication between nodes.

**Pattern 1: Direct Connection**
```gdscript
# button.gd
signal button_pressed(button_id: int)

func _on_pressed() -> void:
    button_pressed.emit(1)

# parent_scene.gd
func _ready() -> void:
    $Button.button_pressed.connect(_on_button_pressed)

func _on_button_pressed(button_id: int) -> void:
    GameLogger.info("Button %d pressed" % button_id)
```

**Pattern 2: Event Bus (Recommended for global events)**
```gdscript
# event_bus.gd (autoload as "EventBus")
extends Node

signal player_died
signal level_completed(level_id: int)
signal game_paused(is_paused: bool)

# player.gd
func die() -> void:
    EventBus.player_died.emit()

# ui_manager.gd
func _ready() -> void:
    EventBus.player_died.connect(_show_death_screen)
```

---

### Component Pattern

**What it is**: Breaking functionality into small, reusable scripts.

```gdscript
# health_component.gd
extends Node
class_name HealthComponent

signal health_changed(new_health: int, max_health: int)
signal died

@export var max_health: int = 100
var current_health: int

func _ready() -> void:
    current_health = max_health
    GameLogger.debug("HealthComponent initialized with %d HP" % max_health)

func take_damage(amount: int) -> void:
    current_health = max(0, current_health - amount)
    health_changed.emit(current_health, max_health)
    GameLogger.debug("Took %d damage. Health: %d/%d" % [amount, current_health, max_health])
    
    if current_health <= 0:
        died.emit()
```

**Usage**:
```gdscript
# player.gd
extends CharacterBody2D

@onready var health_component: HealthComponent = $HealthComponent

func _ready() -> void:
    health_component.died.connect(_on_death)

func _on_enemy_hit() -> void:
    health_component.take_damage(10)
```

---

## Scene Hierarchy

### Typical Game Scene Structure

```
MainGame (Node2D/Node3D)
├── Player (CharacterBody2D)
│   ├── Sprite2D
│   ├── CollisionShape2D
│   ├── Camera2D
│   └── Components (Node)
│       ├── HealthComponent
│       ├── MovementComponent
│       └── InventoryComponent
├── Level (Node2D)
│   ├── TileMap
│   ├── Enemies (Node2D)
│   │   ├── Enemy1
│   │   └── Enemy2
│   └── Collectibles (Node2D)
├── UI (CanvasLayer)
│   ├── HUD
│   │   ├── HealthBar
│   │   └── ScoreLabel
│   └── PauseMenu
└── Managers (Node)
    ├── LevelManager
    └── AudioManager
```

### Node Communication Flow

```
Player detects collision
    ↓
Emits signal: player_hit(damage)
    ↓
HealthComponent receives signal → take_damage()
    ↓
Emits signal: health_changed(new_health)
    ↓
UI HUD receives signal → updates health bar
    ↓
If health <= 0: emits died signal
    ↓
GameManager receives died → show_game_over()
```

---

## Signal Flow

### Example: Player Takes Damage

```gdscript
# 1. Enemy script detects collision
# enemy.gd
func _on_body_entered(body: Node2D) -> void:
    if body.is_in_group("player"):
        body.damage(attack_power)
        GameLogger.debug("Enemy dealt %d damage" % attack_power)

# 2. Player receives damage
# player.gd
func damage(amount: int) -> void:
    health_component.take_damage(amount)

# 3. HealthComponent processes damage
# health_component.gd
func take_damage(amount: int) -> void:
    current_health -= amount
    health_changed.emit(current_health, max_health)
    GameLogger.warning("Player health: %d/%d" % [current_health, max_health])
    
    if current_health <= 0:
        died.emit()

# 4. UI updates
# hud.gd
func _ready() -> void:
    get_tree().get_first_node_in_group("player").health_component.health_changed.connect(_update_health_bar)

func _update_health_bar(current: int, maximum: int) -> void:
    health_bar.value = float(current) / maximum * 100

# 5. Game manager handles death
# game_manager.gd (autoload)
func _ready() -> void:
    EventBus.player_died.connect(_on_player_died)

func _on_player_died() -> void:
    GameLogger.error("Player died!")
    get_tree().change_scene_to_file("res://01_Scenes/game_over.tscn")
```

---

## State Management

### State Machine Pattern

```gdscript
# player_state_machine.gd
extends Node
class_name StateMachine

enum State { IDLE, WALKING, JUMPING, FALLING, ATTACKING }

var current_state: State = State.IDLE
var previous_state: State

func change_state(new_state: State) -> void:
    if current_state == new_state:
        return
    
    GameLogger.debug("State changed: %s → %s" % [State.keys()[current_state], State.keys()[new_state]])
    previous_state = current_state
    _exit_state(current_state)
    current_state = new_state
    _enter_state(new_state)

func _exit_state(state: State) -> void:
    match state:
        State.ATTACKING:
            # Clean up attack animation
            pass

func _enter_state(state: State) -> void:
    match state:
        State.JUMPING:
            # Start jump animation
            pass

func _process(delta: float) -> void:
    match current_state:
        State.IDLE:
            _process_idle(delta)
        State.WALKING:
            _process_walking(delta)
        # ...
```

---

## Common Workflows

### Adding a New Feature (Example: Collectible Coin)

1. **Create the scene**: `05_Prefabs/coin.tscn`
   - Area2D (root)
   - Sprite2D (coin graphic)
   - CollisionShape2D

2. **Create the script**: `02_Scripts/coin.gd`
```gdscript
extends Area2D

@export var points: int = 10

func _ready() -> void:
    body_entered.connect(_on_body_entered)
    GameLogger.debug("Coin spawned at %s" % position)

func _on_body_entered(body: Node2D) -> void:
    if body.is_in_group("player"):
        collect()

func collect() -> void:
    GameLogger.info("Coin collected! +%d points" % points)
    EventBus.coin_collected.emit(points)
    queue_free()
```

3. **Add signal to EventBus**: `00_Global/event_bus.gd`
```gdscript
signal coin_collected(points: int)
```

4. **Update GameManager**: `00_Global/game_manager.gd`
```gdscript
func _ready() -> void:
    EventBus.coin_collected.connect(_on_coin_collected)

func _on_coin_collected(points: int) -> void:
    add_score(points)
```

5. **Update UI**: `02_Scripts/hud.gd`
```gdscript
func _ready() -> void:
    EventBus.coin_collected.connect(_on_coin_collected)

func _on_coin_collected(points: int) -> void:
    score_label.text = "Score: %d" % GameManager.score
```

---

### Debugging Workflow

1. **Add logging statements**:
```gdscript
func _process(delta: float) -> void:
    GameLogger.debug("Player velocity: %s" % velocity)
```

2. **Check Godot Output panel**:
   - Look for `[DEBUG]`, `[INFO]`, `[WARNING]`, `[ERROR]` tags
   - Timestamps help track event order

3. **Use breakpoints**:
   - Click left of line numbers in script editor
   - Run in debug mode (F5)

4. **Remote debugger**:
   - Debug → Deploy with Remote Debug
   - View live scene tree and property values

---

### Best Practices

1. **Always use GameLogger instead of print()**
   - Provides timestamps
   - Filterable by log level
   - Better for production builds

2. **Use signals for cross-node communication**
   - Avoids hard coupling
   - Makes systems testable
   - Easy to add/remove listeners

3. **Group nodes logically**
   - `add_to_group("enemies")` in _ready()
   - Use `get_tree().get_nodes_in_group("enemies")`

4. **Export variables for designer tweaking**
   ```gdscript
   @export var speed: float = 100.0
   @export var jump_force: float = 300.0
   ```

5. **Use class_name for reusable scripts**
   ```gdscript
   extends Node
   class_name InventoryComponent
   ```

6. **Type hints everywhere**
   ```gdscript
   var health: int = 100
   func take_damage(amount: int) -> void:
   ```

---

## Next Steps

1. **Document additional systems as they're created**:
   - SaveManager
   - AudioManager
   - InputManager
   - SceneTransitioner

2. **Create a CONVENTIONS.md** for:
   - Naming conventions (snake_case, PascalCase)
   - File organization rules
   - Comment standards

3. **Build a component library**:
   - Reusable HealthComponent, MovementComponent, etc.
   - Document each component's API

4. **Set up automated testing** (GdUnit):
   - Test HealthComponent damage calculation
   - Test state machine transitions

---

## Troubleshooting

### Common Issues

**Issue**: "Invalid get index 'GameLogger'"
- **Cause**: Autoload not configured
- **Fix**: Project Settings → Autoload → Add game_logger.gd as "GameLogger"

**Issue**: Signals not connecting
- **Cause**: Typo in signal name or wrong node path
- **Fix**: Use code completion (Ctrl+Space), verify node exists in _ready()

**Issue**: Scenes not loading
- **Cause**: Incorrect file path
- **Fix**: Use `"res://01_Scenes/level.tscn"` format, check file exists

**Issue**: Variables not saving
- **Cause**: Not using @export or ConfigFile
- **Fix**: Add @export to inspector-editable vars, use SaveManager for persistence

---

## Resources

- **Godot Docs**: https://docs.godotengine.org
- **GDScript Style Guide**: https://docs.godotengine.org/en/stable/tutorials/scripting/gdscript/gdscript_styleguide.html
- **Project Repository**: [Add your git URL here]

---

**Last Updated**: 2024
**Maintainer**: [Your Name]
**Godot Version**: 4.x

