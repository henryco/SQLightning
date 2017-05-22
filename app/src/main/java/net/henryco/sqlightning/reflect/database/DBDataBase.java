package net.henryco.sqlightning.reflect.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by HenryCo on 11/05/17.
 */

public class DBDataBase extends SQLiteOpenHelper {

	private final String table;
	private final String drop;

	private final DBExtraHelper dbExtraHelper;

	public DBDataBase(DBExtraHelper dbExtraHelper, Context context, String name, int version, String table, String drop) {
		super(context, name, null, version);
		this.dbExtraHelper = dbExtraHelper;
		this.table = table;
		this.drop = drop;
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		db.execSQL("PRAGMA foreign_keys=ON;");
		if (dbExtraHelper != null) dbExtraHelper.onOpen(db, this);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		if (drop != null && !drop.isEmpty())
			exec(db, drop);
		exec(db, table);
		if (dbExtraHelper != null)
			dbExtraHelper.onCreate(db, this);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO: 11/05/17
		if (dbExtraHelper != null)
			dbExtraHelper.onUpgrade(db, oldVersion, newVersion, this);
	}

	@Override
	public void onConfigure(SQLiteDatabase db) {
		super.onConfigure(db);
		if (dbExtraHelper != null)
			dbExtraHelper.onConfigure(db, this);
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		super.onDowngrade(db, oldVersion, newVersion);
		if (dbExtraHelper != null)
			dbExtraHelper.onDowngrade(db, oldVersion, newVersion, this);
	}

	private static void exec(SQLiteDatabase db, String exec) {
		for (String e: exec.trim().split(";"))
			db.execSQL(prepare(e));
	}

	private static String prepare(String exec) {
		return exec.trim().concat(";");
	}


}
