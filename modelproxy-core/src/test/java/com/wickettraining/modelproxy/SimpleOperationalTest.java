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
import com.wickettraining.modelproxy.domain.PhoneNumber;

public class SimpleOperationalTest extends BaseProxyManagerTest {

	public void testSimpleOperations() throws Exception {
		FakeDatabase db = FakeDatabase.get();

		ProxyManager pm = new ProxyManager();
		Person person = db.get(Person.class, 1);
		System.out.println("Person hash code: " + person.hashCode());

		Person orig = person;
		person = pm.proxy(person, "person");

		System.out.println("SETTING ID");
		person.setId(22);
		System.out.println("SETTING LAST NAME");
		person.setLastName("Test");
		System.out.println("SETTING NAME");
		person.setName("Test");
		System.out.println("\n\nFirst phone number change - direct");
		person.addPhoneNumber(new PhoneNumber("555-555-5555"));
		System.out.println("\n\nSecond phone number change - indirect");
		person.getPhoneNumbers().add(new PhoneNumber("254-555-1212"));

		System.out.println("Person hash code: " + person.hashCode());

		Person person2 = db.get(Person.class, 1);
		Person orig2 = person2;
		pm.proxy(person2, "person");
		pm.commit();

		compareTwoPeople(pm, person, person2, orig, orig2);
	}

}
