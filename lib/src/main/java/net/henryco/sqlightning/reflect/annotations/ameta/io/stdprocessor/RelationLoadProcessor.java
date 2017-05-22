package net.henryco.sqlightning.reflect.annotations.ameta.io.stdprocessor;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.henryco.sqlightning.reflect.annotations.ameta.entity.MetaEntityLoader;
import net.henryco.sqlightning.reflect.annotations.ameta.entity.MetaEntityLoaderExec;
import net.henryco.sqlightning.reflect.annotations.ameta.io.MetaLoaderExec;
import net.henryco.sqlightning.reflect.annotations.column.Relation;
import net.henryco.sqlightning.reflect.annotations.repository.Entity;
import net.henryco.sqlightning.reflect.annotations.repository.Table;

import java.lang.reflect.Field;

/**
 * Created by HenryCo on 14/05/17
 */

public class RelationLoadProcessor implements MetaLoaderExec {

	@Override
	public Object getValue(final Object instance, final Field instanceField,
						   final SQLiteDatabase database, final String tableName,
						   final Cursor data) {

		if (data.getCount() == 0) return null;
		data.moveToFirst();

		String innerTableName = Table.methods.getTableName(instanceField);
		Class entityClass = instanceField.getType();

		Object id = methods.getIdObject(data, instance.getClass());
		if (id == null) return null;
		try {
			Entity annotation = instanceField.getType().getAnnotation(Entity.class);
			MetaEntityLoaderExec exec = annotation.annotationType()
					.getAnnotation(MetaEntityLoader.class).value().newInstance();
			return exec.loadEntity(entityClass, database, innerTableName, new String[]{Relation.RELATED_KEY, id.toString()});
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}





}
