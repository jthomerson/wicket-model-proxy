package com.wickettraining.modelproxy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.proxy.Interceptor;
import org.apache.commons.proxy.Invocation;
import org.apache.commons.proxy.ProxyFactory;
import org.apache.commons.proxy.factory.cglib.CglibProxyFactory;

public class ProxyManager implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<Recording> recordings = new ArrayList<Recording>();
	private boolean committing = false;
	private transient ProxyFactory factory;
	private transient Map<String, Object> targets = new HashMap<String, Object>();
	
	public ProxyManager() {
	}
	
	@SuppressWarnings("unchecked")
	public final <T> T proxy(T object, String uniqueID) {
		if (object == null) {
			System.err.println("WARNING: can not proxy null object");
			return null;
		}
		T proxy = (T) getFactory().createInterceptorProxy(object, createInterceptor(uniqueID), new Class[] { object.getClass() });
		Object replaced = targets.put(uniqueID, proxy);
		if (replaced != null) {
			System.err.println("WARNING: you replaced one object with another in the proxy manager.  this was most likely a mistake.  you should make sure that you are truly generating a unique ID for each object");
		}
		return proxy;
	}

	private ProxyFactory getFactory() {
		return factory == null ? createFactory() : factory;
	}

	private ProxyFactory createFactory() {
		return new CglibProxyFactory();
	}

	public void commit() throws CommitException {
		committing = true;
		System.out.println("\nStarting commit");
		for(Recording rec : new ArrayList<Recording>(recordings)) {
			System.out.println("Committing [" + rec.getUniqueTargetObjectID() + "]: " + rec);
			Object target = targets.get(rec.getUniqueTargetObjectID());
			if (target == null) {
				System.err.println("Problem: could not find target for ID: " + rec.getUniqueTargetObjectID());
				System.err.println("All targets: " + targets);
			}
			rec.applyTo(target);
		}
		System.out.println("Commit complete\n");
		committing = false;
	}

	private Interceptor createInterceptor(final String uniqueID) {
		Interceptor inter = new Interceptor() {
			
			public Object intercept(Invocation invocation) throws Throwable {
				int hashBefore = invocation.getProxy().hashCode();
				Recording rec = new Recording(uniqueID, invocation.getMethod(), invocation.getArguments());
				boolean doRecord = false;
				String ourID = invocation.getMethod().toGenericString() + invocation.getProxy().hashCode();
				Object result = invocation.proceed();
				Object proxyResult = null;
				if (result != null && result.getClass().isPrimitive() == false) {
					try {
						System.out.println("Creating subproxy with ID [" + ourID + "]: " + result);
						doRecord = true;
						proxyResult = proxy(result, ourID);
					} catch(Exception ex) {
						System.err.println("Can not create proxy for: " + result + "[" + ex.getMessage() + "]");
					}
				}
				int hashAfter = invocation.getProxy().hashCode();
				doRecord = doRecord ? true : hashAfter != hashBefore;
				if (doRecord && !committing) {
					recordings.add(rec);
					System.out.println("Recorded: " + rec);
				} else {
					System.out.println("No change, did not record: " + rec);
				}
				return proxyResult == null ? result : proxyResult;
			}
		};
		return inter;
	}

	public List<Recording> getRecordings() {
		return recordings;
	}
	public void setRecordings(List<Recording> recordings) {
		this.recordings = recordings;
	}
	
}