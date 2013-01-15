package com.apollo.managers;

import java.util.HashMap;
import java.util.Map;

import com.apollo.Entity;

public class TagManager extends Manager {
	private Map<String, Entity> entityByTag;

	public TagManager() {
		entityByTag = new HashMap<String, Entity>();
	}

	public void register(String tag, Entity e) {
		entityByTag.put(tag, e);
	}

	public void unregister(String tag) {
		entityByTag.remove(tag);
	}

	public boolean isRegistered(String tag) {
		return entityByTag.containsKey(tag);
	}

	public Entity getEntity(String tag) {
		return entityByTag.get(tag);
	}
	
	@Override
	public void removed(Entity e) {
		entityByTag.values().remove(e);
	}
}
