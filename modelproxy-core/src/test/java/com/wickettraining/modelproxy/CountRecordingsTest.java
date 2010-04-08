package com.wickettraining.modelproxy;

import com.wickettraining.modelproxy.domain.FakeDatabase;
import com.wickettraining.modelproxy.domain.Person;

public class CountRecordingsTest extends BaseProxyManagerTest {

	public void testCountOfRecordngs() throws Exception {
		FakeDatabase db = FakeDatabase.get();

		ProxyManager pm = new ProxyManager();
		Person person = db.get(Person.class, 1);

		Person orig = person;
		person = pm.proxy(person, "person");

		// call some methods that should not be recorded:
//		person.getPhoneNumbers();
		person.getFirstName();
		person.hashCode();
		person.toString();
		person.equals(person);
		
		// and now one that should:
		person.setId(22);
		
		assertEquals(1, pm.getRecordingCount());
		
		Person person2 = db.get(Person.class, 1);
		Person orig2 = person2;
		pm.proxy(person2, "person");
		pm.commit();

		compareTwoPeople(pm, person, person2, orig, orig2);
	}


}
