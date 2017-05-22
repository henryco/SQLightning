package net.henryco.sqlightning.dbstable;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.henryco.sqlightning.BuildConfig;
import net.henryco.sqlightning.SQLightningTestActivity;
import net.henryco.sqlightning.dbstable.entity1.ConfigurationOne;
import net.henryco.sqlightning.dbstable.entity1.EntityOne;
import net.henryco.sqlightning.dbstable.entity2.ConfigurationTwo;
import net.henryco.sqlightning.dbstable.entity2.EntityTwo;
import net.henryco.sqlightning.dbstable.entity2.EntityTwoRepo;
import net.henryco.sqlightning.dbstable.entity3.ConfigurationThree;
import net.henryco.sqlightning.dbstable.entity3.Entity3;
import net.henryco.sqlightning.dbstable.entity3.Entity31;
import net.henryco.sqlightning.dbstable.entity3.EntityThreeRepo;
import net.henryco.sqlightning.dbstable.entity4.ConfigurationFour;
import net.henryco.sqlightning.dbstable.entity4.Entity4;
import net.henryco.sqlightning.dbstable.entity4.Repo4;
import net.henryco.sqlightning.dbstable.entity5.Config5;
import net.henryco.sqlightning.dbstable.entity5.Entity5;
import net.henryco.sqlightning.dbstable.entity5.Entity5Inner;
import net.henryco.sqlightning.dbstable.entity5.InnerRepository;
import net.henryco.sqlightning.dbstable.entity5.Repository5;
import net.henryco.sqlightning.SQLightning;
import net.henryco.sqlightning.reflect.annotations.ameta.entity.stdprocessor.EntityLoadProcessor;
import net.henryco.sqlightning.reflect.repository.LightningRepository;
import net.henryco.sqlightning.reflect.repository.RepositoryBuilder;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by HenryCo on 15/05/17
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, manifest = "app/src/main/AndroidManifest.xml")
public class EntityLoaderTests {


	private Activity activity;

	@Before
	public void setUp() throws Exception {

		activity = Robolectric.buildActivity(SQLightningTestActivity.class)
				.create()
				.resume()
				.get();
	}

	@Test
	public void activityNotNullTest() {
		Assert.assertNotNull(activity);
	}

	private Context getAppContext() {
		return activity;
	}

	@Test
	public void testCreateDataBaseOne() {
		SQLightning.setDebugEnable(false);
		String result = SQLightning.run(getAppContext(), ConfigurationOne.class);
		String result2 = SQLightning.run(getAppContext(), ConfigurationOne.class);
		Assert.assertNotEquals(SQLightning.RUN_MORE_THEN_ONE_TIME_MSG, result);
		Assert.assertEquals(SQLightning.RUN_MORE_THEN_ONE_TIME_MSG, result2);
	}

	@Test
	public void testEntityLoaderOne() throws Exception {

		Context context = getAppContext();

		final String tableName = "table_one";
		final String idColumnName = "id";
		final String textColumnName = "some_text_column";

		final long id1 = 36571;
		final long id2 = 464123;

		SQLightning.setDebugEnable(false);
		SQLightning.forceRun(context, ConfigurationOne.class);
		SQLiteDatabase writableDatabase = SQLightning.getDataBaseHelper(context).getWritableDatabase();

		ContentValues values1 = new ContentValues();
		values1.put(idColumnName, id1);
		values1.put(textColumnName, "otherText");

		ContentValues values2 = new ContentValues();
		values2.put(idColumnName, id2);

		long l1 = writableDatabase.insert(tableName, null, values1);
		long l2 = writableDatabase.insert(tableName, null, values2);
		writableDatabase.close();

		Assert.assertEquals(l1, id1);
		Assert.assertEquals(l2, id2);

		EntityLoadProcessor processor = EntityLoadProcessor.class.newInstance();

		EntityOne entityOne = (EntityOne) processor.loadEntity(EntityOne.class,
				SQLightning.getDataBaseHelper(context).getReadableDatabase(),
				tableName, new String[]{idColumnName, Long.toString(id1)}
		);
		EntityOne entityTwo = (EntityOne) processor.loadEntity(EntityOne.class,
				SQLightning.getDataBaseHelper(context).getReadableDatabase(),
				tableName, new String[]{idColumnName, Long.toString(id2)});

		Assert.assertEquals("EntityOne{id=36571, someText='otherText'}", entityOne.toString());
		Assert.assertEquals("EntityOne{id=464123, someText='defaultText'}", entityTwo.toString());
	}


