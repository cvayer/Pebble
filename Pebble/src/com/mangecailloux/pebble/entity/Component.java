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
import com.mangecailloux.pebble.updater.UpdatersHandler;

public abstract class Component
{
	private Entity 	entity;
	private final UpdatersHandler updatersHandler;
	
	private final Array<EntityEventHandler<?>> eventHandlers;
	
	public Component()
	{
		entity = null;
		updatersHandler = new UpdatersHandler();
		
		eventHandlers = new Array<EntityEventHandler<?>>(false, 2);
	}
	
	protected UpdatersHandler getUpdatersHandler() {
		return updatersHandler;
	}

	protected void setEntity(Entity _entity)
	{
		entity = _entity;
	}
	
	public Entity getEntity()
	{
		return entity;
	}
	
	public <T extends Entity> T getEntity(Class<T> type)
	{
		return type.cast(entity);
	}
	
	public <C extends Component> C getComponent(Class<C> _type)
	{
		if(entity != null)
			return entity.getComponent(_type);
		return null;
	}
	
	public <M extends EntityWorldManager> M getManager(Class<M> type)
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
	
	protected void addEventHandler(EntityEventHandler<?> _handler)
	{
		if(_handler != null && eventHandlers.contains(_handler, true))
		{
			eventHandlers.add(_handler);
			if(entity != null)
			{
				entity.registerEventHandler(_handler);
			}
		}
	}
	
	protected void registerEventHandlers()
	{
		if(entity == null)
			return;
		
		for(int i = 0; i < eventHandlers.size; ++i)
		{
			entity.registerEventHandler(eventHandlers.get(i));
		}
	}
			
	protected void onAddToEntity()				{}
	
	protected void onRemoveFromEntity()			{}
	
	protected void onAddToWorld()				{}
	
	protected void onRemoveFromWorld() 			{}
}
