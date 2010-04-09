package com.wickettraining.modelproxy.example;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class CollectionToListModel<T> extends LoadableDetachableModel<List<? extends T>> {

	private static final long serialVersionUID = 1L;

	private final IModel<? extends Collection<? extends T>> inner;
	
	public CollectionToListModel(IModel<? extends Collection<? extends T>> model) {
		this.inner = model;
	}

	@Override
	protected List<? extends T> load() {
		return new ArrayList<T>(inner.getObject());
	}
	

}
