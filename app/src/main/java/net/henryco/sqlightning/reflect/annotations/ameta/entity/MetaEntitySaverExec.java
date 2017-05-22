package net.henryco.sqlightning.reflect.annotations.ameta.entity;

import android.database.sqlite.SQLiteDatabase;

import net.henryco.sqlightning.utils.ActionTree;

/**
 * Created by HenryCo on 15/05/17.
 */

public interface MetaEntitySaverExec {

	/**
	 * Method saves entity to DB, and returning Key value of this entity
	 * @param entity Object annotated as {@link @Entity}
	 * @param database DataBase which must contains saved entity
	 * @param tableName Name of table which will contains saved entity
	 */
	ActionTree saveEntity(final Object entity, final SQLiteDatabase database,
						  final String tableName, final Object parentKey);
}
