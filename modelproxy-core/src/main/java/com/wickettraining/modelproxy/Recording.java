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

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Recording implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final String uniqueTargetObjectID;
	private final Object[] args;
	private final String methodName;
	private final Class<?>[] methodArgs;
	private transient Method method;
	
	public Recording(String uniqueID, Method method, Object[] args) {
		super();
		this.uniqueTargetObjectID = uniqueID;
		this.method = method;
		this.methodName = method.getName();
		this.methodArgs = method.getParameterTypes();
		this.args = args;
	}
	
	public void applyTo(Object target) throws CommitException {
		try {
			if (method == null) {
				method = target.getClass().getMethod(methodName, methodArgs);
			}
			method.invoke(target, args);
		} catch(Exception ex) {
			throw new CommitException("Could not commit " + method.getName() + " call to " + target + " with args " + Arrays.toString(args), ex);
		}
	}
	
	public String getUniqueTargetObjectID() {
		return uniqueTargetObjectID;
	}

	@Override
	public String toString() {
		return "Recording [method=" + method + ", args=" + Arrays.toString(args) + ", uniqueTargetObjectID=" + uniqueTargetObjectID + "]";
	}
	
}