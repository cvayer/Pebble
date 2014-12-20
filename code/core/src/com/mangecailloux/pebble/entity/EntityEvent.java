package com.mangecailloux.pebble.entity;

import com.mangecailloux.pebble.event.Event;

public abstract class EntityEvent extends Event
{
	protected Entity entity;

	protected EntityEvent()
	{
		super();
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

	@Override
	public abstract void reset();
}
