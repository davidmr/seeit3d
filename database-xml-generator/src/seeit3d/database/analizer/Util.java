package seeit3d.database.analizer;

import java.lang.reflect.Field;
import java.sql.Types;
import java.util.*;

public class Util {

	private static final Map<Integer, String> types;

	static {
		types = new HashMap<Integer, String>();
		// Get all field in java.sql.Types
		Field[] fields = Types.class.getFields();
		for (Field field : fields) {
			try {
				String name = field.getName();
				Integer value = (Integer) field.get(null);
				types.put(value, name);
			} catch (IllegalAccessException e) {}
		}
	}

	public static List<String> SQLTypesAsString() {
		return new ArrayList<String>(types.values());

	}

	public static String SQLTypeIntToString(int type) {
		return types.get(type);
	}

}
