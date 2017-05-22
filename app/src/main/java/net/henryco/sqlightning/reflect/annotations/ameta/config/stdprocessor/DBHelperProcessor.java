package net.henryco.sqlightning.reflect.annotations.ameta.config.stdprocessor;

import net.henryco.sqlightning.reflect.annotations.ameta.config.MetaConfigurationExec;
import net.henryco.sqlightning.reflect.annotations.config.DBHelper;
import net.henryco.sqlightning.utils.ReflectUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Created by HenryCo on 21/05/17.
 */

public class DBHelperProcessor implements MetaConfigurationExec {

	@Override
	public <T> T process(T configHolder, Annotation self) {

		for (Field field : configHolder.getClass().getDeclaredFields()) {

			DBHelper helper = field.getAnnotation(DBHelper.class);
			if (helper != null) try {
				ReflectUtils.setFieldValue(configHolder, ((DBHelper) self).value().newInstance(), field);
				return configHolder;
			} catch (Exception ignore) {}
		}
		return configHolder;
	}
}
