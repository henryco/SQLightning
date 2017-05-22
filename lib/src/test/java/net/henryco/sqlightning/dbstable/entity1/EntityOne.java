package net.henryco.sqlightning.dbstable.entity1;

import net.henryco.sqlightning.reflect.annotations.column.Column;
import net.henryco.sqlightning.reflect.annotations.column.Id;
import net.henryco.sqlightning.reflect.annotations.repository.Entity;

/**
 * Created by HenryCo on 14/05/17.
 */
@Entity
public class EntityOne {

	@Id @Column
	private int id;

	@Column("some_text_column")
	private String someText = "defaultText";



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSomeText() {
		return someText;
	}

	public void setSomeText(String someText) {
		this.someText = someText;
	}


	@Override
	public String toString() {
		return "EntityOne{" +
				"id=" + id +
				", someText='" + someText + '\'' +
				'}';
	}
}
