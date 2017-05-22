package net.henryco.sqlightning.reflect.annotations.ameta.io.stdprocessor;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.henryco.sqlightning.reflect.annotations.ameta.io.MetaLoaderExec;
import net.henryco.sqlightning.reflect.annotations.column.Array;
import net.henryco.sqlightning.utils.ReflectUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HenryCo on 14/05/17
 */

public class ArrayLoadProcessor implements MetaLoaderExec {

	private static final String INSTANCE_ERROR_THROW_MSG
			= "Can't crete new instance of Array: ";


	@Override
	public Object getValue(Object instance, Field instanceField,
						   SQLiteDatabase database, final String tableName,
						   final Cursor data) {

		String arrTabName = Array.methods.getArrayName(instanceField, tableName);
		Object id = methods.getIdObject(data, instance.getClass());
		Array arrayAnn = instanceField.getAnnotation(Array.class);
		if (id == null || arrayAnn == null) return null;

		String sql = "SELECT * FROM "+arrTabName+" WHERE "+Array.ID_COLUMN+" = ?;";
		Cursor arrayData = database.rawQuery(sql, new String[]{id.toString()});
		if (arrayData.getCount() == 0) return null;
		arrayData.moveToFirst();

		Class arrType = ReflectUtils.getComponentType(instanceField);
		List<Object> list = new ArrayList<>();

		int index = 0, iterations = 0;
		while (index != -1) {
			String indexName = Array.methods.getArrayIndexName(iterations);
			index = arrayData.getColumnIndex(indexName);
			Object element = methods.getTypedObject(arrType, index, arrayData);

			if (element == null) break;

			list.add(element);
			iterations++;
		}

		Object arrayInstance;
		if (instanceField.getType().isArray()) {
			arrayInstance = java.lang.reflect.Array.newInstance(arrType, iterations);
			for (int k = 0; k < list.size(); k++) java.lang.reflect.Array.set(arrayInstance, k, list.get(k));
		}
		else if (List.class.isAssignableFrom(instanceField.getType())) arrayInstance = list;
		else throw new RuntimeException(INSTANCE_ERROR_THROW_MSG+instanceField.getName());

		arrayData.close();
		return arrayInstance;
	}
}
