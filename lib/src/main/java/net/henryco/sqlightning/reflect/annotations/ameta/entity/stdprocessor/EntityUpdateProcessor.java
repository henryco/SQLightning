package net.henryco.sqlightning.reflect.annotations.ameta.entity.stdprocessor;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import net.henryco.sqlightning.reflect.annotations.ameta.entity.MetaEntityUpdaterExec;
import net.henryco.sqlightning.reflect.annotations.ameta.io.MetaUpdater;
import net.henryco.sqlightning.reflect.annotations.ameta.io.MetaUpdaterExec;
import net.henryco.sqlightning.reflect.annotations.column.Column;
import net.henryco.sqlightning.reflect.annotations.column.Id;
import net.henryco.sqlightning.reflect.annotations.column.Relation;
import net.henryco.sqlightning.utils.ActionTree;
import net.henryco.sqlightning.utils.ReflectUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Created by HenryCo on 20/05/17
 */

public class EntityUpdateProcessor implements MetaEntityUpdaterExec {

	private static final String NO_ID_FIELD_THROW_MSG
			= "@Entity must contains non-null @Id field";

	@Override
	public ActionTree saveEntity(Object entity, final SQLiteDatabase database,
								 final String tableName, Object parentKey) {
		ContentValues contentValues = new ContentValues();
		ActionTree actionTree = new ActionTree();
		String idKeyName = null;
		Object idKeyObj = null;

		if (parentKey != null) {
			idKeyName = Relation.RELATED_KEY;
			idKeyObj = parentKey;
		}

		for (Field field: entity.getClass().getDeclaredFields()) {

			Id id = field.getAnnotation(Id.class);
			Column column = field.getAnnotation(Column.class);
			final String columnName = Column.methods.getColumnName(column, field);

			if (parentKey == null && id != null) {

				idKeyName = columnName;
				idKeyObj = ReflectUtils.getFieldValue(entity, field);
			} else {

				MetaUpdater updater = null;
				for (Annotation an : field.getAnnotations()) {

					if ((updater = an.annotationType().getAnnotation(MetaUpdater.class)) != null) {

						try {
							final MetaUpdaterExec exec = updater.value().newInstance();
							final ActionTree actionNode = exec.setValue(entity, field, database, tableName);
							if (actionNode != null) actionTree.addActionNode(actionNode);
						} catch (Exception e) {e.printStackTrace();}
						break;
					}
				}
				if (updater == null) {
					Object value = ReflectUtils.getFieldValue(entity, field);
					contentValues = MetaUpdaterExec.methods.setTypedObject(contentValues, columnName, value);
				}
			}
		}

		if (idKeyName == null || idKeyObj == null) throw new RuntimeException(NO_ID_FIELD_THROW_MSG);

		final String where = idKeyName + " = ?";
		final String[] args = {idKeyObj.toString()};
		final ContentValues values = contentValues;
		return actionTree.setAction(new Runnable() {
			@Override
			public void run() {
				database.update(tableName, values, where, args);
			}
		});
	}
}
