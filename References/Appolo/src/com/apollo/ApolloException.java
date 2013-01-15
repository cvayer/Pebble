package com.apollo;

public class ApolloException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ApolloException() {
	}

	public ApolloException(String str) {
		super(str);
	}

	public ApolloException(Throwable throwable) {
		super(throwable);
	}

	public ApolloException(String str, Throwable throwable) {
		super(str, throwable);
	}

}
