using Game.core;
using Game.Core;
using Game.Utilities;
using Godot;

namespace Game.Gameplay
{
	public partial class PlayerRoamState : State
	{
		[ExportCategory("State Vars")]
		[Export] public PlayerInput PlayerInput;
		[Export] public CharacterMovement CharacterMovement;

		private bool _wasMoving = false;

		public override void _Ready()
		{
			// If CharacterMovement is not connected in the scene, try to find it
			if (CharacterMovement == null)
			{
				CharacterMovement = GetNode<CharacterMovement>("../../Movement");
				if (CharacterMovement != null)
				{
					Logger.Info("CharacterMovement found via node path");
				}
				else
				{
					Logger.Error("CharacterMovement not found! Please connect it in the scene or check node structure.");
				}
			}
		}

        public override void _Process(double delta)
        {
            GetInputDirection();
			GetInput(delta);
        }

		public void GetInputDirection()
		{
			// Update direction based on currently pressed keys (not just first press)
			if (Input.IsActionPressed("ui_up"))
			{
				PlayerInput.Direction = Vector2.Up;
				PlayerInput.TargetPosition = new Vector2(0, Globals.Instance.GRID_SIZE);
			}
			else if (Input.IsActionPressed("ui_down"))
			{
				PlayerInput.Direction = Vector2.Down;
				PlayerInput.TargetPosition = new Vector2(0, Globals.Instance.GRID_SIZE);
			}
			else if (Input.IsActionPressed("ui_left"))
			{
				PlayerInput.Direction = Vector2.Left;
				PlayerInput.TargetPosition = new Vector2(-Globals.Instance.GRID_SIZE, 0);
			}
			else if (Input.IsActionPressed("ui_right"))
			{
				PlayerInput.Direction = Vector2.Right;
				PlayerInput.TargetPosition = new Vector2(Globals.Instance.GRID_SIZE, 0);
			}
		}

		public void GetInput(double delta)
		{
			bool isPressed = Modules.IsActionPressed();
			bool justPressed = Modules.IsActionJustPressed();
			bool justReleased = Modules.IsActionJustReleased();

			if (isPressed)
			{
				// Reset hold time when direction changes (new key pressed)
				if (justPressed)
				{
					PlayerInput.HoldTime = 0.0f;
					_wasMoving = false;
					Logger.Info("Key just pressed, resetting hold time");
				}

				PlayerInput.HoldTime += delta;

				// Start walking when threshold is reached
				if (PlayerInput.HoldTime >= PlayerInput.HoldThreshold)
				{
					// Debug: Check if CharacterMovement is null
					if (CharacterMovement == null)
					{
						Logger.Error("CharacterMovement is null! Please connect it in the scene.");
						return;
					}

					// If not currently moving, start walking
					if (!CharacterMovement.IsMoving())
					{
						Logger.Info($"Emitting walk signal, direction: {PlayerInput.Direction}");
						PlayerInput.EmitSignal(CharacterInput.SignalName.Walk);
						_wasMoving = true;
					}
				}
			}
			else if (justReleased)
			{
				// If we weren't moving (held for less than threshold), just turn
				if (PlayerInput.HoldTime < PlayerInput.HoldThreshold)
				{
					Logger.Info("Emitting turn signal");
					PlayerInput.EmitSignal(CharacterInput.SignalName.Turn);
				}

				PlayerInput.HoldTime = 0.0f;
				_wasMoving = false;
			}
		}
    }
}

