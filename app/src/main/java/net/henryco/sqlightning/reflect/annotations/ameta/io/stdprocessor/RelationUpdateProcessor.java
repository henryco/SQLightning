package net.henryco.sqlightning.reflect.annotations.ameta.io.stdprocessor;

import android.database.sqlite.SQLiteDatabase;

import net.henryco.sqlightning.reflect.annotations.ameta.entity.MetaEntityUpdater;
import net.henryco.sqlightning.reflect.annotations.ameta.entity.MetaEntityUpdaterExec;
import net.henryco.sqlightning.reflect.annotations.ameta.io.MetaUpdaterExec;
import net.henryco.sqlightning.reflect.annotations.repository.Entity;
import net.henryco.sqlightning.reflect.annotations.repository.Table;
import net.henryco.sqlightning.utils.ActionTree;
import net.henryco.sqlightning.utils.ReflectUtils;

import java.lang.reflect.Field;

/**
 * Created by HenryCo on 20/05/17
 */

public class RelationUpdateProcessor implements MetaUpdaterExec {
	// TODO: 20/05/17 REFACTORING see: "RelationSaveProcessor.java"

	@Override
	public ActionTree setValue(final Object instance, final Field field,
							   final SQLiteDatabase database, final String tableName) {

		final Object relatedEntity = ReflectUtils.getFieldValue(instance, field);
		final Entity entity = relatedEntity != null
				? relatedEntity.getClass().getAnnotation(Entity.class) : null;
		if (entity == null) throw new RuntimeException(NO_ENTITY_ANNOTATION_THROW_MSG);

		final Object relatedKey = methods.getRelatedKey(instance);
		final String tabName = Table.methods.getTableName(field);
		if (relatedKey == null) throw new RuntimeException(NO_RELATED_FIELD_THROW_MSG+tabName);

		final MetaEntityUpdater updater = entity.annotationType()
				.getAnnotation(MetaEntityUpdater.class);
		if (updater != null) {
			try {
				MetaEntityUpdaterExec exec = updater.value().newInstance();
				return exec.saveEntity(relatedEntity, database, tabName, relatedKey);
			} catch (Exception e) {e.printStackTrace();}
		}
		return null;
	}
}
