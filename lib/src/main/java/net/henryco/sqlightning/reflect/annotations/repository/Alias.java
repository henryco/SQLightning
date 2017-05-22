package net.henryco.sqlightning.reflect.annotations.repository;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by HenryCo on 13/05/17.
 */
@Documented
@Retention(RUNTIME)
@Target(PARAMETER)
public @interface Alias {
	String value();
}
