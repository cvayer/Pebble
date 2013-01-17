package com.mangecailloux.pebble.entity;

public class EntityWorldManager implements EntityObserver
{
	private EntityWorld	    world;
	
	public EntityWorldManager()
	{
		
	}
	
	public void setWorld(EntityWorld _world)
	{
		world = _world;
	}
	
	public EntityWorld getWorld()
	{
		return world;
	}

	@Override
	public void onAddToWorld(Entity _entity) {
		
	}

	@Override
	public void onRemoveFromWorld(Entity _entity) {
		
	}
	
	public void update(float _dt)
	{
		
	}
	
}
