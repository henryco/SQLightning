package net.henryco.sqlightning.reflect.annotations.repository;

import net.henryco.sqlightning.reflect.annotations.ameta.table.MetaTable;
import net.henryco.sqlightning.reflect.annotations.ameta.table.stdprocessor.DropProcessor;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by HenryCo on 12/05/17.
 */
@Documented
@Retention(RUNTIME)
@Target({TYPE, FIELD})
@MetaTable(DropProcessor.class)
public @interface Drop {

}
