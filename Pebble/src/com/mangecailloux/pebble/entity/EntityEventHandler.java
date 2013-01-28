package com.mangecailloux.pebble.entity;

public abstract class EntityEventHandler<T extends EntityEvent> 
{
	private final Class<T> type;
	
	public EntityEventHandler(Class<T> _type)
	{
		type = _type;
	}

	public Class<T> getType() 
	{
		return type;
	}

	public void handle(EntityEvent _event) 
	{
		onHandle(type.cast(_event));
	}
	
	public abstract void onHandle(T _event);
}
