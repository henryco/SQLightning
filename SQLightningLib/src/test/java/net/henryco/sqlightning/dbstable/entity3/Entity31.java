package net.henryco.sqlightning.dbstable.entity3;

import net.henryco.sqlightning.reflect.annotations.column.Column;
import net.henryco.sqlightning.reflect.annotations.column.Id;
import net.henryco.sqlightning.reflect.annotations.repository.Entity;

/**
 * Created by HenryCo on 16/05/17.
 */
@Entity
public class Entity31 {

	@Column @Id
	private long uid;

	@Column("ent31_text")
	private String en31Text;


	@Override
	public String toString() {
		return "Entity31{" +
				"uid=" + uid +
				", en31Text='" + en31Text + '\'' +
				'}';
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getEn31Text() {
		return en31Text;
	}

	public void setEn31Text(String en31Text) {
		this.en31Text = en31Text;
	}
}
