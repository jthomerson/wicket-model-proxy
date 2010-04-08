package com.wickettraining.modelproxy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import junit.framework.TestCase;

import com.wickettraining.modelproxy.domain.FakeDatabase;
import com.wickettraining.modelproxy.domain.Person;
import com.wickettraining.modelproxy.domain.PhoneNumber;

public class SimpleOperationalTest extends TestCase {

	public void testname() throws Exception {
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

		assertTrue("Person proxy equals committed person", person.equals(person2));
		assertTrue("Original person object equals proxy committed person", orig.equals(person2));
		assertTrue("Original person object equals original second person", orig.equals(orig2));
		System.out.println("ProxyManager size: " + getObjectSize(pm));
		System.out.println("Person size: " + getObjectSize(orig));
	}

	private static long getObjectSize(Object obj) throws Exception {
		File file = File.createTempFile("testserialization", "bin");
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
		oos.writeObject(obj);
		oos.flush();
		oos.close();
		return file.length();
	}

}
