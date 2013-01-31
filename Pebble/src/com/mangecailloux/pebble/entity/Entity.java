/*******************************************************************************
 * Copyright 2013 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.mangecailloux.pebble.entity;

import com.mangecailloux.pebble.entity.manager.EntityGroup;
import com.mangecailloux.pebble.entity.manager.EntityGroupManager;
import com.mangecailloux.pebble.event.EventManager;

public class Entity 
{
	protected 		static int globalCounter = 0;
	
	private			int 			 id;
	private  		EntityWorld	   	 world;
	private final   ComponentSet	 components;	
	private final	EventManager	 eventManager;
	
	protected Entity()
	{
		// Entity should never be unallocated during runtime, so that should suffice to have an unique id
		id = globalCounter++;
		eventManager = new EventManager();
		components = new ComponentSet(this);
	}
	
	@Override
	public String toString()
	{
		return "Entity " + id;
	}
	
	public void info(String _string)
	{
		if(world != null)
		{
			world.getEntitiesManager().logger.info(toString() + ": " + _string);
		}
	}
	
	protected void setWorld(EntityWorld _world)
	{
		if(world != null)
			onRemoveFromWorld();
		
		world = _world;
		
		if(world != null)
			onAddToWorld();
	}
	
	public EntityWorld getWorld()
	{
		return world;
	}
	
	protected EventManager getEventManager()
	{
		return eventManager;
	}
	
	protected void initComponents(EntityArchetype _archetype)
	{
		components.init(_archetype);
	}
	
	public <C extends Component> C getComponent(Class<C> _type)
	{
		return components.getComponent(_type);
	}
	
	public <M extends EntityManager> M getManager(Class<M> type)
	{
		if(world != null)
			return  world.getManager(type);
		return null;
	}
	
	public void addToGroup(EntityGroup _group)
	{
		if(world == null)
			throw new RuntimeException("Entity::addToGroup : entity has not been added to world yet");
		
		EntityGroupManager manager = world.getManager(EntityGroupManager.class);
		if(manager != null)
			manager.addToGroup(this, _group);
	}
	
	public void removeFromGroup(EntityGroup _group)
	{
		if(world == null)
			throw new RuntimeException("Entity::removeFromGroup : entity has already been added removed from the world");

		EntityGroupManager manager = world.getManager(EntityGroupManager.class);
		if(manager != null)
			manager.removeFromGroup(this, _group);
	}
	
	private void onAddToWorld()
	{
		eventManager.setListener(world.getEventManagerListener());
		components.onAddToWorld();
	}
	
	private void onRemoveFromWorld()
	{
		components.onRemoveFromWorld();
		eventManager.unregisterAllEventHandlers();
		eventManager.setListener(null);
	}
	
	public <E extends EntityEvent> E getEvent(Class<E> _type)
	{
		E event = _type.cast(eventManager.getEvent(_type));
		event.setEntity(this);
		return event;
	}
}
