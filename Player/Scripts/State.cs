using Godot;

public partial class State : Node2D
{
    static Player Player;


    //What happens when the player enters the state
    public void Enter()
    {
        return;
    }

    //What happens when the player exits out the scene
    public void Exit()
    {

    }

    public override void _Ready()
    {
        base._Ready();
    }
    //What happens every frame during the X state player is in
    public override void _Process(double _delta)
    {
        return;

    }

    //What happens to the physics when player is in this staet
    public void Physics(double _delta)
    {
        return;
    }

    public void HandleInput(InputEvent _event)
    {
        return;
    }



}
