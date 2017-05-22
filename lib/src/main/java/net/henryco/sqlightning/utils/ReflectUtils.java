package net.henryco.sqlightning.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by HenryCo on 12/05/17.
 */

public final class ReflectUtils {

	private interface ArrayGetter {
		ArrayGetter arrayGetter = new ArrayGetter() {
			@Override
			public Object get(Object array, int index) {
				return java.lang.reflect.Array.get(array, index);
			}
		};
		ArrayGetter listGetter = new ArrayGetter() {
			@Override
			public Object get(Object array, int index) {
				return ((List) array).get(index);
			}
		};
		Object get(Object array, int index);
	}
	private interface ArraySizer {
		ArraySizer arraySizer = new ArraySizer() {
			@Override
			public int size(Object array) {
				return java.lang.reflect.Array.getLength(array);
			}
		};
		ArraySizer listSizer = new ArraySizer() {
			@Override
			public int size(Object array) {
				return ((List) array).size();
			}
		};
		int size(Object array);
	}
	public static final class ArrayAdapter implements ArraySizer, ArrayGetter {

		private static final String WRONG_ARRAY_TYPE_THROW_MSG
				= "Field annotated as @Array must be Array or List type";
		private final ArrayGetter getter;
		private final ArraySizer sizer;

		public ArrayAdapter(Field arrayField) {
			this(arrayField.getType());
		}
		public ArrayAdapter(Object arrayObject) {
			this(arrayObject.getClass());
		}
		public ArrayAdapter(Class arrayObjectClass) {
			if (List.class.isAssignableFrom(arrayObjectClass)) {
				this.getter = listGetter;
				this.sizer = listSizer;
			} else if (arrayObjectClass.isArray()) {
				this.getter = arrayGetter;
				this.sizer = arraySizer;
			} else throw new RuntimeException(WRONG_ARRAY_TYPE_THROW_MSG);
		}

		@Override
		public Object get(Object array, int index) {
			return getter.get(array, index);
		}

		@Override
		public int size(Object array) {
			return sizer.size(array);
		}
	}





	private static final String GET = "get";
	private static final String IS = "is";



	public static String getMethodNameFromField(String field) {
		String firstSym = String.valueOf(field.charAt(0)).toUpperCase();
		return firstSym.concat(field.substring(1));
	}



	public static String getMethodNameFromField(Field field) {
		return getMethodNameFromField(field.getName());
	}



	public static String getGetterMethodNameFromField(Field field) {
		return ((field.getType() == boolean.class) ? IS : GET).concat(getMethodNameFromField(field));
	}



	public static int getArrayDimension(Field arrayField) {
		if (!arrayField.getType().isArray())
			throw new RuntimeException("Argument is not array class");
		return 1 + arrayField.getType().getName().lastIndexOf('[');
	}



	public static int getArrayDimension(Class arrayClass) {
		if (!arrayClass.isArray())
			throw new RuntimeException("Argument is not array class");
		return 1 + arrayClass.getName().lastIndexOf('[');
	}



	public static int getArrayDimension(Object array) {
		return getArrayDimension(array.getClass());
	}



	public static Object getFieldValue(Object instance, String fieldName) {
		try {
			return getFieldValue(instance, instance.getClass().getDeclaredField(fieldName));
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			return null;
		}
	}



	public static Object getFieldValue(Object instance, Field field) {

		try {
			String getterName = ReflectUtils.getGetterMethodNameFromField(field);
			return field.getDeclaringClass().getDeclaredMethod(getterName).invoke(instance);
		} catch (Exception e) {
			try {
				String getterName = "get".concat(ReflectUtils.getMethodNameFromField(field));
				return field.getDeclaringClass().getDeclaredMethod(getterName).invoke(instance);
			} catch (Exception ex) {
				try {
					field.setAccessible(true);
					return field.get(instance);
				} catch (Exception exx) {exx.printStackTrace();}
			}
		}
		return null;
	}




	public static void setFieldValue(Object instance, Object value, Field field) {

		try {
			String setterName = "set".concat(getMethodNameFromField(field));
			Method setter = field.getDeclaringClass().getDeclaredMethod(setterName, field.getType());
			setter.invoke(instance, value);
		} catch (Exception e) {
			try {
				field.setAccessible(true);
				field.set(instance, value);
			} catch (Exception ex) {ex.printStackTrace();}
		}
	}



	public static Class getComponentType(Field field) {
		Class ct = field.getType().getComponentType();
		if (ct == null) {
			Type type = field.getGenericType();
			if (type instanceof ParameterizedType) {
				ParameterizedType pm = (ParameterizedType) type;
				for (Type t: pm.getActualTypeArguments()) return ((Class) t);
			}
		}
		return ct;
	}
}
