package com.mangecailloux.pebble.entity;

public interface EntityObserver {
	void onAddToWorld(Entity _entity);
	void onRemoveFromWorld(Entity _entity);
}
