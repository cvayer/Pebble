package com.mangecailloux.pebble.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;

public class ContactMultiplexer implements ContactListener
{
	private final Array<ContactListener> listeners;
	
	public ContactMultiplexer()
	{
		listeners = new Array<ContactListener>(false, 4);
	}
	
	public void addListener(ContactListener _listener)
	{
		if(_listener != null)
		{
			listeners.add(_listener);
		}
	}
	
	public void removeListener(ContactListener _listener)
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
	public void beginContact(Contact contact) {
		for(int i=0; i<listeners.size; ++i )
		{
			listeners.get(i).beginContact(contact);
		}
	}

	@Override
	public void endContact(Contact contact) {	
		for(int i=0; i<listeners.size; ++i )
		{
			listeners.get(i).endContact(contact);
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		for(int i=0; i<listeners.size; ++i )
		{
			listeners.get(i).preSolve(contact, oldManifold);
		}
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		for(int i=0; i<listeners.size; ++i )
		{
			listeners.get(i).postSolve(contact, impulse);
		}
	}
}