	@Test
	public void entityLoaderTestTwo() throws Exception {

		Context context = getAppContext();
		SQLightning.setDebugEnable(false);
		SQLightning.forceRun(context, ConfigurationTwo.class);
		SQLiteDatabase writableDatabase = SQLightning.getDataBaseHelper(context).getWritableDatabase();

		final long id = 21456;
		final String textOne = "Hello world";
		final int intVal = 42;
		final float floatVal = 71.002f;
		final short shortVal = 1234;
		final double doubleVal = 4.56d;
		final Byte byteVal = (byte) 34;
		final char charVal = 'v';

		ContentValues values = new ContentValues();
		values.put("id", id);
		values.put("text_one", textOne);
		values.put("int_value_one", intVal);
		values.put("float_value", floatVal);
		values.put("short_value", shortVal);
		values.put("double_value", doubleVal);
		values.put("byte_value", byteVal);
		values.put("boolean_value", true);
		values.put("char_value", String.valueOf(charVal));

		long rid = writableDatabase.insert("example_table_two", null, values);
		Assert.assertEquals(21456, rid);

		EntityTwo entity = EntityTwo.class.newInstance();
		SQLiteDatabase readableDataBase = SQLightning.getDataBaseHelper(context).getReadableDatabase();
		Cursor data = readableDataBase.rawQuery("SELECT * FROM example_table_two WHERE id = 21456", null);
		data.moveToFirst();
		Object i = EntityLoadProcessor.loaderExec.getValue(entity,
				entity.getClass().getDeclaredField("ID"), readableDataBase, null, data);
		Object to = EntityLoadProcessor.loaderExec.getValue(entity,
				entity.getClass().getDeclaredField("smthText"), readableDataBase, null, data);
		Object iv = EntityLoadProcessor.loaderExec.getValue(entity,
				entity.getClass().getDeclaredField("intVal1"), readableDataBase, null, data);
		Object fv = EntityLoadProcessor.loaderExec.getValue(entity,
				entity.getClass().getDeclaredField("floatVal"), readableDataBase, null, data);
		Object sv = EntityLoadProcessor.loaderExec.getValue(entity,
				entity.getClass().getDeclaredField("shortVal"), readableDataBase, null, data);
		Object dv = EntityLoadProcessor.loaderExec.getValue(entity,
				entity.getClass().getDeclaredField("double_value"), readableDataBase, null, data);
		Object bv = EntityLoadProcessor.loaderExec.getValue(entity,
				entity.getClass().getDeclaredField("byte_value"), readableDataBase, null, data);
		Object bbv = EntityLoadProcessor.loaderExec.getValue(entity,
				entity.getClass().getDeclaredField("bool"), readableDataBase, null, data);
		Object cv = EntityLoadProcessor.loaderExec.getValue(entity,
				entity.getClass().getDeclaredField("charVal"), readableDataBase, null, data);
		data.close();

		Assert.assertEquals(id, i);
		Assert.assertEquals(textOne, to);
		Assert.assertEquals(intVal, iv);
		Assert.assertEquals(floatVal, fv);
		Assert.assertEquals(shortVal, sv);
		Assert.assertEquals(doubleVal, dv);
		Assert.assertEquals(byteVal, bv);
		Assert.assertEquals(true, bbv);
		Assert.assertEquals(charVal, cv);

		RepositoryBuilder repositoryBuilder = new RepositoryBuilder(SQLightning.getDataBaseHelper(context));
		EntityTwoRepo repo = repositoryBuilder.create(EntityTwoRepo.class);

		Assert.assertEquals(false, repo.isRecordExist(3466L));
		Assert.assertEquals(true, repo.isRecordExist(id));
	}


	@Test
	public void innerTablesTestOne() {

		Context context = getAppContext();

		SQLightning.setDebugEnable(false);
		SQLightning.forceRun(context, ConfigurationThree.class);

		Entity31 inner = new Entity31();
		inner.setUid(98621);
		inner.setEn31Text("pope");

		Entity3 entity3 = new Entity3();
		entity3.setIdColumn(2137);
		entity3.setTextCol("papai");
		entity3.setVal((short) 26);
		entity3.setOtherEntity(inner);

		RepositoryBuilder builder = new RepositoryBuilder(SQLightning.getDataBaseHelper(context));
		EntityThreeRepo repo = builder.create(EntityThreeRepo.class);

		repo.saveRecord(entity3);

		Entity3 result = repo.getRecordById(entity3.getIdColumn());

		Assert.assertEquals(entity3.toString(), result.toString());
		Assert.assertEquals(inner.toString(), result.getOtherEntity().toString());
	}

