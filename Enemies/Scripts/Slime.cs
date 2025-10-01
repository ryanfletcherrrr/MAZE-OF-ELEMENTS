using Game.Core;
using Godot;
using System.Collections.Generic; // Required for List
using System;

public partial class Slime : CharacterBody2D
{
    [ExportCategory("Slime Settings")]
    [Export] SpriteFrames SlimeImage;
    [Export] public float Speed = 50f; // Faster movement speed for contact damage
    [Export] public float FollowDistance = 80f; // Distance to start following
    [Export] public float ContactDamageCooldown = 1.0f; // Time between contact damage

    [ExportCategory("Health System")]
    [Export] public int MaxHealth = 50;
    [Export] public int AttackDamage = 15;

    private AnimatedSprite2D EnemyAnimation;
    private Timer IdleTimer;
    private ProgressBar healthBar;
    // Removed IdleAnimations - slime is always moving toward player for contact damage
    private Random Random = new Random();

    // Player following variables
    private CharacterBody2D player;
    private Vector2 lastDirection = Vector2.Down;
    private bool playerChase = false;
    private string currentAnimation = "";
    private bool isStopped = false; // Track if slime is currently stopped to prevent oscillation

    // Contact damage variables
    private float contactDamageTimer = 0f;

    // Health system
    public int CurrentHealth { get; private set; }
    public bool IsAlive => CurrentHealth > 0;


    public override void _Ready()
    {
        GameLogger.Info("Loaded in Enemies!");

        // Add to enemies group for player targeting
        AddToGroup("enemies");

        // Initialize health
        CurrentHealth = MaxHealth;

        // Get health bar reference
        healthBar = GetNodeOrNull<ProgressBar>("HealthBar");
        if (healthBar != null)
        {
            healthBar.MaxValue = MaxHealth;
            healthBar.Value = CurrentHealth;
        }

        //Load the AnimatedSprite2D Node
        EnemyAnimation = GetNodeOrNull<AnimatedSprite2D>("Enemy");

        //Load the Timer node
        IdleTimer = GetNodeOrNull<Timer>("Timer");

        IdleTimer.Timeout += OnIdleTimerTimeout;

        if (EnemyAnimation == null)
        {
            GameLogger.Info("Something is wrong with Enemy Idle script..");
            return;
        }

        // Connect to animation finished signal
        EnemyAnimation.AnimationFinished += OnAnimationFinished;

        // Using simple distance-based attacks - no complex hitbox needed
        GameLogger.Info("Slime attack system initialized with distance-based detection");

        // Set sprite immediately on load
        if (SlimeImage != null)
        {
            EnemyAnimation.SpriteFrames = SlimeImage;
            PlayIdleAnimation();
            EnemyAnimation.Play();
        }

        // Start the idle timer
        IdleTimer.Start();
    }

    private void OnIdleTimerTimeout()
    {
        // Only play idle animations when not chasing
        if (!playerChase)
        {
            PlayRandomIdleAnimation();
        }
        IdleTimer.Start();
    }

    private void OnAnimationFinished()
    {
        // Simple animation handling - just continue with current behavior
        // No special attack animation handling needed
    }

    private void PlayRandomIdleAnimation()
    {
        // For contact damage system, slime should always face the player direction
        if (player != null)
        {
            Vector2 directionToPlayer = (player.Position - Position).Normalized();
            ChangeAnimation(directionToPlayer);
        }
        else
        {
            // Default animation if no player
            if (EnemyAnimation != null)
            {
                EnemyAnimation.Play("idle_down");
            }
        }
    }

    private void PlayIdleAnimation()
    {
        // For fast contact damage, slime should always face player direction
        PlayRandomIdleAnimation();
    }

    // No attack animations - slime now uses contact damage only

    private void ChangeAnimation(Vector2 direction)
    {
        // Only change animation if player is being chased
        if (!playerChase)
        {
            if (currentAnimation != "idle")
            {
                PlayIdleAnimation();
                currentAnimation = "idle";
            }
            return;
        }

        string newAnimation = "";

        // Determine which walk animation to play based on movement direction
        if (Mathf.Abs(direction.Y) > Mathf.Abs(direction.X))
        {
            // Vertical movement is dominant
            if (direction.Y < 0)
            {
                newAnimation = "walk_up";
            }
            else
            {
                newAnimation = "walk_down";
            }
        }
        else
        {
            // Horizontal movement is dominant
            if (direction.X < 0)
            {
                newAnimation = "walk_left";
            }
            else
            {
                newAnimation = "walk_right";
            }
        }

        // Only change animation if it's different from current one
        if (newAnimation != currentAnimation)
        {
            EnemyAnimation.Play(newAnimation);
            currentAnimation = newAnimation;
        }
    }



