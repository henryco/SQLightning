package net.henryco.sqlightning.promo;

import android.app.Activity;
import android.content.Context;

import net.henryco.sqlightning.BuildConfig;
import net.henryco.sqlightning.SQLightning;
import net.henryco.sqlightning.SQLightningTestActivity;
import net.henryco.sqlightning.reflect.repository.RepositoryBuilder;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;

/**
 * Created by HenryCo on 23/05/17.
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, manifest = "app/src/main/AndroidManifest.xml")
public class PromoTest {

	private Activity activity;
	private Context getAppContext() {
		return activity;
	}



	@Before
	public void setUp() throws Exception {
		activity = Robolectric.buildActivity(SQLightningTestActivity.class)
				.create().resume().get();
	}


	@Test
	public void activityNotNullTest() {
		Assert.assertNotNull(activity);
	}




	@Test
	public void testOne() {

		SQLightning.run(getAppContext(), ExampleConfig.class);

		RepositoryBuilder repositoryBuilder = SQLightning.getRepositoryBuilder(getAppContext());

		ExampleRepository repository = repositoryBuilder.create(ExampleRepository.class);

		SomeData data = new SomeData();
		data.setId("fbgsea2");
		data.setSomeShortArray(new short[]{1, 2, -3, 4});
		data.setSomeIntList(Arrays.asList(1, 34, 5));

		SomeUser user = new SomeUser();
		user.setUserID(1L);
		user.setFirstName("Alex");
		user.setLast_name("Johnson");
		user.setData(data);

		repository.saveRecord(user);

		SomeUser newUser = repository.getRecordById(1L);

		Assert.assertEquals("Alex", newUser.getFirstName());
		Assert.assertEquals("Johnson", newUser.getLast_name());
		Assert.assertEquals(2, newUser.getData().getSomeShortArray()[1]);
		Assert.assertEquals(((Integer) 5), newUser.getData().getSomeIntList().get(2));
	}


}