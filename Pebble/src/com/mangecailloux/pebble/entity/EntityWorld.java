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
import com.mangecailloux.pebble.entity.manager.EntityGroupManager;
import com.mangecailloux.pebble.entity.manager.EntityTagManager;
import com.mangecailloux.pebble.event.Event;
import com.mangecailloux.pebble.event.EventManager;
import com.mangecailloux.pebble.event.EventManager.EventManagerListener;
import com.mangecailloux.pebble.updater.UpdaterManager;

public class EntityWorld 
{
	private final EntitiesManager  entityManager;
	private final UpdaterManager updaterManager;
	
	private final EventManager	 		eventManager;
	private final EventManagerListener	eventManagerListener;
	
	private final Array<EntityManager> managers;
	private final ObjectMap<Class<? extends EntityManager>, EntityManager> managersPerType;
	
	public EntityWorld()
	{
		entityManager = new EntitiesManager(this);
		updaterManager = new UpdaterManager();
		eventManager = new EventManager();

		managers = new Array<EntityManager>(false, 4);
		managersPerType = new ObjectMap<Class<? extends EntityManager>, EntityManager>(4);
		
		addManager(new EntityGroupManager());
		addManager(new EntityTagManager());
		
		eventManagerListener = new EventManagerListener() {

			@Override
			public void onEvent(Event _event) {
				eventManager.handleEvent(_event);
			}
		};
	}
	
	protected EntitiesManager getEntitiesManager()
	{
		return entityManager;
	}
	
	protected UpdaterManager getUpdaterManager()
	{
		return updaterManager;
	}
	
	protected EventManager getEventManager()
	{
		return eventManager;
	}
	
	protected EventManagerListener getEventManagerListener()
	{
		return eventManagerListener;
	}
	
	public void setLogLevel(int _logLevel)
	{
		entityManager.logger.setLevel(_logLevel);
	}
	
	public void update(float _dt)
	{
		// Update all registered updater (from component or managers)
		updaterManager.update(_dt);
	}
	
	public Entity addEntity(EntityArchetype _archetype)
	{
		 return entityManager.addEntity(_archetype);
	}
	
	public void removeEntity(Entity _entity)
	{
		entityManager.removeEntity(_entity);
	}
	
	public void dispose()
	{
		entityManager.removeAllEntities();
	}
	
	public void addManager(EntityManager _manager)
	{
		if(_manager != null && !managers.contains(_manager, true))
		{
			_manager.setWorld(this);
			managers.add(_manager);
			managersPerType.put(_manager.getClass(), _manager);
			entityManager.addObserver(_manager);
		}
	}
	
	public <M extends EntityManager> M getManager(Class<M> type) {
		return type.cast(managersPerType.get(type));
	}
	
	public void addObserver(IEntityObserver _observer)
	{
		entityManager.addObserver(_observer);
	}
	
	public void removeObserver(IEntityObserver _observer)
	{
		entityManager.removeObserver(_observer);
	}
}
