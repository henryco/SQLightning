package net.henryco.sqlightning.reflect.annotations.ameta.config.stdprocessor;

import net.henryco.sqlightning.reflect.annotations.ameta.config.MetaConfigurationExec;
import net.henryco.sqlightning.reflect.annotations.config.DBName;
import net.henryco.sqlightning.utils.ReflectUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by HenryCo on 12/05/17.
 */

public class DBNameProcessor implements MetaConfigurationExec {

	@Override
	public <T> T process(T configHolder, Annotation self) {

		for (Field field: configHolder.getClass().getDeclaredFields()) {

			DBName dbName = field.getAnnotation(DBName.class);
			if (dbName != null) try {

				String setterName = "set".concat(ReflectUtils.getMethodNameFromField(field));
				Method setter = configHolder.getClass().getDeclaredMethod(setterName, field.getType());
				setter.setAccessible(true);
				setter.invoke(configHolder, ((DBName) self).value());
				return configHolder;

			} catch (Exception ignored) {}
		}

		return configHolder;
	}

}
