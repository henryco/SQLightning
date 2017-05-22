package net.henryco.sqlightning.dbstable;


import net.henryco.sqlightning.dbstable.example.EntityExample;
import net.henryco.sqlightning.dbstable.example.ExampleConfiguration;
import net.henryco.sqlightning.SQLightning;
import net.henryco.sqlightning.reflect.annotations.ameta.column.stdprocessor.ColumnRelationProcessor;
import net.henryco.sqlightning.reflect.annotations.column.Relation;
import net.henryco.sqlightning.reflect.database.DBSConfigurationFinder;
import net.henryco.sqlightning.reflect.table.SQLightningTable;
import net.henryco.sqlightning.reflect.table.SQLightningTableBuilder;
import net.henryco.sqlightning.reflect.table.stb.SimpTableBuilder;
import net.henryco.sqlightning.reflect.table.stb.StbColumn;
import net.henryco.sqlightning.reflect.table.stb.StbType;

import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;


public class DBTableCreatorTests {


	@Test
	public void chaining_creation_test() {
		String testTable = new SimpTableBuilder("test_table")
				.addColumn(new StbColumn("id", StbType.INTEGER).primaryKey())
				.addColumn(new StbColumn("first_name", StbType.TEXT).notNull())
				.addColumn(new StbColumn("last_name", StbType.TEXT))
				.addColumn(new StbColumn("age", StbType.INTEGER))
				.addColumn(new StbColumn("gender", StbType.TEXT))
				.addTable("second_table")
				.addColumn(new StbColumn("id", StbType.INTEGER).primaryKey())
				.addColumn(new StbColumn("sec_id", StbType.REAL).primaryKey().foreignKey("test_table", "id"))
				.addColumn(new StbColumn("some_val", StbType.REAL).unique())
				.addColumn(new StbColumn("other_var", StbType.TEXT).notNull().foreignKey("test_table", "first_name"))
				.addColumn(new StbColumn("null_value", StbType.BLOB))
				.addColumn(new StbColumn("key_val", StbType.INTEGER).foreignKey("third_table", "not_id"))
				.addTable("third_table")
				.addColumn(new StbColumn("not_id", StbType.TEXT).primaryKey())
				.addColumn(new StbColumn("void_column", StbType.TEXT).defaultValue("default"))
				.addColumn(new StbColumn("last_col", StbType.REAL).defaultValue(24))

				.toString();

		assertEquals("CREATE TABLE test_table (\n" +
				"\tid INTEGER NOT NULL,\n" +
				"\tfirst_name TEXT NOT NULL,\n" +
				"\tlast_name TEXT,\n" +
				"\tage INTEGER,\n" +
				"\tgender TEXT,\n" +
				"\tPRIMARY KEY (id)\n" +
				");\n" +
				"\n" +
				"CREATE TABLE second_table (\n" +
				"\tid INTEGER NOT NULL,\n" +
				"\tsec_id REAL NOT NULL,\n" +
				"\tsome_val REAL UNIQUE,\n" +
				"\tother_var TEXT NOT NULL,\n" +
				"\tnull_value BLOB,\n" +
				"\tkey_val INTEGER NOT NULL,\n" +
				"\tFOREIGN KEY (sec_id, other_var) REFERENCES test_table(id, first_name) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
				"\tFOREIGN KEY (key_val) REFERENCES third_table(not_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
				"\tPRIMARY KEY (id, sec_id)\n" +
				");\n" +
				"\n" +
				"CREATE TABLE third_table (\n" +
				"\tnot_id TEXT NOT NULL,\n" +
				"\tvoid_column TEXT DEFAULT 'default',\n" +
				"\tlast_col REAL DEFAULT 24,\n" +
				"\tPRIMARY KEY (not_id)\n" +
				");", testTable);
	}



	@Test
	public void type_find_test() {

		assertEquals(StbType.INTEGER, StbType.findType(byte.class));
		assertEquals(StbType.INTEGER, StbType.findType(Byte.class));
		assertEquals(StbType.INTEGER, StbType.findType(false));
		assertEquals(StbType.REAL, StbType.findType(float.class));
		assertEquals(StbType.REAL, StbType.findType(2d));
		assertEquals(StbType.TEXT, StbType.findType("some text"));
		assertEquals(StbType.TEXT, StbType.findType(""));
		assertEquals(StbType.TEXT, StbType.findType(String.class));
		assertEquals(StbType.TEXT, StbType.findType(char.class));
	}

