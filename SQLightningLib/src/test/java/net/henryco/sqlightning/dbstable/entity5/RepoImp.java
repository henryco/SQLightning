package net.henryco.sqlightning.dbstable.entity5;

import android.database.Cursor;

/**
 * Created by HenryCo on 22/05/17.
 */

public class RepoImp {


	public Object[] readSomeValues(long uid, Repository5 repository) {
		Cursor cursor = repository.getSomeValues(uid);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();

			long id = cursor.getLong(cursor.getColumnIndex("id"));
			String txt = cursor.getString(cursor.getColumnIndex("ent_text"));
			cursor.close();
			return new Object[]{id, txt};
		}
		cursor.close();
		return null;
	}

}