	@Test
	public void tableWithArrayTest() {
		Context context = getAppContext();

		SQLightning.setDebugEnable(false);
		String res = SQLightning.forceRun(context, ConfigurationFour.class);

		Entity4 entity4 = new Entity4();
		entity4.setId(2351);
		float[] arf = {1, 23, 4, 5, 6.05f, 7, 9, 3.2f};
		entity4.setArr(arf);
		entity4.intArray = new Short[]{2, 13, 46, 35, 6, 7};

		ArrayList<String> testList = new ArrayList<>();
		testList.add("one");
		testList.add("two");
		testList.add("three");
		testList.add("four");
		entity4.setTextList(testList);

		RepositoryBuilder builder = new RepositoryBuilder(SQLightning.getDataBaseHelper(context));
		Repo4 repo4 = builder.create(Repo4.class);
		boolean saved = repo4.saveRecord(entity4);
		Entity4 resultEntity = repo4.getRecordById(2351);

		Assert.assertEquals(true, saved);
		Assert.assertEquals("Entity4{id=2351, arr=[1.0, 23.0, 4.0, 5.0, 6.05, 7.0, 9.0, 3.2], " +
				"intArray=[2, 13, 46, 35, 6, 7], textList=[one, two, three, four]}", entity4.toString());
		Assert.assertEquals("Entity4{id=2351, arr=[1.0, 23.0, 4.0, 5.0, 6.05, 7.0, 9.0, 3.2], " +
				"intArray=[2, 13, 46, 35, 6], textList=[one, two, three]}", resultEntity.toString());
		Assert.assertEquals("DROP TABLE IF EXISTS arrayed_table;\n" +
				"DROP TABLE IF EXISTS arrayed_table_ARRAY_array;\n" +
				"DROP TABLE IF EXISTS arrayed_table_ARRAY_int_array;\n" +
				"DROP TABLE IF EXISTS arrayed_table_ARRAY_list;\n" +
				"\n" +
				"CREATE TABLE arrayed_table (\n" +
				"\tid INTEGER NOT NULL,\n" +
				"\tPRIMARY KEY (id)\n" +
				");\n" +
				"\n" +
				"CREATE TABLE arrayed_table_ARRAY_array (\n" +
				"\tarray_id INTEGER NOT NULL,\n" +
				"\te_0 REAL,\n" +
				"\te_1 REAL,\n" +
				"\te_2 REAL,\n" +
				"\te_3 REAL,\n" +
				"\te_4 REAL,\n" +
				"\te_5 REAL,\n" +
				"\te_6 REAL,\n" +
				"\te_7 REAL,\n" +
				"\te_8 REAL,\n" +
				"\te_9 REAL,\n" +
				"\tFOREIGN KEY (array_id) REFERENCES arrayed_table(id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
				"\tPRIMARY KEY (array_id)\n" +
				");\n" +
				"\n" +
				"CREATE TABLE arrayed_table_ARRAY_int_array (\n" +
				"\tarray_id INTEGER NOT NULL,\n" +
				"\te_0 INTEGER,\n" +
				"\te_1 INTEGER,\n" +
				"\te_2 INTEGER,\n" +
				"\te_3 INTEGER,\n" +
				"\te_4 INTEGER,\n" +
				"\tFOREIGN KEY (array_id) REFERENCES arrayed_table(id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
				"\tPRIMARY KEY (array_id)\n" +
				");\n" +
				"\n" +
				"CREATE TABLE arrayed_table_ARRAY_list (\n" +
				"\tarray_id INTEGER NOT NULL,\n" +
				"\te_0 TEXT,\n" +
				"\te_1 TEXT,\n" +
				"\te_2 TEXT,\n" +
				"\tFOREIGN KEY (array_id) REFERENCES arrayed_table(id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
				"\tPRIMARY KEY (array_id)\n" +
				");", res);

	}

