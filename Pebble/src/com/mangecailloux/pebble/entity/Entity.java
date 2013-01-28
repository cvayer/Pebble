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

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.ObjectMap.Values;
import com.mangecailloux.pebble.entity.manager.EntityGroup;
import com.mangecailloux.pebble.entity.manager.EntityGroupManager;

public class Entity 
{
	protected 		static int globalCounter = 0;
	
	private			int 			 id;
	private  		EntityWorld	   	 world;
	private final   ComponentSet	 components;
	
	private final	ObjectMap<Class<?>, Array<EntityEventHandler<?>>> 	eventsHandlerByType;
	private final 	Pool<Array<EntityEventHandler<?>>>					eventsHandlerArrayPool;
	
	protected Entity()
	{
		// Entity should never be unallocated during runtime, so that should suffice to have an unique id
		id = globalCounter++;
		
		eventsHandlerByType = new ObjectMap<Class<?>, Array<EntityEventHandler<?>>>(4);
		eventsHandlerArrayPool = new Pool<Array<EntityEventHandler<?>>>()
		{
			@Override
			protected Array<EntityEventHandler<?>> newObject() {
				return new Array<EntityEventHandler<?>>(false, 4);
			}
		};
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
	
	public <M extends EntityWorldManager> M getManager(Class<M> type)
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
	
	protected void onAddToWorld()
	{
		components.onAddToWorld();
	}
	
	protected void onRemoveFromWorld()
	{
		components.onRemoveFromWorld();
		unregisterAllEventHandlers();
	}
	
	public <E extends EntityEvent> E getEvent(Class<E> _type)
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
		
		Array<EntityEventHandler<?>> handlers = eventsHandlerByType.get(_event.getClass());
		
		if(handlers != null)
		{
			for(int i=0; i < handlers.size; ++i)
			{
				handlers.get(i).handle(_event);
			}
		}
		
		_event.setEntity(null);
		Pools.free(_event);
	}
	
	protected void registerEventHandler(EntityEventHandler<?> _handler)
	{
		if(_handler == null)
			return;
		
		Array<EntityEventHandler<?>> handlers = eventsHandlerByType.get(_handler.getType());
		if(handlers == null)
			handlers = eventsHandlerArrayPool.obtain();
		
		if(!handlers.contains(_handler, true))
		{
			handlers.add(_handler);
		}
	}
	
	protected void unregisterAllEventHandlers()
	{
		Values<Array<EntityEventHandler<?>>> values = eventsHandlerByType.values();
		
		while(values.hasNext())
    	{
			Array<EntityEventHandler<?>> handlers = values.next();
			handlers.clear();
			eventsHandlerArrayPool.free(handlers);
    	}
		eventsHandlerByType.clear();
	}
}
