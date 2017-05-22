package net.henryco.sqlightning.reflect.annotations.ameta.repository.stdprocessor;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.henryco.sqlightning.reflect.annotations.ameta.repository.MetaQueryExec;
import net.henryco.sqlightning.reflect.annotations.repository.Alias;
import net.henryco.sqlightning.reflect.annotations.repository.Query;
import net.henryco.sqlightning.reflect.annotations.repository.Read;
import net.henryco.sqlightning.reflect.annotations.repository.Write;
import net.henryco.sqlightning.reflect.repository.query.Invokable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by HenryCo on 22/05/17
 */

public class QueryProcessor implements MetaQueryExec {

	@Override
	public Invokable getInvokable(final Method method, final Annotation metaHolder,
								  final SQLiteOpenHelper dbHelper) {

		Query query = (Query) metaHolder;

		Read read = method.getAnnotation(Read.class);
		Write write = method.getAnnotation(Write.class);

		return (instance, args) -> {

			final SQLiteDatabase database;
			if (read != null && write == null) database = dbHelper.getReadableDatabase();
			else if (read == null && write != null) database = dbHelper.getWritableDatabase();
			else if (query.value().startsWith("SELECT")) database = dbHelper.getReadableDatabase();
			else if (query.value().startsWith("INSERT") || query.value().startsWith("UPDATE")
					|| query.value().startsWith("DELETE")) database = dbHelper.getWritableDatabase();
			else return null;

			int i = 0;
			String qrt = query.value();
			for (Annotation[] annotations: method.getParameterAnnotations()) {
				for (Annotation annotation: annotations) {
					if (annotation.annotationType() == Alias.class) {
						Alias alias = (Alias) annotation;
						qrt = qrt.replace("{"+alias.value()+"}", args[i].toString());
						break;
					}
				}
				i++;
			}
			@SuppressLint("Recycle")
			Cursor cursor = database.rawQuery(qrt, null);
			return cursor;
		};
	}
}
