package net.henryco.sqlightning.reflect.annotations.ameta.repository.stdprocessor;

import android.database.sqlite.SQLiteOpenHelper;

import net.henryco.sqlightning.reflect.annotations.ameta.repository.MetaQueryExec;
import net.henryco.sqlightning.reflect.annotations.repository.Implementation;
import net.henryco.sqlightning.reflect.repository.query.Invokable;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by HenryCo on 22/05/17
 */

public class ImplementationProcessor implements MetaQueryExec {

	@Override
	public Invokable getInvokable(Method method,
								  Annotation metaHolder,
								  final SQLiteOpenHelper dbHelper) {

		final Implementation implementation = (Implementation) metaHolder;
		String methodName = method.getName();
		Class[] methodArgs = method.getParameterTypes();

		try {
			final Method impMethod = implementation.value().getDeclaredMethod(methodName, methodArgs);
			return new Invokable() {
				@Override
				public Object invoke(Object instance, Object... args) throws InvocationTargetException, IllegalAccessException {
					try {
						return impMethod.invoke(implementation.value().newInstance(), args);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}
			};
		} catch (Exception e) {e.printStackTrace();}
		return null;
	}
}
