package com.mangecailloux.pebble.entity.manager;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;
import com.mangecailloux.pebble.entity.Entity;
import com.mangecailloux.pebble.entity.EntityWorldManager;

public class EntityGroupManager extends EntityWorldManager
{
	private final ObjectMap<EntityGroup, Array<Entity>> groups;
	private final ObjectMap<Entity, Array<EntityGroup>> groupsByEntity;
	
	private final Pool<Array<EntityGroup>>	entityGroupArrayPool;
	
	public EntityGroupManager()
	{
		super();
		
		groups = new ObjectMap<EntityGroup, Array<Entity>>(4);
		groupsByEntity = new ObjectMap<Entity, Array<EntityGroup>>(4);
				
		entityGroupArrayPool = new Pool<Array<EntityGroup>>()
		{
			@Override
			protected Array<EntityGroup> newObject() {
				return new Array<EntityGroup>(false, 4);
			}
			
		};
	}
	
	public Array<Entity> getGroup(EntityGroup _group)
	{
		Array<Entity> group = groups.get(_group);
		return group;
	}
	
	public void addToGroup(Entity _entity, EntityGroup _group) 
	{
		register(_entity, _group);
	}
	
	public void removeFromGroup(Entity _entity, EntityGroup _group)
	{
		unregisterFromGroup(_entity, _group);
	}
	
	public void removeFromAllGroups(Entity _entity)
	{
		unregisterFromAllGroups(_entity);
	}

	private void register(Entity _entity, EntityGroup _group) 
	{
		if(_entity == null)
			throw new IllegalArgumentException("EntityGroupManager::register : _entity cannot be null");
		
		if(_group == null)
			throw new IllegalArgumentException("EntityGroupManager::register : _group cannot be null");
		
		Array<EntityGroup> groupsForThisEntity = groupsByEntity.get(_entity);
		if(groupsForThisEntity == null)
		{
			groupsForThisEntity = entityGroupArrayPool.obtain();
			groupsByEntity.put(_entity, groupsForThisEntity);
		}
		groupsForThisEntity.add(_group);
		
		Array<Entity> group = groups.get(_group);
		if(group == null) 
		{
			group = new Array<Entity>(false, 4);
			groups.put(_group, group);
		}
		group.add(_entity);
	}
	
	@Override
	public	void onRemoveFromWorld(Entity _entity) {
		unregisterFromAllGroups(_entity);
	}

	private void unregisterFromAllGroups(Entity _entity) {
		
		if(_entity == null)
			throw new IllegalArgumentException("EntityGroupManager::register : _entity cannot be null");
		
		Array<EntityGroup> groupsForThisEntity = groupsByEntity.remove(_entity);
		
		if(groupsForThisEntity != null)
		{
			for(int i=0; i < groupsForThisEntity.size; ++i)
			{
				Array<Entity> group = groups.get(groupsForThisEntity.get(i));
				if(group != null) 
				{
					group.removeValue(_entity, true);
				}
			}
			entityGroupArrayPool.free(groupsForThisEntity);
		}
	}
	
	private void unregisterFromGroup(Entity _entity, EntityGroup _group)
	{
		if(_entity == null)
			throw new IllegalArgumentException("EntityGroupManager::register : _entity cannot be null");
		
		if(_group == null)
			throw new IllegalArgumentException("EntityGroupManager::register : _group cannot be null");
		
		Array<Entity> group = groups.get(_group);
		if(group != null) 
		{
			group.removeValue(_entity, true);
		}
		
		Array<EntityGroup> groupsForThisEntity = groupsByEntity.get(_entity);
		if(groupsForThisEntity != null)
		{
			groupsForThisEntity.removeValue(_group, true);
		}
		
		if(groupsForThisEntity.size == 0)
		{
			groupsByEntity.remove(_entity);
			entityGroupArrayPool.free(groupsForThisEntity);
		}
	}
}
