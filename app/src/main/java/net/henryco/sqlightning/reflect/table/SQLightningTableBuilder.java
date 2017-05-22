package net.henryco.sqlightning.reflect.table;


import android.support.annotation.Nullable;

import net.henryco.sqlightning.reflect.database.DBSConfigurationFinder;
import net.henryco.sqlightning.reflect.table.stb.SimpTableBuilder;
import net.henryco.sqlightning.utils.SQLightningDebugable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by HenryCo on 10/05/17.
 */

public class SQLightningTableBuilder implements SQLightningDebugable {


	private static final String text_debug_field_name = "<FieldName: ";
	private static final String NO_CONFIGURATOR_THROW_MSG
			= "SQLightningTableBuilder required DBSConfigurationFinder";


	private final StringBuilder debugStatBuilder;
	private SimpTableBuilder builder;
	private DBSConfigurationFinder configurator;


	public SQLightningTableBuilder() {
		builder = null;
		this.debugStatBuilder = new StringBuilder();
	}

	public SQLightningTableBuilder(DBSConfigurationFinder configurator) {
		this();
		setConfigurator(configurator);
	}

	public SQLightningTableBuilder setConfigurator(DBSConfigurationFinder configurator) {
		this.configurator = configurator;
		return this;
	}

	public SQLightningTableBuilder create() {

		debugStatBuilder.setLength(0);

		if (configurator == null) throw new RuntimeException(NO_CONFIGURATOR_THROW_MSG);
		if (!configurator.isConfigurationsFound()) configurator.createConfiguration();

		Map<String, Field> tables = configurator.getTableMap();
		List<String> tableNames = new ArrayList<>();
		List<SQLightningTable> tbaList = new ArrayList<>();

		for (Field field : tables.values()) {

			SQLightningTable table = new SQLightningTable(field, tableNames);
			if (table.getTableBuilder() != null) tbaList.add(table);
			debugEnd(debugStatBuilder, table.getDebugString(), field.getName());
		}

		for (SQLightningTable t: tbaList) {

			SimpTableBuilder b = t.getTableBuilder();
			if (b != null) {
				if (builder == null) builder = b;
				else builder = builder.addTable(b);
			}
		}

		return this;
	}


	public DBSConfigurationFinder getConfigurator() {
		return this.configurator;
	}

	@Nullable
	public SimpTableBuilder getCreatedTables() {
		return builder;
	}

	@Override
	public String toString() {
		return builder == null ? "CREATION ERROR" : builder.toString();
	}

	public String getDroppedTables() {
		return builder == null ? "CREATION ERROR" : builder.getDropTable();
	}

	@Override
	public StringBuilder getDebugStatBuilder() {
		return debugStatBuilder;
	}

	@Override
	public String getDebugString() {
		return debugStatBuilder.toString();
	}





	private static void debugEnd(StringBuilder debugStatBuilder, String debugString, String fieldName) {

		debugStatBuilder.append(text_debug_field_name).append(fieldName);
		debugStatBuilder.append(debugString);
		debugStatBuilder.append("\n/>\n\n");
	}
}
