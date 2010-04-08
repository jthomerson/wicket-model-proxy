package com.wickettraining.modelproxy;

public class CommitException extends Exception {

	private static final long serialVersionUID = 1L;

	public CommitException() {
		super();
	}

	public CommitException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public CommitException(String message) {
		super(message);
	}

	public CommitException(Throwable throwable) {
		super(throwable);
	}

}