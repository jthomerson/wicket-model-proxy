package com.wickettraining.modelproxy.example;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import com.wickettraining.modelproxy.domain.Person;
import com.wickettraining.modelproxy.domain.PhoneNumber;

public class PersonViewPanel extends Panel {

	private static final long serialVersionUID = 1L;

	public PersonViewPanel(String id, IModel<Person> model) {
		super(id, new CompoundPropertyModel<Person>(model));
		add(new Label("firstName"));
		add(new Label("lastName"));
		add(createPhoneNumberListView("phoneNumbers"));
		add(new Link<Void>("edit") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				onEditClicked();
			}
		});
	}

	protected ListView<PhoneNumber> createPhoneNumberListView(String id) {
		return new PropertyListView<PhoneNumber>(id) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<PhoneNumber> item) {
				item.add(new Label("number"));
			}
		};
	}

	protected void onEditClicked() {
		// to be overridden
	}
}
