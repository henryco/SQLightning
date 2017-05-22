package net.henryco.sqlightning.reflect.annotations.ameta.entity;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by HenryCo on 14/05/17.
 */

public interface MetaEntityLoaderExec {

	/**
	 *
	 * @param entityClass Class annotated as {@link @Entity}
	 * @param database Readable SQLiteDatabase
	 * @param tableName Name of table in database
	 * @param id array, where <b>first arg</b> is name of {@link @Column} annotated as {@link @Id}
	 *              <br> <b>second arg</b> is ID value
	 * @return New instance of Object annotated as {@link @Entity} with data from database
	 */
	Object loadEntity(final Class entityClass, final SQLiteDatabase database,
					  final String tableName, final String[] id
	);
}
