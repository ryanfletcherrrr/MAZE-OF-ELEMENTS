using Game.Core;
using Godot;

public partial class Player : CharacterBody2D
{
  [ExportCategory("Debug zone")]
  [Export] public float Speed = 60f;
  [Export] public int LEVEL;

  [ExportCategory("Health System")]
  [Export] public int MaxHealth = 100;
  [Export] public int AttackDamage = 25;

  [ExportCategory("Combat Settings")]
  [Export] public float AttackRange = 48f; // Extended attack range

  [Export] public SpriteFrames CHARACTER_SPRITE;
  // Direction chosen by the current state
  public Vector2 Direction { get; set; } = Vector2.Zero;
  // Last non-zero direction for idle facing
  public Vector2 LastDirection { get; set; } = Vector2.Down;

  private PlayerStateMachine stateMachine;
  private AnimatedSprite2D animator;
  private ProgressBar healthBar;

  // Health system
  public int CurrentHealth { get; private set; }
  public bool IsAlive => CurrentHealth > 0;


  public override void _Ready()
  {
    // Use default render ordering; don't force YSort or global Z overrides.
    // If needed, set these in the scene instead of code.
    LEVEL = 0;

    // Initialize health
    CurrentHealth = MaxHealth;

    // Get health bar reference
    healthBar = GetNodeOrNull<ProgressBar>("HealthBar");
    if (healthBar != null)
    {
      healthBar.MaxValue = MaxHealth;
      healthBar.Value = CurrentHealth;
    }

    stateMachine = GetNodeOrNull<PlayerStateMachine>("StateMachine");
    if (stateMachine != null)
    {
      stateMachine.Initialize(this);
    }

    ManageCharacter();

    // Connect AttackHitbox signal if it exists
    var attackHitbox = GetNodeOrNull<Area2D>("AttackHitbox");
    if (attackHitbox != null)
    {
      // Initially disable the attack hitbox
      attackHitbox.Monitoring = false;
      attackHitbox.Visible = false;

      // Disconnect any existing connections to avoid conflicts
      if (attackHitbox.IsConnected("body_entered", new Callable(GetNode("StateMachine/Attack"), "BodyEntered")))
      {
        attackHitbox.Disconnect("body_entered", new Callable(GetNode("StateMachine/Attack"), "BodyEntered"));
        GameLogger.Info("Disconnected old AttackHitbox signal");
      }

      // Connect the body_entered signal to Player
      attackHitbox.BodyEntered += OnAttackHitboxBodyEntered;
      GameLogger.Info("AttackHitbox connected successfully");
    }
    else
    {
      GameLogger.Warning("AttackHitbox not found in player scene - will use fallback attack system");
    }

    // Set up PlayerHitbox for receiving damage (if it doesn't exist, create it)
    var playerHitbox = GetNodeOrNull<Area2D>("PlayerHitbox");
    if (playerHitbox == null)
    {
      // Create PlayerHitbox programmatically
      playerHitbox = new Area2D();
      playerHitbox.Name = "PlayerHitbox";
      AddChild(playerHitbox);

      // Add collision shape
      var collisionShape = new CollisionShape2D();
      var circleShape = new CircleShape2D();
      circleShape.Radius = 12f;
      collisionShape.Shape = circleShape;
      playerHitbox.AddChild(collisionShape);

      GameLogger.Info("Created PlayerHitbox programmatically");
    }

    // Connect PlayerHitbox signals
    playerHitbox.BodyEntered += OnPlayerHitboxBodyEntered;
    playerHitbox.BodyExited += OnPlayerHitboxBodyExited;
    GameLogger.Info("PlayerHitbox connected successfully");
  }

  public override void _PhysicsProcess(double delta)
  {
    // Movement executed after state decides Direction
    Velocity = Direction * Speed;
    MoveAndSlide();
  }

  public override void _Input(InputEvent @event)
  {
    // Check for X key press to attack
    if (@event is InputEventKey keyEvent && keyEvent.Pressed && keyEvent.Keycode == Key.X)
    {
      PerformAttack();
    }

    // Test key to damage player (for debugging) - Press Z
    if (@event is InputEventKey testKeyEvent && testKeyEvent.Pressed && testKeyEvent.Keycode == Key.Z)
    {
      TakeDamage(10);
      GameLogger.Debug("Player took 10 test damage!");
    }
  }

  public void ManageCharacter()
  {
    //Get the AnimatedSprite2D node
    animator = GetNodeOrNull<AnimatedSprite2D>("AnimatedSprite2D");
    if (animator == null)
    {
      GameLogger.Warning("No character sprite framed detected");
      return;
    }


    if (CHARACTER_SPRITE == null)
    {
      animator.SpriteFrames = animator.SpriteFrames;
      return;
    }

    animator.SpriteFrames = CHARACTER_SPRITE;
    GameLogger.Info($"Detected level selection {CHARACTER_SPRITE} switching sprite to appropritate level");
  }

