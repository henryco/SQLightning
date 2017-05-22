package net.henryco.sqlightning.reflect.annotations.ameta.io;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.henryco.sqlightning.reflect.annotations.column.Column;
import net.henryco.sqlightning.reflect.annotations.column.Id;

import java.lang.reflect.Field;

/**
 * Created by HenryCo on 14/05/17.
 */

public interface MetaLoaderExec {

	/**
	 *
	 * @param instance Instance of class annotated as {@link @Entity} which contains
	 *                 inner {@link @Entity} what this method will load to {@linkplain @instanceField}
	 * @param instanceField Field from instance which represents loadable {@link @Entity}
	 * @param database Readable database
	 * @param tableName Name of table which represents {@link @Entity}
	 * @param data Cursor that contains data from SELECT query to database
	 * @return loaded Object from DataBase
	 */
	Object getValue(final Object instance, final Field instanceField,
					final SQLiteDatabase database, final String tableName,
					final Cursor data
	);


	final class methods {


		public static Object getIdObject(final Cursor cursor, final Class container) {
			for (Field f: container.getDeclaredFields()) {
				Id id = f.getAnnotation(Id.class);
				Column column = f.getAnnotation(Column.class);
				if (id != null && column != null) {
					final String idColumnName = Column.methods.getColumnName(column, f);
					final int idIndex = cursor.getColumnIndex(idColumnName);
					return methods.getTypedObject(f.getType(), idIndex, cursor);
				}
			}
			return null;
		}

		public static Object getIdObject(final Cursor cursor, final Field field,
										 final String relationId, final String columnName) {

			for (Field f: field.getType().getDeclaredFields()) {

				Id id = f.getAnnotation(Id.class);
				Column column = f.getAnnotation(Column.class);

				if (column != null && id != null && Column.methods.getColumnName(column, f).equals(relationId))
					return methods.getTypedObject(f.getType(), cursor.getColumnIndex(columnName), cursor);
			}
			return null;
		}

		public static Object getTypedObject(final Class objClass, final int columnIndex, final Cursor cursor) {

			try {
				switch (cursor.getType(columnIndex)) {

					case Cursor.FIELD_TYPE_NULL:
						return null;
					case Cursor.FIELD_TYPE_BLOB:
						return cursor.getBlob(columnIndex);

					case Cursor.FIELD_TYPE_STRING: {
						if (objClass == String.class)
							return cursor.getString(columnIndex);
						if (objClass == char.class || objClass == Character.class)
							return cursor.getString(columnIndex).charAt(0);
					}

					case Cursor.FIELD_TYPE_INTEGER: {
						if (objClass == byte.class || objClass == Byte.class)
							return (byte) cursor.getShort(columnIndex);
						if (objClass == short.class || objClass == Short.class)
							return cursor.getShort(columnIndex);
						if (objClass == long.class || objClass == Long.class)
							return cursor.getLong(columnIndex);
						if (objClass == int.class || objClass == Integer.class)
							return cursor.getInt(columnIndex);
						if (objClass == boolean.class || objClass == Boolean.class)
							return cursor.getInt(columnIndex) > 0;
						break;
					}

					case Cursor.FIELD_TYPE_FLOAT: {
						if (objClass == float.class || objClass == Float.class)
							return cursor.getFloat(columnIndex);
						if (objClass == double.class || objClass == Double.class)
							return cursor.getDouble(columnIndex);
						break;
					}

				}
			} catch (Exception e) {
				return null;
			}
			return null;
		}

	}
}
