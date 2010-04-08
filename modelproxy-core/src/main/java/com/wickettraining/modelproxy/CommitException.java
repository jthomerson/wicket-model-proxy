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