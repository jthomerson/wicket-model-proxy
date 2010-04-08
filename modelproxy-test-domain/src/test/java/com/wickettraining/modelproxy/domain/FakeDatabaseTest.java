package com.wickettraining.modelproxy.domain;

import junit.framework.TestCase;

public class FakeDatabaseTest extends TestCase {

	public void testObjectRetrievalAndCloning() throws Exception {
		FakeDatabase db = FakeDatabase.get();
		Person p1 = db.get(Person.class, 1);
		assertNotNull(p1);
		Person p2 = db.get(Person.class, 1);
		assertEquals(p1, p2);
		assertNotSame(p1, p2);
	}
}
