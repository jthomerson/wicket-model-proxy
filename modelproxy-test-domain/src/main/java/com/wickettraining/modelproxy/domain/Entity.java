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
		this.id = id;
	}

	public int getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Entity other = (Entity) obj;
		if (id != other.id)
			return false;
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
