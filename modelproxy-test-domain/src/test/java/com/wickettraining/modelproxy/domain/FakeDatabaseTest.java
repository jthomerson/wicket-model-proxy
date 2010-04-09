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

package com.wickettraining.modelproxy.domain;

import junit.framework.TestCase;

public class FakeDatabaseTest extends TestCase {

	public void testObjectRetrievalAndCloning() throws Exception {
		FakeDatabase db = FakeDatabase.get();
		Person p1 = db.get(Person.class, 1);
		assertNotNull(p1);
		Person p2 = db.get(Person.class, 1);
		assertNotNull(p2);
		
		assertEquals(p1, p2);
		assertNotSame(p1, p2);
		
		p1.addPhoneNumber(new PhoneNumber("abcd"));

		assertFalse("p1 changes should not equal p2", p1.equals(p2));
		assertNotSame(p1, p2);
	}
}
