# Maze of Elements - University Project

## Project Overview
A top-down action RPG built in Godot 4 with C# and GDScript, featuring elemental-themed dungeons, combat, inventory management, and NPC interactions.

**Target Gameplay Duration:** 30 minutes
**Team Members:** [Add your team members]
**Submission Date:** [Add your deadline]

---

## Current Implementation Status

### ✅ Completed Features

#### Player System (C#)
- ✅ Character movement (4-directional with animations)
- ✅ Health system with progress bar
- ✅ Manual attack system (X key)
- ✅ State machine (Idle/Walk/Attack states)
- ✅ Attack hitbox with directional cone detection
- ✅ Damage feedback (hurt animations)

#### Enemy System (C#)
- ✅ Slime enemy with AI
- ✅ Contact damage system
- ✅ Health bars with color-coded feedback
- ✅ Chase/follow behavior
- ✅ Death animations and cleanup
- ✅ Flash effects for damage feedback

#### Map System
- ✅ Multiple existing maps (air temple, ancient cave, fire area, hidden forest, monk forest)
- ✅ Tiled map integration via YATI plugin
- ✅ Tileset atlases for various themes

#### Core Systems (C#)
- ✅ Camera system (GlobalCameraHandler)
- ✅ Logging system (GameLogger)
- ✅ Scene management structure

---

## 🚧 Required Features (To Complete)

### 1. **Dungeons with Chests** (HIGH PRIORITY)
**Estimated Time:** 8-10 hours
**Recommended Language:** **GDScript** (faster prototyping for level design)

#### What to Implement:
- [ ] **3-4 Elemental Dungeons** (Fire, Water, Earth, Air)
  - Each dungeon: 5-7 rooms
  - Progressive difficulty (more enemies as you advance)
  - Boss room at the end (reuse/scale up slime with more health)

- [ ] **Chest System**
  - Treasure chests scattered throughout dungeons
  - Different chest types: wooden (common), silver (rare), gold (legendary)
  - Drop tables: health potions, coins, equipment
  - Interaction prompt (E key to open)
  - Opening animation/sound effect

- [ ] **Locked Doors**
  - Require keys found in previous rooms/chests
  - Color-coded keys/doors for different dungeons

**GDScript Example for Chest:**
```gdscript
extends Area2D

@export var chest_type: String = "wooden"
@export var is_opened: bool = false

func _ready():
    body_entered.connect(_on_body_entered)

func _on_body_entered(body):
    if body.name == "Player" and not is_opened:
        if Input.is_action_just_pressed("interact"):
            open_chest()

func open_chest():
    is_opened = true
    # Play animation
    $AnimatedSprite2D.play("open")
    # Drop loot
    spawn_loot()
```

---

### 2. **Inventory System** (HIGH PRIORITY)
**Estimated Time:** 6-8 hours
**Recommended Language:** **GDScript** (faster UI development, built-in signals)

#### What to Implement:
- [ ] **Inventory UI**
  - Grid-based inventory (6x4 slots = 24 slots)
  - Item icons with tooltips
  - Stack system for consumables
  - Equipment slots (weapon, armor, accessory)
  - Open with Tab/I key

- [ ] **Item Types**
  - Consumables: Health Potions, Mana Potions, Speed Boosts
  - Equipment: Swords (increase attack), Armor (increase defense), Rings (special effects)
  - Key Items: Dungeon keys, quest items
  - Currency: Gold coins

- [ ] **Item Data System**
  - Create ItemResource class for item definitions
  - JSON/CSV file for item database
  - Pickup system (walk over items or open chests)

**GDScript Structure Suggestion:**
```gdscript
extends Node

const MAX_SLOTS = 24
var items = []
var gold = 0

func add_item(item_data: Dictionary, quantity: int = 1) -> bool:
    # Check for existing stack or empty slot
    for i in range(MAX_SLOTS):
        if items[i] != null and items[i].id == item_data.id:
            items[i].quantity += quantity
            emit_signal("inventory_updated")
            return true
    # Add to new slot
    return true

func use_item(slot_index: int):
    if items[slot_index] != null:
        match items[slot_index].type:
            "health_potion":
                get_node("/root/Player").heal(50)
            "speed_boost":
                get_node("/root/Player").apply_speed_boost()
        items[slot_index].quantity -= 1
        emit_signal("inventory_updated")
```

---

### 3. **NPC Dialogue System** (MEDIUM PRIORITY)
**Estimated Time:** 6-8 hours
**Recommended Language:** **GDScript** (easier for rapid dialogue scripting)

