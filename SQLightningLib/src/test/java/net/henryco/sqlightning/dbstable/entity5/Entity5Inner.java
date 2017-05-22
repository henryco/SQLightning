package net.henryco.sqlightning.dbstable.entity5;

import net.henryco.sqlightning.reflect.annotations.column.Array;
import net.henryco.sqlightning.reflect.annotations.column.Column;
import net.henryco.sqlightning.reflect.annotations.column.Id;
import net.henryco.sqlightning.reflect.annotations.repository.Drop;
import net.henryco.sqlightning.reflect.annotations.repository.Entity;

import java.util.Arrays;

/**
 * Created by HenryCo on 18/05/17.
 */
@Entity
public class Entity5Inner {

	@Column("inner_id") @Id
	private String innerId;

	@Column("array")
	@Array(2) @Drop
	private Short[] innerArray;

	@Column("inner_int")
	private long innerInt;


	@Override
	public String toString() {
		return "Entity5Inner{" +
				"innerId='" + innerId + '\'' +
				", innerArray=" + Arrays.toString(innerArray) +
				", innerInt=" + innerInt +
				'}';
	}


	public String getInnerId() {
		return innerId;
	}

	public void setInnerId(String innerId) {
		this.innerId = innerId;
	}

	public Short[] getInnerArray() {
		return innerArray;
	}

	public void setInnerArray(Short[] innerArray) {
		this.innerArray = innerArray;
	}

	public long getInnerInt() {
		return innerInt;
	}

	public void setInnerInt(int innerInt) {
		this.innerInt = innerInt;
	}
}
