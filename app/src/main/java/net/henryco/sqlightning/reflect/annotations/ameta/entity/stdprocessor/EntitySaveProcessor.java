package net.henryco.sqlightning.reflect.annotations.ameta.entity.stdprocessor;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import net.henryco.sqlightning.reflect.annotations.ameta.entity.MetaEntitySaverExec;
import net.henryco.sqlightning.reflect.annotations.ameta.io.MetaSaver;
import net.henryco.sqlightning.reflect.annotations.ameta.io.MetaSaverExec;
import net.henryco.sqlightning.reflect.annotations.column.Column;
import net.henryco.sqlightning.reflect.annotations.column.Relation;
import net.henryco.sqlightning.utils.ActionTree;
import net.henryco.sqlightning.utils.ReflectUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Created by HenryCo on 15/05/17.
 */

public class EntitySaveProcessor implements MetaEntitySaverExec {


	private static ContentValues setValue(final Object instance, final Field field,
										  final ContentValues values) {

		Column column = field.getAnnotation(Column.class);
		String columnName = Column.methods.getColumnName(column, field);
		Object value = ReflectUtils.getFieldValue(instance, field);
		return MetaSaverExec.methods.setTypedObject(values, columnName, value);
	}


	@Override
	public ActionTree saveEntity(final Object entity, final SQLiteDatabase database,
								 final String tableName, final Object parentKey) {

		ContentValues values = new ContentValues();
		if (parentKey != null)
			values = MetaSaverExec.methods.setTypedObject(values, Relation.RELATED_KEY, parentKey);

		final ActionTree actionTree = new ActionTree();
		final Class entityClass = entity.getClass();
		for (Field field: entityClass.getDeclaredFields()) {

			Column column = field.getAnnotation(Column.class);
			if (column != null) {

				MetaSaver saver = null;
				try {
					for (Annotation an : field.getAnnotations()) {
						// Arrays and inner tables
						if ((saver = an.annotationType().getAnnotation(MetaSaver.class)) != null) {
							final ActionTree actionNode = saver.value().newInstance().setValue(entity, field, database, tableName);
							if (actionNode != null) actionTree.addActionNode(actionNode);
							break;
						}
					}
				} catch (Exception ex) {ex.printStackTrace();}
				if (saver == null) values = setValue(entity, field, values);
			}
		}

		final ContentValues val = values;
		return actionTree.setAction(() -> database.insert(tableName, null, val));
	}

}
