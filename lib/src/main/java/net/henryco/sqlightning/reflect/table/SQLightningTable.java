package net.henryco.sqlightning.reflect.table;

import android.support.annotation.Nullable;

import net.henryco.sqlightning.reflect.annotations.ameta.column.MetaColumn;
import net.henryco.sqlightning.reflect.annotations.ameta.column.MetaRelation;
import net.henryco.sqlightning.reflect.annotations.ameta.column.MetaRelationExec;
import net.henryco.sqlightning.reflect.annotations.column.Column;
import net.henryco.sqlightning.reflect.annotations.repository.Table;
import net.henryco.sqlightning.reflect.table.stb.STBBuilder;
import net.henryco.sqlightning.reflect.table.stb.SimpTableBuilder;
import net.henryco.sqlightning.reflect.table.stb.StbColumn;
import net.henryco.sqlightning.reflect.table.stb.StbType;
import net.henryco.sqlightning.utils.ReflectUtils;
import net.henryco.sqlightning.utils.SQLightningDebugable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by HenryCo.
 */

public class SQLightningTable implements SQLightningDebugable {

	private static final String text_debug_tab_name = "\n\tTableName: ";
	private static final String DEBUG_GLOBAL_TAB_FOUND = "\n\n\t<GLOBAL TABLE FOUND: ";
	private static final String DEBUG_FOREIGN_DISABLED = "\n\t\tFOREIGN KEYS will be disabled/>\n";
	private static void debugFoundStaticTable(StringBuilder builder, Field tabField) {
		builder.append(DEBUG_GLOBAL_TAB_FOUND).append(Table.methods.getTableName(tabField))
				.append(DEBUG_FOREIGN_DISABLED);
	}
	private static void debugFoundStaticTable(StringBuilder builder, Class tableClass) {
		builder.append(DEBUG_GLOBAL_TAB_FOUND).append(Table.methods.getTableName(tableClass))
				.append(DEBUG_FOREIGN_DISABLED);
	}

	private final StringBuilder debugStatBuilder = new StringBuilder();
	private final Collection<String> nameList;
	private final Class tableClass;
	private final Object tableInstance;

	private SimpTableBuilder tableBuilder;

	public SQLightningTable(Class tableClass, Collection<String> nameList) {
		this(STBBuilder.instanceEntity(tableClass), nameList);
	}

	public SQLightningTable(Object tableInstance, Collection<String> nameList) {

		this.tableClass = STBBuilder.checkForEntity(tableInstance.getClass());
		this.nameList = loadTableNames(nameList);
		this.tableInstance = tableInstance;

		if ((tableBuilder = STBBuilder.createSimpleTableBuilder(tableInstance, nameList)) != null) {
			this.nameList.add(tableBuilder.getTableName());
			loadColumns();
		} else debugFoundStaticTable(debugStatBuilder, tableInstance.getClass());
	}

	public SQLightningTable(Field tableField, Collection<String> nameList) {

		this.tableInstance = STBBuilder.instanceEntity(tableField.getType());
		this.tableClass = STBBuilder.checkForEntity(tableField.getType());
		this.nameList = loadTableNames(nameList);

		if ((tableBuilder = STBBuilder.createSimpleTableBuilder(tableField, nameList)) != null) {
			this.nameList.add(tableBuilder.getTableName());
			loadColumns();
		} else debugFoundStaticTable(debugStatBuilder, tableField);
	}


	public SQLightningTable(Field arrayField, String arrName, Collection<String> nameList) {

		this.nameList = loadTableNames(nameList);
		this.tableClass = null;
		this.tableInstance = null;

		if ((tableBuilder = STBBuilder.createSimpleArrayBuilder(arrayField, arrName, nameList)) != null)
			this.nameList.add(tableBuilder.getTableName());
		else debugFoundStaticTable(debugStatBuilder, arrayField);
	}

	private static Collection<String> loadTableNames(Collection<String> nameList) {
		return nameList == null ? new ArrayList<String>() : nameList;
	}




	private void loadColumns() {

		debugStatBuilder.setLength(0);
		debugStatBuilder.append(text_debug_tab_name).append(tableBuilder.getTableName());

		List<SQLightningTable> selfList = new ArrayList<>();

		Field[] fields = tableClass.getDeclaredFields();
		for (Field field: fields) {

			Column columnAnnotation = field.getAnnotation(Column.class);
			if (columnAnnotation != null) {

				String columnName =
						columnAnnotation.name().isEmpty()
							? (columnAnnotation.value().isEmpty()
								? field.getName()
								: columnAnnotation.value())
							: columnAnnotation.name();

				StbType type =
						columnAnnotation.type() == StbType.NULL
							? StbType.findType(field)
							: columnAnnotation.type();

				StbColumn column = new StbColumn(columnName, type);
				for (Annotation annotation : field.getAnnotations()) {

					MetaColumn processor;
					if ((processor = annotation.annotationType()
							.getAnnotation(MetaColumn.class)) != null) {
						try {
							if (column != null)
								column = processor.value().newInstance().process(column, annotation, field);
						} catch (Exception e) {e.printStackTrace();}
					}
				}

				if (column != null) {
					try {
						String getterName = ReflectUtils.getGetterMethodNameFromField(field);
						Method getter = field.getDeclaringClass().getDeclaredMethod(getterName);
						column.defaultValue(getter.invoke(tableInstance));

					} catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
						try {

							field.setAccessible(true);
							Object val = field.get(tableInstance);
							if (val != null) column.defaultValue(val);

						} catch (Exception ex) {ex.printStackTrace();}
					}
				}

				for (Annotation annotation: field.getAnnotations()) {
					MetaRelation metaRelation = annotation
							.annotationType().getAnnotation(MetaRelation.class);
					if (metaRelation != null) {
						try {

							MetaRelationExec exec = metaRelation.value().newInstance();
							SQLightningTable inner = exec.createRelatedTable(tableBuilder.getTableName(), field, nameList);
							if (inner.getTableBuilder() != null) selfList.add(inner);
							else debugStatBuilder.append(inner.getDebugStatBuilder());

						} catch (Exception e) {e.printStackTrace();}
					}
				}


				if (column != null) setTableBuilder(tableBuilder.addColumn(column));
			}
		}


		for (SQLightningTable table: selfList) {
			if (table.getTableBuilder() != null) {
				setTableBuilder(tableBuilder.addTable(table.getTableBuilder()));
				debugStatBuilder.append(table.getDebugStatBuilder().toString());
			}
		}

	}

	private SQLightningTable setTableBuilder(SimpTableBuilder builder) {
		this.tableBuilder = builder;
		return this;
	}

	@Nullable
	public SimpTableBuilder getTableBuilder() {
		return tableBuilder;
	}


	@Override
	public StringBuilder getDebugStatBuilder() {
		return debugStatBuilder;
	}

	@Override
	public String getDebugString() {
		return debugStatBuilder.toString();
	}
}
