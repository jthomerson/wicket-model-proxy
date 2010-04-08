package com.wickettraining.modelproxy.example;

import java.util.List;

import org.apache.wicket.model.LoadableDetachableModel;

import com.wickettraining.modelproxy.domain.FakeDatabase;
import com.wickettraining.modelproxy.domain.Person;

public final class LoadAllPeopleModel extends LoadableDetachableModel<List<? extends Person>> {
	private static final long serialVersionUID = 1L;

	@Override
	protected List<? extends Person> load() {
		System.out.println("Loading all people from DB");
		return FakeDatabase.get().getAll(Person.class);
	}
}
