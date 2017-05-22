package net.henryco.sqlightning.dbstable.example;

import net.henryco.sqlightning.reflect.annotations.column.Column;
import net.henryco.sqlightning.reflect.annotations.column.Id;
import net.henryco.sqlightning.reflect.annotations.repository.Entity;

/**
 * Created by HenryCo on 10/05/17.
 */
@Entity
public class AloneTable {

	@Id @Column
	private short alone_id;

	@Column
	private String alone_text;


}
