package net.henryco.sqlightning.dbstable.entity4;

import net.henryco.sqlightning.reflect.annotations.config.Configuration;
import net.henryco.sqlightning.reflect.annotations.config.DBName;
import net.henryco.sqlightning.reflect.annotations.config.DBVersion;
import net.henryco.sqlightning.reflect.annotations.repository.Drop;
import net.henryco.sqlightning.reflect.annotations.repository.Table;

/**
 * Created by HenryCo on 17/05/17.
 */
@DBName("ent4.db") @DBVersion(1)
@Configuration("config_4")
public class ConfigurationFour {

	@Drop
	@Table("arrayed_table")
	private Entity4 table;
}
