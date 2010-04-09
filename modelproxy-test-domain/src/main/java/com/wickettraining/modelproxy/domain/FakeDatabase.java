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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FakeDatabase {

	private static final Object LOCK = new Object();
	private static FakeDatabase instance;
	private static final Logger logger = LoggerFactory.getLogger(FakeDatabase.class);

	public static final FakeDatabase get() {
		synchronized(LOCK) {
			if (instance == null) {
				instance = new FakeDatabase();
			}
			return instance;
		}
	}
	
	private Map<Class<? extends Entity>, Map<Integer, Entity>> data = new HashMap<Class<? extends Entity>, Map<Integer,Entity>>();
	
	public FakeDatabase() {
		createData();
	}
	
	public <T extends Entity> List<T> getAll(Class<T> clazz) {
		List<T> list = new ArrayList<T>();
		for(Integer id : data.get(clazz).keySet()) {
			list.add(get(clazz, id));
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public final <T extends Entity> T get(Class<T> clazz, int id) {
		try {
			return (T) SerializationUtils.clone((T) data.get(clazz).get(id));
		} catch (Exception ex) {
			throw new RuntimeException("Could not clone " + clazz.getName() + " [" + id + "]", ex);
		}
	}
	
	public void saveAll(List<? extends Entity> objects) {
		logger.debug("Saving list of objects [" + objects.size() + "]");
		for(Entity ent : objects) {
			save(ent);
		}
	}

	public final void save(Entity entity) {
		logger.debug("saving object: " + entity);
		putEntity(entity);
	}

	private synchronized void putEntity(Entity entity) {
		Map<Integer, Entity> map = data.get(entity.getClass());
		if (map == null) {
			map = new HashMap<Integer, Entity>();
			data.put(entity.getClass(), map);
		}
		if (entity.isTransient()) {
			entity.setId(getNextId(map));
		}
		map.put(entity.getId(), entity);
	}
	
	private synchronized int getNextId(Map<Integer, Entity> map) {
		int id = 0;
		for(Entity ent : map.values()) {
			if (ent.getId() > id) {
				id = ent.getId();
			}
		}
		if (id < 0) {
			throw new IllegalStateException("should not have id: " + id);
		}
		return id + 1;
	}

	private void createData() {
		save(createPerson("Jeremy", "Thomerson", new PhoneNumber[] {
				new PhoneNumber("123-555-1212"), new PhoneNumber("123-555-1414")
		}));
		save(createPerson("Joe", "Brown", new PhoneNumber[] {
				new PhoneNumber("124-555-1212")
		}));
		save(createPerson("Jim", "Brown", new PhoneNumber[] {
				new PhoneNumber("125-555-1212"), new PhoneNumber("125-555-1414"), new PhoneNumber("125-555-1616")
		}));
	}

	private Entity createPerson(String first, String last, PhoneNumber[] phoneNumbers) {
		Person person = new Person();
		person.setFirstName(first);
		person.setLastName(last);
		for(PhoneNumber num : phoneNumbers) {
			save(num);
			person.addPhoneNumber(num);
		}
		return person;
	}

}
