package net.henryco.sqlightning.reflect.annotations.config;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.henryco.sqlightning.reflect.annotations.ameta.config.MetaConfiguration;
import net.henryco.sqlightning.reflect.annotations.ameta.config.stdprocessor.DBHelperProcessor;
import net.henryco.sqlightning.reflect.database.DBExtraHelper;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by HenryCo on 21/05/17.
 */
@Documented
@Retention(RUNTIME)
@Target({TYPE, METHOD, FIELD})
@MetaConfiguration(DBHelperProcessor.class)
public @interface DBHelper {

	Class<? extends DBExtraHelper> value() default ProxyHelper.class;

	final class methods {

		public static DBExtraHelper getDefault(Object instance) {
			return getDefault(instance.getClass());
		}
		public static DBExtraHelper getDefault(Class instanceClass) {

			for (Field field: instanceClass.getDeclaredFields()) {
				DBHelper helper = field.getAnnotation(DBHelper.class);
				if (helper != null) {
					try {
						return helper.value().newInstance();
					} catch (InstantiationException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
			return null;
		}
	}
}

final class ProxyHelper implements DBExtraHelper {
	@Override public void onConfigure(SQLiteDatabase db, SQLiteOpenHelper helper) {}
	@Override public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion, SQLiteOpenHelper helper) {}
	@Override public void onOpen(SQLiteDatabase db, SQLiteOpenHelper helper) {}
	@Override public void onCreate(SQLiteDatabase db, SQLiteOpenHelper helper) {}
	@Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion, SQLiteOpenHelper helper) {}
}