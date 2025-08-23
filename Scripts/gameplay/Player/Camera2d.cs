using Game.Core;
using Godot;
using Microsoft.VisualBasic;
using System;

public partial class Camera2d : Camera2D
{

	[ExportCategory("Camera boundry for this level")]

	[Export] int limit_left = 0;
	[Export] int limit_right = 0;
	[Export] int limit_top = 0;
	[Export] int limit_bottom = 0;
	// Called when the node enters the scene tree for the first time.


	public void UpdateLimit(Vector2[] bounds)
	{
		LimitLeft = (int)bounds[0].X;
		LimitTop = (int)bounds[0].Y;
		LimitRight = (int)bounds[1].X;
		LimitBottom = (int)bounds[1].Y;
	}
	public override void _Ready()
	{
		Logger.Info("Loading camera");
	}

}
