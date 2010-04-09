/**
 * Copyright 2010 WicketTraining.com (Jeremy Thomerson at Expert Tech Services, LLC)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wickettraining.modelproxy;

import com.wickettraining.modelproxy.domain.FakeDatabase;
import com.wickettraining.modelproxy.domain.Person;

public class CountRecordingsTest extends BaseProxyManagerTest {

	public void testCountOfRecordngs() throws Exception {
		FakeDatabase db = FakeDatabase.get();

		ProxyManager pm = createProxyManager();
		Person person = db.get(Person.class, 1);

		Person orig = person;
		person = pm.proxy(person);

		// call some methods that should not be recorded:
//		person.getPhoneNumbers();
		person.getFirstName();
		person.hashCode();
		person.toString();
		person.equals(person);
		
		// and now one that should:
		person.setName("update the name");
		
		assertEquals(1, pm.getRecordingCount());
		
		Person person2 = db.get(Person.class, 1);
		Person orig2 = person2;
		pm.proxy(person2);
		pm.commit();

		compareTwoPeople(pm, person, person2, orig, orig2);
	}


}
