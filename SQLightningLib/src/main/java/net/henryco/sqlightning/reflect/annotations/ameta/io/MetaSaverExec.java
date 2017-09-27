package net.henryco.sqlightning.reflect.annotations.ameta.io;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import net.henryco.sqlightning.reflect.annotations.column.Id;
import net.henryco.sqlightning.utils.ActionTree;
import net.henryco.sqlightning.utils.ReflectUtils;

import java.lang.reflect.Field;

/**
 * Created by HenryCo on 15/05/17.
 */

public interface MetaSaverExec {

	String NO_ENTITY_ANNOTATION_THROW_MSG
			= "Inner entity object class must be annotated as @Entity";
	String NO_RELATED_FIELD_THROW_MSG
			= "Can't find related KEY for table: ";
	String NO_ARRAY_ANNOTATION_THROW_MSG
			= "Array field must be annotated as @Array";

	ActionTree setValue(
			final Object instance, final Field field,
			final SQLiteDatabase database, final String tableName);



	final class methods {

		public static Object getRelatedKey(Object instance) {
			for (Field f : instance.getClass().getDeclaredFields()) {
				if (f.getAnnotation(Id.class) != null) {
					return ReflectUtils.getFieldValue(instance, f);
				}
			}
			return null;
		}

		public static ContentValues setTypedObject(final ContentValues values,
		                                           final String columnName,
		                                           final Object object,
		                                           final Class<?> objectClass) {

			final Class oc = objectClass;
			if (oc == String.class) {
				values.put(columnName, (String) object);
				return values;
			}

			if (oc == char.class || oc == Character.class) {
				values.put(columnName, String.valueOf(object));
				return values;
			}

			if (oc == byte[].class) {
				values.put(columnName, ((byte[]) object));
				return values;
			}

			if (oc == int.class || oc == Integer.class)
				values.put(columnName, ((int) object));
			else if (oc == short.class || oc == Short.class)
				values.put(columnName, ((short) object));
			else if (oc == byte.class || oc == Byte.class)
				values.put(columnName, ((byte) object));
			else if (oc == long.class || oc == Long.class)
				values.put(columnName, ((long) object));
			else if (oc == float.class || oc == Float.class)
				values.put(columnName, ((float) object));
			else if (oc == double.class || oc == Double.class)
				values.put(columnName, ((double) object));
			else if (oc == boolean.class || oc == Boolean.class)
				values.put(columnName, ((boolean) object));

			return values;
		}


	}
}