	@Test
	public void relation_type_finder_test() throws NoSuchFieldException {

		Field field = EntityExample.class.getDeclaredField("otherEntity");
		Relation relation = field.getAnnotation(Relation.class);
		assertEquals(StbType.BLOB, ColumnRelationProcessor.getRelatedType(field, relation));
	}

	@Test
	public void relation_table_key_test() throws NoSuchFieldException {

		Field field = EntityExample.class.getDeclaredField("otherEntity");
		Relation relation = field.getAnnotation(Relation.class);
		String[] tableKey = ColumnRelationProcessor.getRelatedTableKey(field, relation);

		Field otherField = EntityExample.class.getDeclaredField("aloneTable");
		Relation otherRelation = otherField.getAnnotation(Relation.class);
		String[] otherTabKey = ColumnRelationProcessor.getRelatedTableKey(otherField, otherRelation);

		Field lastField = EntityExample.class.getDeclaredField("last_tab");
		Relation lastRelation = lastField.getAnnotation(Relation.class);
		String[] lastTabKey = ColumnRelationProcessor.getRelatedTableKey(lastField, lastRelation);

		assertArrayEquals(new String[]{"other_table", "related_key"}, tableKey);
		assertArrayEquals(new String[]{"AloneTable", "related_key"}, otherTabKey);
		assertArrayEquals(new String[]{"last_table", "related_key"}, lastTabKey);
	}


	@Test
	public void dbsTable_loading_test() throws NoSuchFieldException, IllegalAccessException, InstantiationException {

		Field tableField = ExampleConfiguration.class.getDeclaredField("exampleTable");
		SQLightningTable table = new SQLightningTable(tableField, null);
		SimpTableBuilder builder = table.getTableBuilder();

		assertNotNull(builder);
		String result = builder.getDropTable() + "\n\n" + builder.toString();
		assertEquals("DROP TABLE IF EXISTS last_table;\n" +
				"\n" +
				"CREATE TABLE example_table (\n" +
				"\tid INTEGER NOT NULL,\n" +
				"\tsome_field TEXT DEFAULT 'defText',\n" +
				"\tunique_obj BLOB UNIQUE,\n" +
				"\tnot_null_value REAL NOT NULL DEFAULT 5.0,\n" +
				"\tjust_some_string TEXT NOT NULL,\n" +
				"\tPRIMARY KEY (id)\n" +
				");\n" +
				"\n" +
				"CREATE TABLE other_table (\n" +
				"\tother_id INTEGER NOT NULL,\n" +
				"\tother_column TEXT,\n" +
				"\trelated_key INTEGER NOT NULL,\n" +
				"\tFOREIGN KEY (related_key) REFERENCES example_table(id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
				"\tPRIMARY KEY (other_id)\n" +
				");\n" +
				"\n" +
				"CREATE TABLE AloneTable (\n" +
				"\talone_id INTEGER NOT NULL,\n" +
				"\talone_text TEXT,\n" +
				"\trelated_key INTEGER NOT NULL,\n" +
				"\tFOREIGN KEY (related_key) REFERENCES example_table(id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
				"\tPRIMARY KEY (alone_id)\n" +
				");\n" +
				"\n" +
				"CREATE TABLE last_table (\n" +
				"\tlast_id TEXT NOT NULL,\n" +
				"\tdouble_value REAL UNIQUE,\n" +
				"\trelated_key INTEGER NOT NULL,\n" +
				"\tFOREIGN KEY (related_key) REFERENCES example_table(id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
				"\tPRIMARY KEY (last_id)\n" +
				");", result);
	}

