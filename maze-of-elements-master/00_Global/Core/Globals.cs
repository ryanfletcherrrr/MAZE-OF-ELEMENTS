using Game.Core;
using Godot;

namespace Game.core
{
	public partial class Globals : Node
	{
		public static Globals Instance { get; private set; }
		[ExportCategory("Gameplay")]
		[Export] public int GRID_SIZE = 8;
		public override void _Ready()
		{
			Instance = this;
			Logger.Info("Game loaded...");
		}
	}
}
