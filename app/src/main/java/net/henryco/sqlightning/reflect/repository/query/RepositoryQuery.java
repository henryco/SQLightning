package net.henryco.sqlightning.reflect.repository.query;

import android.database.sqlite.SQLiteOpenHelper;

import net.henryco.sqlightning.reflect.annotations.ameta.repository.MetaQuery;
import net.henryco.sqlightning.reflect.annotations.ameta.repository.MetaQueryExec;
import net.henryco.sqlightning.reflect.repository.LightningRepository;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by HenryCo on 21/05/17.
 */

public class RepositoryQuery {

	private static final String NOT_AN_INTERFACE_THROW_MSG
			= "Repository class must be Interface";

	private final HashMap<String, Invokable> invokableHashMap;

	private final Class<? extends LightningRepository> repositoryClass;
	private final SQLiteOpenHelper dbHelper;

	public RepositoryQuery(final Class<? extends LightningRepository> repoClass,
						   final SQLiteOpenHelper dbHelper) {
		this.invokableHashMap = new HashMap<>();
		this.repositoryClass = repoClass;
		this.dbHelper = dbHelper;
		init();
	}

	private void init() {

		if (!repositoryClass.isInterface())
			throw new RuntimeException(NOT_AN_INTERFACE_THROW_MSG);
		for (Method method : repositoryClass.getDeclaredMethods()) {

			final String methodName = method.getName();

			for (Annotation an : method.getAnnotations()) {
				MetaQuery metaQuery = an.annotationType().getAnnotation(MetaQuery.class);
				if (metaQuery != null) {
					try {
						MetaQueryExec exec = metaQuery.value().newInstance();
						Invokable invokable = exec.getInvokable(method, an, dbHelper);
						invokableHashMap.put(methodName, invokable);
					} catch (InstantiationException | IllegalAccessException e) {e.printStackTrace();}
					break;
				}
			}
		}

	}

	public Object invoke(String methodName, Object instance, Object ... args) {

		try {
			Invokable invokable = invokableHashMap.get(methodName);
			return invokable.invoke(instance, args);
		} catch (InvocationTargetException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

}
