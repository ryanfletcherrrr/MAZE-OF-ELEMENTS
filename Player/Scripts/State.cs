using Godot;

public abstract partial class State : Node
{
    // Reference to the player owning this state
    public Player Player { get; set; }
    // Back-reference to the state machine for convenience
    public PlayerStateMachine Machine { get; set; }

    // Called when the state becomes active
    public virtual void Enter() { }
    // Called when the state is about to be replaced
    public virtual void Exit() { }

    // Per-frame (non-physics) update. Return a different State to transition, or null to stay.
    public virtual State Update(double delta) => null;
    // Physics update. Return next state or null to stay.
    public virtual State PhysicsUpdate(double delta) => null;
    // Input handling. Return next state or null.
    public virtual State HandleInput(InputEvent inputEvent) => null;
}
