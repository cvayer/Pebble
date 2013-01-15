package com.mangecailloux.pebble.entity;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;

public class EntityManager 
{
	private final Array<Entity> entities;
	
	protected EntityManager()
	{
		entities = new Array<Entity>(false, 8);
	}
	
	public <T extends Entity> T NewEntity(Class<T> type)
	{
		 T entity = Pools.obtain(type);
		 registerEntity(entity);
		 return entity;
	}
	
	public void freeEntity(Entity _entity)
	{
		unregisterEntity(_entity);
		Pools.free(_entity);
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
			entities.removeValue(_entity, true);
		}
	}
	
	public void update(float _fDt)
	{
		for(int i = 0; i < entities.size; ++i)
		{
			entities.get(i).update(_fDt);
		}
	}
}
