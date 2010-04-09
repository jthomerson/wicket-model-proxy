package com.wickettraining.modelproxy.example;

import junit.framework.TestCase;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;

public class HomePageTest extends TestCase {
	private WicketTester tester;

	@Override
	public void setUp() {
		tester = new WicketTester(new WicketApplication());
	}
	
	protected Class<? extends WebPage> getPageClassToTest() {
		return HomePage.class;
	}

	protected boolean isExpectedToWork() {
		return false;
	}

	public void testRenderMyPage() {
		tester.startPage(getPageClassToTest());
		tester.assertRenderedPage(getPageClassToTest());
		tester.assertComponent("form", Form.class);
		tester.assertComponent("form:people", ListView.class);
		tester.assertComponent("form:people:0:content", PersonViewPanel.class);
		assertFirstNameLabels(new String[] { "Jeremy", "Joe", "Jim" });
		changeFirstName(0, "JimBob");
		changeFirstName(1, "BillyBob");
		if (isExpectedToWork()) {
			assertFirstNameLabels(new String[] { "JimBob", "BillyBob", "Jim" });
		} else {
			assertFirstNameLabels(new String[] { "Jeremy", "Joe", "Jim" });
		}
	}

	private void changeFirstName(int i, String newName) {
		String contentPanelId = "form:people:" + i + ":content";
		tester.assertComponent(contentPanelId, PersonViewPanel.class);
		tester.clickLink(contentPanelId + ":edit");
		tester.assertRenderedPage(getPageClassToTest());
		tester.assertComponent(contentPanelId, PersonEditPanel.class);
		tester.assertComponent(contentPanelId + ":form:firstName", TextField.class);
		FormTester form = tester.newFormTester(contentPanelId + ":form");
		form.setValue("firstName", newName);
		form.submit();
		tester.assertRenderedPage(getPageClassToTest());
		tester.assertComponent(contentPanelId, PersonViewPanel.class);
	}

	private void assertFirstNameLabels(String[] labels) {
		for (int i = 0; i < labels.length; i++) {
			tester.assertLabel("form:people:" + i + ":content:firstName", labels[i]);
		}
	}

}
