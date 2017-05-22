package net.henryco.sqlightning.reflect.annotations.ameta.column.stdprocessor;

import net.henryco.sqlightning.reflect.annotations.ameta.column.MetaColumnExec;
import net.henryco.sqlightning.reflect.annotations.column.Column;
import net.henryco.sqlightning.reflect.annotations.column.Relation;
import net.henryco.sqlightning.reflect.annotations.repository.Table;
import net.henryco.sqlightning.reflect.table.stb.StbColumn;
import net.henryco.sqlightning.reflect.table.stb.StbType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Created by HenryCo on 10/05/17
 */

public class ColumnRelationProcessor implements MetaColumnExec {

	@Override
	public StbColumn process(StbColumn column, Annotation self, Field annotatedField) {
//		StbType type = getRelatedType(annotatedField, (Relation) self);
//		return column.field(column.getName(), type).notNull();
		return null; // NULL EXACTLY WHAT WE NEED
	}


	public static String[] getRelatedTableKey(Field annotatedField, Relation relation) {

		String tableName;
		Table tableAnnotation = annotatedField.getAnnotation(Table.class);
		if (tableAnnotation == null || (tableName = tableAnnotation.value()).isEmpty()) {

			Class tableClass = annotatedField.getType();
			Annotation classAnnotation = tableClass.getAnnotation(Table.class);
			if (classAnnotation == null || (tableName = ((Table) classAnnotation).value()).isEmpty())
				tableName = tableClass.getSimpleName();
		}
		return new String[]{tableName, Relation.RELATED_KEY};
	}

	public static StbType getRelatedType(final Field field, Relation relation) {

		Field[] fields = field.getType().getDeclaredFields();
		for (Field f: fields) {

			Column column;
			if ((column = f.getAnnotation(Column.class)) != null) {

				String columnName =
						column.name().isEmpty()
							? (column.value().isEmpty()
								? f.getName()
								: column.value())
							: column.name();
				if (columnName.equals(Relation.RELATED_KEY)) return StbType.findType(f);
			}
		}
		return StbType.findType(field);
	}
}
