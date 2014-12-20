package com.mangecailloux.pebble.event;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.ObjectMap.Values;

public class EventManager 
{
	private final	ObjectMap<Class<?>, Array<EventHandler<?>>> 	eventsHandlerByType;
	private final 	Pool<Array<EventHandler<?>>>					eventsHandlerArrayPool;
	private 		EventManagerListener							listener;
	
	public EventManager()
	{
		eventsHandlerByType = new ObjectMap<Class<?>, Array<EventHandler<?>>>(4);
		eventsHandlerArrayPool = new Pool<Array<EventHandler<?>>>()
		{
			@Override
			protected Array<EventHandler<?>> newObject() {
				return new Array<EventHandler<?>>(false, 4);
			}
		};
	}

	public void setListener(EventManagerListener _listener)
	{
		listener = _listener;
	}

	public <E extends Event> E getEvent(Class<E> _type)
	{
		E event = Pools.obtain(_type);
		event.setManager(this);
		return event;
	}
	
	public void handleEvent(Event _event)
	{
		Array<EventHandler<?>> handlers = eventsHandlerByType.get(_event.getClass());
		
		if(handlers != null)
		{
			for(int i=0; i < handlers.size; ++i)
			{
				handlers.get(i).handle(_event);
			}
		}
		
		if(listener != null)
			listener.onEvent(_event);
	}
	
	protected void sendEvent(Event _event)
	{
		handleEvent(_event);
		_event.setManager(null);
		Pools.free(_event);
	}
	
	public void registerEventHandler(EventHandler<?> _handler)
	{
		if(_handler == null)
			return;
		
		Array<EventHandler<?>> handlers = eventsHandlerByType.get(_handler.getType());
		if(handlers == null)
		{
			handlers = eventsHandlerArrayPool.obtain();
			eventsHandlerByType.put(_handler.getType(), handlers);
		}
		
		if(!handlers.contains(_handler, true))
		{
			handlers.add(_handler);
		}
	}
	
	public void unregisterAllEventHandlers()
	{
		Values<Array<EventHandler<?>>> values = eventsHandlerByType.values();
		
		while(values.hasNext())
    	{
			Array<EventHandler<?>> handlers = values.next();
			handlers.clear();
			eventsHandlerArrayPool.free(handlers);
    	}
		eventsHandlerByType.clear();
	}
	
	public static interface EventManagerListener
	{
		void onEvent(Event _event);
	}
	
}