    public override void _PhysicsProcess(double delta)
    {
        // Don't do anything if dead
        if (!IsAlive) return;

        // Update contact damage timer
        if (contactDamageTimer > 0)
        {
            contactDamageTimer -= (float)delta;
        }

        if (playerChase && player != null)
        {
            // Calculate distance to player
            float distanceToPlayer = Position.DistanceTo(player.Position);
            Vector2 directionToPlayer = (player.Position - Position).Normalized();

            // Check for contact damage (when very close)
            if (distanceToPlayer <= 16f && contactDamageTimer <= 0f)
            {
                // Deal contact damage
                DealContactDamage();
                contactDamageTimer = ContactDamageCooldown;
            }

            // Always chase the player (no stopping)
            Vector2 targetVelocity = directionToPlayer * Speed;
            Velocity = Velocity.Lerp(targetVelocity, 10.0f * (float)delta);

            MoveAndSlide();

            // Update animation based on movement direction
            ChangeAnimation(directionToPlayer);
        }
        else
        {
            // Stop moving and play idle animation when not chasing
            Velocity = Velocity.Lerp(Vector2.Zero, 8.0f * (float)delta);
            if (Velocity.Length() > 0.1f)
            {
                MoveAndSlide();
            }

            if (currentAnimation != "idle")
            {
                PlayIdleAnimation();
                currentAnimation = "idle";
            }
        }
    }

    /**
    * Signal for entering Body Node2D and exit
    */
    public void BodyEntered(Node2D body)
    {
        // Only chase if it's specifically the player node (not other CharacterBody2D like slimes)
        if (body is CharacterBody2D characterBody && body.Name.ToString().Contains("Player"))
        {
            player = characterBody;
            playerChase = true;
            GameLogger.Info("Player chase started");
        }
    }

    public void BodyExit(Node2D body)
    {
        // Only stop chasing if the exiting body was our target
        if (body == player)
        {
            player = null;
            playerChase = false;
            currentAnimation = ""; // Reset animation state
            isStopped = false; // Reset stopped state
            GameLogger.Info("Player chase stopped");
        }
    }

    public void Enemy()
    {
        // Marker method for enemy identification
    }

    // Health and Combat Methods
    public void TakeDamage(int damage)
    {
        if (!IsAlive) return;

        CurrentHealth = Mathf.Max(0, CurrentHealth - damage);
        GameLogger.Info($"Slime took {damage} damage! Health: {CurrentHealth}/{MaxHealth}");

        // Update health bar
        UpdateHealthBar();

        // Flash red when taking damage (optional visual feedback)
        FlashDamage();

        if (!IsAlive)
        {
            Die();
        }
    }

    private void FlashDamage()
    {
        if (EnemyAnimation != null)
        {
            // Flash red briefly
            EnemyAnimation.Modulate = Colors.Red;
            var tween = CreateTween();
            tween.TweenProperty(EnemyAnimation, "modulate", Colors.White, 0.2f);
        }
    }

    private void Die()
    {
        GameLogger.Info("Slime died!");

        // Stop all actions
        playerChase = false;
        Velocity = Vector2.Zero;

        // Play death animation (if you have one)
        if (EnemyAnimation != null && EnemyAnimation.SpriteFrames.HasAnimation("death"))
        {
            EnemyAnimation.Play("death");
        }

        // Remove from scene after a short delay
        var timer = new Timer();
        timer.WaitTime = 1.0f;
        timer.OneShot = true;
        timer.Timeout += () => {
            GameLogger.Info("Slime removed from scene");
            QueueFree();
        };
        AddChild(timer);
        timer.Start();
    }

    private void DealContactDamage()
    {
        if (player == null || !player.HasMethod("TakeDamage")) return;

        // Deal contact damage
        player.Call("TakeDamage", AttackDamage);
        GameLogger.Info($"Slime contact damage hit {player.Name} for {AttackDamage} damage!");

        // Flash slime to show it dealt damage
        FlashAttack();

        // Flash player to show hurt animation (same effect as slime damage flash)
        FlashPlayerHurt();
    }

    private void FlashPlayerHurt()
    {
        if (player != null)
        {
            // Get player's animated sprite
            var playerSprite = player.GetNodeOrNull<AnimatedSprite2D>("AnimatedSprite2D");
            if (playerSprite != null)
            {
                // Flash red briefly (same as slime damage effect)
                playerSprite.Modulate = Colors.Red;
                var tween = CreateTween();
                tween.TweenProperty(playerSprite, "modulate", Colors.White, 0.3f);
            }
        }
    }    private void FlashAttack()
    {
        if (EnemyAnimation != null)
        {
            // Flash blue briefly to show attack
            EnemyAnimation.Modulate = Colors.Cyan;
            var tween = CreateTween();
            tween.TweenProperty(EnemyAnimation, "modulate", Colors.White, 0.3f);
        }
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

    // Simplified attack system - no complex collision detection needed

}
