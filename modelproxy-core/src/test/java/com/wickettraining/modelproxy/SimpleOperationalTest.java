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

		ProxyManager pm = createProxyManager();
		Person person = db.get(Person.class, 1);

		Person orig = person;
		person = pm.proxy(person);

		logger.debug("SETTING LAST NAME");
		person.setLastName("Test");
		logger.debug("SETTING NAME");
		person.setName("interface works");
		logger.debug("First phone number change - direct");
		person.addPhoneNumber(new PhoneNumber("direct add"));
		logger.debug("Second phone number change - indirect");
		person.getPhoneNumbers().add(new PhoneNumber("add through collection"));

		logger.debug("Creating a second person to commit changes to");
		Person person2 = db.get(Person.class, 1);
		Person orig2 = person2;
		pm.proxy(person2);
		pm.commit();

		logger.debug("P1 RESULT: " + person);
		logger.debug("P2 RESULT: " + person2);
		compareTwoPeople(pm, person, person2, orig, orig2);
	}

	public void testAddPhoneNumberInTwoSteps() throws Exception {
		FakeDatabase db = FakeDatabase.get();

		ProxyManager pm = createProxyManager();
		Person person = db.get(Person.class, 2);
		int originalNumberOfPhoneNumbers = person.getPhoneNumbers().size();
		logger.debug("Person hash code: " + person.hashCode());

		Person orig = person;
		person = pm.proxy(person);

		person.setName("Test");
		PhoneNumber pn = new PhoneNumber();
		person.addPhoneNumber(pn);
		pn.setNumber("555-555-0987");
		
		assertEquals(originalNumberOfPhoneNumbers + 1, person.getPhoneNumbers().size());

		Person person2 = db.get(Person.class, 2);
		logger.debug("person before: " + person);
		logger.debug("person2 before: " + person2);
		Person orig2 = person2;
		pm.proxy(person2);
		pm.commit();
		logger.debug("person2 after: " + person2);

		assertEquals(person.getPhoneNumbers().size(), person2.getPhoneNumbers().size());

		compareTwoPeople(pm, person, person2, orig, orig2);
	}

}
