using Game.Core;
using Godot;

public partial class Attack : State
{
    private AnimatedSprite2D animator;
    private Timer attackTimer;

    public override void Enter()
    {
        // Get animator
        animator = Player.GetNodeOrNull<AnimatedSprite2D>("AnimatedSprite2D");

        if (animator != null)
        {
            // Play attack animation
            string animName = GetAttackAnimation(Player.LastDirection);
            if (animator.SpriteFrames.HasAnimation(animName))
            {
                animator.Play(animName);

                // Start timer for attack duration (adjust time as needed)
                attackTimer = new Timer();
                attackTimer.WaitTime = 0.5f; // 0.5 seconds attack duration
                attackTimer.OneShot = true;
                attackTimer.Timeout += OnAttackFinished;
                AddChild(attackTimer);
                attackTimer.Start();
            }
            else
            {
                // No animation, go back immediately
                Machine.ChangeState(Machine.GetState<Walk>());
            }
        }
    }

    public override void Exit()
    {
        // Clean up timer
        if (attackTimer != null)
        {
            attackTimer.QueueFree();
            attackTimer = null;
        }
    }

    private void OnAttackFinished()
    {
        // Attack done, go back to walking
        Machine.ChangeState(Machine.GetState<Walk>());
    }

    private string GetAttackAnimation(Vector2 direction)
    {
        if (direction == Vector2.Up) return "attack_up";
        if (direction == Vector2.Down) return "attack_down";
        if (direction == Vector2.Left) return "attack_left";
        if (direction == Vector2.Right) return "attack_right";
        return "attack_down";
    }
}
