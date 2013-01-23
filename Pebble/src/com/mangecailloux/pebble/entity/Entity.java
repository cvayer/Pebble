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

import com.badlogic.gdx.utils.Pools;
import com.mangecailloux.pebble.entity.manager.EntityGroup;
import com.mangecailloux.pebble.entity.manager.EntityGroupManager;

public class Entity 
{
	protected 		static int globalCounter = 0;
	
	private			int 			 id;
	private  		EntityWorld	   	 world;
	private final   ComponentSet	 components;
	
	protected Entity()
	{
		// Entity should never be unallocated during runtime, so that should suffice to have an unique id
		id = globalCounter++;
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
			world.getEntityManager().logger.info(toString() + ": " + _string);
		}
	}
	
	protected void setWorld(EntityWorld _world)
	{
		world = _world;
	}
	
	public EntityWorld getWorld()
	{
		return world;
	}
	
	protected void initComponents(EntityArchetype _archetype)
	{
		components.init(_archetype);
	}
	
	public <C extends Component> C getComponent(Class<C> _type)
	{
		return components.getComponent(_type);
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
	
	protected void onAddToWorld()
	{
		components.onAddToWorld();
	}
	
	protected void onRemoveFromWorld()
	{
		components.onRemoveFromWorld();
	}
	
	protected <E extends EntityEvent> E getEvent(Class<E> _type)
	{
		E event = Pools.obtain(_type);
		event.setEntity(this);
		return event;
	}
	
	protected void sendEvent(EntityEvent _event)
	{
		if(world != null)
		{
			world.getEntityManager().sendEvent(this, _event);
		}
		
		components.sendEvent(_event);
		_event.setEntity(null);
		Pools.free(_event);
	}
}
