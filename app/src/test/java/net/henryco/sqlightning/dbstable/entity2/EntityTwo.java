package net.henryco.sqlightning.dbstable.entity2;

import net.henryco.sqlightning.reflect.annotations.column.Column;
import net.henryco.sqlightning.reflect.annotations.column.Id;
import net.henryco.sqlightning.reflect.annotations.column.NotNULL;
import net.henryco.sqlightning.reflect.annotations.repository.Entity;
import net.henryco.sqlightning.reflect.annotations.repository.Table;

/**
 * Created by HenryCo on 15/05/17.
 */
@Entity
@Table("example_table_two")
public class EntityTwo {

	@Column("id") @Id
	private long ID;

	@Column("text_one") @NotNULL
	private String smthText;

	@Column("int_value_one")
	private int intVal1;

	@Column("float_value")
	private float floatVal;

	@Column("short_value")
	private Short shortVal;

	@Column
	private Double double_value;

	@Column
	private Byte byte_value;

	@Column("boolean_value")
	private boolean bool;

	@Column("char_value")
	private char charVal;


	@Override
	public String toString() {
		return "EntityTwo{" +
				"ID=" + ID +
				", smthText='" + smthText + '\'' +
				", intVal1=" + intVal1 +
				", floatVal=" + floatVal +
				", shortVal=" + shortVal +
				", double_value=" + double_value +
				", byte_value=" + byte_value +
				", bool=" + bool +
				", charVal=" + charVal +
				'}';
	}

	public long getID() {
		return ID;
	}

	public void setID(long ID) {
		this.ID = ID;
	}

	public String getSmthText() {
		return smthText;
	}

	public void setSmthText(String smthText) {
		this.smthText = smthText;
	}

	public int getIntVal1() {
		return intVal1;
	}

	public void setIntVal1(int intVal1) {
		this.intVal1 = intVal1;
	}

	public float getFloatVal() {
		return floatVal;
	}

	public void setFloatVal(float floatVal) {
		this.floatVal = floatVal;
	}

	public Short getShortVal() {
		return shortVal;
	}

	public void setShortVal(Short shortVal) {
		this.shortVal = shortVal;
	}

	public Double getDouble_value() {
		return double_value;
	}

	public void setDouble_value(Double double_value) {
		this.double_value = double_value;
	}

	public Byte getByte_value() {
		return byte_value;
	}

	public void setByte_value(Byte byte_value) {
		this.byte_value = byte_value;
	}

	public boolean isBool() {
		return bool;
	}

	public void setBool(boolean bool) {
		this.bool = bool;
	}

	public char getCharVal() {
		return charVal;
	}

	public void setCharVal(char charVal) {
		this.charVal = charVal;
	}
}
