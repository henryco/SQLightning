package net.henryco.sqlightning.dbstable.entity5;

import android.database.Cursor;

import net.henryco.sqlightning.reflect.annotations.repository.Alias;
import net.henryco.sqlightning.reflect.annotations.repository.Implementation;
import net.henryco.sqlightning.reflect.annotations.repository.Query;
import net.henryco.sqlightning.reflect.annotations.repository.Repository;
import net.henryco.sqlightning.reflect.repository.LightningRepository;

/**
 * Created by HenryCo on 18/05/17.
 */
@Repository("table_entity_5")
public interface Repository5 extends LightningRepository <Long, Entity5> {

	@Query("SELECT ent_text, id FROM table_entity_5 WHERE id = {uid}")
	Cursor getSomeValues(@Alias("uid") long id);

	@Implementation(RepoImp.class)
	Object[] readSomeValues(long uid, Repository5 repository);

}
