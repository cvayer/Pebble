package com.mangecailloux.tools;

import com.badlogic.gdx.utils.Array;

abstract public class Pool<T> {
	public final int max;

	private final Array<T> freeObjects;

	/** Creates a pool with an initial capacity of 16 and no maximum. */
	public Pool () {
		this(16, Integer.MAX_VALUE);
	}

	/** Creates a pool with the specified initial capacity and no maximum. */
	public Pool (int initialCapacity) {
		this(initialCapacity, Integer.MAX_VALUE);
	}

	/** @param max The maximum number of free objects to store in this pool. */
	public Pool (int initialCapacity, int max) {
		freeObjects = new Array<T>(false, initialCapacity);
		this.max = max;
	}

	abstract protected T newObject ();

	/** Returns an object from this pool. The object may be new (from {@link #newObject()}) or reused (previously
	 * {@link #free(Object) freed}). */
	public T obtain () {
		return freeObjects.size == 0 ? newObject() : freeObjects.pop();
	}

	/** Puts the specified object in the pool, making it eligible to be returned by {@link #obtain()}. If the pool already contains
	 * {@link #max} free objects, the specified object is ignored. */
	public void free (T object) {
		if (object == null) throw new IllegalArgumentException("object cannot be null.");
		if (freeObjects.size < max) freeObjects.add(object);
	}

	/** Puts the specified objects in the pool.
	 * @see #free(Object) */
	public void free (Array<T> objects) {
		for (int i = 0, n = Math.min(objects.size, max - freeObjects.size); i < n; i++)
			freeObjects.add(objects.get(i));
	}

	/** Removes all free objects from this pool. */
	public void clear () {
		freeObjects.clear();
	}
	
	public int getFreeCount()
	{
		return freeObjects.size;
	}
}
