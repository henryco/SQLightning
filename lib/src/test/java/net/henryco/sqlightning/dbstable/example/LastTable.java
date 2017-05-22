package net.henryco.sqlightning.dbstable.example;

import net.henryco.sqlightning.reflect.annotations.column.Column;
import net.henryco.sqlightning.reflect.annotations.column.Id;
import net.henryco.sqlightning.reflect.annotations.column.Unique;
import net.henryco.sqlightning.reflect.annotations.repository.Drop;
import net.henryco.sqlightning.reflect.annotations.repository.Entity;
import net.henryco.sqlightning.reflect.annotations.repository.Table;

/**
 * Created by HenryCo on 11/05/17.
 */
@Entity
@Table("last_table") @Drop
public class LastTable {

	@Column("last_id") @Id
	private String lastId;

	@Unique
	@Column("double_value")
	private double dobval;
}
