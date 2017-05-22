package net.henryco.sqlightning.dbstable.entity3;

import net.henryco.sqlightning.reflect.annotations.repository.Repository;
import net.henryco.sqlightning.reflect.repository.LightningRepository;

/**
 * Created by HenryCo on 16/05/17.
 */

@Repository("table_ent3")
public interface EntityThreeRepo extends LightningRepository<Integer, Entity3> {

}
