package com.wickettraining.modelproxy.example;

import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;

import com.wickettraining.modelproxy.domain.FakeDatabase;
import com.wickettraining.modelproxy.domain.Person;

/**
 * Homepage
 */
public class HomePage extends BasePage {

	private static final long serialVersionUID = 1L;

    public HomePage(final PageParameters parameters) {
    	createComponents();
    }

	protected void createComponents() {
		final IModel<? extends List<? extends Person>> ldm = new LoadAllPeopleModel();
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
    			db.saveAll(ldm.getObject());
    			setResponsePage(getPageClass());
    		}
    	});
    	add(form);
		form.add(new ListView<Person>("people", ldm) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<Person> item) {
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

}
