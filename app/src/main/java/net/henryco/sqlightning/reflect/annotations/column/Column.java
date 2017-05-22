package net.henryco.sqlightning.reflect.annotations.column;

import net.henryco.sqlightning.reflect.table.stb.StbType;

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
public @interface Column {

	String value() default "";
	String name() default "";
	/**
	 *  if type is NULL (default value), then type will be automatically selected in runtime.
	 */ StbType type() default StbType.NULL;

	final class methods {

		public static String getColumnName(Column annotation, Field field) {
			return annotation.name().isEmpty()
					? (annotation.value().isEmpty()
						? field.getName()
						: annotation.value())
					: annotation.name();
		}
	}
}
