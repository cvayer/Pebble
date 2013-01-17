package com.mangecailloux.pebble.entity;

public interface EntityObserver {
	public void onAddToWorld(Entity _entity);
	public void onRemoveFromWorld(Entity _entity);
}
