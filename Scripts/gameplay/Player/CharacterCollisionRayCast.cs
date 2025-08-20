using Game.Core;
using Godot;
using System;


namespace Game.Gameplay
{
	public partial class CharacterCollisionRayCast : RayCast2D
	{

		[Signal] public delegate void CollissionEventHandler(bool collider);

		[ExportCategory("Collission!!!")]


		[Export] public CharacterInput CharacterInput;
		[Export] public GodotObject Collider;

		public override void _Ready()

		{

			Logger.Info("Loading character collissions");
		}


		public override void _Process(double delta)
		{
			if (TargetPosition != CharacterInput.TargetPosition)
			{
				TargetPosition = CharacterInput.TargetPosition;
			}


			if (CharacterInput.Direction == Vector2.Up)
			{
				TargetPosition = new Vector2(0, -30);
			}
			if (CharacterInput.Direction == Vector2.Down)
			{
				TargetPosition = new Vector2(0, 30);
			}
			if (CharacterInput.Direction == Vector2.Left)
			{
				TargetPosition = new Vector2(-30, 0);
			}
			if (CharacterInput.Direction == Vector2.Right)
			{
				TargetPosition = new Vector2(30, 0);
			}

			if (IsColliding())
			{
				Collider = GetCollider();
				string colliderType = Collider.GetType().Name;

				switch (colliderType)
				{

					default:
						EmitSignal(SignalName.Collission, true);
						break;
				}
			}
			else
			{
				EmitSignal(SignalName.Collission, false);
			}

 		}
	}

}
