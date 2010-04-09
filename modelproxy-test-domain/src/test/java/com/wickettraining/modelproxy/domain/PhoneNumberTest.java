package com.wickettraining.modelproxy.domain;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang.SerializationUtils;

import junit.framework.TestCase;

public class PhoneNumberTest extends TestCase {

	@SuppressWarnings("unchecked")
	public void testUseInLinkedHashSet() throws Exception {
		Set<PhoneNumber> set = new LinkedHashSet<PhoneNumber>();
		PhoneNumber p1 = new PhoneNumber("123");
		PhoneNumber p2 = new PhoneNumber("234");
		set.add(p1);
		assertTrue(set.contains(p1));
		set.add(p2);
		assertTrue(set.contains(p2));
		
		Set<PhoneNumber> set2 = (Set<PhoneNumber>) SerializationUtils.clone((Serializable) set);
		assertTrue(set2.containsAll(set));
		assertEquals(set, set2);
	}
}
