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
}
