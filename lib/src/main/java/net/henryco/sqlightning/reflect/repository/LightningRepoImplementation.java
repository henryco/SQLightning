package net.henryco.sqlightning.reflect.repository;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.henryco.sqlightning.reflect.annotations.ameta.entity.MetaEntityLoader;
import net.henryco.sqlightning.reflect.annotations.ameta.entity.MetaEntityLoaderExec;
import net.henryco.sqlightning.reflect.annotations.ameta.entity.MetaEntitySaver;
import net.henryco.sqlightning.reflect.annotations.ameta.entity.MetaEntitySaverExec;
import net.henryco.sqlightning.reflect.annotations.ameta.entity.MetaEntityUpdater;
import net.henryco.sqlightning.reflect.annotations.ameta.entity.MetaEntityUpdaterExec;
import net.henryco.sqlightning.reflect.annotations.column.Id;
import net.henryco.sqlightning.reflect.annotations.repository.Entity;
import net.henryco.sqlightning.utils.ActionTree;
import net.henryco.sqlightning.utils.ReflectUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Created by HenryCo on 15/05/17
 */

class LightningRepoImplementation implements LightningRepository {

	private final SQLiteOpenHelper dbHelper;
	private final String repository;
	private final Class keyType;
	private final Class entityType;


	LightningRepoImplementation(final SQLiteOpenHelper dbHelper, final String repository,
								final Class keyType, final Class entityType) {
		this.dbHelper = dbHelper;
		this.repository = repository;
		this.keyType = keyType;
		this.entityType = entityType;
	}

	@Override
	public Object getRecordById(Object key) {

		SQLiteDatabase database = dbHelper.getReadableDatabase();
		try {
			Object entity = entityType.newInstance();
			Annotation annotation = entityType.getAnnotation(Entity.class);
			if (annotation != null) {
				MetaEntityLoader metaEntityLoader = ((Entity)annotation)
						.annotationType().getAnnotation(MetaEntityLoader.class);
				if (metaEntityLoader != null) {
					MetaEntityLoaderExec exec = metaEntityLoader.value().newInstance();
					String[] args = {Id.method.findIdColumn(entityType), keyType.cast(key).toString()};
					entity = exec.loadEntity(entityType, database, repository, args);
				}
			}
			database.close();
			return entity;
		} catch (Exception e) {
			e.printStackTrace();
			database.close();
			return null;
		}
	}

	@Override
	public boolean saveRecord(Object record) {

		for (Field field: record.getClass().getDeclaredFields()) {
			Id id = field.getAnnotation(Id.class);
			if (id != null) {
				Object key = ReflectUtils.getFieldValue(record, field);
				if (key != null) if (isRecordExist(key)) deleteRecordById(key);
				break;
			}
		}

		SQLiteDatabase database = dbHelper.getWritableDatabase();
		try {
			Annotation annotation = record.getClass().getAnnotation(Entity.class);
			ActionTree actionTree = null;
			if (annotation != null) {
				MetaEntitySaver metaEntitySaver = annotation
						.annotationType().getAnnotation(MetaEntitySaver.class);
				if (metaEntitySaver != null) {
					MetaEntitySaverExec exec = metaEntitySaver.value().newInstance();
					actionTree = exec.saveEntity(record, database, repository, null);
				}
			}
			if (actionTree != null) {
				final ActionTree finalActionTree = actionTree;
				return transAction(database, new Runnable() {
					@Override
					public void run() {
						finalActionTree.action();
					}
				});
			}
		} catch (Exception e) {e.printStackTrace();}
		database.close();
		return false;
	}

	@Override
	public boolean deleteRecordById(Object key) {
		final String id;
		return (id = Id.method.findIdColumn(entityType)) != null && dbHelper.getWritableDatabase()
				.delete(repository, id.concat(" = ?"), new String[]{key.toString()}) > 0;
	}

	@Override
	public boolean updateRecord(Object record) {

		SQLiteDatabase database = dbHelper.getWritableDatabase();
		try {
			Annotation annotation = record.getClass().getAnnotation(Entity.class);
			ActionTree actionTree = null;
			if (annotation != null) {
				MetaEntityUpdater updater = annotation
						.annotationType().getAnnotation(MetaEntityUpdater.class);
				if (updater != null) {
					MetaEntityUpdaterExec exec = updater.value().newInstance();
					actionTree = exec.saveEntity(record, database, repository, null);
				}
			}
			if (actionTree != null) {
				final ActionTree finalActionTree = actionTree;
				return transAction(database, new Runnable() {
					@Override
					public void run() {
						finalActionTree.action();
					}
				});
			}
		} catch (Exception e) {e.printStackTrace();}
		database.close();
		return false;
	}


	@Override
	public boolean isRecordExist(Object key) {

		final String id;
		if ((id = Id.method.findIdColumn(entityType)) == null) return false;
		String sql = queryExists(repository, id, key);
		SQLiteDatabase readableDataBase = dbHelper.getReadableDatabase();
		Cursor cursor = readableDataBase.rawQuery(sql, null);
		boolean exists = cursor.getCount() > 0;
		cursor.close();
		readableDataBase.close();
		return exists;
	}


	private static boolean transAction(SQLiteDatabase database, Runnable action) {
		boolean done = true;
		database.beginTransaction();
		try {
			action.run();
			database.setTransactionSuccessful();
		} catch (Exception e) {
			done = false;
		} finally {
			database.endTransaction();
			database.close();
		}
		return done;
	}

	private static String queryExists(String tableName, String idName, Object key) {
		return "SELECT "+idName+" FROM "+ tableName+" WHERE "+idName+" = "+key.toString()+" LIMIT 1;";
	}
}
