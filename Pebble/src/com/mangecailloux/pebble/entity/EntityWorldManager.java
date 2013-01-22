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

import com.mangecailloux.pebble.updater.Updatable;

public class EntityWorldManager extends Updatable implements IEntityObserver
{
	private 		EntityWorld	    world;
	
	public EntityWorldManager()
	{
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

	@Override
	public	void onAddToWorld(Entity _entity) {}

	@Override
	public	void onRemoveFromWorld(Entity _entity) {}
}
