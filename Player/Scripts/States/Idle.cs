using Game.Core;
using Godot;

public partial class Idle : State
{
    private AnimatedSprite2D animator;
    public override void Enter()
    {
        Player.Direction = Vector2.Zero;
        if (animator == null)
        {
            animator = Player.GetNodeOrNull<AnimatedSprite2D>("CharacterAnimation");
            if (animator == null)
            {
                animator = Player.GetNodeOrNull<AnimatedSprite2D>("../CharacterAnimation");
            }
        }
        if (animator != null)
        {

            string anim = "idle_down";
            if (Player.LastDirection == Vector2.Up)
            {
                anim = "idle_up";
            }
            else if (Player.LastDirection == Vector2.Down)
            {
                anim = "idle_down";
            }
            else if (Player.LastDirection == Vector2.Left)
            {
                anim = "idle_left";
            }
            else if (Player.LastDirection == Vector2.Right)
            {
                anim = "idle_right";
            }

            if (animator.Animation.IsEmpty)
            {
                animator.Play("idle_down");
            }
            if (animator.Animation != anim)
            {
                animator.Play(anim);
            }
        }
    }

    public override State HandleInput(InputEvent inputEvent)
    {
        if (inputEvent is InputEventKey key && key.Pressed)
        {
            if (Input.IsActionPressed("ui_left") || Input.IsActionPressed("ui_right") || Input.IsActionPressed("ui_up") || Input.IsActionPressed("ui_down"))
            {
                return Machine.GetState<Walk>();
            }
        }
        return null;
    }

    public override State PhysicsUpdate(double delta)
    {
        float horizontal = Input.GetAxis("ui_left", "ui_right");
        float vertical = Input.GetAxis("ui_up", "ui_down");
        if (horizontal != 0 || vertical != 0)
        {
            return Machine.GetState<Walk>();
        }
        if (animator != null)
        {
            string anim = "idle_down";
            if (Player.LastDirection == Vector2.Up)
            {
                anim = "idle_up";
            }
            else if (Player.LastDirection == Vector2.Down)
            {
                anim = "idle_down";
            }
            else if (Player.LastDirection == Vector2.Left)
            {
                anim = "idle_left";
            }
            else if (Player.LastDirection == Vector2.Right)
            {
                anim = "idle_right";
            }
            if (animator.Animation != anim)
            {
                animator.Play(anim);
            }
        }
        return null;
    }
}
