package net.henryco.sqlightning.reflect.annotations.column;

import net.henryco.sqlightning.reflect.annotations.ameta.column.MetaColumn;
import net.henryco.sqlightning.reflect.annotations.ameta.column.MetaRelation;
import net.henryco.sqlightning.reflect.annotations.ameta.column.stdprocessor.ColumnArrayProcessor;
import net.henryco.sqlightning.reflect.annotations.ameta.column.stdprocessor.RelationArrayProcessor;
import net.henryco.sqlightning.reflect.annotations.ameta.io.MetaLoader;
import net.henryco.sqlightning.reflect.annotations.ameta.io.MetaSaver;
import net.henryco.sqlightning.reflect.annotations.ameta.io.MetaUpdater;
import net.henryco.sqlightning.reflect.annotations.ameta.io.stdprocessor.ArrayLoadProcessor;
import net.henryco.sqlightning.reflect.annotations.ameta.io.stdprocessor.ArraySaveProcessor;
import net.henryco.sqlightning.reflect.annotations.ameta.io.stdprocessor.ArrayUpdateProcessor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

/**
 * Created by HenryCo on 14/05/17.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@MetaRelation(RelationArrayProcessor.class)
@MetaColumn(ColumnArrayProcessor.class)
@MetaLoader(ArrayLoadProcessor.class)
@MetaSaver(ArraySaveProcessor.class)
@MetaUpdater(ArrayUpdateProcessor.class)
public @interface Array {

	int value() default 10;

	String NO_COLUMN_ANNOTATION_THROW_MSG
			= "Field annotated as @Array must be annotated as @Column too";
	String ARRAY_TABLE_SUFFIX = "_ARRAY_";
	String ARRAY_INDEX_SUFFIX = "e_";
	String ID_COLUMN = "array_id";

	final class methods {

		public static String getArrayName(Field field, String tableName) {
			Column column;
			if ((column = field.getAnnotation(Column.class)) == null)
				throw new RuntimeException(NO_COLUMN_ANNOTATION_THROW_MSG);
			String columnName = Column.methods.getColumnName(column, field);
			return tableName.concat(ARRAY_TABLE_SUFFIX).concat(columnName);
		}

		public static String getArrayIndexName(int index) {
			return ARRAY_INDEX_SUFFIX.concat(Integer.toString(index));
		}

	}
}
