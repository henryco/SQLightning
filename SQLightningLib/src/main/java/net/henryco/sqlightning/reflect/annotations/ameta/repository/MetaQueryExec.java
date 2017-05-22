package net.henryco.sqlightning.reflect.annotations.ameta.repository;

import android.database.sqlite.SQLiteOpenHelper;

import net.henryco.sqlightning.reflect.repository.query.Invokable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by HenryCo on 21/05/17.
 */

public interface MetaQueryExec {

	Invokable getInvokable(final Method method,
						   final Annotation metaHolder,
						   final SQLiteOpenHelper dbHelper);
}
