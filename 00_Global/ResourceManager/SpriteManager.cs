using Godot;
using Game.Core;
using System.Drawing;
public partial class SpriteManager : SpriteFrames
{

    // [ext_resource type="Texture2D" uid="uid://bhdqmekfcup4e" path="res://Resources/Character/Swordsman_lvl3/With_shadow/Swordsman_lvl3_attack_with_shadow.png" id="1_v0u7d"]
    // [ext_resource type="Texture2D" uid="uid://cfvxc4gngv3fo" path="res://Resources/Character/Swordsman_lvl3/With_shadow/Swordsman_lvl3_Idle_with_shadow.png" id="2_8inml"]
    // [ext_resource type="Texture2D" uid="uid://b8o3hq4cq2jji" path="res://Resources/Character/Swordsman_lvl3/With_shadow/Swordsman_lvl3_Walk_with_shadow.png" id="3_yvrob"]

    [Export] int level;

    Texture2D WalkingFrames;
    Texture2D IdleFrames;
    Texture2D AtackFrames;
    Texture2D WalkAttackFrame;
    Texture2D HurtFrame;
    Texture2D DeathFrame;

    public void SwapTexture(SpriteFrames spriteFrames)
    {
        Logger.Info("Loaded smartSprites");
    }


}
