package com.mangecailloux.pebble.physics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.mangecailloux.pebble.entity.Component;

public class Box2DComponent extends Component
{
	private Box2DManager	  				manager;
	private final Array<Body> 				bodies;
	private final ObjectMap<String, Body> 	bodiesByName;
	
	private final DelayedContactMultiplexer		delayedContactListeners;
	
	public Box2DComponent(){
		bodies = new Array<Body>(false, 2);
		bodiesByName = new ObjectMap<String, Body>(2);
		delayedContactListeners = new DelayedContactMultiplexer();
	}
	
	public void addBody(Body _body)
	{
		internalAddBody(_body);
	}
	
	public void addBody(Body _body, String _name)
	{
		if(internalAddBody(_body))
		{
			bodiesByName.put(_name, _body);
		}
	}
	
	public void removeBody(Body _body)
	{
		bodies.removeValue(_body, true);
		bodiesByName.remove(bodiesByName.findKey(_body, true));
	}
	
	public void clearBodies()
	{
		bodies.clear();
		bodiesByName.clear();
	}
	
	public Body getBody(String _name)
	{
		return bodiesByName.get(_name, null);
	}
	
	public int getBodyCount()
	{
		return bodies.size;
	}
	
	public Array<Body> getBodies()
	{
		return bodies;
	}
	
	public void addDelayedContactListener(DelayedContactListener _listener)
	{
		delayedContactListeners.addListener(_listener);
	}
	
	public void removeDelayedContactListener(DelayedContactListener _listener)
	{
		delayedContactListeners.removeListener(_listener);
	}
	
	public void clearDelayedContactListeners()
	{
		delayedContactListeners.clear();
	}
	
	private boolean internalAddBody(Body _body)
	{
		if(!bodies.contains(_body, true))
		{
			bodies.add(_body);
			return true;
		}
		return false;
	}
	
	private void setManager(Box2DManager _manager)
	{
		if(_manager != manager)
		{
			if(manager != null)
			{
				manager.removeDelayedContactListener(delayedContactListeners);
			}
			
			manager = _manager;
			
			if(manager != null)
			{
				manager.addDelayedContactListener(delayedContactListeners);
			}
		}
	}
	
	@Override
	protected void onAddToWorld() {
		setManager(getManager(Box2DManager.class));
	}

	@Override
	protected void onRemoveFromWorld() {
		clearBodies();
		clearDelayedContactListeners();
		setManager(null);
	}
}
