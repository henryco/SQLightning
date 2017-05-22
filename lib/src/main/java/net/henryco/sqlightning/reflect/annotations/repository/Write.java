package net.henryco.sqlightning.reflect.annotations.repository;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by HenryCo on 22/05/17.
 */
@Documented
@Retention(RUNTIME)
@Target(ElementType.METHOD)
public @interface Write {
}
