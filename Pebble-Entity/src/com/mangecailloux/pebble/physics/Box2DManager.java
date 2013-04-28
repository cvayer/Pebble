package com.mangecailloux.pebble.physics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.rube.RubeScene;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pools;
import com.mangecailloux.pebble.entity.Entity;
import com.mangecailloux.pebble.entity.EntityManager;
import com.mangecailloux.pebble.updater.Updater;

public class Box2DManager extends EntityManager
{
	private 	  World world;
	private final ContactRecorder recorder;
	
	private final ObjectMap<String, Body> 		bodiesByName;
	private final ObjectMap<String, Fixture> 	fixturesByName;
	private final ObjectMap<String, Joint> 		jointsByName;
	
	public Box2DManager()
	{
		this(null);
	}
	
	public Box2DManager(RubeScene scene)
	{
		super();
		
		bodiesByName 	= new ObjectMap<String, Body>();
		fixturesByName 	= new ObjectMap<String, Fixture>();
		jointsByName 	= new ObjectMap<String, Joint>();
		
		recorder = new ContactRecorder();
		
		initFromRubeScene(scene);
	}
	
	public void initFromRubeScene(RubeScene scene)
	{
		if(scene != null)
		{
			setWorld(scene.world);
			bodiesByName.putAll(scene.getObjectsByName(Body.class));
			fixturesByName.putAll(scene.getObjectsByName(Fixture.class));
			jointsByName.putAll(scene.getObjectsByName(Joint.class));
		}
	}
	
	public void setWorld(World _world)
	{
		if(_world != world)
		{
			if(world != null)
			{
				world.setContactListener(null);
			}
			
			world = _world;
			
			if(world != null)
			{
				world.setContactListener(recorder);
			}
		}
	}
	
	public World getBox2DWorld()
	{
		return world;
	}
	
	@Override
	public void onAddToWorld(Entity _entity) {
		
		
	}

	@Override
	public void onRemoveFromWorld(Entity _entity) {
	
		
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> type, String name) {
		
		if(name == null || type == null)
			return null;
		
		if(type == Body.class)
		{
			return (T) bodiesByName.get(name, null);
		}
		else if(type == Fixture.class)
		{
			return (T) fixturesByName.get(name, null);
		}
		else if(type == Joint.class)
		{
			return (T) jointsByName.get(name, null);
		}

		return null;
	}
	

	@Override
	public void dispose() {
		world.dispose();
	}
	
	public void addDelayedContactListener(DelayedContactListener _listener)
	{
		recorder.addDelayedContactListener(_listener);
	}
	
	public void removeDelayedContactListener(DelayedContactListener _listener)
	{
		recorder.removeDelayedContactListener(_listener);
	}
	
	public void clearDelayedContactListeners()
	{
		recorder.clearDelayedContactListeners();
	}
	
	public void runSimulation(float _dt) {
		
		if(world != null)
		{
			world.step(_dt, 8, 3);
			recorder.processDelayedContacts();
		}
	}
	
	//----------------------------------------------------------------
	// ContactRecorder
	public class ContactRecorder implements ContactListener
	{
		private final DelayedContactMultiplexer	delayedContactListeners;
		private final Array<DelayedContact>		delayedContacts;
		
		public ContactRecorder()
		{
			delayedContactListeners = new DelayedContactMultiplexer();
			delayedContacts = new Array<DelayedContact>(false, 16);
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
		
		@Override
		public void beginContact(Contact contact) {
			
		}

		@Override
		public void endContact(Contact contact) {
			DelayedContact delayed = Pools.obtain(DelayedContact.class);
			delayed.setFromContact(contact);
			delayedContacts.add(delayed);
		}

		@Override
		public void preSolve(Contact contact, Manifold oldManifold) {
			
		}

		@Override
		public void postSolve(Contact contact, ContactImpulse impulse) {
			
		}
		
		public void	processDelayedContacts()
		{
			if(delayedContacts.size != 0)
			{
				for(int i=0; i < delayedContacts.size ; ++i)
				{
					DelayedContact delayed = delayedContacts.get(i);
					delayedContactListeners.onContact(delayed);
					Pools.free(delayed);
				}
				delayedContacts.clear();
			}
		}
	}
}
