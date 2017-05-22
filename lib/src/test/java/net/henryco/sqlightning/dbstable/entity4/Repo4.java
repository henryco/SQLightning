package net.henryco.sqlightning.dbstable.entity4;

import net.henryco.sqlightning.reflect.annotations.repository.Repository;
import net.henryco.sqlightning.reflect.repository.LightningRepository;

/**
 * Created by HenryCo on 17/05/17.
 */
@Repository("arrayed_table")
public interface Repo4 extends LightningRepository<Integer, Entity4> {

}
