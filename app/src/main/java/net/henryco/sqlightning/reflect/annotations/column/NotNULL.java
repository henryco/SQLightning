package net.henryco.sqlightning.reflect.annotations.column;

import net.henryco.sqlightning.reflect.annotations.ameta.column.MetaColumn;
import net.henryco.sqlightning.reflect.annotations.ameta.column.stdprocessor.ColumnNotNULLProcessor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by HenryCo on 10/05/17.
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@MetaColumn(ColumnNotNULLProcessor.class)
public @interface NotNULL {

}
