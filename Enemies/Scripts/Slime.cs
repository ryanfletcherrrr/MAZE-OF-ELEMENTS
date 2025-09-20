using Game.Core;
using Godot;
using System.Collections.Generic; // Required for List
using System;

public partial class Slime : CharacterBody2D
{
    [ExportCategory("Slime Image path")]
    [Export] SpriteFrames SlimeImage;
    private AnimatedSprite2D EnemyAnimation;
    private Timer IdleTimer;
    private List<string> IdleAnimations = new List<string>() { "idle_down", "idle_left", "idle_up", "idle_right" };
    private Random Random = new Random();
    public override void _Ready()
    {
        GameLogger.Info("Loaded in Enemies!");
        //Load the AnimatedSprite2D Node
        EnemyAnimation = GetNodeOrNull<AnimatedSprite2D>("Enemy");

        //Load the Timer node
        IdleTimer = GetNodeOrNull<Timer>("Timer");

        IdleTimer.Timeout += OnIdleTimerTimeout;

        if (EnemyAnimation == null)
        {
            GameLogger.Info("Something is wrong with Enemy Idle script..");
            return;
        }

        // Set sprite immediately on load
        if (SlimeImage != null)
        {
            EnemyAnimation.SpriteFrames = SlimeImage;
            PlayRandomIdleAnimation();
            EnemyAnimation.Play();
        }
    }

    private void OnIdleTimerTimeout()
    {
        PlayRandomIdleAnimation();
        IdleTimer.Start();
    }

    private void PlayRandomIdleAnimation()
    {
        if (IdleAnimations.Count > 0)
        {
            int randomIndex = Random.Next(0, IdleAnimations.Count);
            string selectedAnimation = IdleAnimations[randomIndex];
            if (EnemyAnimation.SpriteFrames == null)
            {
                GameLogger.Info("Invalid sprite");
                return;
            }
            else
            {
                // Don't reassign SpriteFrames every time - it's already set in _Ready
                EnemyAnimation.Play(selectedAnimation);
            }
        }
    }

    public override void _Process(double delta)
    {
        base._Process(delta);

    }

}
