package net.henryco.sqlightning.reflect.annotations.ameta.config.stdprocessor;

import net.henryco.sqlightning.reflect.annotations.ameta.config.MetaConfigurationExec;
import net.henryco.sqlightning.reflect.annotations.config.DBVersion;
import net.henryco.sqlightning.utils.ReflectUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by HenryCo on 12/05/17.
 */

public class VersionProcessor implements MetaConfigurationExec {

	@Override
	public <T> T process(T configHolder, Annotation self) {

		for (Field field: configHolder.getClass().getDeclaredFields()) {

			DBVersion vAn = field.getAnnotation(DBVersion.class);
			if (vAn != null) try {

				String setterName = "set".concat(ReflectUtils.getMethodNameFromField(field));
				Method setVersionMethod = configHolder.getClass().getDeclaredMethod(setterName, field.getType());
				setVersionMethod.setAccessible(true);
				setVersionMethod.invoke(configHolder, ((DBVersion) self).value());
				return configHolder;

			} catch (Exception ignored) {}
		}

		return configHolder;
	}

}
