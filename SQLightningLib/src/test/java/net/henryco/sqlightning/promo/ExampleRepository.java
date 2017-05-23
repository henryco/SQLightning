package net.henryco.sqlightning.promo;

import android.database.Cursor;

import net.henryco.sqlightning.reflect.annotations.repository.Alias;
import net.henryco.sqlightning.reflect.annotations.repository.Implementation;
import net.henryco.sqlightning.reflect.annotations.repository.Query;
import net.henryco.sqlightning.reflect.annotations.repository.Read;
import net.henryco.sqlightning.reflect.annotations.repository.Repository;
import net.henryco.sqlightning.reflect.repository.LightningRepository;

/**
 * Created by HenryCo on 23/05/17
 */
@Repository("user_table")
public interface ExampleRepository extends LightningRepository<Long, SomeUser> {


	@Read // optional annotation, because SELECT case enables Read mode automatically
	@Query("SELECT * FROM user_table WHERE id = {uid} AND first_name = {name}")
	Cursor someMethodOne(@Alias("uid") long id, @Alias("name") String name);


	@Implementation(MethodsImp.class)
	boolean someMethodTwo(long id, ExampleRepository self);


}
