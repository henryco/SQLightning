package net.henryco.sqlightning.reflect.annotations.column;

import net.henryco.sqlightning.reflect.annotations.ameta.column.MetaColumn;
import net.henryco.sqlightning.reflect.annotations.ameta.column.stdprocessor.ColumnIdProcessor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

/**
 * Created by HenryCo on 10/05/17.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@MetaColumn(ColumnIdProcessor.class)
public @interface Id {

	final class method {
		public static String findIdColumn(Class entity) {
			for (Field field: entity.getDeclaredFields()) {
				Column column = field.getAnnotation(Column.class);
				if (column != null && field.getAnnotation(Id.class) != null)
					return Column.methods.getColumnName(column, field);
			}	return null;
		}
	}
}
