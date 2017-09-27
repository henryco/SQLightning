package net.henryco.sqlightning.reflect.annotations.ameta.io.stdprocessor;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import net.henryco.sqlightning.reflect.annotations.ameta.io.MetaSaverExec;
import net.henryco.sqlightning.reflect.annotations.column.Array;
import net.henryco.sqlightning.reflect.annotations.column.Id;
import net.henryco.sqlightning.utils.ActionTree;
import net.henryco.sqlightning.utils.ReflectUtils;

import java.lang.reflect.Field;

import static net.henryco.sqlightning.utils.ReflectUtils.ArrayAdapter;

/**
 * Created by HenryCo on 15/05/17
 */

public class ArraySaveProcessor implements MetaSaverExec {


	@Override
	public ActionTree setValue(
			final Object instance, final Field field, final SQLiteDatabase database,
			final String tableName) {

		Array array = field.getAnnotation(Array.class);
		if (array == null) throw new RuntimeException(NO_ARRAY_ANNOTATION_THROW_MSG);

		final ArrayAdapter adapter = new ArrayAdapter(field);
		ContentValues arrValues = new ContentValues();
		for (Field f: instance.getClass().getDeclaredFields()) {
			if (f.getAnnotation(Id.class) != null) {
				Object key = ReflectUtils.getFieldValue(instance, f);
				arrValues = methods.setTypedObject(arrValues, Array.ID_COLUMN, key, f.getType());
				break;
			}
		}

		final Object arrayInstance = ReflectUtils.getFieldValue(instance, field);
		if (arrayInstance == null) return null;

		final int arrLimit = Math.min(array.value(), adapter.size(arrayInstance));
		for (int i = 0; i < arrLimit; i++) {
			Object arrElement = adapter.get(arrayInstance, i);
			String elementIndexName = Array.methods.getArrayIndexName(i);
			arrValues = methods.setTypedObject(arrValues, elementIndexName, arrElement, arrElement.getClass());
		}

		final String arrTabName = Array.methods.getArrayName(field, tableName);

		final ContentValues val = arrValues;
		final ActionTree actionTree = new ActionTree();
		return actionTree.setAction(new Runnable() {
			@Override
			public void run() {
				database.insert(arrTabName, null, val);
			}
		});
	}




}
