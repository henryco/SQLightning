package net.henryco.sqlightning.promo;

import net.henryco.sqlightning.reflect.annotations.repository.Repository;
import net.henryco.sqlightning.reflect.repository.LightningRepository;

/**
 * Created by HenryCo on 23/05/17
 */
@Repository("user_table")
public interface ExampleRepository extends LightningRepository<Long, SomeUser> {

}
