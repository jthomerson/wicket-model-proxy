package com.wickettraining.modelproxy;

import java.lang.reflect.Method;

public class ChainedLocatorMethodInvokingLocator implements ObjectLocator {
	private static final long serialVersionUID = 1L;
	
	private final ObjectLocator parent;
	private final String methodName;
	private final Object[] args;
	private final Class<?>[] methodParameterTypes;
	
	public ChainedLocatorMethodInvokingLocator(ObjectLocator locator, Method method, Object[] methodArgs) {
		this.parent = locator;
		this.methodName = method.getName();
		this.methodParameterTypes = method.getParameterTypes();
		this.args = methodArgs;
	}
	
	public Object getObject() {
		try {
			Method method = parent.getObject().getClass().getMethod(methodName, methodParameterTypes);
			return method.invoke(parent.getObject(), args);
		} catch (Exception e) {
			throw new RuntimeException("error while trying to locate a child object of the originally proxied object: " + e.getMessage(), e);
		}
	}


}
