package net.henryco.sqlightning.reflect.annotations.ameta.column.stdprocessor;

import net.henryco.sqlightning.reflect.annotations.ameta.column.MetaRelationExec;
import net.henryco.sqlightning.reflect.annotations.column.Array;
import net.henryco.sqlightning.reflect.annotations.column.Column;
import net.henryco.sqlightning.reflect.annotations.column.Id;
import net.henryco.sqlightning.reflect.table.SQLightningTable;
import net.henryco.sqlightning.reflect.table.stb.SimpTableBuilder;
import net.henryco.sqlightning.reflect.table.stb.StbColumn;
import net.henryco.sqlightning.reflect.table.stb.StbType;
import net.henryco.sqlightning.utils.ReflectUtils;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * Created by HenryCo on 16/05/17
 */

public class RelationArrayProcessor implements MetaRelationExec {

	private static final String TYPE_IS_NOT_ARRAY_THROW_MSG
			= "Type of filed annotated as @Array in Entity must be Array type!!!";
	private static final String TABLE_BUILDER_IN_NULL
			= SimpTableBuilder.class.getSimpleName() + "is NULL";
	private static final String CANNOT_FIND_RELATED_ID
			= "Can't find related id for array table: ";
	private static final String NO_COLUMN_THROW_MSG
			= "Field annotated as @Array must be annotated as @Column too";


	@Override
	public SQLightningTable createRelatedTable(
			final String parentTableName,
			final Field field, final Collection<String> names) {

		String arrTabName = Array.methods.getArrayName(field, parentTableName);
		SQLightningTable table = new SQLightningTable(field, arrTabName, names);

		int limit = field.getAnnotation(Array.class).value();
		final Class arrComponentClass = ReflectUtils.getComponentType(field);
		SimpTableBuilder builder = table.getTableBuilder();

		if (arrComponentClass == null) throw new RuntimeException(TYPE_IS_NOT_ARRAY_THROW_MSG);
		if (builder == null) throw new RuntimeException(TABLE_BUILDER_IN_NULL);

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

		if (relationId == null) throw new RuntimeException(CANNOT_FIND_RELATED_ID + arrTabName);
		StbColumn column = new StbColumn(Array.ID_COLUMN, relationType)
				.primaryKey().foreignKey(parentTableName, relationId);
		builder.addColumn(column);

		Column indexColumn = field.getAnnotation(Column.class);
		if (indexColumn == null) throw new RuntimeException(NO_COLUMN_THROW_MSG);

		final StbType indexType = indexColumn.type() == StbType.NULL
				? StbType.findType(arrComponentClass) : indexColumn.type();
		for (int i = 0; i < limit; i++) {
			String cIndex = Array.methods.getArrayIndexName(i);
			builder.addColumn(new StbColumn(cIndex, indexType));
		}

		return table;
	}
}
