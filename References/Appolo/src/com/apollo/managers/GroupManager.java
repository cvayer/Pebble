package com.apollo.managers;

import java.util.HashMap;
import java.util.Map;

import com.apollo.Entity;
import com.apollo.utils.Bag;

public class GroupManager extends Manager {
	private Map<Group,Bag<Entity>> groups;
	private Map<Entity,Group> groupByEntity;
	
	public GroupManager() {
		groups = new HashMap<Group, Bag<Entity>>();
		groupByEntity = new HashMap<Entity, Group>();
	}
	
	public Bag<Entity> getEntityGroup(Group group) {
		Bag<Entity> bag = groups.get(group);
		if(bag == null) {
			bag = new Bag<Entity>();
			groups.put(group, bag);
		}
		return bag;
	}
	
	public void setGroup(Entity e, Group group) {
		removeIfPresent(e);
		insertToGroup(e, group);
	}

	private void insertToGroup(Entity e, Group group) {
		groupByEntity.put(e,group);
		Bag<Entity> bag = groups.get(group);
		if(bag == null) {
			bag = new Bag<Entity>();
			groups.put(group, bag);
		}
		bag.add(e);
	}
	
	@Override
	public void removed(Entity e) {
		removeIfPresent(e);
	}

	private void removeIfPresent(Entity e) {
		Group g = groupByEntity.remove(e);
		if(g != null) {
			Bag<Entity> group = groups.get(g);
			if(group != null) {
				group.remove(e);
			}
		}
	}
	
}