	@Test
	public void dbCreator_test() {


		SQLightningTableBuilder dbsTableBuilder = new SQLightningTableBuilder();
		dbsTableBuilder.setConfigurator(new DBSConfigurationFinder(EntityExample.class));
		dbsTableBuilder.create();
		String result = dbsTableBuilder.getDroppedTables() + "\n\n" + dbsTableBuilder.toString();

		assertNotEquals("CREATION ERROR", result);
		assertEquals("DROP TABLE IF EXISTS alone_table;\n" +
				"DROP TABLE IF EXISTS last_table;\n" +
				"\n" +
				"CREATE TABLE alone_table (\n" +
				"\talone_id INTEGER NOT NULL,\n" +
				"\talone_text TEXT,\n" +
				"\tPRIMARY KEY (alone_id)\n" +
				");\n" +
				"\n" +
				"CREATE TABLE other_table (\n" +
				"\tother_id INTEGER NOT NULL,\n" +
				"\tother_column TEXT,\n" +
				"\tPRIMARY KEY (other_id)\n" +
				");\n" +
				"\n" +
				"CREATE TABLE example_table (\n" +
				"\tid INTEGER NOT NULL,\n" +
				"\tsome_field TEXT DEFAULT 'defText',\n" +
				"\tunique_obj BLOB UNIQUE,\n" +
				"\tnot_null_value REAL NOT NULL DEFAULT 5.0,\n" +
				"\tjust_some_string TEXT NOT NULL,\n" +
				"\tPRIMARY KEY (id)\n" +
				");\n" +
				"\n" +
				"CREATE TABLE AloneTable (\n" +
				"\talone_id INTEGER NOT NULL,\n" +
				"\talone_text TEXT,\n" +
				"\trelated_key INTEGER NOT NULL,\n" +
				"\tFOREIGN KEY (related_key) REFERENCES example_table(id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
				"\tPRIMARY KEY (alone_id)\n" +
				");\n" +
				"\n" +
				"CREATE TABLE last_table (\n" +
				"\tlast_id TEXT NOT NULL,\n" +
				"\tdouble_value REAL UNIQUE,\n" +
				"\trelated_key INTEGER NOT NULL,\n" +
				"\tFOREIGN KEY (related_key) REFERENCES example_table(id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
				"\tPRIMARY KEY (last_id)\n" +
				");", result);
	}


	@Test
	public void sqLightningRunWithNoContextOneTime_test() {

		SQLightning.setDebugEnable(true);
		String debugResult = SQLightning.run(null, ExampleConfiguration.class);
		String secondResult = SQLightning.run(null, ExampleConfiguration.class);
		assertEquals("Cannot call method: " +SQLightning.class.getSimpleName()
				+ ".run(), more then one time!", secondResult);

		assertEquals("DROP TABLE IF EXISTS alone_table;\n" +
				"DROP TABLE IF EXISTS last_table;\n" +
				"\n" +
				"CREATE TABLE alone_table (\n" +
				"\talone_id INTEGER NOT NULL,\n" +
				"\talone_text TEXT,\n" +
				"\tPRIMARY KEY (alone_id)\n" +
				");\n" +
				"\n" +
				"CREATE TABLE other_table (\n" +
				"\tother_id INTEGER NOT NULL,\n" +
				"\tother_column TEXT,\n" +
				"\tPRIMARY KEY (other_id)\n" +
				");\n" +
				"\n" +
				"CREATE TABLE example_table (\n" +
				"\tid INTEGER NOT NULL,\n" +
				"\tsome_field TEXT DEFAULT 'defText',\n" +
				"\tunique_obj BLOB UNIQUE,\n" +
				"\tnot_null_value REAL NOT NULL DEFAULT 5.0,\n" +
				"\tjust_some_string TEXT NOT NULL,\n" +
				"\tPRIMARY KEY (id)\n" +
				");\n" +
				"\n" +
				"CREATE TABLE AloneTable (\n" +
				"\talone_id INTEGER NOT NULL,\n" +
				"\talone_text TEXT,\n" +
				"\trelated_key INTEGER NOT NULL,\n" +
				"\tFOREIGN KEY (related_key) REFERENCES example_table(id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
				"\tPRIMARY KEY (alone_id)\n" +
				");\n" +
				"\n" +
				"CREATE TABLE last_table (\n" +
				"\tlast_id TEXT NOT NULL,\n" +
				"\tdouble_value REAL UNIQUE,\n" +
				"\trelated_key INTEGER NOT NULL,\n" +
				"\tFOREIGN KEY (related_key) REFERENCES example_table(id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
				"\tPRIMARY KEY (last_id)\n" +
				");", debugResult);
	}





}
