package net.henryco.sqlightning.reflect.repository;

/**
 * Created by HenryCo on 13/05/17.
 */

public interface LightningRepository<KEY, ENTITY> {

	ENTITY getRecordById(KEY key);
	boolean saveRecord(ENTITY record);
	boolean deleteRecordById(KEY key);
	boolean updateRecord(ENTITY record);
	boolean isRecordExist(KEY key);
}
