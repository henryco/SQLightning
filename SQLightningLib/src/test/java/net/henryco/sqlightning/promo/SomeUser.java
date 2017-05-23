package net.henryco.sqlightning.promo;

import net.henryco.sqlightning.reflect.annotations.column.Column;
import net.henryco.sqlightning.reflect.annotations.column.Id;
import net.henryco.sqlightning.reflect.annotations.column.Relation;
import net.henryco.sqlightning.reflect.annotations.repository.Entity;
import net.henryco.sqlightning.reflect.annotations.repository.Table;
import net.henryco.sqlightning.reflect.table.stb.StbType;

/**
 * Created by HenryCo on 23/05/17
 */
@Entity
public class SomeUser {

	@Column("id")
	@Id private Long userID;

	@Column("first_name")
	private String firstName;

	@Column(type = StbType.TEXT)
	private String last_name;

	@Column @Relation
	@Table("some_data_table")
	private SomeData data;

	@Column("is_human")
	private Boolean isHuman = true;


	public SomeUser() {
	//	required public empty constructor
	}


	public Long getUserID() {
		return userID;
	}
	public void setUserID(Long userID) {
		this.userID = userID;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public SomeData getData() {
		return data;
	}
	public void setData(SomeData data) {
		this.data = data;
	}
}
