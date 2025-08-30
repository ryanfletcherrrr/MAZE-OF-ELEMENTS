using Game.Core;
using Godot;

public partial class Walk : State
{
    private AnimatedSprite2D animator;

    public override void Enter()
    {
        if (animator == null)
        {
            animator = Player.GetNodeOrNull<AnimatedSprite2D>("AnimatedSprite2D");
            if (animator == null)
            {
                animator = Player.GetNodeOrNull<AnimatedSprite2D>("AnimatedSprite2D");
            }
        }
    }

    public override State PhysicsUpdate(double delta)
    {
        float horizontal = Input.GetAxis("ui_left", "ui_right");
        float vertical = Input.GetAxis("ui_up", "ui_down");
        if (horizontal != 0 && vertical != 0)
        {
            vertical = 0; // prevent diagonals
        }

        Vector2 dir = new Vector2(horizontal, vertical);
        Player.Direction = dir;

        if (dir != Vector2.Zero)
        {
            Player.LastDirection = dir;
        }

        if (dir == Vector2.Zero)
        {
            return Machine.GetState<Idle>();
        }
        if (animator != null)
        {
            string anim = "walk_down";
            if (dir == Vector2.Up)
            {
                anim = "walk_up";
            }
            else if (dir == Vector2.Down)
            {
                anim = "walk_down";
            }
            else if (dir == Vector2.Left)
            {
                anim = "walk_left";
            }
            else if (dir == Vector2.Right)
            {
                anim = "walk_right";
            }
            if (animator.Animation != anim)
            {
                animator.Play(anim);
            }
        }
        return null;
    }

    public override void Exit()
    {
        if (animator == null)
        {
            return;
        }
        string idle = "idle_down";
        Vector2 face = Player.LastDirection;
        if (face == Vector2.Up)
        {
            idle = "idle_up";
        }
        else if (face == Vector2.Down)
        {
            idle = "idle_down";
        }
        else if (face == Vector2.Left)
        {
            idle = "idle_left";
        }
        else if (face == Vector2.Right)
        {
            idle = "idle_right";
        }
        if (animator.Animation != idle)
        {
            animator.Play(idle);
        }
    }
}
