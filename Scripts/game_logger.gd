extends Node

# Simple GDScript Logger to replace C# GameLogger
# Add this as an autoload in Project Settings → Autoload
# Name: GameLogger
# Path: res://Scripts/game_logger.gd

enum LogLevel {
	DEBUG,
	INFO,
	WARNING,
	ERROR
}

var current_log_level: LogLevel = LogLevel.DEBUG

func _ready() -> void:
	print("[GameLogger] Initialized")

func debug(message: String) -> void:
	if current_log_level <= LogLevel.DEBUG:
		print("[DEBUG] ", message)

func info(message: String) -> void:
	if current_log_level <= LogLevel.INFO:
		print("[INFO] ", message)

func warning(message: String) -> void:
	if current_log_level <= LogLevel.WARNING:
		push_warning(message)
		print("[WARNING] ", message)

func error(message: String) -> void:
	if current_log_level <= LogLevel.ERROR:
		push_error(message)
		print("[ERROR] ", message)

# Alias methods with capitalized names (to match C# convention if needed)
func Debug(message: String) -> void:
	debug(message)

func Info(message: String) -> void:
	info(message)

func Warning(message: String) -> void:
	warning(message)

func Error(message: String) -> void:
	error(message)
