package net.henryco.sqlightning.reflect.annotations.ameta.io.stdprocessor;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import net.henryco.sqlightning.reflect.annotations.ameta.io.MetaUpdaterExec;
import net.henryco.sqlightning.reflect.annotations.column.Array;
import net.henryco.sqlightning.utils.ActionTree;
import net.henryco.sqlightning.utils.ReflectUtils;

import java.lang.reflect.Field;

import static net.henryco.sqlightning.utils.ReflectUtils.ArrayAdapter;

/**
 * Created by HenryCo on 20/05/17
 */

public class ArrayUpdateProcessor implements MetaUpdaterExec {

	@Override
	public ActionTree setValue(Object instance, Field field,
							   final SQLiteDatabase database, String tableName) {

		Array array = field.getAnnotation(Array.class);
		if (array == null) throw new RuntimeException(NO_ARRAY_ANNOTATION_THROW_MSG);

		final ArrayAdapter adapter = new ArrayAdapter(field);
		ContentValues contentValues = new ContentValues();

		final Object key = methods.getRelatedKey(instance);
		if (key == null) throw new RuntimeException(NO_RELATED_FIELD_THROW_MSG);

		final Object arrayInstance = ReflectUtils.getFieldValue(instance, field);
		if (arrayInstance == null) return null;

		final int arrLimit = Math.min(array.value(), adapter.size(arrayInstance));
		for (int i = 0; i < arrLimit; i++) {
			Object arrElement = adapter.get(arrayInstance, i);
			String elementIndexName = Array.methods.getArrayIndexName(i);
			contentValues = methods.setTypedObject(contentValues, elementIndexName, arrElement, arrElement.getClass());
		}

		final String arrTabName = Array.methods.getArrayName(field, tableName);
		final String where = Array.ID_COLUMN + " = ?";
		final String[] args = {key.toString()};
		final ActionTree actionTree = new ActionTree();
		final ContentValues values = contentValues;
		return actionTree.setAction(new Runnable() {
			@Override
			public void run() {
				database.update(arrTabName, values, where, args);
			}
		});
	}
}
