package net.henryco.sqlightning.reflect.repository.query;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by HenryCo on 22/05/17.
 */

public interface Invokable {
	Object invoke(Object instance, Object... args) throws InvocationTargetException, IllegalAccessException;
}
