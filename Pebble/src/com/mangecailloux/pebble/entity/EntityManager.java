package com.mangecailloux.pebble.entity;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;

public class EntityManager 
{
	private final EntityWorld	   world;
	private final Array<Entity> entities;
	private final Array<Entity> toDelete;
	
	protected EntityManager(EntityWorld _world)
	{
		world = _world;
		entities = new Array<Entity>(false, 8);
		toDelete = new Array<Entity>(false, 8);
	}
	
	public <T extends Entity> T NewEntity(Class<T> type)
	{
		 T entity = Pools.obtain(type);
		 entity.setWorld(world);
		 registerEntity(entity);
		 return entity;
	}
	
	public Entity NewEntity()
	{
		return NewEntity(Entity.class);
	}
	
	public void freeEntity(Entity _entity)
	{
		unregisterEntity(_entity);
	}
	
	private void registerEntity(Entity _entity)
	{
		if(_entity != null && !entities.contains(_entity, true))
		{
			entities.add(_entity);
		}
	}
	
	private void unregisterEntity(Entity _entity)
	{
		if(_entity != null)
		{
			toDelete.add(_entity);
		}
	}
	
	private void deleteEntities() 
	{
		for(int i = 0;  i < toDelete.size; ++i) {
			Entity e = toDelete.get(i);
			
			entities.removeValue(e, true);
			Pools.free(e);
		}
		toDelete.clear();
	}
	
	protected void update(float _fDt)
	{
		if(toDelete.size > 0)
		{
			deleteEntities();
		}
		
		for(int i = 0; i < entities.size; ++i)
		{
			entities.get(i).update(_fDt);
		}
	}
}
