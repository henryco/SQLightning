package net.henryco.sqlightning.reflect.annotations.ameta.config;

import java.lang.annotation.Annotation;

/**
 * Created by HenryCo on 12/05/17.
 */

public interface MetaConfigurationExec {

	 <T> T process(final T configHolder, final Annotation self);
}
