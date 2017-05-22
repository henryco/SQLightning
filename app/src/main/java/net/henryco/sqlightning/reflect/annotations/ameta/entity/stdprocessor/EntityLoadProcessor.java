package net.henryco.sqlightning.reflect.annotations.ameta.entity.stdprocessor;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.henryco.sqlightning.reflect.annotations.ameta.entity.MetaEntityLoaderExec;
import net.henryco.sqlightning.reflect.annotations.ameta.io.MetaLoader;
import net.henryco.sqlightning.reflect.annotations.ameta.io.MetaLoaderExec;
import net.henryco.sqlightning.reflect.annotations.column.Column;
import net.henryco.sqlightning.utils.ReflectUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Created by HenryCo on 14/05/17
 */

public class EntityLoadProcessor implements MetaEntityLoaderExec {


	public static final MetaLoaderExec loaderExec = (instance, instanceField, database, tabName, data) -> {
		String columnName = Column.methods.getColumnName(instanceField.getAnnotation(Column.class), instanceField);
		return MetaLoaderExec.methods.getTypedObject(instanceField.getType(), data.getColumnIndex(columnName), data);
	};


	@Override
	public Object loadEntity(
			final Class entityClass,
			final SQLiteDatabase database,
			final String tableName,
			final String[] id
	) {
		String sql = "SELECT * FROM "+tableName+" WHERE "+id[0]+" = ?;";
		try {

			Object entity = entityClass.newInstance();
			Cursor data = database.rawQuery(sql, new String[]{id[1]});
			if (data.getCount() <= 0) return null;
			data.moveToFirst();

			for (Field field: entityClass.getDeclaredFields()) {

				if (field.getAnnotation(Column.class) != null) {

					Object fieldValue = null;

					MetaLoader loader = null;
					for (Annotation an: field.getAnnotations()) {

						// arrays, other (inner) entities etc. loader
						if ((loader = an.annotationType().getAnnotation(MetaLoader.class)) != null) {
							MetaLoaderExec exec = loader.value().newInstance();
							fieldValue = exec.getValue(entity, field, database, tableName, data);
							break;
						}
					}
					// TODO: 19/05/17
					// primitive types loader
					if (loader == null) fieldValue = loaderExec.getValue(entity, field, database, tableName, data);
					if (fieldValue != null) ReflectUtils.setFieldValue(entity, fieldValue, field);
				}
			}

			data.close();
			return entity;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}


}
