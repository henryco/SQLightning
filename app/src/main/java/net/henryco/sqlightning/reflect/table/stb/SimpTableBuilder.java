package net.henryco.sqlightning.reflect.table.stb;


import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by HenryCo.
 */

public final class SimpTableBuilder {

	private static final String THROW_MSG_EMPTY_TABLE = "Table must contains columns";
	private static final String THROW_MSG_NO_NAME_TABLE = "Table must have name";
	private static final String THROW_MSG_NO_KEYS_TABLE = "Table must contains at least one PRIMARY KEY";

	private static final String CASCADE = " ON UPDATE CASCADE ON DELETE CASCADE";

	private static final String DROP_TABLE = "\nDROP TABLE IF EXISTS ";
	private static final String CREATE_TABLE = "CREATE TABLE ";
	private static final String PRIMARY_KEY = "\tPRIMARY KEY ";
	private static final String FOREIGN_KEY = "\tFOREIGN KEY (";
	private static final String REFERENCES = " REFERENCES ";

	private static final String SEMICOLON=  ";";
	private static final String CASE_OPEN = "(";
	private static final String CASE_CLOSE = ")";
	private static final String COMA = ", ";


	private boolean dropTable;

	private String tableName;
	private String result;
	private String drop;
	private final List<StbColumn> columns;

	private SimpTableBuilder parentTable;
	private SimpTableBuilder childTable;


	public SimpTableBuilder() {
		dropIFExists(false);
		columns = new ArrayList<>();
		parentTable = null;
		childTable = null;
		tableName = null;
		result = "";
		drop = "";
	}
	public SimpTableBuilder(String tableName) {
		this();
		tableName(tableName);
	}


	private SimpTableBuilder create() {

		if (columns.size() == 0) throw new RuntimeException(THROW_MSG_EMPTY_TABLE);
		if (tableName == null) throw new RuntimeException(THROW_MSG_NO_NAME_TABLE);

		// String[] : [0] = inner_key and [1] = outer_key;
		Map<String, List<String[]>> foreignMap = new HashMap<>();
		List<String> keyList = new ArrayList<>();

		String head = CREATE_TABLE + tableName + " " + CASE_OPEN + "\n";
		String fields = "";
		for (StbColumn column : columns) {

			fields += "\t" + column.toString() + COMA.trim() +"\n";
			if (column.isPrimaryKey()) keyList.add(column.getName());

			StbColumn.Foreign foreign;
			if ((foreign = column.getForeign()) != null) {

				String foreignTableName = foreign.getTable();
				if (!foreignMap.containsKey(foreignTableName))
					foreignMap.put(foreignTableName, new ArrayList<>());

				foreignMap.get(foreignTableName).add(new String[]{column.getName(), foreign.getKey()});
			}
		}

		for (Map.Entry<String, List<String[]>> entry: foreignMap.entrySet()) {

			String inners = FOREIGN_KEY;
			String outers = REFERENCES +entry.getKey() + CASE_OPEN;

			Iterator<String[]> iterator = entry.getValue().iterator();
			while (iterator.hasNext()) {

				String[] keys = iterator.next();
				String ending = (iterator.hasNext() ? COMA : CASE_CLOSE);
				inners += keys[0] + ending;
				outers += keys[1] + ending;
			}

			fields += inners + outers + CASCADE +COMA.trim() +"\n";
		}

		String primKey = PRIMARY_KEY;
		if (keyList.size() == 0) throw new RuntimeException(THROW_MSG_NO_KEYS_TABLE);
		else {
			primKey += CASE_OPEN;
			Iterator<String> ktr = keyList.iterator();
			while (ktr.hasNext()) primKey += ktr.next() + (ktr.hasNext() ? COMA : "");
			primKey += CASE_CLOSE +"\n";
		}

		this.result = head + fields + primKey + CASE_CLOSE + SEMICOLON;
		return this;
	}

	private SimpTableBuilder drop() {
		this.drop = dropTable ? DROP_TABLE + tableName + SEMICOLON : "";
		return this;
	}

	public SimpTableBuilder addColumn(StbColumn column) {
		this.columns.add(column);
		return this;
	}

	public SimpTableBuilder tableName(String name) {
		this.tableName = name;
		return this;
	}

	public SimpTableBuilder dropIFExists() {
		return dropIFExists(true);
	}

	public SimpTableBuilder dropIFExists(boolean drop) {
		this.dropTable = drop;
		return this;
	}

	@Nullable
	public StbColumn getColumn(String columnName) {
		for (StbColumn column: columns)
			if (column.getName().equals(columnName))
				return column;
		return null;
	}

	public String getTableName() {
		return tableName;
	}


	private SimpTableBuilder setParent(SimpTableBuilder parent) {
		this.parentTable = parent;
		return this;
	}

	private SimpTableBuilder setChild(SimpTableBuilder child) {
		this.childTable = child;
		return this;
	}

	public SimpTableBuilder addTable(SimpTableBuilder table) {
		setChild(getRootTable(table).setParent(this));
		return getYoungerChildTable(table);
	}

	public SimpTableBuilder addTable(String tableName) {
		return addTable(new SimpTableBuilder(tableName));
	}

	public SimpTableBuilder addTable() {
		return addTable(new SimpTableBuilder());
	}

	private String recursiveCreation() {

		String result = "";
		SimpTableBuilder child = getRootTable(this);
		while (child != null) {
			result = result.concat(child.create().result.concat("\n\n"));
			child = child.childTable;
		}
		return result.trim();
	}

	private String recursiveDropping() {

		String drop = "";
		SimpTableBuilder child = getRootTable(this);
		while (child != null) {
			drop = drop.concat(child.drop().drop.concat(""));
			child = child.childTable;
		}
		return drop.trim();
	}

	public String getDropTable() {
		return recursiveDropping();
	}


	@Override
	public String toString() {
		return recursiveCreation();
	}


	public SimpTableBuilder getRoot() {
		return getRootTable(this);
	}
	public SimpTableBuilder getYoungest() {
		return getYoungerChildTable(this);
	}

	private static SimpTableBuilder getRootTable(SimpTableBuilder tableBuilder) {

		SimpTableBuilder root = tableBuilder;
		while (root.parentTable != null)
			root = root.parentTable;
		return root;
	}

	private static SimpTableBuilder getYoungerChildTable(SimpTableBuilder tableBuilder) {

		SimpTableBuilder child = tableBuilder;
		while (child.childTable != null)
			child = child.childTable;
		return child;
	}
}
