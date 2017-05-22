package net.henryco.sqlightning.dbstable.entity3;

import net.henryco.sqlightning.reflect.annotations.config.Configuration;
import net.henryco.sqlightning.reflect.annotations.config.DBName;
import net.henryco.sqlightning.reflect.annotations.config.DBVersion;
import net.henryco.sqlightning.reflect.annotations.config.Main;
import net.henryco.sqlightning.reflect.annotations.repository.Drop;
import net.henryco.sqlightning.reflect.annotations.repository.Table;

/**
 * Created by HenryCo on 16/05/17.
 */
@Main @DBName("ent31.db") @DBVersion(1)
@Configuration("configuration_three")
public class ConfigurationThree {

	@Table("table_ent3") @Drop
	private Entity3 entity3;
}