#### What to Implement:
- [ ] **Dialogue System**
  - Text box UI at bottom of screen
  - Character portrait/name display
  - Text scrolling effect (typewriter)
  - Multiple dialogue branches
  - Choice system (A/B options)

- [ ] **NPCs to Create**
  - **Village Elder:** Gives main quest, explains story
  - **Blacksmith:** Sells weapons and armor
  - **Innkeeper:** Heals player for gold, saves game
  - **Mysterious Traveler:** Gives hints about dungeon secrets
  - **Dungeon Guardian:** Optional boss hints

- [ ] **Dialogue Data**
  - JSON/CSV format for dialogue lines
  - Support for quest triggers
  - Conditional dialogue (changes after events)

**GDScript Example:**
```gdscript
extends Node2D

@export var npc_name: String = "Village Elder"
@export var dialogue_file: String = "res://Dialogues/elder.json"
var dialogue_data = []
var current_line = 0

func interact():
    DialogueManager.show_dialogue(npc_name, dialogue_data)
```

---

### 4. **Shop System** (MEDIUM PRIORITY)
**Estimated Time:** 4-5 hours
**Recommended Language:** **GDScript** (integrates easily with inventory)

#### What to Implement:
- [ ] **Shop UI**
  - Buy/Sell tabs
  - Item list with prices
  - Player gold display
  - Quantity selector
  - Confirmation dialogs

- [ ] **Shop Types**
  - **Weapon Shop:** Swords, daggers, staffs
  - **Armor Shop:** Light, medium, heavy armor
  - **Potion Shop:** Health, mana, buff potions
  - **General Store:** Keys, torches, misc items

- [ ] **Economic System**
  - Gold currency
  - Buy price vs Sell price (50% of buy price)
  - Limited stock system (optional)
  - Shop refresh after dungeons

**GDScript Integration:**
```gdscript
extends Control

@onready var inventory = get_node("/root/InventoryManager")
var shop_items = []

func _ready():
    load_shop_items("res://Data/weapon_shop.json")

func buy_item(item_data: Dictionary, quantity: int = 1):
    var cost = item_data.price * quantity
    if inventory.gold >= cost:
        inventory.gold -= cost
        inventory.add_item(item_data, quantity)
        update_ui()
        return true
    else:
        show_message("Not enough gold!")
        return false

func sell_item(slot_index: int):
    var item = inventory.items[slot_index]
    var sell_price = item.price * 0.5  # 50% of buy price
    inventory.gold += sell_price
    inventory.remove_item(slot_index)
```

---

## 🎮 Additional Features for 30-Min Gameplay

### 5. **Quest System** (Ties Everything Together)
**Estimated Time:** 6-8 hours
**Recommended Language:** **GDScript** (easier data management with dictionaries)

- [ ] **Main Quest Chain** (15-20 minutes)
  1. Talk to Elder → Learn about elemental corruption
  2. Clear Fire Dungeon → Obtain Fire Crystal
  3. Clear Water Dungeon → Obtain Water Crystal
  4. Clear Earth Dungeon → Obtain Earth Crystal
  5. Clear Air Dungeon → Obtain Air Crystal
  6. Final Boss Fight → Restore balance

- [ ] **Side Quests** (10-15 minutes)
  - Collect 10 slime cores for blacksmith
  - Find lost items for NPCs
  - Defeat X number of enemies
  - Discover secret areas

**Quest Tracker UI:**
- Active quest display (top right)
- Quest log menu
- Objective markers on map

---

### 6. **Combat Enhancements**
**Estimated Time:** 4-5 hours
**Recommended Language:** **C#** (extends existing combat)

- [ ] **More Enemy Types**
  - Fire Slime (in Fire Dungeon) - faster attacks
  - Ice Slime (in Water Dungeon) - slows player
  - Rock Golem (in Earth Dungeon) - high HP, slow
  - Wind Spirit (in Air Dungeon) - teleports

- [ ] **Boss Enemies**
  - Elemental guardians (one per dungeon)
  - Unique attack patterns
  - Phase transitions (50% health = new attacks)
  - Boss health bar UI

- [ ] **Combat Polish**
  - Knockback on hit
  - Screen shake
  - Particle effects for attacks
  - Sound effects

---

### 7. **Save/Load System**
**Estimated Time:** 3-4 hours
**Recommended Language:** **GDScript** (JSON serialization built-in)

