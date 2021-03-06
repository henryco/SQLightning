package net.henryco.sqlightning.dbstable.entity2;

import net.henryco.sqlightning.reflect.annotations.config.Configuration;
import net.henryco.sqlightning.reflect.annotations.config.DBName;
import net.henryco.sqlightning.reflect.annotations.config.DBVersion;
import net.henryco.sqlightning.reflect.annotations.config.Main;
import net.henryco.sqlightning.reflect.annotations.repository.Drop;
import net.henryco.sqlightning.reflect.annotations.repository.Table;

/**
 * Created by HenryCo on 15/05/17.
 */

@Configuration("conf_2") @Main
@DBName("conf2Db.db") @DBVersion(1)
public class ConfigurationTwo {

	@Table @Drop
	private EntityTwo entityTwo;

}
