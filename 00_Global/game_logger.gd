extends Node


enum LogLevel {
	DEBUG,
	INFO,
	WARNING,
	ERROR
}

var current_log_level: LogLevel = LogLevel.DEBUG

func _ready() -> void:
	print("[GameLogger] Initialized")

func _get_timestamp() -> String:
	var time := Time.get_time_dict_from_system()
	return "%02d:%02d:%02d" % [time.hour, time.minute, time.second]

func debug(message: String) -> void:
	if current_log_level <= LogLevel.DEBUG:
		print("[DEBUG: %s] : %s" % [_get_timestamp(), message])

func info(message: String) -> void:
	if current_log_level <= LogLevel.INFO:
		print("[INFO: %s] : %s" % [_get_timestamp(), message])

func warning(message: String) -> void:
	if current_log_level <= LogLevel.WARNING:
		var formatted := "[WARNING: %s] : %s" % [_get_timestamp(), message]
		push_warning(formatted)
		print(formatted)

func error(message: String) -> void:
	if current_log_level <= LogLevel.ERROR:
		var formatted := "[ERROR: %s] : %s" % [_get_timestamp(), message]
		push_error(formatted)
		print(formatted)
