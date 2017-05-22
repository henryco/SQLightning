package net.henryco.sqlightning.reflect.annotations.ameta.column.stdprocessor;

import net.henryco.sqlightning.reflect.annotations.ameta.column.MetaColumnExec;
import net.henryco.sqlightning.reflect.table.stb.StbColumn;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Created by HenryCo on 10/05/17.
 */

public class ColumnIdProcessor implements MetaColumnExec {

	@Override
	public StbColumn process(StbColumn column, Annotation self, Field annotatedField) {
		return column.primaryKey();
	}
}
