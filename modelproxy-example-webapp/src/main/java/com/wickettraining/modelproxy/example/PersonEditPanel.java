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

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import com.wickettraining.modelproxy.domain.Person;

public class PersonEditPanel extends Panel {

	private static final long serialVersionUID = 1L;

	public PersonEditPanel(String id, IModel<Person> model) {
		super(id, model);
		Form<Person> form = new Form<Person>("form", new CompoundPropertyModel<Person>(model)) {
			private static final long serialVersionUID = 1L;

			protected void onSubmit() {
				formSubmitted();
			}
		};
		form.add(new TextField<String>("firstName"));
		form.add(new TextField<String>("lastName"));
		add(form);
	}

	protected void formSubmitted() {
		// to be overridden
	}

}
