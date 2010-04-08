package com.wickettraining.modelproxy.domain;

import java.util.HashMap;
import java.util.Map;

public class FakeDatabase {

	private static FakeDatabase instance = new FakeDatabase();

	public static final FakeDatabase get() {
		return instance;
	}
	
	private Map<Class<? extends Entity>, Map<Integer, Entity>> data = new HashMap<Class<? extends Entity>, Map<Integer,Entity>>();
	
	public FakeDatabase() {
		createData();
	}
	
	@SuppressWarnings("unchecked")
	public final <T extends Entity> T get(Class<T> clazz, int id) {
		try {
			return (T) data.get(clazz).get(id).clone();
		} catch (CloneNotSupportedException ex) {
			throw new RuntimeException("Could not clone " + clazz.getName() + " [" + id + "]", ex);
		}
	}
	
	public final void save(Entity entity) {
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
