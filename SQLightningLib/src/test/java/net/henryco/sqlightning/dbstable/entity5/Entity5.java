package net.henryco.sqlightning.dbstable.entity5;

import net.henryco.sqlightning.reflect.annotations.column.Array;
import net.henryco.sqlightning.reflect.annotations.column.Column;
import net.henryco.sqlightning.reflect.annotations.column.Id;
import net.henryco.sqlightning.reflect.annotations.column.Relation;
import net.henryco.sqlightning.reflect.annotations.repository.Drop;
import net.henryco.sqlightning.reflect.annotations.repository.Entity;
import net.henryco.sqlightning.reflect.annotations.repository.Table;

import java.util.Arrays;
import java.util.List;

/**
 * Created by HenryCo on 18/05/17.
 */
@Entity
public class Entity5 {

	@Column("id") @Id
	private long entityId;

	@Column("ent_text")
	private String entityText;

	@Column("bytes")
	@Array(3) @Drop
	private List<Byte> byteList;

	@Column @Relation
	@Table("inner_table") @Drop
	private Entity5Inner inner;

	@Drop @Array(2)
	@Column("text_array")
	private String[] textArray;


	@Override
	public String toString() {
		return "Entity5{" +
				"entityId=" + entityId +
				", entityText='" + entityText + '\'' +
				", byteList=" + byteList +
				", inner=" + inner +
				", textArray=" + Arrays.toString(textArray) +
				'}';
	}

	public long getEntityId() {
		return entityId;
	}

	public void setEntityId(long entityId) {
		this.entityId = entityId;
	}

	public String getEntityText() {
		return entityText;
	}

	public void setEntityText(String entityText) {
		this.entityText = entityText;
	}

	public List<Byte> getByteList() {
		return byteList;
	}

	public void setByteList(List<Byte> byteList) {
		this.byteList = byteList;
	}

	public Entity5Inner getInner() {
		return inner;
	}

	public void setInner(Entity5Inner inner) {
		this.inner = inner;
	}

	public String[] getTextArray() {
		return textArray;
	}

	public void setTextArray(String[] textArray) {
		this.textArray = textArray;
	}
}
