package net.henryco.sqlightning.promo;

import net.henryco.sqlightning.reflect.annotations.column.Array;
import net.henryco.sqlightning.reflect.annotations.column.Column;
import net.henryco.sqlightning.reflect.annotations.column.Id;
import net.henryco.sqlightning.reflect.annotations.repository.Entity;

import java.util.List;

/**
 * Created by HenryCo on 23/05/17.
 */
@Entity
public class SomeData {

	@Column @Id
	private String id;

	@Array(3) @Column("some_int_list")
	private List<Integer> someIntList;

	@Array(15) @Column("short_array")
	private short[] someShortArray;

	public SomeData() {
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<Integer> getSomeIntList() {
		return someIntList;
	}
	public void setSomeIntList(List<Integer> someIntList) {
		this.someIntList = someIntList;
	}
	public short[] getSomeShortArray() {
		return someShortArray;
	}
	public void setSomeShortArray(short[] someShortArray) {
		this.someShortArray = someShortArray;
	}
}
