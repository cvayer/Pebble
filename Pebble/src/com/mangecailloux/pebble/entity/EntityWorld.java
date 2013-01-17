package com.mangecailloux.pebble.entity;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class EntityWorld 
{
	private final EntityManager entityManager;
	
	private final Array<EntityWorldManager> managers;
	private final Array<EntityWorldManager> toAdd;
	private final ObjectMap<Class<? extends EntityWorldManager>, EntityWorldManager> managersPerType;
	
	public EntityWorld()
	{
		entityManager = new EntityManager(this);
		
		managers = new Array<EntityWorldManager>(false, 4);
		toAdd = new Array<EntityWorldManager>(false, 4);
		managersPerType = new ObjectMap<Class<? extends EntityWorldManager>, EntityWorldManager>(4);
	}
	
	public EntityManager getEntityManager()
	{
		return entityManager;
	}
	
	public void update(float _dt)
	{
		if(toAdd.size > 0)
		{
			addManagers();
		}
		
		for(int i = 0; i < managers.size; ++i)
		{
			managers.get(i).update(_dt);
		}
		
		// Update entities
		entityManager.update(_dt);
	}
	
	public Entity addEntity(EntityArchetype _archetype)
	{
		 return entityManager.addEntity(_archetype);
	}
	
	public void removeEntity(Entity _entity)
	{
		entityManager.removeEntity(_entity);
	}
	
	public void addManager(EntityWorldManager _manager)
	{
		if(_manager != null && !managers.contains(_manager, true))
		{
			toAdd.add(_manager);
		}
	}
	
	private void addManagers()
	{
		for(int i = 0;  i < toAdd.size; ++i)
		{
			EntityWorldManager manager = toAdd.get(i);
			
			managers.add(manager);
			managersPerType.put(manager.getClass(), manager);
			entityManager.addObserver(manager);
		}
		toAdd.clear();
	}
	
	public <M extends EntityWorldManager> M getManager(Class<M> type) {
		return type.cast(managersPerType.get(type));
	}
}
