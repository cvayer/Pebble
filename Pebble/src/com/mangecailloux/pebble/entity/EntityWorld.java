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
import com.mangecailloux.pebble.updater.UpdaterManager;

public class EntityWorld 
{
	private final EntityManager entityManager;
	private final UpdaterManager updaterManager;
	
	private final Array<EntityWorldManager> managers;
	private final ObjectMap<Class<? extends EntityWorldManager>, EntityWorldManager> managersPerType;
	
	public EntityWorld()
	{
		entityManager = new EntityManager(this);
		updaterManager = new UpdaterManager();
		
		managers = new Array<EntityWorldManager>(false, 4);
		managersPerType = new ObjectMap<Class<? extends EntityWorldManager>, EntityWorldManager>(4);
		
		addManager(new EntityGroupManager());
		addManager(new EntityTagManager());
	}
	
	protected EntityManager getEntityManager()
	{
		return entityManager;
	}
	
	protected UpdaterManager getUpdaterManager()
	{
		return updaterManager;
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
	
	public Entity newEntity(EntityArchetype _archetype)
	{
		return entityManager.newEntity(_archetype);
	}
	
	protected void addEntity(Entity _entity)
	{
		 entityManager.addEntity(_entity);
	}
	
	public void removeEntity(Entity _entity)
	{
		entityManager.removeEntity(_entity);
	}
	
	public void dispose()
	{
		entityManager.removeAllEntities();
	}
	
	public void addManager(EntityWorldManager _manager)
	{
		if(_manager != null && !managers.contains(_manager, true))
		{
			_manager.setWorld(this);
			_manager.registerAllUpdaters();
			
			managers.add(_manager);
			managersPerType.put(_manager.getClass(), _manager);
			entityManager.addObserver(_manager);
		}
	}
	
	public <M extends EntityWorldManager> M getManager(Class<M> type) {
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
