using Game.Core;
using Godot;
using System.Collections.Generic; // Required for List
using System;

public partial class Slime : CharacterBody2D
{
    [ExportCategory("Slime Settings")]
    [Export] SpriteFrames SlimeImage;
    [Export] public float Speed = 30f; // Movement speed
    [Export] public float FollowDistance = 64f; // Distance to start following
    [Export] public float StopDistance = 32f; // Distance to stop moving
    [Export] public float ResumeDistance = 48f; // Distance to resume movement (prevents oscillation)
    [Export] public float AttackDistance = 24f; // Distance to start attacking
    [Export] public float AttackCooldown = 1.5f; // Time between attacks

    private AnimatedSprite2D EnemyAnimation;
    private Timer IdleTimer;
    private List<string> IdleAnimations = new List<string>() { "idle_down", "idle_left", "idle_up", "idle_right" };
    private Random Random = new Random();

    // Player following variables
    private CharacterBody2D player;
    private Vector2 lastDirection = Vector2.Down;
    private bool playerChase = false;
    private string currentAnimation = "";
    private bool isStopped = false; // Track if slime is currently stopped to prevent oscillation

    // Attack variables
    private bool isAttacking = false;
    private float attackTimer = 0f;


    public override void _Ready()
    {
        GameLogger.Info("Loaded in Enemies!");
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
        // When attack animation finishes, return to appropriate state
        if (isAttacking)
        {
            isAttacking = false;
            currentAnimation = ""; // Reset so next animation change will trigger

            // Return to idle or movement based on current state
            if (playerChase && player != null)
            {
                float distanceToPlayer = Position.DistanceTo(player.Position);
                if (distanceToPlayer <= StopDistance)
                {
                    PlayIdleAnimation();
                    currentAnimation = "idle";
                }
            }
            else
            {
                PlayIdleAnimation();
                currentAnimation = "idle";
            }
        }
    }

    private void PlayRandomIdleAnimation()
    {
        if (IdleAnimations.Count > 0)
        {
            int randomIndex = Random.Next(0, IdleAnimations.Count);
            string selectedAnimation = IdleAnimations[randomIndex];
            if (EnemyAnimation.SpriteFrames == null)
            {
                GameLogger.Info("Invalid sprite");
                return;
            }
            else
            {
                // Don't reassign SpriteFrames every time - it's already set in _Ready
                EnemyAnimation.Play(selectedAnimation);
            }
        }
    }

    private void PlayIdleAnimation()
    {
        PlayRandomIdleAnimation();
    }

    private void PlayAttackAnimation(Vector2 direction)
    {
        string attackAnimation = "";

        // Determine which attack animation to play based on direction to player
        if (Mathf.Abs(direction.Y) > Mathf.Abs(direction.X))
        {
            // Vertical direction is dominant
            if (direction.Y < 0)
            {
                attackAnimation = "attack_up";
            }
            else
            {
                attackAnimation = "attack_down";
            }
        }
        else
        {
            // Horizontal direction is dominant
            if (direction.X < 0)
            {
                attackAnimation = "attack_left";
            }
            else
            {
                attackAnimation = "attack_right";
            }
        }

        // Play the attack animation
        if (attackAnimation != currentAnimation)
        {
            EnemyAnimation.Play(attackAnimation);
            currentAnimation = attackAnimation;
            isAttacking = true;
            attackTimer = AttackCooldown;
            GameLogger.Info("Slime attacking with: " + attackAnimation);
        }
    }

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

    public override void _Process(double delta)
    {
        base._Process(delta);

    }

    public override void _PhysicsProcess(double delta)
    {
        // Update attack timer
        if (attackTimer > 0)
        {
            attackTimer -= (float)delta;
            if (attackTimer <= 0)
            {
                isAttacking = false;
            }
        }

        if (playerChase && player != null)
        {
            // Calculate distance to player
            float distanceToPlayer = Position.DistanceTo(player.Position);
            Vector2 directionToPlayer = (player.Position - Position).Normalized();

            // Check if close enough to attack
            if (distanceToPlayer <= AttackDistance && !isAttacking && attackTimer <= 0)
            {
                // Attack the player
                PlayAttackAnimation(directionToPlayer);
                Velocity = Vector2.Zero; // Stop moving during attack
            }
            // Use hysteresis to prevent oscillation for movement
            else if (!isStopped && distanceToPlayer <= StopDistance && !isAttacking)
            {
                // Too close - stop moving (but not attacking)
                isStopped = true;
                Velocity = Vector2.Zero;
                if (currentAnimation != "idle" && !isAttacking)
                {
                    PlayIdleAnimation();
                    currentAnimation = "idle";
                }
            }
            else if (isStopped && distanceToPlayer >= ResumeDistance)
            {
                // Far enough away - resume movement
                isStopped = false;
            }

            // Only move if not stopped and not attacking
            if (!isStopped && !isAttacking)
            {
                // Smooth speed adjustment based on distance
                float speedMultiplier = Mathf.Clamp(distanceToPlayer / FollowDistance, 0.2f, 1.0f);

                // Apply some smoothing to prevent jittery movement
                Vector2 targetVelocity = directionToPlayer * Speed * speedMultiplier;
                Velocity = Velocity.Lerp(targetVelocity, 8.0f * (float)delta);

                MoveAndSlide();

                // Update animation based on movement direction
                ChangeAnimation(directionToPlayer);
            }
            else if (!isAttacking)
            {
                // Gradually stop movement for smoother deceleration
                Velocity = Velocity.Lerp(Vector2.Zero, 10.0f * (float)delta);
                if (Velocity.Length() > 0.1f)
                {
                    MoveAndSlide();
                }
            }
        }
        else
        {
            // Stop moving and play idle animation
            Velocity = Velocity.Lerp(Vector2.Zero, 5.0f * (float)delta);
            if (Velocity.Length() > 0.1f)
            {
                MoveAndSlide();
            }
            isStopped = false; // Reset stopped state when not chasing
            isAttacking = false; // Reset attack state when not chasing
            attackTimer = 0f;
        }
    }

    /**
    * Signal for entering Body Node2D and exit
    */
    public void BodyEntered(Node2D body)
    {
        GameLogger.Info("Body entered slime range: " + body.Name);

        // Only chase if it's a CharacterBody2D (likely the player)
        if (body is CharacterBody2D characterBody)
        {
            player = characterBody;
            playerChase = true;
            GameLogger.Info("Player chase started");
        }
    }

    public void BodyExit(Node2D body)
    {
        GameLogger.Info("Body exited slime range: " + body.Name);

        // Only stop chasing if the exiting body was our target
        if (body == player)
        {
            player = null;
            playerChase = false;
            currentAnimation = ""; // Reset animation state
            isStopped = false; // Reset stopped state
            isAttacking = false; // Reset attack state
            attackTimer = 0f; // Reset attack timer
            GameLogger.Info("Player chase stopped");
        }
    }

}
