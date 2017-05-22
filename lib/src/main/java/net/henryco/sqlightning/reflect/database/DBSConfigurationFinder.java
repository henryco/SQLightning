package net.henryco.sqlightning.reflect.database;

import net.henryco.sqlightning.reflect.annotations.config.Configuration;
import net.henryco.sqlightning.reflect.annotations.repository.Table;
import net.henryco.sqlightning.utils.ClassFinder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HenryCo on 10/05/17.
 */

@SuppressWarnings("Convert2streamapi")
public final class DBSConfigurationFinder {


	private static final String CONF_ANNOTATION_INNER_CONF_THROW_MSG
			= "Field annotated as @Configuration in other Configuration class must have empty annotation value (configuration name)";


	private final List<Class> configList;
	private final Map<String, Class> configMap;
	private final Package rootPackage;

	private boolean configsFund;

	public DBSConfigurationFinder(Class rootClass, List<Class> configList) {
		this(rootClass.getPackage(), configList);
	}

	public DBSConfigurationFinder(Package rootPackage, List<Class> configList) {

		this.configList = configList;
		this.configMap = new HashMap<>();
		this.rootPackage = rootPackage;
		this.configsFund = false;
	}

	public DBSConfigurationFinder(Package rootPackage, Class ... configs) {
		this(rootPackage, createConfList(configs));
	}

	public DBSConfigurationFinder(Class rootClass, Class ... configs) {
		this(rootClass, createConfList(configs));
	}

	private static List<Class> createConfList(Class ... configs) {
		List<Class> list = new ArrayList<>();
		Collections.addAll(list, configs);
		return list;
	}



	public DBSConfigurationFinder addConfiguration(Class ... configs) {
		Collections.addAll(configList, configs);
		return this;
	}

	public DBSConfigurationFinder createConfiguration() {
		processConfigs(configList);
		configsFund = true;
		return this;
	}

	private void processConfigs(List<Class> configList) {
		try {
			for (Class confClass: ClassFinder.find(rootPackage.getName())) addConfigToMap(confClass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (Class config: configList) addConfigToMap(config);
	}

	private void addConfigToMap(Class config) {

		Configuration configAnnotation = (Configuration) config.getAnnotation(Configuration.class);
		if (configAnnotation != null) {

			String configName = configAnnotation.value();
			configMap.put(configName.isEmpty() ? config.getSimpleName() : configName, config);

			Field[] fields = config.getDeclaredFields();
			for (Field field: fields) {

				Configuration configuration = field.getAnnotation(Configuration.class);
				if (configuration != null) {

					if (!configuration.value().isEmpty()) throw new RuntimeException(CONF_ANNOTATION_INNER_CONF_THROW_MSG);
					addConfigToMap(field.getType());
				}
			}
		}
	}


	private Map<String, Field> processTables() {

		final Map<String, Field> tableMap = new HashMap<>();
		for (Map.Entry<String, Class> entry : configMap.entrySet()) {

			Class config = entry.getValue();
			Field[] tables = config.getDeclaredFields();
			for (Field table: tables) {

				Table tabAnnotation = table.getAnnotation(Table.class);
				if (tabAnnotation != null) {

					String tableName = tabAnnotation.value();
					Class tableClass = table.getType();
					if (tableName.isEmpty()) {

						Table supTable = (Table) tableClass.getAnnotation(Table.class);
						if (supTable == null || (tableName = supTable.value()).isEmpty())
							tableName = tableClass.getSimpleName();
					}

					tableMap.put(tableName, table);
				}
			}
		}
		return tableMap;
	}

	public Map<String, Field> getTableMap() {
		return processTables();
	}

	public Map<String, Class> getConfigMap() {
		return configMap;
	}

	public boolean isConfigurationsFound() {
		return configsFund;
	}
}
