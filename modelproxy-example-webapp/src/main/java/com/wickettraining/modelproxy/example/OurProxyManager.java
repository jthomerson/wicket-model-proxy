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
