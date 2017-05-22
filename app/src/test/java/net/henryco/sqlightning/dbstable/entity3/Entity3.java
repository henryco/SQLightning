package net.henryco.sqlightning.dbstable.entity3;

import net.henryco.sqlightning.reflect.annotations.column.Column;
import net.henryco.sqlightning.reflect.annotations.column.Id;
import net.henryco.sqlightning.reflect.annotations.column.Relation;
import net.henryco.sqlightning.reflect.annotations.repository.Drop;
import net.henryco.sqlightning.reflect.annotations.repository.Entity;
import net.henryco.sqlightning.reflect.annotations.repository.Table;

/**
 * Created by HenryCo on 16/05/17.
 */

@Entity
public class Entity3 {

	@Column("id") @Id
	private int idColumn;

	@Column("text_column")
	private String textCol;

	@Drop
	@Column("inner_entity")
	@Relation
	@Table("inner_table")
	private Entity31 otherEntity;

	@Column("short_value")
	private short val;


	@Override
	public String toString() {
		return "Entity3{" +
				"idColumn=" + idColumn +
				", textCol='" + textCol + '\'' +
				", otherEntity=" + otherEntity +
				", val=" + val +
				'}';
	}

	public int getIdColumn() {
		return idColumn;
	}

	public void setIdColumn(int idColumn) {
		this.idColumn = idColumn;
	}

	public String getTextCol() {
		return textCol;
	}

	public void setTextCol(String textCol) {
		this.textCol = textCol;
	}

	public Entity31 getOtherEntity() {
		return otherEntity;
	}

	public void setOtherEntity(Entity31 otherEntity) {
		this.otherEntity = otherEntity;
	}

	public short getVal() {
		return val;
	}

	public void setVal(short val) {
		this.val = val;
	}
}
