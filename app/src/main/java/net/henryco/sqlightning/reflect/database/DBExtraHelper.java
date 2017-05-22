package net.henryco.sqlightning.reflect.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by HenryCo on 21/05/17.
 */

public interface DBExtraHelper {



	void onConfigure(SQLiteDatabase db, SQLiteOpenHelper helper);

	void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion, SQLiteOpenHelper helper);

	void onOpen(SQLiteDatabase db, SQLiteOpenHelper helper);

	void onCreate(SQLiteDatabase db, SQLiteOpenHelper helper);

	void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion, SQLiteOpenHelper helper);

}