	@Test
	public void emptyArraysTest() {

		SQLightning.setDebugEnable(false);
		Context context = getAppContext();
		SQLightning.forceRun(context, ConfigurationFour.class);

		RepositoryBuilder builder = new RepositoryBuilder(SQLightning.getDataBaseHelper(context));
		Repo4 repository = builder.create(Repo4.class);

		Entity4 entity = new Entity4();
		entity.setId(123);
		entity.setArr(new float[]{2.3f, 0.45f});
		entity.setTextList(new ArrayList<>(Arrays.asList("hello", "world", "!")));

		repository.saveRecord(entity);
		Entity4 result = repository.getRecordById(123);

		Assert.assertEquals("Entity4{id=123, arr=[2.3, 0.45], " +
				"intArray=null, textList=[hello, world, !]}", result.toString()
		);
	}


	@Test
	public void deleteTest() {

		SQLightning.setDebugEnable(false);
		Context context = getAppContext();
		String result = SQLightning.forceRun(context, Config5.class);

		RepositoryBuilder builder = new RepositoryBuilder(SQLightning.getDataBaseHelper(context));
		Repository5 repository = builder.create(Repository5.class);
		InnerRepository innerRepository = builder.create(InnerRepository.class);


		Entity5Inner inner = new Entity5Inner();
		inner.setInnerId("inner123");
		inner.setInnerInt(26);
		inner.setInnerArray(new Short[]{42, 24});

		Entity5 entity5 = new Entity5();
		entity5.setEntityId(230);
		entity5.setEntityText("Main text");
		entity5.setTextArray(new String[]{"one", "two"});
		entity5.setByteList(Arrays.asList(new Byte[]{112, 45, 12}));
		entity5.setInner(inner);

		boolean added = repository.saveRecord(entity5);
		Entity5 result1 = repository.getRecordById(230L);
		Entity5Inner result2 = innerRepository.getRecordById("inner123");
		boolean deleted = repository.deleteRecordById(230L);
		boolean isExist = repository.isRecordExist(230L);
		Entity5 result3 = repository.getRecordById(230L);
		Entity5Inner result4 = innerRepository.getRecordById("inner123");

		Assert.assertEquals(true, added);
		Assert.assertEquals(entity5.toString(), result1.toString());
		Assert.assertEquals(inner.toString(), result2.toString());
		Assert.assertEquals(true, deleted);
		Assert.assertEquals(false, isExist);
		Assert.assertNull(result3);
		Assert.assertNull(result4);
		Assert.assertEquals("DROP TABLE IF EXISTS table_entity_5;\n" +
				"DROP TABLE IF EXISTS table_entity_5_ARRAY_bytes;\n" +
				"DROP TABLE IF EXISTS inner_table;\n" +
				"DROP TABLE IF EXISTS inner_table_ARRAY_array;\n" +
				"DROP TABLE IF EXISTS table_entity_5_ARRAY_text_array;\n" +
				"\n" +
				"CREATE TABLE table_entity_5 (\n" +
				"\tid INTEGER NOT NULL,\n" +
				"\tent_text TEXT,\n" +
				"\tPRIMARY KEY (id)\n" +
				");\n" +
				"\n" +
				"CREATE TABLE table_entity_5_ARRAY_bytes (\n" +
				"\tarray_id INTEGER NOT NULL,\n" +
				"\te_0 INTEGER,\n" +
				"\te_1 INTEGER,\n" +
				"\te_2 INTEGER,\n" +
				"\tFOREIGN KEY (array_id) REFERENCES table_entity_5(id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
				"\tPRIMARY KEY (array_id)\n" +
				");\n" +
				"\n" +
				"CREATE TABLE inner_table (\n" +
				"\tinner_id TEXT NOT NULL,\n" +
				"\tinner_int INTEGER,\n" +
				"\trelated_key INTEGER NOT NULL,\n" +
				"\tFOREIGN KEY (related_key) REFERENCES table_entity_5(id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
				"\tPRIMARY KEY (inner_id)\n" +
				");\n" +
				"\n" +
				"CREATE TABLE inner_table_ARRAY_array (\n" +
				"\tarray_id TEXT NOT NULL,\n" +
				"\te_0 INTEGER,\n" +
				"\te_1 INTEGER,\n" +
				"\tFOREIGN KEY (array_id) REFERENCES inner_table(inner_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
				"\tPRIMARY KEY (array_id)\n" +
				");\n" +
				"\n" +
				"CREATE TABLE table_entity_5_ARRAY_text_array (\n" +
				"\tarray_id INTEGER NOT NULL,\n" +
				"\te_0 TEXT,\n" +
				"\te_1 TEXT,\n" +
				"\tFOREIGN KEY (array_id) REFERENCES table_entity_5(id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
				"\tPRIMARY KEY (array_id)\n" +
				");", result);
	}

