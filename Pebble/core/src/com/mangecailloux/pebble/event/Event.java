package com.mangecailloux.pebble.event;

import com.badlogic.gdx.utils.Pool.Poolable;

public abstract class Event implements Poolable
{
	private EventManager manager;

	protected Event()
	{
		manager = null;
	}
	
	protected void setManager(EventManager _manager)
	{
		manager = _manager;
	}
	
	public void send()
	{
		if(manager == null)
			throw new RuntimeException("Event::send -> manager must not be null");
		
		manager.sendEvent(this);
	}

	@Override
	public abstract void reset();
}
