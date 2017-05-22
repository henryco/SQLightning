package net.henryco.sqlightning.dbstable.example;

import net.henryco.sqlightning.reflect.annotations.config.Configuration;
import net.henryco.sqlightning.reflect.annotations.config.DBName;
import net.henryco.sqlightning.reflect.annotations.config.DBVersion;
import net.henryco.sqlightning.reflect.annotations.config.Main;
import net.henryco.sqlightning.reflect.annotations.repository.Drop;
import net.henryco.sqlightning.reflect.annotations.repository.Table;

/**
 * Created by HenryCo on 10/05/17.
 */

@Configuration("example_conf_name")
@Main
@DBVersion(1)
@DBName("ExampleDataBase.db")
public class ExampleConfiguration {

	@Table
	public EntityExample exampleTable;

	@Table("alone_table") @Drop
	public AloneTable aloneTable;

	@Table("other_table")
	private OtherEntity otherEntity;

	@Configuration
	private OtherConfiguration otherConfiguration;

}
