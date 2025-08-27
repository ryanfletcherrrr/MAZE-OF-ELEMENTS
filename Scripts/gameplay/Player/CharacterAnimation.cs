using Godot;
using Game.Core;
using System;
namespace Game.Gameplay;
public partial class CharacterAnimation : AnimatedSprite2D
{
    private Player _player;
    private Vector2 _lastDirection = Vector2.Down;

    public override void _Ready()
    {
        _player = GetParent() as Player;
        Logger.Info("Playing character animation " + GetIdleAnimation(Vector2.Down));
        Play("idle_down");
    }

    public override void _Process(double delta)
    {
        if (_player == null) return;

        Vector2 direction = _player.Direction;

        if (direction != Vector2.Zero)
        {
            _lastDirection = direction;
        }

        // Pick animation name
        string animName;
        if (direction == Vector2.Zero)
        {
            // Not moving - show idle animation
            animName = GetIdleAnimation(_lastDirection);
        }
        else
        {
            // Moving - show walk animation
            animName = GetWalkAnimation(direction);
        }

        // Play the animation if it's different
        if (Animation != animName)
        {
            Play(animName);
        }
    }

    private string GetIdleAnimation(Vector2 dir)
    {
        if (dir == Vector2.Up) return "idle_up";
        if (dir == Vector2.Down) return "idle_down";
        if (dir == Vector2.Left) return "idle_left";
        if (dir == Vector2.Right) return "idle_right";
        return "idle_down";
    }

    private string GetWalkAnimation(Vector2 dir)
    {
        if (dir == Vector2.Up) return "walk_up";
        if (dir == Vector2.Down) return "walk_down";
        if (dir == Vector2.Left) return "walk_left";
        if (dir == Vector2.Right) return "walk_right";
        return "walk_down";
    }
}
