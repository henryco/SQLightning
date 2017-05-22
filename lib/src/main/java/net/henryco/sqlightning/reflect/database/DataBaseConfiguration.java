package net.henryco.sqlightning.reflect.database;

import net.henryco.sqlightning.reflect.annotations.config.DBHelper;
import net.henryco.sqlightning.reflect.annotations.config.DBName;
import net.henryco.sqlightning.reflect.annotations.config.DBVersion;

/**
 * Created by HenryCo on 12/05/17
 */

public class DataBaseConfiguration {


	@DBName
	private String dbName;

	@DBVersion
	private int dbVersion;

	@DBHelper
	private DBExtraHelper extraHelper;

	public DataBaseConfiguration() {
		setDbName("");
		setDbVersion(-1);
		// TODO: 13/05/17
		setExtraHelper(DBHelper.methods.getDefault(this));
	}



	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public int getDbVersion() {
		return dbVersion;
	}

	public void setDbVersion(int dbVersion) {
		this.dbVersion = dbVersion;
	}

	public DBExtraHelper getExtraHelper() {
		return extraHelper;
	}

	public void setExtraHelper(DBExtraHelper extraHelper) {
		this.extraHelper = extraHelper;
	}
}
