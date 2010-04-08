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

import com.wickettraining.modelproxy.CommitException;
import com.wickettraining.modelproxy.ProxyManager;
import com.wickettraining.modelproxy.domain.FakeDatabase;
import com.wickettraining.modelproxy.domain.Person;

public class ProxyingModelHomePage extends HomePage {

	public ProxyingModelHomePage(PageParameters parameters) {
		super(parameters);
    }

	protected void createComponents() {
		final ProxyManager pm = new OurProxyManager();
		final IModel<List<? extends Person>> ldm = new LoadAllPeopleModel();
		
    	Form<Void> form = new Form<Void>("form");
    	form.add(new Button("cancel") {
			private static final long serialVersionUID = 1L;

			@Override
    		public void onSubmit() {
    			System.out.println("Button('cancel').onSubmit()");
    			setResponsePage(getPageClass());
    		}
    	});
    	form.add(new Button("save") {
			private static final long serialVersionUID = 1L;

			@Override
    		public void onSubmit() {
    			System.out.println("Button('save').onSubmit()");
    			FakeDatabase db = FakeDatabase.get();
    			List<? extends Person> all = ldm.getObject();
    			try {
					pm.commitTo(all);
				} catch (CommitException e) {
					System.err.println("Error committing changes to the list before saving");
					e.printStackTrace();
				}
    			db.saveAll(all);
    			setResponsePage(getPageClass());
    		}
    	});
    	add(form);
		form.add(new ListView<Person>("people", ldm) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<Person> item) {
				item.setModel(pm.proxyModel(item.getModel()));
				final PersonViewPanel personViewPanel = new PersonViewPanel("content", item.getModel()) {
					private static final long serialVersionUID = 1L;

					protected void onEditClicked() {
						final PersonViewPanel pvp = this;
						replaceWith(new PersonEditPanel(getId(), item.getModel()) {
							private static final long serialVersionUID = 1L;

							protected void formSubmitted() {
								try {
									pm.commit();
								} catch (CommitException e) {
									throw new RuntimeException(e);
								}
								replaceWith(pvp);
							}
						});
					};
				};
				item.add(personViewPanel);
			}
		}.setReuseItems(true));
	}

}
