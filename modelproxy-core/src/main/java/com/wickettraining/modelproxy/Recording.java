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