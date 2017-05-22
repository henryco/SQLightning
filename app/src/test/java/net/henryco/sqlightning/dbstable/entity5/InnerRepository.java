package net.henryco.sqlightning.dbstable.entity5;

import net.henryco.sqlightning.reflect.annotations.repository.Repository;
import net.henryco.sqlightning.reflect.repository.LightningRepository;

/**
 * Created by HenryCo on 18/05/17.
 */
@Repository("inner_table")
public interface InnerRepository extends LightningRepository <String, Entity5Inner> {
}
