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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProxyManager implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(ProxyManager.class);

	private List<Recording> recordings = new ArrayList<Recording>();
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
		final String uniqueID = createUniqueID(object);
		Object replaced = targets.put(uniqueID, object);
		if (replaced != null) {
			System.err.println("WARNING: you replaced one object with another in the proxy manager.  this was most likely a mistake.  you should make sure that you are truly generating a unique ID for each object");
		}
		ObjectLocator locator = new ObjectLocator() {
			private static final long serialVersionUID = 1L;

			public Object getObject() {
				return targets.get(uniqueID);
			}
		};

		return proxy(object, locator);
	}

	@SuppressWarnings("unchecked")
	public final <T> T proxy(T object, final ObjectLocator locator) {
		if (object == null) {
			System.err.println("WARNING: can not proxy null object");
			return null;
		}
		T proxy = (T) getFactory().createInterceptorProxy(object, createInterceptor(locator), new Class[] { object.getClass() });
		return proxy;
	}

	private ProxyFactory getFactory() {
		return factory == null ? createFactory() : factory;
	}

	private ProxyFactory createFactory() {
		return new CglibProxyFactory();
	}

	public synchronized void commitTo(Object object) throws CommitException {
		commitTo(object, createUniqueID(object));
	}
	public synchronized void commitTo(Object object, String string) throws CommitException {
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
		logger.debug("Starting commit");
		for(Recording rec : new ArrayList<Recording>(recordings)) {
			logger.debug("Committing: " + rec);
			Object target = rec.getObjectLocator().getObject();
			Object result = rec.applyTo(target);
			logger.debug("return from apply: " + result);
		}
		logger.debug("Commit complete");
	}

	private Interceptor createInterceptor(final ObjectLocator locator) {
		Interceptor inter = new Interceptor() {
			
			public Object intercept(Invocation invocation) throws Throwable {
				int hashBefore = invocation.getProxy().hashCode();
				Recording rec = new Recording(locator, invocation.getMethod(), invocation.getArguments());
				boolean doRecord = false;
				Object result = invocation.proceed();
				Object proxyResult = null;
				if (result != null && isModifiable(result)) {
					try {
						logger.debug("Because of invocation [" + toString(invocation) + "] , we are creating a subproxy: " + result);
						doRecord = true;
						final Method method = invocation.getMethod();
						final Object[] methodArgs = invocation.getArguments();
						ObjectLocator ourLocator = new ChainedLocatorMethodInvokingLocator(locator, method, methodArgs);
						proxyResult = proxy(result, ourLocator);
					} catch(Exception ex) {
						System.err.println("Can not create proxy for: " + result + "[" + ex.getMessage() + "]");
					}
				}
				int hashAfter = invocation.getProxy().hashCode();
				doRecord = doRecord ? true : hashAfter != hashBefore;
				synchronized (ProxyManager.this) {
					if (doRecord) {
						recordings.add(rec);
						logger.debug("Recorded: " + rec);
					} else {
						logger.debug("No change, did not record: " + rec);
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