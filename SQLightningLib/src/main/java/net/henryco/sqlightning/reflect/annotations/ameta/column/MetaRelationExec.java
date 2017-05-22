package net.henryco.sqlightning.reflect.annotations.ameta.column;

import net.henryco.sqlightning.reflect.table.SQLightningTable;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * Created by HenryCo on 16/05/17.
 */

public interface MetaRelationExec {

	SQLightningTable createRelatedTable(final String parentTableName,
										final Field field, final Collection<String> names);
}
