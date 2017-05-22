package net.henryco.sqlightning.reflect.table.stb;

import android.support.annotation.Nullable;

/**
 * Created by HenryCo on 10/05/17.
 */

public final class StbColumn {

	public static final class Foreign {

		private String table;
		private String key;

		private Foreign(final String table, final String key) {
			this.table = table;
			this.key = key;
		}

		public String getKey() {
			return key;
		}
		public String getTable() {
			return table;
		}
	}


	private String field;
	private String name;

	private String m_null;
	private String m_unique;
	private String m_default;

	private boolean m_key;
	private Foreign foreign_key;

	public StbColumn() {

		defaultValue("");
		primaryKey(false);
		notNull(false);
		unique(false);

		this.field = "";
		this.name = "";
	}

	public StbColumn(final String name, final StbType type) {
		this();
		field(name, type);
	}

	public StbColumn field(final String name, final StbType type) {
		this.field = name + " " +type.toString();
		this.name = name;
		return this;
	}

	public StbColumn notNull() {
		return notNull(true);
	}

	public StbColumn notNull(boolean notNull) {
		this.m_null = notNull || m_key || foreign_key != null ? " NOT NULL" : "";
		return this;
	}

	public StbColumn unique() {
		return unique(true);
	}

	public StbColumn unique(boolean unique) {
		this.m_unique = unique ? " UNIQUE" : "";
		return this;
	}

	public StbColumn defaultValue(Object object) {

		if (object == null) return defaultValue("");
		if (object instanceof Number) {
			Number number = (Number) object;
			return number.doubleValue() == 0d ? defaultValue("") : defaultValue(number);
		}
		if (object instanceof Boolean) {
			 return defaultValue(((Boolean) object) ? 0b1 : 0b0);
		}
		if (object.getClass() == char.class || object.getClass() == Character.class)
			return defaultValue(Character.getName((char) object).equals("NULL") ? "" : String.valueOf(object));
		return defaultValue(object.toString());
	}

	public StbColumn defaultValue(Number number) {
		this.m_default = number == null ? "" : " DEFAULT " + number.toString();
		return this;
	}

	public StbColumn defaultValue(String value) {
		this.m_default = value.isEmpty() ? "" : " DEFAULT \'" + value.trim() + "\'";
		return this;
	}

	public StbColumn primaryKey() {
		return primaryKey(true);
	}

	public StbColumn primaryKey(boolean key) {
		this.m_key = key;
		return notNull();
	}

	public StbColumn foreignKey(Foreign foreign) {
		this.foreign_key = foreign;
		return foreign == null ? this : notNull();
	}

	public StbColumn foreignKey(String table, String key) {
		return foreignKey(new Foreign(table, key));
	}

	@Nullable  public Foreign getForeign() {
		return foreign_key;
	}

	public boolean isPrimaryKey() {
		return m_key;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return field + m_null + m_unique + m_default;
	}
}
