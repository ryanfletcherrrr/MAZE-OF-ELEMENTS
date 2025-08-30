using Godot;
namespace Game.Gameplay;
public partial class CharacterAnimation : AnimatedSprite2D
{
    public override void _Ready()
    {
        // Idle default; states will drive further animations
        if (Animation != "idle_down")
            Play("idle_down");
    }
}
