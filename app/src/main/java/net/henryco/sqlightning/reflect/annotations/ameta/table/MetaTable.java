package net.henryco.sqlightning.reflect.annotations.ameta.table;

import net.henryco.sqlightning.reflect.table.stb.SimpTableBuilder;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

/**
 * Created by HenryCo on 12/05/17.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface MetaTable {

	Class<? extends MetaTableExec> value();


	final class method {

		public static SimpTableBuilder processAnnotations(SimpTableBuilder builder, Annotation... annotations) {

			for (Annotation annotation: annotations) {

				MetaTable tableProcessor = annotation.annotationType().getAnnotation(MetaTable.class);
				if (tableProcessor != null) try {

					MetaTableExec tableExec = tableProcessor.value().newInstance();
					builder = tableExec.process(builder, annotation);
				} catch (Exception ignore){}
			}

			return builder;
		}

		public static SimpTableBuilder processTableClass(final Class tableClass, SimpTableBuilder builder) {
			return processAnnotations(builder, tableClass.getAnnotations());
		}

		public static SimpTableBuilder processTableField(final Field tableField, SimpTableBuilder builder) {
			return processAnnotations(processTableClass(tableField.getType(), builder), tableField.getAnnotations());
		}
	}
}