- [ ] Save player position, health, inventory
- [ ] Save quest progress, opened chests
- [ ] Auto-save at checkpoints
- [ ] Manual save at inns/save points
- [ ] Use `FileAccess.open()` and JSON for easy save data

---

### 8. **Audio & Polish**
**Estimated Time:** 3-4 hours
**Recommended Language:** **GDScript** (audio players)

- [ ] Background music (menu, village, dungeons, boss fights)
- [ ] Sound effects (attacks, footsteps, UI clicks, chest open)
- [ ] Ambient sounds (dungeon atmosphere)
- [ ] Volume settings

---

## 📋 Development Timeline (6-8 Weeks)

### Week 1-2: Core Gameplay Loop
- [ ] Dungeons with basic layout (GDScript)
- [ ] Chest system (GDScript)
- [ ] Basic inventory system (C#)

### Week 3-4: Content & Systems
- [ ] NPC dialogue system (GDScript)
- [ ] Shop system (GDScript)
- [ ] Quest system foundation (GDScript)
- [ ] Additional enemy types (C# - extends existing enemy code)

### Week 5-6: Integration & Polish
- [ ] Quest implementation (GDScript)
- [ ] Boss fights (C# - copy slime and scale up)
- [ ] Save/Load system (GDScript)
- [ ] UI polish (GDScript)

### Week 7-8: Testing & Balancing
- [ ] Playtest 30-min gameplay loop
- [ ] Balance difficulty
- [ ] Bug fixing
- [ ] Audio implementation
- [ ] Final polish

---

## 🛠️ Language Recommendations

### Use **C#** for:
✅ **Player/Enemy combat logic** (already implemented)
✅ **Enemy AI behaviors** (complex state machines)
✅ **Damage calculations** (performance-critical)
✅ **Core game managers** (if you need strong typing)

**Why:** Already established for player/enemy, better performance for combat-heavy calculations, strong typing helps with complex logic

### Use **GDScript** for:
✅ **Everything else!** (seriously, use GDScript for 90% of your project)
✅ **Inventory system** (dictionaries & arrays are super easy)
✅ **Quest system** (JSON loading, state management)
✅ **Shop system** (UI and data handling)
✅ **Save/Load** (built-in JSON serialization)
✅ **Level design & dungeon layout** (faster iteration)
✅ **Chest interactions** (simple scene scripts)
✅ **NPC dialogue** (quick scripting)
✅ **UI scenes** (built-in Godot UI integration)
✅ **Audio managers** (simpler with Godot's audio nodes)
✅ **Particle effects & visual polish**

**Why:**
- **3-5x faster development** than C# for UI/data systems
- **No compilation time** (instant testing)
- **Better Godot integration** (signals, nodes, resources)
- **Easier debugging** (print statements everywhere!)
- **Less boilerplate** (no type declarations if you don't want them)
- **Perfect for prototyping** (change and test immediately)
- **Your team can read/edit it easily** (Python-like syntax)

---

## 📦 Suggested File Structure

```
Maze Of Elements/
├── Scenes/
│   ├── Player/
│   ├── Enemies/ (Slime, FireSlime, IceSlime, Golem, etc.)
│   ├── Maps/
│   │   ├── Village.tscn (hub area)
│   │   ├── FireDungeon.tscn
│   │   ├── WaterDungeon.tscn
│   │   ├── EarthDungeon.tscn
│   │   └── AirDungeon.tscn
│   ├── UI/
│   │   ├── InventoryUI.tscn
│   │   ├── ShopUI.tscn
│   │   ├── DialogueBox.tscn
│   │   └── QuestTracker.tscn
│   └── Objects/
│       ├── Chest.tscn (GDScript)
│       ├── Door.tscn (GDScript)
│       └── NPC.tscn (GDScript)
├── Scripts/
│   ├── Core/ (C#)
│   │   ├── InventoryManager.cs
│   │   ├── QuestManager.cs
│   │   ├── ShopManager.cs
│   │   └── SaveManager.cs
│   └── Utilities/ (C#)
├── Resources/
│   ├── Items/ (ItemData resources)
│   ├── Quests/ (QuestData resources)
│   └── Dialogues/ (JSON/CSV files)
└── Audio/
    ├── Music/
    ├── SFX/
    └── Ambient/
```

---

## 🎯 Priority Order (Limited Time)

If you're short on time, implement in this order:

### Must-Have (Core Loop - 20 hours)
1. **One complete dungeon** with chests (Fire Dungeon)
2. **Basic inventory** (add/remove items, use potions)
3. **One shop** (buy health potions and basic sword)
4. **One quest** (clear Fire Dungeon)

### Should-Have (Extends Gameplay - 15 hours)
5. **Second dungeon** (Water Dungeon)
6. **NPC dialogue** (2-3 NPCs minimum)
7. **More enemy variety** (2 new enemy types)
8. **Quest tracker UI**

### Nice-to-Have (Polish - 10 hours)
9. **Third dungeon** (Earth Dungeon)
10. **Boss fights** (one per dungeon)
11. **Save system**
12. **Audio & effects**

---

## 🧪 Testing Checklist

Before submission, ensure:
- [ ] Can complete full gameplay loop in 25-30 minutes
- [ ] No game-breaking bugs
- [ ] Save/Load works correctly
- [ ] All UI elements are functional
- [ ] Inventory doesn't overflow/crash
- [ ] Enemies spawn correctly
- [ ] Chests don't respawn after opening
- [ ] Quest progression works linearly
- [ ] Player can't get softlocked

---

## 📝 Documentation for Submission

Include with your project:
1. **This README.md** (design document)
2. **Controls.md** (WASD movement, X attack, E interact, Tab inventory, ESC menu)
3. **Credits.md** (team members, asset sources, tools used)
4. **Known Issues.md** (document any known bugs you couldn't fix)
5. **Playthrough Guide.md** (how to complete the game)

---

## 🚀 Quick Start for New Features

### Adding a New Chest (GDScript):
1. Create `Chest.tscn` with Area2D, AnimatedSprite2D, CollisionShape2D
2. Attach `chest.gd` script
3. Place in dungeon scene
4. Configure loot table in inspector

### Adding a New Item (C#):
1. Create ItemData resource file
2. Add to item database (JSON/CSV)
3. Add icon to `Resources/Items/Icons/`
4. Reference in chest loot tables

### Adding a New NPC (GDScript):
1. Create `NPC.tscn` with CharacterBody2D, AnimatedSprite2D
2. Attach `npc.gd` script
3. Create dialogue JSON file
4. Place in village/dungeon

---

## 🎓 University Project Tips

1. **Use Version Control:** Commit frequently with clear messages
2. **Divide Tasks:** Assign team members to specific systems
3. **Daily Standups:** 10-min check-ins on progress
4. **Playtest Weekly:** Catch issues early
5. **Keep Scope Realistic:** Better to polish 4 features than rush 10
6. **Document As You Go:** Don't leave documentation for last week
7. **Asset Sources:** Use free assets from itch.io, OpenGameArt (credit them!)
8. **Build Buffer Time:** Leave 1 week for unexpected bugs

---

## 📞 Team Contact & Roles

| Name | Role | Responsibilities |
|------|------|------------------|
| [Name] | Lead Programmer | C# systems, combat |
| [Name] | Level Designer | GDScript, dungeons, chests |
| [Name] | UI/UX | Inventory, menus, dialogue |
| [Name] | Audio/Polish | Sound, effects, testing |

---

## 🏆 Success Criteria

Your project should demonstrate:
- ✅ Complete gameplay loop (start to finish)
- ✅ Multiple interconnected systems
- ✅ Clear player progression
- ✅ Polished core mechanics
- ✅ 30 minutes of engaging content
- ✅ Bug-free submission build

---

---

## ⚡ Quick Decision Guide: GDScript vs C#

**When in doubt, use GDScript!** Here's the simple rule:

| Feature | Language | Why |
|---------|----------|-----|
| Player combat | C# ✅ | Already done |
| Enemy AI | C# ✅ | Already done |
| New enemy types | C# ✅ | Copy existing slime code |
| Inventory | GDScript 🚀 | Arrays/Dicts are easy |
| Quests | GDScript 🚀 | JSON data loading |
| Shop | GDScript 🚀 | UI integration |
| Dialogue | GDScript 🚀 | Simple scripting |
| Save/Load | GDScript 🚀 | Built-in JSON |
| Chests | GDScript 🚀 | Scene scripts |
| Dungeons | GDScript 🚀 | Level design |
| UI | GDScript 🚀 | Godot nodes |
| Audio | GDScript 🚀 | Simple audio players |

**The 80/20 Rule:** Use C# for the 20% (combat), GDScript for the 80% (everything else)

---

**Last Updated:** October 2, 2025
**Godot Version:** 4.x
**Target Platform:** Windows/Linux/Mac

Good luck with your project! 🎮✨

**Pro Tip:** Don't overthink the language choice. If you're not sure, pick GDScript. You can always optimize later if needed (which you won't need to for a 30-min game).
