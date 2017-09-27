package net.henryco.sqlightning.reflect;


import net.henryco.sqlightning.dbstable.example.ExampleRepo;
import net.henryco.sqlightning.reflect.repository.LightningRepository;
import net.henryco.sqlightning.utils.ReflectUtils;

import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by HenryCo on 12/05/17.
 */

public class ReflectTests {

	private static class anon {
		private Integer[] iArr1;
		private Object[][] oArr2;
		private int[] iArr3 = null;
		private String[][][] sArr4;

		private Collection<Byte> byteCollection;
		private List<String> stringList;
		private ArrayList<Short> shortArrayList;

	}


	@Test
	public void getterMethodNameTestTwo() throws NoSuchFieldException {

		class SomeClass {
			private int someFieldOne;
			private short SomeFieldTwo;
			private String other_field;
		}

		Field f1 = SomeClass.class.getDeclaredField("someFieldOne");
		Field f2 = SomeClass.class.getDeclaredField("SomeFieldTwo");
		Field f3 = SomeClass.class.getDeclaredField("other_field");

		String f1r = ReflectUtils.getGetterMethodNameFromField(f1);
		String f2r = ReflectUtils.getGetterMethodNameFromField(f2);
		String f3r = ReflectUtils.getGetterMethodNameFromField(f3);

		Assert.assertEquals("getSomeFieldOne", f1r);
		Assert.assertEquals("getSomeFieldTwo", f2r);
		Assert.assertEquals("getOther_field", f3r);
	}


	@Test
	public void interfaceMethodsTest() {

		Method[] methods1 = LightningRepository.class.getDeclaredMethods();
		Method[] methods2 = ExampleRepo.class.getDeclaredMethods();

		String s1 = Arrays.toString(methods1);
		String s2 = Arrays.toString(methods2);

		Assert.assertNotEquals(s1, s2);
	}


	@Test
	public void genericTypesTest() {

		class test implements LightningRepository<Integer, String>, Runnable {
			@Override public String getRecordById(Integer integer) {
				return null;
			}
			@Override public boolean saveRecord(String record) {
				return false;
			}
			@Override public boolean deleteRecordById(Integer integer) {
				return false;
			}
			@Override public boolean updateRecord(String record) {
				return false;
			}
			@Override public boolean isRecordExist(Integer integer) {
				return false;
			}

			@Override public void run() {}
		}

		LightningRepository<Integer, String> repoTest = new test();

		for (Class<?> aClass : repoTest.getClass().getInterfaces()) {
			System.out.println(aClass);
			if (aClass == LightningRepository.class) System.out.println("YEP");
		}

		Type[] genericInterfaces = repoTest.getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces) {
			System.out.println("Generic interface: "+genericInterface);
			if (genericInterface instanceof ParameterizedType) {
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();

				ParameterizedType type = (ParameterizedType) genericInterface;
				System.out.println("RAW TYPE: "+ type.getRawType());
				if (type.getRawType() == LightningRepository.class) System.out.println("YEAH");

				for (Type genericType : genericTypes) {

					System.out.println("Generic type: " + genericType);
					Class someClass = (Class) genericType;
					System.out.println(someClass);
				}
			}
		}
	}

	@Test
	public void arrayDimTest() throws NoSuchFieldException {

		Field f1 = anon.class.getDeclaredField("iArr1");
		Field f2 = anon.class.getDeclaredField("oArr2");
		Field f3 = anon.class.getDeclaredField("iArr3");
		Field f4 = anon.class.getDeclaredField("sArr4");

		Assert.assertEquals(1, ReflectUtils.getArrayDimension(f1));
		Assert.assertEquals(2, ReflectUtils.getArrayDimension(f2));
		Assert.assertEquals(1, ReflectUtils.getArrayDimension(f3));
		Assert.assertEquals(3, ReflectUtils.getArrayDimension(f4));
	}

	@Test
	public void arrayInstanceClassTest() throws NoSuchFieldException {

		Field array = anon.class.getDeclaredField("iArr1");
		System.out.println(array.getType().isArray());

		Field byteCollection = anon.class.getDeclaredField("byteCollection");
		System.out.println(List.class.isAssignableFrom(byteCollection.getType()));

		Field stringList = anon.class.getDeclaredField("stringList");
		System.out.println(List.class.isAssignableFrom(stringList.getType()));

		Field shortList = anon.class.getDeclaredField("shortArrayList");
		System.out.println(List.class.isAssignableFrom(shortList.getType()));

		Type genericType = shortList.getGenericType();
		if (genericType instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) genericType;
			for (Type type: pt.getActualTypeArguments()) {
				System.out.println(((Class) type).getSimpleName());
			}
		}

	}


	@Test
	public void paramTest() throws NoSuchMethodException {

		Class<ExampleRepo> repo = ExampleRepo.class;
		for (Method method : repo.getDeclaredMethods()) {

			for (Annotation[] annotations : method.getParameterAnnotations()) {
				System.out.println(Arrays.toString(annotations));
			}
		}

	}

	@Test
	public void fieldClass() throws Exception {

		class Some {
			String fieldo;
		}

		Field fieldo = Some.class.getDeclaredField("fieldo");
		System.out.println(fieldo.getType());
	}
}
