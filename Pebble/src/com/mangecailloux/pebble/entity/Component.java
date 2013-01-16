package com.mangecailloux.pebble.entity;

public abstract class Component 
{
	public static final int InvalidID = -1;
	
	private Entity 	entity;
	
	public Component()
	{
		entity = null;
	}
	
	protected void setEntity(Entity _entity)
	{
		entity = _entity;
	}
	
	public Entity getEntity()
	{
		return entity;
	}
	
	public <T extends Entity> T getEntity(Class<T> type)
	{
		return type.cast(entity);
	}
	
	public abstract void update(float _fDt);
}
