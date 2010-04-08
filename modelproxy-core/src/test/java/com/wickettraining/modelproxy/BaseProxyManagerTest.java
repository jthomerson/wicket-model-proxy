package com.wickettraining.modelproxy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import junit.framework.TestCase;

import com.wickettraining.modelproxy.domain.Person;

public class BaseProxyManagerTest extends TestCase {

	public void testNothing() throws Exception {
		// no-op test so this doesn't fail
		// this is simply a base class, not a real test case class
	}
	
	protected void compareTwoPeople(ProxyManager pm, Person p1, Person p2, Person orig1, Person orig2) {
		assertTrue("Person proxy equals committed person", p1.equals(p2));
		assertTrue("Original person object equals proxy committed person", orig1.equals(p2));
		assertTrue("Original person object equals original second person", orig1.equals(orig2));
		try {
			System.out.println("ProxyManager size: " + getObjectSize(pm));
			System.out.println("Person size: " + getObjectSize(orig1));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected static long getObjectSize(Object obj) throws Exception {
		File file = File.createTempFile("testserialization", "bin");
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
		oos.writeObject(obj);
		oos.flush();
		oos.close();
		return file.length();
	}


}
