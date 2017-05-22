package net.henryco.sqlightning.reflect.annotations.ameta.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by HenryCo on 12/05/17.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface MetaConfiguration {

	Class<? extends MetaConfigurationExec> value();
}
