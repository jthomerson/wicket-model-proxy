package com.wickettraining.modelproxy.domain;

import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings("serial")
public class Person extends Entity implements NamedThing {

	private String firstName;
	private String lastName;
	private Collection<PhoneNumber> phoneNumbers = new ArrayList<PhoneNumber>();

	public void setName(String name) {
		setLastName(name);
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public Collection<PhoneNumber> getPhoneNumbers() {
		return phoneNumbers;
	}
	public void setPhoneNumbers(Collection<PhoneNumber> phoneNumbers) {
		if (phoneNumbers == null) {
			this.phoneNumbers.clear();
			return;
		}
		this.phoneNumbers = phoneNumbers;
	}
	public void addPhoneNumber(PhoneNumber number) {
		this.phoneNumbers.add(number);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Person [firstName=" + firstName + ", lastName=" + lastName + "]";
	}

}
