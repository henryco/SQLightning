package net.henryco.sqlightning.reflect.annotations.config;

import net.henryco.sqlightning.reflect.annotations.ameta.config.MetaConfiguration;
import net.henryco.sqlightning.reflect.annotations.ameta.config.stdprocessor.VersionProcessor;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by HenryCo on 11/05/17.
 */
@Documented
@Retention(RUNTIME)
@Target({TYPE, METHOD, FIELD})
@MetaConfiguration(VersionProcessor.class)
public @interface DBVersion {
	int value() default -1;
}
