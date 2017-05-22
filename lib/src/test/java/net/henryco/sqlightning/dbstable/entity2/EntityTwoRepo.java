package net.henryco.sqlightning.dbstable.entity2;

import net.henryco.sqlightning.reflect.annotations.repository.Repository;
import net.henryco.sqlightning.reflect.repository.LightningRepository;

/**
 * Created by HenryCo on 15/05/17.
 */
@Repository("example_table_two")
public interface EntityTwoRepo extends LightningRepository <Long, EntityTwo> {

}
