package net.henryco.sqlightning.reflect.table.stb;

import net.henryco.sqlightning.reflect.annotations.ameta.table.MetaTable;
import net.henryco.sqlightning.reflect.annotations.repository.Entity;
import net.henryco.sqlightning.reflect.annotations.repository.Table;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;

/**
 * Created by HenryCo on 12/05/17.
 */

public abstract class STBBuilder {

	public static Class checkForEntity(Class tableClass) {
		Annotation entity = tableClass.getAnnotation(Entity.class);
		if (entity == null) throw new RuntimeException("Class must be annotated as @Entity");
		return tableClass;
	}

	public static Object instanceEntity(Class tableClass) {
		try {
			return tableClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("@Entity "+tableClass.getSimpleName() +" instancing FAIL");
	}




	public static SimpTableBuilder createSimpleTableBuilder(final Class tableClass) {
		return createSimpleTableBuilder(tableClass, null);
	}
	public static SimpTableBuilder createSimpleTableBuilder(final Class tableClass,
															Collection<String> nameList) {
		return createSimpleTableBuilder(instanceEntity(tableClass), nameList);
	}

	public static SimpTableBuilder createSimpleTableBuilder(final Field tableField) {
		return createSimpleTableBuilder(tableField, null);
	}

	public static SimpTableBuilder createSimpleTableBuilder(final Object tableInstance) {
		return createSimpleTableBuilder(tableInstance, null);
	}



	public static SimpTableBuilder createSimpleTableBuilder(final Field tableField,
															Collection<String> nameList) {

		checkForEntity(tableField.getType());
		String value = Table.methods.getTableName(tableField);

		if (nameList != null && nameList.contains(value)) return null;
		return MetaTable.method.processTableField(tableField, new SimpTableBuilder(value));
	}


	public static SimpTableBuilder createSimpleTableBuilder(final Object tableInstance,
															Collection<String> nameList) {
		Class tableClass = checkForEntity(tableInstance.getClass());
		String value = Table.methods.getTableName(tableInstance.getClass());
		if (nameList != null && nameList.contains(value)) return null;
		return MetaTable.method.processTableClass(tableClass, new SimpTableBuilder(value));
	}

	public static SimpTableBuilder createSimpleArrayBuilder(final Field tableField, final String tableName,
															Collection<String> nameList) {
		Annotation[] annotations = tableField.getAnnotations();
		if (nameList != null && nameList.contains(tableName)) return null;
		return MetaTable.method.processAnnotations(new SimpTableBuilder(tableName), annotations);
	}
}
