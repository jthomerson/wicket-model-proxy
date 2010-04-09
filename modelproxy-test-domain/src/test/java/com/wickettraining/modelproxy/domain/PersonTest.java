package com.wickettraining.modelproxy.domain;

import junit.framework.TestCase;

public class PersonTest extends TestCase {

	public void testSimpleStuff() throws Exception {
		Person p1 = new Person();
		PhoneNumber n1 = new PhoneNumber("1234");
		PhoneNumber n2 = new PhoneNumber("2345");
		p1.addPhoneNumber(n1);
		p1.addPhoneNumber(n2);
		
		assertEquals(2, p1.getPhoneNumbers().size());
		
		p1.getPhoneNumbers().remove(n1);

		assertEquals(1, p1.getPhoneNumbers().size());
	}
}
