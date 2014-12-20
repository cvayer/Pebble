package com.mangecailloux.pebble.event;

public abstract class EventHandler<T extends Event> 
{
	private final Class<T> type;
	
	public EventHandler(Class<T> _type)
	{
		type = _type;
	}

	public Class<T> getType() 
	{
		return type;
	}

	public void handle(Event _event) 
	{
		onHandle(type.cast(_event));
	}
	
	public abstract void onHandle(T _event);
}
