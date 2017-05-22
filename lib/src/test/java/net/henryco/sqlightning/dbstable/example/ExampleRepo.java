package net.henryco.sqlightning.dbstable.example;

import net.henryco.sqlightning.reflect.annotations.repository.Alias;
import net.henryco.sqlightning.reflect.annotations.repository.Implementation;
import net.henryco.sqlightning.reflect.annotations.repository.Query;
import net.henryco.sqlightning.reflect.annotations.repository.Read;
import net.henryco.sqlightning.reflect.annotations.repository.Repository;
import net.henryco.sqlightning.reflect.repository.LightningRepository;

/**
 * Created by HenryCo on 13/05/17
 */
@Repository("example_table")
public interface ExampleRepo extends LightningRepository<Integer, EntityExample> {

	@Query("SELECT * FROM {tab}") @Read
	void doSmth(@Alias("tab") String tabName);

	@Implementation(ExampleRepo.class)
	void impFunction();

}
