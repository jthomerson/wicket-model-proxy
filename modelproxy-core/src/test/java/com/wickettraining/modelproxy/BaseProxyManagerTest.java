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

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import junit.framework.TestCase;

import com.wickettraining.modelproxy.domain.Entity;
import com.wickettraining.modelproxy.domain.Person;

public class BaseProxyManagerTest extends TestCase {

	public void testNothing() throws Exception {
		// no-op test so this doesn't fail
		// this is simply a base class, not a real test case class
	}
	
	private static class MyProxyManager extends ProxyManager {
		private static final long serialVersionUID = 1L;

		@Override
		protected String createUniqueID(Object object) {
			if (object instanceof Entity) {
				return object.getClass().getSimpleName() + "--" + ((Entity) object).getId();
			}
			return super.createUniqueID(object);
		}
	};
	protected ProxyManager createProxyManager() {
		return new MyProxyManager();
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
