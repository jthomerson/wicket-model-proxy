package com.wickettraining.modelproxy.example;

import org.apache.wicket.markup.html.WebPage;

public class ProxyingModelHomePageTest extends HomePageTest {
	
	protected Class<? extends WebPage> getPageClassToTest() {
		return ProxyingModelHomePage.class;
	}

	@Override
	protected boolean isExpectedToWork() {
		return true;
	}

}