	@Test
	public void entityUpdateTest() {

		final Context context = getAppContext();
		SQLightning.setDebugEnable(false);
		SQLightning.forceRun(context, Config5.class);

		RepositoryBuilder builder = new RepositoryBuilder(SQLightning.getDataBaseHelper(context));
		LightningRepository<Long, Entity5> repository = builder.create(LightningRepository.class, "table_entity_5", Long.class, Entity5.class);


		Entity5 entity = new Entity5();
		entity.setEntityId(34);
		entity.setEntityText("first text");
		entity.setByteList(Arrays.asList(new Byte[]{123, 45, 1}));
		entity.setTextArray(new String[]{"1", "a"});

		Entity5Inner inner = new Entity5Inner();
		inner.setInnerId("inn asc 23");
		inner.setInnerInt(234);
		inner.setInnerArray(new Short[]{21, 300});
		entity.setInner(inner);

		String entString1 = entity.toString();
		boolean added = repository.saveRecord(entity);
		String res = repository.getRecordById(34L).toString();

		entity.setEntityText("second text");
		entity.setTextArray(new String[]{"one"});
		entity.getInner().setInnerInt(9);
		entity.getInner().setInnerArray(new Short[]{666});
		String entString2 = entity.toString();

		boolean updated = repository.updateRecord(entity);
		String entString3 = repository.getRecordById(34L).toString();
		boolean overwritten = repository.saveRecord(entity);
		String entString4 = repository.getRecordById(34L).toString();
		boolean deleted = repository.deleteRecordById(34L);
		boolean exist = repository.isRecordExist(34L);

		Assert.assertTrue(added);
		Assert.assertTrue(updated);
		Assert.assertTrue(deleted);
		Assert.assertTrue(overwritten);
		Assert.assertFalse(exist);
		Assert.assertEquals("Entity5{entityId=34, entityText='first text', " +
				"byteList=[123, 45, 1], inner=Entity5Inner{innerId='inn asc 23', " +
				"innerArray=[21, 300], innerInt=234}, textArray=[1, a]}", entString1);
		Assert.assertEquals("Entity5{entityId=34, entityText='first text', " +
				"byteList=[123, 45, 1], inner=Entity5Inner{innerId='inn asc 23', " +
				"innerArray=[21, 300], innerInt=234}, textArray=[1, a]}", res);
		Assert.assertEquals("Entity5{entityId=34, entityText='second text', " +
				"byteList=[123, 45, 1], inner=Entity5Inner{innerId='inn asc 23', " +
				"innerArray=[666], innerInt=9}, textArray=[one]}", entString2);
		Assert.assertEquals("Entity5{entityId=34, entityText='second text', " +
				"byteList=[123, 45, 1], inner=Entity5Inner{innerId='inn asc 23', " +
				"innerArray=[666, 300], innerInt=9}, textArray=[one, a]}", entString3);
		Assert.assertEquals(entString2, entString4);
	}


	@Test
	public void queryTest() {

		final Context context = getAppContext();
		SQLightning.setDebugEnable(false);
		SQLightning.forceRun(context, Config5.class);
		RepositoryBuilder builder = SQLightning.getRepositoryBuilder(context);
		Repository5 repository = builder.create(Repository5.class);

		Entity5 entity5 = new Entity5();
		entity5.setEntityId(1235);
		entity5.setEntityText("some text");
		repository.saveRecord(entity5);

		Entity5 ret = repository.getRecordById(1235L);

		Cursor cursor = repository.getSomeValues(1235L);
		cursor.moveToFirst();

		long id1 = cursor.getLong(cursor.getColumnIndex("id"));
		String txt1 = cursor.getString(cursor.getColumnIndex("ent_text"));

		Object[] r = repository.readSomeValues(1235L, repository);
		long id2 = (long) r[0];
		String txt2 = (String) r[1];

		Assert.assertEquals(entity5.toString(), ret.toString());
		Assert.assertEquals(id1, entity5.getEntityId());
		Assert.assertEquals(id2, entity5.getEntityId());
		Assert.assertEquals(txt1, entity5.getEntityText());
		Assert.assertEquals(txt2, entity5.getEntityText());
	}
}
