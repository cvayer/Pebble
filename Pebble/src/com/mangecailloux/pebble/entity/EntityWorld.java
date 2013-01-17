package com.mangecailloux.pebble.entity;

public class EntityWorld 
{
	private final EntityManager entityManager;
	
	public EntityWorld()
	{
		entityManager = new EntityManager(this);
	}
	
	public EntityManager getEntityManager()
	{
		return entityManager;
	}
	
	public void update(float _dt)
	{
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
}
