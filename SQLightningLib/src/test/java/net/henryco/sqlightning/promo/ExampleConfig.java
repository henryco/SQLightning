package net.henryco.sqlightning.promo;

import net.henryco.sqlightning.reflect.annotations.config.Configuration;
import net.henryco.sqlightning.reflect.annotations.config.DBName;
import net.henryco.sqlightning.reflect.annotations.config.DBVersion;
import net.henryco.sqlightning.reflect.annotations.config.Main;
import net.henryco.sqlightning.reflect.annotations.repository.Drop;
import net.henryco.sqlightning.reflect.annotations.repository.Table;

/**
 * Created by HenryCo on 23/05/17
 */

@Main
@DBName("example.db")
@DBVersion(1)
@Configuration("example_configuration")
public class ExampleConfig {

	@Drop @Table("user_table")
	SomeUser user;

	@Configuration
	OtherEmptyConfig config;

}
