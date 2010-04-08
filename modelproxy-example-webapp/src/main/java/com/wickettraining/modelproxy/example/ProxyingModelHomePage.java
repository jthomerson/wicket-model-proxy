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

import org.apache.wicket.PageParameters;
import org.apache.wicket.model.IModel;

import com.wickettraining.modelproxy.CommitException;
import com.wickettraining.modelproxy.ProxyManager;
import com.wickettraining.modelproxy.domain.FakeDatabase;
import com.wickettraining.modelproxy.domain.Person;

public class ProxyingModelHomePage extends HomePage {

	private final ProxyManager pm = new OurProxyManager();
	
	public ProxyingModelHomePage(PageParameters parameters) {
		super(parameters);
    }

	@Override
	protected IModel<Person> wrapListItemModel(IModel<Person> model) {
		return pm.proxyModel(model);
	}
	
	@Override
	protected void doSaveAll(IModel<? extends List<? extends Person>> ldm) {
		FakeDatabase db = FakeDatabase.get();
		List<? extends Person> all = ldm.getObject();
		try {
			pm.commitTo(all);
		} catch (CommitException e) {
			System.err.println("Error committing changes to the list before saving");
			e.printStackTrace();
		}
		db.saveAll(all);
	}

}
