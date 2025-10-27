# Maze Of Elements

A 2D action-adventure game built with Godot 4.5 where you explore mysterious mazes, battle slimes, and interact with friendly NPCs.

## About The Game

Maze Of Elements is a top-down adventure game featuring smooth combat mechanics, an inventory system, and dialog-driven storytelling. Navigate through different environments, defeat enemies, collect items, and uncover the secrets of the elemental mazes.

## Features

### Core Gameplay

- **Smooth Player Movement** - Responsive WASD controls with directional animations
- **Combat System** - Attack enemies with the X key, featuring proper hitboxes and damage feedback
- **Enemy AI** - Slimes that chase and attack the player with intelligent pathfinding
- **Health System** - Take damage from enemies, heal with potions, and manage your survival

### Systems

- **Inventory Management** - Collect and use items like health potions and gems
- **Dialog System** - Talk to NPCs with a polished typing effect and multi-line conversations
- **NPC Interactions** - Wandering NPCs with unique behaviors and dialog trees
- **Level Transitions** - Seamlessly move between different maps and areas
- **Pause Menu** - Full game pause system with inventory access

### Technical Features

- **State Machine Architecture** - Clean player and enemy state management
- **Component-Based Design** - Modular health, combat, and animation components
- **Camera System** - Smooth camera following with proper boundary constraints
- **Scene Management** - Professional scene transition system with fade effects

## How To Play

### Controls

- **WASD** - Move your character
- **X** - Attack
- **E** - Interact with NPCs and objects
- **ESC** - Pause menu
- **Space/Enter** - Advance dialog

### Getting Started

1. Launch the game from Godot or run the exported executable
2. Use WASD to explore the world
3. Press E to talk to NPCs when the interaction prompt appears
4. Press X to attack slimes and other enemies
5. Collect items and manage your inventory through the pause menu

## Building The Game

### Requirements

- Godot Engine 4.5 (Mono version if using C# scripts)
- Git (for version control)

### Running From Source

1. Clone this repository
2. Open Godot 4.5
3. Click "Import" and select the `project.godot` file
4. Press F5 to run the game

### Exporting

1. Open the project in Godot
2. Go to Project → Export
3. Add an export preset for your target platform (Windows, Linux, macOS)
4. Click "Export Project"

## Project Structure

```
Maze Of Elements/
├── 00_Global/          # Core managers and autoload scripts
├── Player/             # Player character, states, and components
├── Enemies/            # Enemy scripts and scenes (Slimes, etc.)
├── NPC/                # NPC system with dialog and behaviors
├── GUI/                # User interface (menus, dialogs, HUD)
├── Scenes/             # Game levels and maps
├── Items/              # Collectible items and effects
├── Interaction/        # Dialog and interaction systems
└── Resources/          # Sprites, audio, and art assets
```

## Key Systems

### Player State Machine

The player uses a robust state machine with distinct states:

- **Idle** - Standing still
- **Walk** - Moving around
- **Attack** - Performing attacks

### Enemy AI

Enemies feature component-based architecture:

- **Health Component** - Damage tracking and death
- **Combat Component** - Attack logic and cooldowns
- **Animation Component** - Direction-based sprite animation

### Dialog System

Professional dialog system with:

- Typing effect for text reveal
- Multiple dialog items per conversation
- NPC name display
- Interaction prompts with smooth animations

## Development Notes

This game was built using modern Godot practices including:

- Autoload singletons for global managers
- Signal-based communication between systems
- Component architecture for reusable logic
- State machines for complex behavior
- Resource-based data storage

## Known Issues & Future Improvements

- Combat could use more attack varieties
- Additional enemy types would add variety
- Quest system would give more direction
- Sound effects and music need expansion

## Credits

Built with Godot Engine 4.5
