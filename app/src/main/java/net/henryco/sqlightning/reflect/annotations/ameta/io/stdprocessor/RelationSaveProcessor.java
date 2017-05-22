package net.henryco.sqlightning.reflect.annotations.ameta.io.stdprocessor;

import android.database.sqlite.SQLiteDatabase;

import net.henryco.sqlightning.reflect.annotations.ameta.entity.MetaEntitySaver;
import net.henryco.sqlightning.reflect.annotations.ameta.entity.MetaEntitySaverExec;
import net.henryco.sqlightning.reflect.annotations.ameta.io.MetaSaverExec;
import net.henryco.sqlightning.reflect.annotations.repository.Entity;
import net.henryco.sqlightning.reflect.annotations.repository.Table;
import net.henryco.sqlightning.utils.ActionTree;
import net.henryco.sqlightning.utils.ReflectUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Created by HenryCo on 15/05/17
 */

public class RelationSaveProcessor implements MetaSaverExec {


	@Override
	public ActionTree setValue(final Object instance, final Field field,
							   final SQLiteDatabase database, final String tableName) {

		final Object relatedEntity = ReflectUtils.getFieldValue(instance, field);
		if (relatedEntity == null) return null;
		Annotation annotation = relatedEntity.getClass().getAnnotation(Entity.class);

		if (annotation == null) throw new RuntimeException(NO_ENTITY_ANNOTATION_THROW_MSG);
		Entity entity = (Entity) annotation;
		MetaEntitySaver saver = entity.annotationType()
				.getAnnotation(MetaEntitySaver.class);

		final Object relatedKey = methods.getRelatedKey(instance);
		final String tabName = Table.methods.getTableName(field);
		if (relatedKey == null) throw new RuntimeException(NO_RELATED_FIELD_THROW_MSG+tabName);

		if (saver != null) {
			try {
				MetaEntitySaverExec exec = saver.value().newInstance();
				return exec.saveEntity(relatedEntity, database, tabName, relatedKey);
			} catch (Exception e) {e.printStackTrace();}
		}
		return null;
	}
}
