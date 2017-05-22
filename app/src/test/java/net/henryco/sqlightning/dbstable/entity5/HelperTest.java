package net.henryco.sqlightning.dbstable.entity5;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.henryco.sqlightning.reflect.database.DBExtraHelper;

/**
 * Created by HenryCo on 21/05/17.
 */

public class HelperTest implements DBExtraHelper {

	@Override
	public void onConfigure(SQLiteDatabase db, SQLiteOpenHelper helper) {

	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion, SQLiteOpenHelper helper) {

	}

	@Override
	public void onOpen(SQLiteDatabase db, SQLiteOpenHelper helper) {

	}

	@Override
	public void onCreate(SQLiteDatabase db, SQLiteOpenHelper helper){
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion, SQLiteOpenHelper helper) {
	}
}
