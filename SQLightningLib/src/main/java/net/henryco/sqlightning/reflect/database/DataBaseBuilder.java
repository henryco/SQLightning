package net.henryco.sqlightning.reflect.database;

import android.content.Context;

import net.henryco.sqlightning.reflect.annotations.ameta.config.MetaConfiguration;
import net.henryco.sqlightning.reflect.annotations.config.Configuration;
import net.henryco.sqlightning.reflect.annotations.config.Main;
import net.henryco.sqlightning.utils.SQLightningDebugable;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by HenryCo on 11/05/17.
 */

public class DataBaseBuilder implements SQLightningDebugable {

	private static final String CONF_NO_ANNOTATION_THROW_MSG
			= "Configuration class must be annotated as @Configuration and contains name";
	private static final String CONF_NO_NAME_OR_VERSION_THROW_MSG
			= "At least one configuration class annotated as @Configuration, must be annotated as @DBName and @DBVersion too";
	private static final String CONF_NO_CREATE_TABLE_TEXT_MSG
			= "Need string file with table creation query";

	private static final String DEBUG_HEAD = "\n<DataBaseBuilder: ";
	private static final String DEBUG_MAIN_CONF = "\n\tMainConfiguration: ";
	private static final String DEBUG_MAIN_CONF_NONE = "NONE";
	private static final String DEBUG_CONFIGURATION = "\n\t<Configuration\n\t\tname: ";
	private static final String DEBUG_CONFIGURATION_CLASS = "\n\t\tclass: ";
	private static final String DEBUG_CONFIGURATION_CLOSE = "\n\t/>";
	private static final String DEBUG_DB_NAME = "\n\tDBName: ";
	private static final String DEBUG_DB_VERSION = "\n\tDBVersion: ";
	private static final String DEBUG_CLOSE = "\n/>\n\n";
	private static final String DEBUG_SEP = ",";

	private final StringBuilder debugStrBuilder;
	private final List<Class> configList;


	private String mainConfig;
	private String tableCreateText;
	private String tableDropText;
	private DBSConfigurationFinder configurator;

	private String dbName;
	private int dbVersion;
	private DBExtraHelper extraHelper;

	public DataBaseBuilder() {
		this.mainConfig = null;
		this.configList = new ArrayList<>();
		this.debugStrBuilder = new StringBuilder();
	}

	public DataBaseBuilder addConfiguration(Class ... configuration) {
		Collections.addAll(configList, configuration);
		return this;
	}

	public DataBaseBuilder addConfigurator(DBSConfigurationFinder configurator) {
		this.configurator = configurator;
		return this;
	}

	public DataBaseBuilder setMainConfig(String mainConfig) {
		this.mainConfig = mainConfig;
		return this;
	}

	public DataBaseBuilder setCreateTableText(String tableCreateText) {
		this.tableCreateText = tableCreateText;
		return this;
	}


	public DataBaseBuilder setDropTableText(String tableDropText) {
		this.tableDropText = tableDropText;
		return this;
	}


	public DataBaseBuilder create(Context context) {

		if (configurator != null) {
			if (!configurator.isConfigurationsFound()) configurator.createConfiguration();
			configList.addAll(configurator.getConfigMap().values());
		}

		for (Class config: configList) {

			Annotation confAnnotation = config.getAnnotation(Configuration.class);
			Annotation mainAnnotation = config.getAnnotation(Main.class);
			if (confAnnotation == null) throw new RuntimeException(CONF_NO_ANNOTATION_THROW_MSG);
			String conf_name = ((Configuration) confAnnotation).value();
			if (mainAnnotation != null) mainConfig = conf_name;
		}

		debugHead(debugStrBuilder, mainConfig);
		DataBaseConfiguration dbConfiguration = new DataBaseConfiguration();
		for (Class config: configList) {

			Configuration configuration = (Configuration) config.getAnnotation(Configuration.class);
			debugConfName(debugStrBuilder, configuration.value(), config.getSimpleName());
			if (mainConfig == null || configuration.value().equals(mainConfig)) {

				for (Annotation ann : config.getAnnotations()) {
					MetaConfiguration meta = ann.annotationType().getAnnotation(MetaConfiguration.class);
					if (meta != null) try {
						dbConfiguration = meta.value().newInstance().process(dbConfiguration, ann);
					} catch (Exception ignored) {}
				}
			}
		}
		// TODO: 13/05/17
		dbVersion = dbConfiguration.getDbVersion();
		dbName = dbConfiguration.getDbName();
		extraHelper = dbConfiguration.getExtraHelper();

		debugDB(debugStrBuilder, dbName, dbVersion);

		if (dbName.isEmpty() || dbVersion == -1) throw new RuntimeException(CONF_NO_NAME_OR_VERSION_THROW_MSG);
		if (tableCreateText == null) throw new RuntimeException(CONF_NO_CREATE_TABLE_TEXT_MSG);
		if (context != null) new DBDataBase(extraHelper, context, dbName, dbVersion, tableCreateText, tableDropText);
		return this;
	}




	public DBDataBase getCreatedDataBase(Context context) {
		if (context != null) return new DBDataBase(extraHelper, context, dbName, dbVersion, tableCreateText, tableDropText);
		return null;
	}

	@Override
	public StringBuilder getDebugStatBuilder() {
		return debugStrBuilder;
	}

	@Override
	public String getDebugString() {
		return debugStrBuilder.toString();
	}





	private static void debugHead(final StringBuilder debugStrBuilder, String mainConfig) {

		debugStrBuilder.setLength(0);
		debugStrBuilder.append(DEBUG_HEAD);
		debugStrBuilder.append(DEBUG_MAIN_CONF).append(mainConfig == null ? DEBUG_MAIN_CONF_NONE : mainConfig);
		debugStrBuilder.append(DEBUG_SEP);
	}

	private static void debugConfName(final StringBuilder debugStrBuilder, String conf_name, String className) {

		debugStrBuilder.append(DEBUG_CONFIGURATION).append(conf_name).append(DEBUG_SEP).append(DEBUG_CONFIGURATION_CLASS);
		debugStrBuilder.append(className).append(DEBUG_SEP).append(DEBUG_CONFIGURATION_CLOSE);
	}

	private static void debugDB(final StringBuilder debugStrBuilder, String dbName, int dbVersion) {

		debugStrBuilder.append(DEBUG_DB_NAME).append(dbName).append(DEBUG_SEP);
		debugStrBuilder.append(DEBUG_DB_VERSION).append(dbVersion).append(DEBUG_CLOSE);
	}
}
