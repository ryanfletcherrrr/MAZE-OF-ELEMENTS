class_name ItemEffectHeal extends ItemEffect

@export var heal_amount : int = 50
@export var audio : AudioStream


func use() -> void:
	PlayerManager.update_hp(heal_amount)
