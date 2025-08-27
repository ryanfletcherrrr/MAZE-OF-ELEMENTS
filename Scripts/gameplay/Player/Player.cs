using Godot;

/* Character movement
  - Works with Arrow keys / WASD
  - Constrain movement to 2D plane no diagonal movement
*/
public partial class Player : CharacterBody2D
{
  [ExportCategory("see code to change behavior")]
  [Export] public string PlayerName = "Player";
  public float Speed = 60f;

  public Vector2 Direction { get; private set; } = Vector2.Zero;

  public override void _PhysicsProcess(double delta)
  {
    //Define the input for going up
    float x = Input.GetAxis("ui_left", "ui_right");
    //Define the input for going down
    float y = Input.GetAxis("ui_up", "ui_down");

    // Only allow one direction at a time (no diagonals)
    if (x != 0 && y != 0) y = 0;
    Direction = new Vector2(x, y);
    // Move the character
    Velocity = Direction * Speed;
    MoveAndSlide();
  }
}
