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
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wickettraining.modelproxy.domain.FakeDatabase;
import com.wickettraining.modelproxy.domain.Person;

public class HomePage extends BasePage {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(HomePage.class);

    public HomePage(final PageParameters parameters) {
		final IModel<? extends List<? extends Person>> ldm = new LoadAllPeopleModel();
    	Form<Void> form = new Form<Void>("form");
    	form.add(new Button("cancel") {
			private static final long serialVersionUID = 1L;

			@Override
    		public void onSubmit() {
    			logger.debug("Button('cancel').onSubmit()");
    			setResponsePage(getPageClass());
    		}
    	});
    	form.add(new Button("save") {
			private static final long serialVersionUID = 1L;

			@Override
    		public void onSubmit() {
    			logger.debug("Button('save').onSubmit()");
    			doSaveAll(ldm);
    			setResponsePage(getPageClass());
    		}
    	});
    	add(form);
		form.add(new ListView<Person>("people", ldm) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<Person> item) {
				item.setModel(wrapListItemModel(item.getModel()));
				final PersonViewPanel personViewPanel = new PersonViewPanel("content", item.getModel()) {
					private static final long serialVersionUID = 1L;

					protected void onEditClicked() {
						final PersonViewPanel pvp = this;
						replaceWith(new PersonEditPanel(getId(), item.getModel()) {
							private static final long serialVersionUID = 1L;

							protected void formSubmitted() {
								replaceWith(pvp);
							}
						});
					};
				};
				item.add(personViewPanel);
			}
		}.setReuseItems(true));
	}

	protected IModel<Person> wrapListItemModel(IModel<Person> model) {
		return model;
	}

	protected void doSaveAll(IModel<? extends List<? extends Person>> ldm) {
		FakeDatabase db = FakeDatabase.get();
		db.saveAll(ldm.getObject());
	}

}
