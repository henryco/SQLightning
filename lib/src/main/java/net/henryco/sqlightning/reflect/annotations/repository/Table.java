package net.henryco.sqlightning.reflect.annotations.repository;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <br>
 * <b>[Field Table(name)] > [Class Table(name)] > [Class.simpleName()]</b>
 * <br><br>
 * Created by HenryCo.
 */
@Documented
@Retention(RUNTIME)
@Target({TYPE, FIELD})
public @interface Table {

	String value() default "";

	final class methods {

		public static String getTableName(final Field tableField) {

			String value;
			Table tabAnnotation = tableField.getAnnotation(Table.class);
			if (tabAnnotation == null || (value = tabAnnotation.value()).isEmpty()) {

				Class tabClass = tableField.getType();
				Annotation classAnnotation = tabClass.getAnnotation(Table.class);
				if (classAnnotation == null ||
						(value = tableField.getType().getAnnotation(Table.class).value()).isEmpty())
					value = tabClass.getSimpleName();
			}
			return value;
		}

		public static String getTableName(final Class tableClass) {

			String value;
			Table annotation = (Table) tableClass.getAnnotation(Table.class);
			if(annotation == null || (value = annotation.value()).isEmpty())
				value = tableClass.getSimpleName();
			return value;
		}

	}

}
