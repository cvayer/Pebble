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
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.Pools;

public class EntityManager 
{
	private final Logger 				logger;
	private final EntityWorld	   		world;
	private final Array<Entity> 		entities;
	private final Array<Entity> 		toDelete;
	private final Array<Entity> 		toAdd;
	private final Array<EntityObserver> obervers;
	
	protected EntityManager(EntityWorld _world)
	{
		// reset the global counter as static data can be retained between application instances
		Entity.globalCounter = 0;
		
		logger = new Logger("EntityManager");
		logger.setLevel(Logger.INFO);
		world = _world;
		entities = new Array<Entity>(false, 8);
		toDelete = new Array<Entity>(false, 4);
		toAdd = new Array<Entity>(false, 4);
		obervers = new Array<EntityObserver>(false, 4);
	}
	
	protected Entity newEntity(EntityArchetype _archetype)
	{
		if(_archetype == null)
			throw new RuntimeException("EntityManager::NewEntity -> Archetype cannot be null");
		
		if(_archetype.getComponentTypesCount() == 0)
			throw new RuntimeException("EntityManager::NewEntity -> Archetype cannot be empty");
		
		 
		 Entity entity = Pools.obtain(Entity.class);
		 entity.setWorld(world);
		 entity.initComponents(_archetype);
		 logger.info("New" + entity);
		 return entity;
	}
	
	protected void addEntity(Entity _entity)
	{
		if(_entity != null)
		{
			logger.info("Add" + _entity);
			_entity.onAddToWorld();
			for(int o =0; o < obervers.size; ++o)
			{
				obervers.get(o).onAddToWorld(_entity);
			}
			toAdd.add(_entity);
		}
	}
	
	protected Entity addEntity(EntityArchetype _archetype)
	{
		Entity entity = newEntity(_archetype);
		addEntity(entity);
		return entity;
	}
	
	protected void removeEntity(Entity _entity)
	{
		if(_entity != null)
		{
			for(int o =0; o < obervers.size; ++o)
			{
				obervers.get(o).onRemoveFromWorld(_entity);
			}
			_entity.onRemoveFromWorld();
			_entity.setWorld(null);
			_entity.setDeletePending(true);
			toDelete.add(_entity);
		}
	}
	
	protected void addObserver(EntityObserver _observer)
	{
		if(_observer != null && !obervers.contains(_observer, true))
		{
			obervers.add(_observer);
		}
	}
	
	protected void removeObserver(EntityObserver _observer)
	{
		if(_observer != null)
		{
			obervers.removeValue(_observer, true);
		}
	}
	
	private void deleteEntities() 
	{
		for(int i = 0;  i < toDelete.size; ++i) {
			Entity e = toDelete.get(i);
			entities.removeValue(e, true);
			e.setDeletePending(false);
			Pools.free(e);
		}
		toDelete.clear();
	}
	
	private void addEntities() 
	{
		for(int i = 0;  i < toAdd.size; ++i) {
			Entity e = toAdd.get(i);
			entities.add(e);
		}
		toAdd.clear();
	}
	
	protected void update(float _fDt)
	{
		if(toDelete.size > 0)
		{
			deleteEntities();
		}
		
		if(toAdd.size > 0)
		{
			addEntities();
		}
		
		for(int i = 0; i < entities.size; ++i)
		{
			Entity e = entities.get(i);
			if(!e.isDeletePending())
				e.update(_fDt);
		}
	}
}
