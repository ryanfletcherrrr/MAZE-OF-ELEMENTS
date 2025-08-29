using Godot;

// Simple 4-directional character movement (no diagonals)
public partial class Player : CharacterBody2D
{
  [Export] public float Speed = 60f;

  public Vector2 Direction { get; private set; } = Vector2.Zero;

  public override void _PhysicsProcess(double delta)
  {
    HandleInput();
    Move();
  }

  private void HandleInput()
  {
    float horizontal = Input.GetAxis("ui_left", "ui_right");
    float vertical = Input.GetAxis("ui_up", "ui_down");

    // Block diagonal movement - prioritize horizontal
    if (horizontal != 0 && vertical != 0)
    {
      vertical = 0;
    }

    Direction = new Vector2(horizontal, vertical);
  }

  private void Move()
  {
    Velocity = Direction * Speed;
    MoveAndSlide();
  }
}
