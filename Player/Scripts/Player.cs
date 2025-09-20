using Game.Core;
using Godot;

public partial class Player : CharacterBody2D
{
  [ExportCategory("Debug zone")]
  [Export] public float Speed = 60f;
  [Export] public int LEVEL;

  [Export] public SpriteFrames CHARACTER_SPRITE;
  // Direction chosen by the current state
  public Vector2 Direction { get; set; } = Vector2.Zero;
  // Last non-zero direction for idle facing
  public Vector2 LastDirection { get; set; } = Vector2.Down;

  private PlayerStateMachine stateMachine;

  private AnimatedSprite2D animator;


  public override void _Ready()
  {
    LEVEL = 0;

    stateMachine = GetNodeOrNull<PlayerStateMachine>("StateMachine");
    if (stateMachine != null)
    {
      stateMachine.Initialize(this);
    }

    ManageCharacter();
  }

  public override void _PhysicsProcess(double delta)
  {
    // Movement executed after state decides Direction
    Velocity = Direction * Speed;
    MoveAndSlide();
  }

  public void ManageCharacter()
  {
    //Get the AnimatedSprite2D node
    animator = GetNodeOrNull<AnimatedSprite2D>("AnimatedSprite2D");
    if (animator == null)
    {
      GameLogger.Warning("No character sprite framed detected");
      return;
    }


    if (CHARACTER_SPRITE == null)
    {
      animator.SpriteFrames = animator.SpriteFrames;
      return;
    }

    animator.SpriteFrames = CHARACTER_SPRITE;
    GameLogger.Info($"Detected level selection {CHARACTER_SPRITE} switching sprite to appropritate level");
  }

}
