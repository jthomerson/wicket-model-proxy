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

import junit.framework.TestCase;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;

import com.wickettraining.modelproxy.domain.FakeDatabase;
import com.wickettraining.modelproxy.domain.PhoneNumber;

public class HomePageTest extends TestCase {
	private WicketTester tester;

	@Override
	public void setUp() {
		tester = new WicketTester(new WicketApplication());
		FakeDatabase.reset();
	}
	
	protected Class<? extends WebPage> getPageClassToTest() {
		return HomePage.class;
	}

	protected boolean isExpectedToWork() {
		return false;
	}

	public void testPageOperations() {
		tester.startPage(getPageClassToTest());
		tester.assertRenderedPage(getPageClassToTest());
		tester.assertComponent("form", Form.class);
		tester.assertComponent("form:people", ListView.class);
		tester.assertComponent("form:people:0:content", PersonViewPanel.class);
		assertFirstNameLabels(new String[] { "Jeremy", "Joe", "Jim" });
		changeFirstName(0, "JimBob");
		changeFirstName(1, "BillyBob");
		checkNumbersBeforeChanges();
		removePhoneNumber(0, 1);
		removePhoneNumber(2, 1);
		checkNumbersAfterChanges();
		checkLabelsAfterChange();
		FormTester form = tester.newFormTester("form");
		form.submit("save");
		checkLabelsAfterChange();
		tester.startPage(getPageClassToTest());
		checkLabelsAfterChange();
	}

	private void checkNumbersAfterChanges() {
		if (isExpectedToWork()) {
			checkPhoneNumbers(0, new String[] { "123-555-1212" });
			checkPhoneNumbers(1, new String[] { "124-555-1212" });
			checkPhoneNumbers(2, new String[] { "125-555-1212", "125-555-1616" });
		} else {
			checkNumbersBeforeChanges();
		}
	}

	private void checkNumbersBeforeChanges() {
		checkPhoneNumbers(0, new String[] { "123-555-1212", "123-555-1414" });
		checkPhoneNumbers(1, new String[] { "124-555-1212" });
		checkPhoneNumbers(2, new String[] { "125-555-1212", "125-555-1414", "125-555-1616" });
	}

	private void removePhoneNumber(int personInd, int numInd) {
		String contentPanelId = "form:people:" + personInd + ":content";
		tester.assertComponent(contentPanelId, PersonViewPanel.class);
		tester.clickLink(contentPanelId + ":edit");
		tester.assertRenderedPage(getPageClassToTest());
		tester.assertComponent(contentPanelId, PersonEditPanel.class);
		tester.assertComponent(contentPanelId + ":form:firstName", TextField.class);
		tester.clickLink(contentPanelId + ":form:phoneNumbers:" + numInd + ":delete");
		FormTester form = tester.newFormTester(contentPanelId + ":form");
		form.submit();
		tester.assertRenderedPage(getPageClassToTest());
		tester.assertComponent(contentPanelId, PersonViewPanel.class);
	}

	@SuppressWarnings("unchecked")
	private void checkPhoneNumbers(int personId, String[] nums) {
		String contentPanelId = "form:people:" + personId + ":content";
		ListView<PhoneNumber> lv = (ListView<PhoneNumber>) tester.getComponentFromLastRenderedPage(contentPanelId + ":phoneNumbers");
		assertEquals(nums.length, lv.getList().size());
		tester.assertComponent(contentPanelId, PersonViewPanel.class);
		for (int i = 0; i < nums.length; i++) {
			tester.assertLabel(contentPanelId + ":phoneNumbers:" + i + ":number", nums[i]);
		}
	}

	private void checkLabelsAfterChange() {
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
