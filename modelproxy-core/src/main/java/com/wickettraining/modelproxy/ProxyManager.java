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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.proxy.Interceptor;
import org.apache.commons.proxy.Invocation;
import org.apache.commons.proxy.ProxyFactory;
import org.apache.commons.proxy.factory.cglib.CglibProxyFactory;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class ProxyManager implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Recording> recordings = new ArrayList<Recording>();
	private boolean committing = false;
	private transient ProxyFactory factory;
	private transient Map<String, Object> targets = new HashMap<String, Object>();
	
	public ProxyManager() {
	}
	
	public <T> IModel<T> proxyModel(final IModel<T> model) {
		return new LoadableDetachableModel<T>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected T load() {
				T ret = proxy(model.getObject());
				try {
					commit();
				} catch(Exception ex) {
					System.err.println("Error while committing during proxyModel#load: " + ex.getMessage());
					ex.printStackTrace();
				}
				return ret;
			}
			
			@Override
			public void setObject(T object) {
				super.setObject(object);
				model.setObject(object);
			}
			
			@Override
			public void detach() {
				super.detach();
				model.detach();
			}
		};
	}

	public final <T> T proxy(T object) {
		return proxy(object, createUniqueID(object));
	}

	@SuppressWarnings("unchecked")
	public final <T> T proxy(T object, String uniqueID) {
		if (object == null) {
			System.err.println("WARNING: can not proxy null object");
			return null;
		}
		T proxy = (T) getFactory().createInterceptorProxy(object, createInterceptor(uniqueID), new Class[] { object.getClass() });
		Object replaced = targets.put(uniqueID, object);
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

	public synchronized void commitTo(Object object) throws CommitException {
		if (object instanceof Collection<?>) {
			for(Object obj : ((Collection<?>) object)) {
				replaceTarget(obj);
			}
		}
		replaceTarget(object);
		commit();
	}

	private void replaceTarget(Object obj) {
		targets.put(createUniqueID(obj), obj);
	}

	public synchronized void commit() throws CommitException {
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
				Object result = invocation.proceed();
				Object proxyResult = null;
				if (result != null && isModifiable(result)) {
					String ourID = createUniqueID(result);
					try {
						System.out.println("Because of invocation [" + toString(invocation) + "] , we are creating subproxy with ID [" + ourID + "]: " + result);
						doRecord = true;
						proxyResult = proxy(result, ourID);
					} catch(Exception ex) {
						System.err.println("Can not create proxy for: " + result + "[" + ex.getMessage() + "]");
					}
				}
				int hashAfter = invocation.getProxy().hashCode();
				doRecord = doRecord ? true : hashAfter != hashBefore;
				synchronized (ProxyManager.this) {
					if (doRecord && !committing) {
						recordings.add(rec);
						System.out.println("Recorded: " + rec);
					} else {
//						System.out.println("No change, did not record: " + rec);
					}
				}
				return proxyResult == null ? result : proxyResult;
			}

			private String toString(Invocation invocation) {
				return invocation.getMethod() + "(" + Arrays.toString(invocation.getArguments()) + ") on " + invocation.getProxy();
			}
		};
		return inter;
	}

	protected boolean isModifiable(Object obj) {
		if (obj.getClass().isPrimitive() || obj instanceof String || obj instanceof Number || obj instanceof Void || obj instanceof Boolean) {
			return false;
		}
		return true;
	}

	protected String createUniqueID(Object object, Method method) {
		return createUniqueID(object) + "--" + method.toGenericString();
	}
	protected String createUniqueID(Object object) {
		return object.getClass().getSimpleName() + "--" + object.hashCode();
	}

	public int getRecordingCount() {
		return recordings.size();
	}

}