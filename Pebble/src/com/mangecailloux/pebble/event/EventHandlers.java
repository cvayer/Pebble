package com.mangecailloux.pebble.event;

import com.badlogic.gdx.utils.Array;

public class EventHandlers 
{
	private		 	EventManager manager;
	private final 	Array<EventHandler<?>> eventHandlers;
	
	public EventHandlers()
	{
		eventHandlers = new Array<EventHandler<?>>(false, 2);
	}

	public void setManager(EventManager _manager) 
	{
		manager = _manager;
		
		if(manager != null)
		{
			registerEventHandlers();
		}
	}
	
	public void addEventHandler(EventHandler<?> _handler)
	{
		if(_handler != null && !eventHandlers.contains(_handler, true))
		{
			eventHandlers.add(_handler);
			if(manager != null)
			{
				manager.registerEventHandler(_handler);
			}
		}
	}
	
	private void registerEventHandlers()
	{
		if(manager == null)
			return;
		
		for(int i = 0; i < eventHandlers.size; ++i)
		{
			manager.registerEventHandler(eventHandlers.get(i));
		}
	}
}
