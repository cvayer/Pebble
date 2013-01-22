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
import com.mangecailloux.pebble.updater.Updater;

public class EntityWorldManager implements IEntityObserver
{
	private 		EntityWorld	    world;
	private final 	Array<Updater> 	updaters;
	private		  	boolean		 	updatersRegistred;
	
	public EntityWorldManager()
	{
		updaters = new Array<Updater>(false, 2);
		updatersRegistred = false;
	}
	
	protected void setWorld(EntityWorld _world)
	{
		world = _world;
	}
	
	public EntityWorld getWorld()
	{
		return world;
	}
	
	public <M extends EntityWorldManager> M getManager(Class<M> type)
	{
		if(world != null)
			return world.getManager(type);
		return null;
	}
	
	public void addUpdater(Updater _updater)
	{
		if(_updater != null && !updaters.contains(_updater, true))
		{
			updaters.add(_updater);
			if(updatersRegistred && world != null)
				world.getUpdaterManager().addUpdater(_updater);
		}
	}
	
	protected void registerAllUpdaters()
	{
		if(world == null)
			throw new RuntimeException("EntityWorldManager::registerAllUpdaters : world is null");
		
		for(int i = 0; i < updaters.size; ++i)
		{
			world.getUpdaterManager().addUpdater(updaters.get(i));
		}
		updatersRegistred = true;
	}
	
	protected void unregisterAllUpdaters()
	{
		if(world == null)
			throw new RuntimeException("EntityWorldManager::registerAllUpdaters : world is null");
		
		for(int i = 0; i < updaters.size; ++i)
		{
			world.getUpdaterManager().removeUpdater(updaters.get(i));
		}
		updatersRegistred = false;
	}

	@Override
	public	void onAddToWorld(Entity _entity) {}

	@Override
	public	void onRemoveFromWorld(Entity _entity) {}
}
