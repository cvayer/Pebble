package com.apollo.components.spatial;

import com.apollo.Entity;
import com.apollo.utils.Bag;

public abstract class Node<T> extends Spatial<T> {
	private Bag<Spatial<T>> children;

	public Node() {
		children = new Bag<Spatial<T>>();
		attachChildren();
	}

	public Bag<Spatial<T>> getChildren() {
		return children;
	}

	protected void attachChildren() {
	}
	
	@Override
	public void setOwner(Entity owner) {
		super.setOwner(owner);

		for (int i = 0; children.size() > i; i++) {
			children.get(i).setOwner(owner);
		}
	}

	public void addChild(Spatial<T> spatial) {
		spatial.setOwner(owner);
		children.add(spatial);
	}

	public void addToRenderBuckets(Bag<Bag<Spatial<T>>> buckets) {
		super.addToRenderBuckets(buckets);

		for (int i = 0; children.size() > i; i++) {
			children.get(i).addToRenderBuckets(buckets);
		}
	}

	@Override
	public void initialize() {
		for (int i = 0; children.size() > i; i++) {
			children.get(i).initialize();
		}
	}

	@Override
	public void update(int delta) {
		for (int i = 0; children.size() > i; i++) {
			children.get(i).update(delta);
		}
	}
	
	@Override
	public void applyAnnotations() {
		super.applyAnnotations();
		
		for (int i = 0; children.size() > i; i++) {
			children.get(i).applyAnnotations();
		}
	}

}
