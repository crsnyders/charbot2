package za.co.chris.wug.utils;

import java.util.HashMap;
import java.util.Map;

public class ConfigProperties {
	private static Map<String, Object> configMap = new HashMap<String, Object>();

	public static Object get(String key) {
		return configMap.get(key);
	}

	public static <T> T get(String key, Class<T> type) {
		return type.cast(get(key));
	}

	public static <T> T get(String key, Class<T> type, T defaultValue) {
		if (configMap.containsKey(key)) {
			return type.cast(get(key));
		}
		return defaultValue;
	}

	public static Object put(String key, Object Value) {
		return configMap.put(key, Value);
	}
}
