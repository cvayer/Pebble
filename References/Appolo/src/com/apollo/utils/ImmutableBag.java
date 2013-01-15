package com.apollo.utils;

public interface ImmutableBag<E> {

	E get(int index);

	int size();

	boolean isEmpty();

}
