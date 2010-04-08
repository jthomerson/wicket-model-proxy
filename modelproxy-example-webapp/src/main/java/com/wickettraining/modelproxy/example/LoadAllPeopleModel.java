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

package com.wickettraining.modelproxy.example;

import java.util.List;

import org.apache.wicket.model.LoadableDetachableModel;

import com.wickettraining.modelproxy.domain.FakeDatabase;
import com.wickettraining.modelproxy.domain.Person;

public class LoadAllPeopleModel extends LoadableDetachableModel<List<? extends Person>> {
	private static final long serialVersionUID = 1L;

	@Override
	protected List<? extends Person> load() {
		System.out.println("Loading all people from DB");
		return FakeDatabase.get().getAll(Person.class);
	}
}
