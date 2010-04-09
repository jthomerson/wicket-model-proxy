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

package com.wickettraining.modelproxy.domain;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Entity implements Serializable, Cloneable {

	public static final int TRANSIENT_ID = Integer.MIN_VALUE;
	
	private int id = TRANSIENT_ID;

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	public void setId(int id) {
		if (!isTransient()) {
			throw new RuntimeException("you can not set the id a second time");
		}
		this.id = id;
	}

	public int getId() {
		return id;
	}

	@Override
	public int hashCode() {
//		final int prime = 31;
		int result = 1;
//		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
//		Entity other = (Entity) obj;
//		if (id != other.id)
//			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Entity [id=" + id + "]";
	}

	public boolean isTransient() {
		return id == TRANSIENT_ID;
	}

}
