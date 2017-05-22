package net.henryco.sqlightning.dbstable.entity5;

import net.henryco.sqlightning.reflect.annotations.config.Configuration;
import net.henryco.sqlightning.reflect.annotations.config.DBHelper;
import net.henryco.sqlightning.reflect.annotations.config.DBName;
import net.henryco.sqlightning.reflect.annotations.config.DBVersion;
import net.henryco.sqlightning.reflect.annotations.repository.Drop;
import net.henryco.sqlightning.reflect.annotations.repository.Table;

/**
 * Created by HenryCo on 18/05/17.
 */
@DBName("DB5.db") @DBVersion(1)
@DBHelper(HelperTest.class)
@Configuration("configuration5")
public class Config5 {

	@Table("table_entity_5")
	@Drop Entity5 entity;

}
