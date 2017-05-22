package net.henryco.sqlightning.reflect.annotations.repository;

import net.henryco.sqlightning.reflect.annotations.ameta.repository.MetaQuery;
import net.henryco.sqlightning.reflect.annotations.ameta.repository.stdprocessor.ImplementationProcessor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by HenryCo on 21/05/17.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@MetaQuery(ImplementationProcessor.class)
public @interface Implementation {

	Class<?> value();
}
