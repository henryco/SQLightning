package net.henryco.sqlightning;

import android.content.Context;

import net.henryco.sqlightning.reflect.annotations.config.Configuration;
import net.henryco.sqlightning.reflect.database.DBDataBase;
import net.henryco.sqlightning.reflect.database.DBSConfigurationFinder;
import net.henryco.sqlightning.reflect.database.DataBaseBuilder;
import net.henryco.sqlightning.reflect.repository.RepositoryBuilder;
import net.henryco.sqlightning.reflect.table.SQLightningTableBuilder;

import java.lang.annotation.Annotation;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by HenryCo on 11/05/17.
 */

public class SQLightning {

	public static final String RUN_MORE_THEN_ONE_TIME_MSG =
			"Cannot call method: " + SQLightning.class.getSimpleName() + ".run(), more then one time!";

	private static final SQLightning ourInstance = new SQLightning();
	private static SQLightning getInstance() {
		return ourInstance;
	}
	private SQLightning() {
		firstRun = new AtomicBoolean(true);
		debug = new AtomicBoolean(false);
	}

	private static AtomicBoolean firstRun;
	private static AtomicBoolean debug;
	private static DataBaseBuilder dataBaseBuilder;




	public static void setDebugEnable(boolean debugEnable) {
		debug.set(debugEnable);
	}

	private static Class[] findExtraConfig(Class root, Class ... configs) {
		Annotation confAnn = root.getAnnotation(Configuration.class);
		if (confAnn == null) return configs;
		Class[] extra = new Class[configs.length + 1];
		System.arraycopy(configs, 0, extra, 0, configs.length);
		extra[extra.length - 1] = root;
		return extra;
	}

	private static String debug(String debugMsg) {
		if (debug.get()) try {
			System.out.println(debugMsg);
		} catch (Exception ignored) {}
		return debugMsg.trim();
	}



	public static DBDataBase getDataBaseHelper(Context context) {
		return dataBaseBuilder.getCreatedDataBase(context);
	}

	public static RepositoryBuilder getRepositoryBuilder(Context context) {
		return new RepositoryBuilder(getDataBaseHelper(context));
	}


	public static String run(Context context, Object rootObjInstance, Class ... configurations) {
		return run(context, rootObjInstance.getClass(), configurations);
	}

	public static String run(Context context, Class rootClass, Class ... configurations) {
		return run(context, rootClass.getPackage(), findExtraConfig(rootClass, configurations));
	}

	public static String run(Context context, Package rootPackage, Class ... configurations) {

		if (firstRun.get()) {
			firstRun.set(false);
			return forceRun(context, rootPackage, configurations);
		}
		return RUN_MORE_THEN_ONE_TIME_MSG;
	}




	/**
	 * For tests only!
	 */
	public static String forceRun(Context context, Object rootObjInstance, Class ... configurations) {
		return forceRun(context, rootObjInstance.getClass(), configurations);
	}

	/**
	 * For tests only!
	 */
	public static String forceRun(Context context, Class rootClass, Class ... configurations) {
		return forceRun(context, rootClass.getPackage(), findExtraConfig(rootClass, configurations));
	}

	/**
	 * For tests only!
	 */
	public static synchronized String forceRun(Context context, Package rootPackage, Class ... configurations) {
		DBSConfigurationFinder configurator = new DBSConfigurationFinder(rootPackage, configurations);
		configurator.createConfiguration();

		SQLightningTableBuilder tableBuilder = new SQLightningTableBuilder(configurator);
		tableBuilder.create();

		dataBaseBuilder = new DataBaseBuilder();
		dataBaseBuilder.addConfigurator(configurator);
		dataBaseBuilder.setCreateTableText(tableBuilder.toString());
		dataBaseBuilder.setDropTableText(tableBuilder.getDroppedTables());
		dataBaseBuilder.create(context);


		debug(dataBaseBuilder.getDebugString().trim() + "\n\n"
				+ tableBuilder.getDebugString().trim()+"\n\n\n");
		return debug(tableBuilder.getDroppedTables() + "\n\n" +tableBuilder.toString() + "\n\n");
	}


}
