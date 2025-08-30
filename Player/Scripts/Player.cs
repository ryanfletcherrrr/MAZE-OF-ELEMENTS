using Godot;

public partial class Player : CharacterBody2D
{
  [Export] public float Speed = 60f;

  // Direction chosen by the current state
  public Vector2 Direction { get; set; } = Vector2.Zero;
  // Last non-zero direction for idle facing
  public Vector2 LastDirection { get; set; } = Vector2.Down;

  private PlayerStateMachine stateMachine;

  public override void _Ready()
  {
    // Find state machine as a child (or sibling) - adjust path if needed
    stateMachine = GetNodeOrNull<PlayerStateMachine>("StateMachine") ??
            GetNodeOrNull<PlayerStateMachine>("StateMachine");
    if (stateMachine != null)
      stateMachine.Initialize(this);
  }

  public override void _PhysicsProcess(double delta)
  {
    // Movement executed after state decides Direction
    Velocity = Direction * Speed;
    MoveAndSlide();
  }
}
