package com.mangecailloux.pebble.physics;

import com.badlogic.gdx.utils.Array;

public class DelayedContactMultiplexer implements DelayedContactListener
{
	private final Array<DelayedContactListener> listeners;
	
	public DelayedContactMultiplexer()
	{
		listeners = new Array<DelayedContactListener>(false, 4);
	}
	
	public void addListener(DelayedContactListener _listener)
	{
		if(_listener != null)
		{
			listeners.add(_listener);
		}
	}
	
	public void removeListener(DelayedContactListener _listener)
	{
		if(_listener != null)
		{
			listeners.removeValue(_listener, true);
		}
	}
	
	public void clear()
	{
		listeners.clear();
	}

	@Override
	public void onContact(DelayedContact contact) {
		for(int i=0; i<listeners.size; ++i )
		{
			listeners.get(i).onContact(contact);
		}
	}
}
