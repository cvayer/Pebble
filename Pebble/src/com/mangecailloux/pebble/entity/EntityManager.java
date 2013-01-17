package com.mangecailloux.pebble.entity;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;

public class EntityManager 
{
	private final EntityWorld	   		world;
	private final Array<Entity> 		entities;
	private final Array<Entity> 		toDelete;
	private final Array<Entity> 		toAdd;
	private final Array<EntityObserver> obervers;
	
	protected EntityManager(EntityWorld _world)
	{
		world = _world;
		entities = new Array<Entity>(false, 8);
		toDelete = new Array<Entity>(false, 4);
		toAdd = new Array<Entity>(false, 4);
		obervers = new Array<EntityObserver>(false, 4);
	}
	
	protected Entity addEntity(EntityArchetype _archetype)
	{
		if(_archetype == null)
			throw new RuntimeException("EntityManager::NewEntity -> Archetype cannot be null");
		
		if(_archetype.getComponentTypesCount() == 0)
			throw new RuntimeException("EntityManager::NewEntity -> Archetype cannot be empty");
			
		 Entity entity = Pools.obtain(Entity.class);
		 entity.setWorld(world);
		 entity.initComponents(_archetype);
		 
		 toAdd.add(entity);
		 return entity;
	}
	
	protected void removeEntity(Entity _entity)
	{
		if(_entity != null)
		{
			_entity.deinitComponents();
			_entity.setWorld(null);
			_entity.setDeletePending(true);
			toDelete.add(_entity);
		}
	}
	
	protected void addObserver(EntityObserver _observer)
	{
		if(_observer != null && !obervers.contains(_observer, true))
		{
			obervers.add(_observer);
		}
	}
	
	protected void removeObserver(EntityObserver _observer)
	{
		if(_observer != null)
		{
			obervers.removeValue(_observer, true);
		}
	}
	
	private void deleteEntities() 
	{
		for(int i = 0;  i < toDelete.size; ++i) {
			Entity e = toDelete.get(i);
			
			entities.removeValue(e, true);
			e.setDeletePending(false);
			
			e.onRemoveFromWorld();
			for(int o =0; o < obervers.size; ++o)
			{
				obervers.get(o).onRemoveFromWorld(e);
			}
			
			Pools.free(e);
		}
		toDelete.clear();
	}
	
	private void addEntities() 
	{
		for(int i = 0;  i < toAdd.size; ++i) {
			Entity e = toAdd.get(i);
			
			entities.add(e);
			
			e.onAddToWorld();
			for(int o =0; o < obervers.size; ++o)
			{
				obervers.get(o).onAddToWorld(e);
			}
		}
		toAdd.clear();
	}
	
	protected void update(float _fDt)
	{
		if(toDelete.size > 0)
		{
			deleteEntities();
		}
		
		if(toAdd.size > 0)
		{
			addEntities();
		}
		
		for(int i = 0; i < entities.size; ++i)
		{
			Entity e = entities.get(i);
			if(!e.isDeletePending())
				e.update(_fDt);
		}
	}
}
