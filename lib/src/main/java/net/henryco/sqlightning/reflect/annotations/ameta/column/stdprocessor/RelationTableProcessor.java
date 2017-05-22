package net.henryco.sqlightning.reflect.annotations.ameta.column.stdprocessor;

import net.henryco.sqlightning.reflect.annotations.ameta.column.MetaRelationExec;
import net.henryco.sqlightning.reflect.annotations.column.Column;
import net.henryco.sqlightning.reflect.annotations.column.Id;
import net.henryco.sqlightning.reflect.annotations.column.Relation;
import net.henryco.sqlightning.reflect.table.SQLightningTable;
import net.henryco.sqlightning.reflect.table.stb.SimpTableBuilder;
import net.henryco.sqlightning.reflect.table.stb.StbColumn;
import net.henryco.sqlightning.reflect.table.stb.StbType;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * Created by HenryCo on 16/05/17.
 */

public class RelationTableProcessor implements MetaRelationExec {

	private static final String NO_RELATION_THROW_MSG
			= "Table field must be annotated as @Relation(arg)";
	private static final String NO_COLUMN_THROW_MSG
			= "Table field must be annotated as @Column too";
	private static final String CANNOT_FIND_RELATED_ID
			= "Can't find related id for array table: ";
	@Override
	public SQLightningTable createRelatedTable(
			final String parentTableName, final Field field, final Collection<String> names) {

		Relation relation = field.getAnnotation(Relation.class);
		if (relation == null) throw new RuntimeException(NO_RELATION_THROW_MSG);
		Column columnAnnotation = field.getAnnotation(Column.class);
		if (columnAnnotation == null) throw new RuntimeException(NO_COLUMN_THROW_MSG);

		SQLightningTable table = new SQLightningTable(field, names);
		SimpTableBuilder tableBuilder = table.getTableBuilder();
		if (tableBuilder == null) return table;

		StbType relationType = StbType.NULL;
		String relationId = null;
		for (Field f: field.getDeclaringClass().getDeclaredFields()) {
			if (f.getAnnotation(Id.class) != null) {
				Column column = f.getAnnotation(Column.class);
				relationId = Column.methods.getColumnName(column, f);
				relationType = column.type() == StbType.NULL ?
						StbType.findType(f) : column.type();
				break;
			}
		}
		if (relationId == null)
			throw new RuntimeException(CANNOT_FIND_RELATED_ID + parentTableName);

		StbColumn column = new StbColumn(Relation.RELATED_KEY, relationType);
		tableBuilder.getRoot().addColumn(column.foreignKey(parentTableName, relationId));
		return table;
	}
}