  // Health and Combat Methods
  public void TakeDamage(int damage)
  {
    if (!IsAlive) return;

    CurrentHealth = Mathf.Max(0, CurrentHealth - damage);
    GameLogger.Info($"Player took {damage} damage! Health: {CurrentHealth}/{MaxHealth}");

    // Update health bar
    UpdateHealthBar();

    if (!IsAlive)
    {
      Die();
    }
  }

  public void Heal(int amount)
  {
    if (!IsAlive) return;

    CurrentHealth = Mathf.Min(MaxHealth, CurrentHealth + amount);
    GameLogger.Info($"Player healed {amount}! Health: {CurrentHealth}/{MaxHealth}");

    // Update health bar
    UpdateHealthBar();
  }

  private void Die()
  {
    GameLogger.Info("Player died!");
    // Handle death (could restart level, show game over, etc.)
    // For now, just respawn with full health
    CurrentHealth = MaxHealth;
    UpdateHealthBar();
    GameLogger.Info("Player respawned!");
  }

  private void UpdateHealthBar()
  {
    if (healthBar != null)
    {
      healthBar.Value = CurrentHealth;

      // Change color based on health percentage
      float healthPercent = (float)CurrentHealth / MaxHealth;
      if (healthPercent > 0.6f)
        healthBar.Modulate = Colors.Green;
      else if (healthPercent > 0.3f)
        healthBar.Modulate = Colors.Yellow;
      else
        healthBar.Modulate = Colors.Red;
    }
  }

  private void AttackNearbyEnemies()
  {
    if (!IsAlive) return;

    // Enable the attack hitbox for collision-based damage
    var attackHitbox = GetNodeOrNull<Area2D>("AttackHitbox");
    if (attackHitbox != null)
    {
      // Position the attack hitbox in front of the player based on direction
      Vector2 attackDirection = LastDirection.Normalized();
      Vector2 hitboxOffset = attackDirection * 32; // 32 pixels in front of player
      attackHitbox.Position = hitboxOffset;

      // Rotate hitbox to match attack direction (optional, for visual consistency)
      attackHitbox.Rotation = attackDirection.Angle();

      // Enable the hitbox
      attackHitbox.Monitoring = true;
      attackHitbox.Visible = true;

      GameLogger.Debug($"Attack hitbox enabled at offset: {hitboxOffset}");

      // Disable hitbox after a short duration (attack animation length)
      GetTree().CreateTimer(0.3f).Timeout += () => {
        if (IsInstanceValid(attackHitbox))
        {
          attackHitbox.Monitoring = false;
          attackHitbox.Visible = false;
          GameLogger.Debug("Attack hitbox disabled");
        }
      };
    }
    else
    {
      GameLogger.Warning("AttackHitbox not found! Please add an Area2D named 'AttackHitbox' to the player scene");
      // No fallback attacks - only manual attacks with proper AttackHitbox
    }
  }

  private void PerformAttack()
  {
    if (!IsAlive) return;

    // Trigger attack state for animation
    if (stateMachine != null && stateMachine.CurrentState != null)
    {
      var attackState = stateMachine.GetState<Attack>();
      if (attackState != null)
      {
        stateMachine.ChangeState(attackState);
      }
    }

    // Also do immediate attack check
    AttackNearbyEnemies();
  }

  private void CreateAttackEffect(Vector2 position)
  {
    // Create a simple visual effect for the attack
    // You can replace this with a proper particle effect or sprite later
    GameLogger.Debug($"Attack effect at position: {position}");
  }

  // Signal handlers for hitbox
  public void BodyEntered(PhysicsBody2D body)
  {
    GameLogger.Debug($"Something entered players hitbox: {body.Name}");
    // Note: Manual attack with X key instead of automatic attack
  }

  public void BodyExited(PhysicsBody2D body)
  {
    GameLogger.Debug($"Left players hitbox: {body.Name}");
  }

  // Signal handler for AttackHitbox collision-based damage
  public void OnAttackHitboxBodyEntered(Node2D body)
  {
    if (!IsAlive) return;

    // Only damage enemies, not the player or other objects
    if (body != this && body.IsInGroup("enemies") && body.HasMethod("TakeDamage"))
    {
      body.Call("TakeDamage", AttackDamage);
      GameLogger.Info($"Player attack hitbox hit {body.Name} for {AttackDamage} damage!");

      // Visual feedback
      CreateAttackEffect(body.GlobalPosition);
    }
  }

  // Signal handlers for PlayerHitbox (for receiving damage from enemies)
  public void OnPlayerHitboxBodyEntered(Node2D body)
  {
    GameLogger.Debug($"Something entered player damage hitbox: {body.Name}");
    // This hitbox is just for detection - actual damage comes from enemy attack hitboxes
  }

  public void OnPlayerHitboxBodyExited(Node2D body)
  {
    GameLogger.Debug($"Left player damage hitbox: {body.Name}");
  }

}
