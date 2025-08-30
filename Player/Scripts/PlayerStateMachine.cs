using Game.Core;
using Godot;
using System.Collections.Generic;
public partial class PlayerStateMachine : Node
{
    //Array of States a player can have
    private readonly List<State> stateList = new();
    // Previous state of the player
    public State PreviousState { get; private set; }
    //Current state of the player
    public State CurrentState { get; private set; }

    public override void _Ready()
    {
        ProcessMode = Node.ProcessModeEnum.Disabled;
        base._Ready();
    // Attempt automatic initialization if a Player parent exists
    TryAutoInitialize();
    }

    public override void _Process(double delta)
    {
        if (CurrentState == null) return;
        var next = CurrentState.Update(delta);
        if (next != null && next != CurrentState)
        {
            ChangeState(next);
        }
    }

    public override void _PhysicsProcess(double delta)
    {
        if (CurrentState == null) return;
        var next = CurrentState.PhysicsUpdate(delta);
        if (next != null && next != CurrentState)
        {
            ChangeState(next);
        }
    }

    public override void _UnhandledInput(InputEvent inputEvent)
    {
        if (CurrentState == null) return;
        var next = CurrentState.HandleInput(inputEvent);
        if (next != null && next != CurrentState)
        {
            ChangeState(next);
        }
    }
    public void Initialize(Player player)
    {
        if (player == null)
        {
            Logger.Warning("Initialize called with null player");
            return;
        }
        if (CurrentState != null)
        {
            // Already initialized
            return;
        }
        Logger.Info("PlayerStateMachine: Initializing with player " + player.Name);
        stateList.Clear();
        foreach (Node child in GetChildren())
        {
            if (child is State s)
                stateList.Add(s);
        }
        if (stateList.Count == 0)
        {
            Logger.Warning("PlayerStateMachine has no child State nodes.");
            return;
        }
        // Attach references to all states
        foreach (var s in stateList)
        {
            s.Player = player;
            s.Machine = this;
        }

        ChangeState(stateList[0]);
        ProcessMode = Node.ProcessModeEnum.Inherit; // enable processing
        Logger.Info("PlayerStateMachine: Entered initial state " + CurrentState.GetType().Name);
    }

    public void ChangeState(State newState)
    {
        //check if the states are valid if not exit
        if (newState == null)
        {
            Logger.Warning("Attempted to change to null state");
            return;
        }
        if (newState == CurrentState)
        {
            return;
        }

        if (CurrentState != null)
        {
            CurrentState.Exit();
        }

        PreviousState = CurrentState;

        CurrentState = newState;

        CurrentState.Enter();
    }

    // Generic helper for states to request another state instance
    public T GetState<T>() where T : State
    {
        for (int i = 0; i < stateList.Count; i++)
        {
            T t = stateList[i] as T;
            if (t != null)
            {
                return t;
            }
        }
        return null;
    }

    private void TryAutoInitialize()
    {
        if (CurrentState != null) return;
        Player player = GetParent() as Player;
        if (player == null)
        {
            // Try one level up just in case
            Node parent = GetParent();
            if (parent != null)
            {
                player = parent.GetParent() as Player;
            }
        }
        if (player != null)
        {
            Initialize(player);
        }
        else
        {
            // Defer another attempt after tree is fully ready
            CallDeferred(nameof(TryAutoInitialize));
        }
    }


}
