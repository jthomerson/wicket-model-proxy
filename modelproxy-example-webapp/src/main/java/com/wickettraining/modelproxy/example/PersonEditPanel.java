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
