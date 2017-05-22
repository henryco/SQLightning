package net.henryco.sqlightning.reflect.table.stb;

import java.lang.reflect.Field;

/**
 * Created by HenryCo on 10/05/17.
 */

public enum StbType {
	NULL(), REAL(), TEXT(), BLOB(), INTEGER();

	public static StbType findType(Field field) {
		return findType(field.getType());
	}
	public static StbType findType(Object instance) {
		return findType(instance.getClass());
	}

	public static StbType findType(Class obClass) {

		//TODO ARRAYS

		if (obClass == String.class || obClass == char.class
				|| obClass == Character.class)
			return StbType.TEXT;


		if (obClass == byte.class || obClass == Byte.class ||
				obClass == long.class || obClass == Long.class ||
				obClass == int.class || obClass == Integer.class ||
				obClass == short.class || obClass == Short.class ||
				obClass == boolean.class || obClass == Boolean.class)
			return StbType.INTEGER;


		if (obClass == float.class || obClass == Float.class ||
				obClass == double.class || obClass == Double.class)
			return StbType.REAL;

		return StbType.BLOB;
	}
}
