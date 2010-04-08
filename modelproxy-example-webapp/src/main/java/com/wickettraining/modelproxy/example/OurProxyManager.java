package com.wickettraining.modelproxy.example;

import com.wickettraining.modelproxy.ProxyManager;
import com.wickettraining.modelproxy.domain.Entity;

public class OurProxyManager extends ProxyManager {

	private static final long serialVersionUID = 1L;

	@Override
	protected String createUniqueID(Object object) {
		if (object instanceof Entity) {
			Entity ent = (Entity) object;
			// TODO: problem here if transient
			return ent.getClass().getSimpleName() + "--" + ent.getId();
		}
		return super.createUniqueID(object);
	}
}
