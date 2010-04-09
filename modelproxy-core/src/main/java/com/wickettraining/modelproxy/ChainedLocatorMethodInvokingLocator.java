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
	
	public boolean appliesTo(Object obj) {
		return obj.equals(getObject()) || parent.appliesTo(obj);
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
