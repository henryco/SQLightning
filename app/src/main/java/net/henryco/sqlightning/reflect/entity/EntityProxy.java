package net.henryco.sqlightning.reflect.entity;

import net.henryco.sqlightning.reflect.annotations.column.Column;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by HenryCo on 19/05/17.
 */

public class EntityProxy implements InvocationHandler {

	// TODO: 19/05/17  // FIXME: 19/05/17

	private final List<String> updateList;
	private final Class entityClass;
	private final String tableName;

	public EntityProxy(final Class entityClass, final String tableName) {
		this.updateList = Collections.synchronizedList(new LinkedList<>());
		this.entityClass = entityClass;
		this.tableName = tableName;
	}


	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {

		String methodName = method.getName();
		if (methodName.startsWith("set") || methodName.startsWith("get")) {

			String fieldName = methodName.substring(3, methodName.length());
			String firstSym = String.valueOf(methodName.charAt(0)).toLowerCase();
			fieldName = firstSym.concat(fieldName.substring(1));

			for (Field field: entityClass.getDeclaredFields()) {
				if (field.getName().equals(fieldName)) {

					Column column = field.getAnnotation(Column.class);
					if (column != null) updateList.add(Column.methods.getColumnName(column, field));
					break;
				}
			}
		}
		return method.invoke(proxy, args);
	}

	public List<String> getUpdateList() {
		return updateList;
	}

	public Class getEntityClass() {
		return entityClass;
	}

	public String getTableName() {
		return tableName;
	}
}
