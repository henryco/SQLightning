package net.henryco.sqlightning.dbstable.entity4;

import net.henryco.sqlightning.reflect.annotations.column.Array;
import net.henryco.sqlightning.reflect.annotations.column.Column;
import net.henryco.sqlightning.reflect.annotations.column.Id;
import net.henryco.sqlightning.reflect.annotations.repository.Drop;
import net.henryco.sqlightning.reflect.annotations.repository.Entity;

import java.util.Arrays;
import java.util.List;

/**
 * Created by HenryCo on 16/05/17.
 */
@Entity
public class Entity4 {

	@Column @Id
	private int id;


	@Array @Drop
	@Column(value = "array")
	private float[] arr;


	@Array(5) @Drop
	@Column("int_array")
	public Short[] intArray;

	@Array(3)
	@Column("list") @Drop
	private List<String> textList;


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float[] getArr() {
		return arr;
	}

	public void setArr(float[] arr) {
		this.arr = arr;
	}

	public List<String> getTextList() {
		return textList;
	}

	public void setTextList(List<String> textList) {
		this.textList = textList;
	}


	@Override
	public String toString() {
		return "Entity4{" +
				"id=" + id +
				", arr=" + Arrays.toString(arr) +
				", intArray=" + Arrays.toString(intArray) +
				", textList=" + textList +
				'}';
	}
}
