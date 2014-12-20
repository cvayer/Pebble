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

import com.mangecailloux.pebble.event.EventHandler;
import com.mangecailloux.pebble.event.EventHandlers;
import com.mangecailloux.pebble.updater.Updater;
import com.mangecailloux.pebble.updater.Updaters;

public abstract class Component
{
	private 	  Entity 					entity;
	private final Updaters 			updatersHandler;
	private final EventHandlers 		eventHandlers;
	
	public Component()
	{
		entity = null;
		updatersHandler = new Updaters();
		
		eventHandlers = new EventHandlers();
	}

	protected void setEntity(Entity _entity)
	{		
		entity = _entity;
	}
	
	protected void onSetWorld(EntityWorld _world)
	{
		if(_world != null)
		{
			updatersHandler.setManager(_world.getUpdaterManager());
			eventHandlers.setManager(entity.getEventManager());
			onAddToWorld();
		}
		else // remove from world
		{
			//TODO put that in one function
			onRemoveFromWorld();
			eventHandlers.setManager(null);
			updatersHandler.setManager(null);
		}
	}
	
	public Entity getEntity()
	{
		return entity;
	}
	
	public <C extends Component> C getComponent(Class<C> _type)
	{
		if(entity != null)
			return entity.getComponent(_type);
		return null;
	}
	
	public <M extends EntityManager> M getManager(Class<M> type)
	{
		if(entity != null)
			return  entity.getManager(type);
		return null;
	}
	
	public <E extends EntityEvent> E getEvent(Class<E> _type)
	{
		if(entity != null)
			return entity.getEvent(_type);
		return null;
	}
	
	protected void addEventHandler(EventHandler<?> _handler)
	{
		eventHandlers.addEventHandler(_handler);
	}
	
	protected void addUpdater(Updater _updater)
	{
		updatersHandler.addUpdater(_updater);
	}
	
	protected abstract void onAddToWorld();
	
	protected abstract void onRemoveFromWorld();
}
