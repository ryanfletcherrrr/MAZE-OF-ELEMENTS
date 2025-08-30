using Game.Core;
using Godot;
using Godot.Collections;
using System;
using System.Linq;
public partial class PlayerStateMachine : Node2D
{
    //Array of States a player can have
    public State[] _states;
    // Previous state of the player
    public State _PreviousState;
    //Current state of the player
    public State _CurrentSate;

    public override void _Ready()
    {
        ProcessMode = Node.ProcessModeEnum.Disabled;
        base._Ready();
    }

    public override void _Process(double delta)
    {
        base._Process(delta);
    }

    public void Initialize()
    {
        _states = [];

        foreach (State _Children in _states)
        {
            if (_Children is State)
            {
                _states.Append(_Children);
            }
        }
    }

    public void ChangeState(State _NewState)
    {
        //check if the states are valid if not exit
        if (_NewState == null || _NewState == _CurrentSate) { Logger.Warning("Player states null"); return; }


        if (_CurrentSate != null) _CurrentSate.Exit();


        _PreviousState = _CurrentSate;

        _CurrentSate = _NewState;

        _CurrentSate.Enter();
    }


}
