package net.henryco.sqlightning.dbstable.entity1;

import net.henryco.sqlightning.reflect.annotations.config.Configuration;
import net.henryco.sqlightning.reflect.annotations.config.DBName;
import net.henryco.sqlightning.reflect.annotations.config.DBVersion;
import net.henryco.sqlightning.reflect.annotations.config.Main;
import net.henryco.sqlightning.reflect.annotations.repository.Drop;
import net.henryco.sqlightning.reflect.annotations.repository.Table;

/**
 * Created by HenryCo on 14/05/17.
 */
@Main
@Configuration("main_configuration")
@DBName("testDbOne.db") @DBVersion(1)
public class ConfigurationOne {

	@Table("table_one") @Drop
	private EntityOne tableOne;


}
