package net.henryco.sqlightning.reflect.repository;

import android.database.sqlite.SQLiteOpenHelper;

import net.henryco.sqlightning.reflect.annotations.repository.Repository;
import net.henryco.sqlightning.reflect.repository.query.RepositoryQuery;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

/**
 * Created by HenryCo on 13/05/17
 */

public class RepositoryBuilder {

	private static final String NO_REPO_ANNOTATION_THROW_MSG
			= "Repository interface which implements LightningRepository<K,E> must be annotated as @Repository";
	private static final String NOT_AN_INTERFACE_THROW_MSG
			= "Repository class must be Interface";



	private final SQLiteOpenHelper dbHelper;
	public RepositoryBuilder(SQLiteOpenHelper helper) {
		this.dbHelper = helper;
	}



	@SuppressWarnings("unchecked")
	public <T extends LightningRepository> T create(final Class repoClass, final String repositoryTable ,
													final Class KeyType, final Class EntityType) {
		if (!repoClass.isInterface()) throw new RuntimeException(NOT_AN_INTERFACE_THROW_MSG);
		final LightningRepository generalRepresentation
				= new LightningRepoImplementation(dbHelper, repositoryTable, KeyType, EntityType);
		return (T) createProxy(repoClass, generalRepresentation);
	}



	public <T extends LightningRepository> T create(final Class<T> repoClass) {
		if (!repoClass.isInterface()) throw new RuntimeException(NOT_AN_INTERFACE_THROW_MSG);
		final LightningRepository generalRepresentation = getGeneralRepresentation(repoClass, dbHelper);
		return createProxy(repoClass, generalRepresentation);
	}



	@SuppressWarnings("unchecked")
	private <T extends LightningRepository> T createProxy(final Class<T> repoClass,
														  final LightningRepository generalRepresentation) {

		final RepositoryQuery queryExecutor = new RepositoryQuery(repoClass, dbHelper);
		return (T) Proxy.newProxyInstance(repoClass.getClassLoader(),
				new Class<?>[]{repoClass}, (proxy, method, args) -> {
					if (isGeneralMethod(method))
						return method.invoke(generalRepresentation, args);
					return queryExecutor.invoke(method.getName(), generalRepresentation, args);
				}
		);
	}



	private static boolean isGeneralMethod(Method method) {
		for (Method m: LightningRepository.class.getDeclaredMethods())
			if (m.equals(method)) return true;
		return false;
	}



	private static LightningRepository getGeneralRepresentation
			(final Class<? extends LightningRepository> repoClass, final SQLiteOpenHelper dbHelper) {

		for (Type interType: repoClass.getGenericInterfaces()) {
			if (interType instanceof ParameterizedType &&
					((ParameterizedType)interType).getRawType() == LightningRepository.class) {

				Type[] types = ((ParameterizedType)interType).getActualTypeArguments();
				Repository repository = repoClass.getAnnotation(Repository.class);
				if (repository == null) throw new RuntimeException(NO_REPO_ANNOTATION_THROW_MSG);
				return new LightningRepoImplementation(dbHelper, repository.value(), (Class) types[0], (Class) types[1]);
			}
		}
		return null;
	}


}
