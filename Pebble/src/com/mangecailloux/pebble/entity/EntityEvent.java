package com.mangecailloux.pebble.entity;

import com.badlogic.gdx.utils.Pool.Poolable;

public abstract class EntityEvent implements Poolable
{
	public static final int INVALID_TYPE = -1;
	
	protected Entity entity;
	protected final int type; // Use the type is you want to make fast type check ( instead of using reflection )
	
	protected EntityEvent(int _type)
	{
		entity = null;
		type = _type;
	}
	
	protected EntityEvent()
	{
		this(INVALID_TYPE);
	}
	
	public int type()
	{
		return type;
	}
	
	public void setEntity(Entity _entity)
	{
		entity = _entity;
	}
	
	public void send()
	{
		if(entity == null)
			throw new RuntimeException("EntityEvent::send -> entity must not be null");
		
		entity.sendEvent(this);
	}

	@Override
	public abstract void reset();
}
