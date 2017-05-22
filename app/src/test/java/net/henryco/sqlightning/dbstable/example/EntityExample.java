package net.henryco.sqlightning.dbstable.example;

import net.henryco.sqlightning.reflect.annotations.column.Column;
import net.henryco.sqlightning.reflect.annotations.column.Id;
import net.henryco.sqlightning.reflect.annotations.column.NotNULL;
import net.henryco.sqlightning.reflect.annotations.column.Relation;
import net.henryco.sqlightning.reflect.annotations.column.Unique;
import net.henryco.sqlightning.reflect.annotations.repository.Entity;
import net.henryco.sqlightning.reflect.annotations.repository.Table;
import net.henryco.sqlightning.reflect.table.stb.StbType;

/**
 * Created by HenryCo on 10/05/17.
 */

@Entity
@Table("example_table")
public class EntityExample {

	@Id @Column(type = StbType.INTEGER)
	private int id;

	@Column(name = "some_field", type = StbType.TEXT)
	private String someField = "defText";

	@Unique
	@Column("unique_obj")
	private Object someUniqueObj;

	@NotNULL
	@Column(value = "not_null_value", type = StbType.REAL)
	private double notNullDouble = 5;

	@NotNULL @Column
	private String just_some_string;

	@Column("other_entity")
	@Relation
	@Table("other_table")
	private OtherEntity otherEntity;

	@Column("alone") @Relation
	private AloneTable aloneTable;

	@Column @Table @Relation
	private LastTable last_tab;




	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSomeField() {
		return someField;
	}

	public void setSomeField(String someField) {
		this.someField = someField;
	}

	public Object getSomeUniqueObj() {
		return someUniqueObj;
	}

	public void setSomeUniqueObj(Object someUniqueObj) {
		this.someUniqueObj = someUniqueObj;
	}

	public double getNotNullDouble() {
		return notNullDouble;
	}

	public void setNotNullDouble(double notNullDouble) {
		this.notNullDouble = notNullDouble;
	}

	public String getJust_some_string() {
		return just_some_string;
	}

	public void setJust_some_string(String just_some_string) {
		this.just_some_string = just_some_string;
	}

	public OtherEntity getOtherEntity() {
		return otherEntity;
	}

	public void setOtherEntity(OtherEntity otherEntity) {
		this.otherEntity = otherEntity;
	}

	public AloneTable getAloneTable() {
		return aloneTable;
	}

	public void setAloneTable(AloneTable aloneTable) {
		this.aloneTable = aloneTable;
	}

	public LastTable getLast_tab() {
		return last_tab;
	}

	public void setLast_tab(LastTable last_tab) {
		this.last_tab = last_tab;
	}
}
