package net.henryco.sqlightning.reflect.annotations.config;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by HenryCo on 11/05/17.
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface Main {

}
