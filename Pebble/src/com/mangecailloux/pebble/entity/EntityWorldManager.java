package com.mangecailloux.pebble.entity;

public class EntityWorldManager implements EntityObserver
{
	private EntityWorld	    world;
	
	public EntityWorldManager()
	{
		
	}
	
	protected void setWorld(EntityWorld _world)
	{
		world = _world;
	}
	
	public EntityWorld getWorld()
	{
		return world;
	}
	
	public <M extends EntityWorldManager> M getManager(Class<M> type)
	{
		if(world != null)
			return world.getManager(type);
		return null;
	}

	@Override
	public	void onAddToWorld(Entity _entity) {
		
	}

	@Override
	public	void onRemoveFromWorld(Entity _entity) {
		
	}
	
	protected void update(float _dt)
	{
		
	}
	
}
