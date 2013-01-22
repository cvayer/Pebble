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

public abstract class Component 
{
	public static final int InvalidID = -1;
	
	private Entity 	entity;
	private final Array<Updater> updaters;
	
	public Component()
	{
		entity = null;
		
		updaters = new Array<Updater>(false, 2);
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
		if(entity != null && entity.getWorld() != null)
			return  entity.getWorld().getManager(type);
		return null;
	}
	
	public void addUpdater(Updater _updater)
	{
		if(entity == null || entity.getWorld() == null)
			return;
		
		if(_updater != null && !updaters.contains(_updater, true))
		{
			updaters.add(_updater);
			entity.getWorld().getUpdaterManager().addUpdater(_updater);
		}
	}
	
	public void removeUpdater(Updater _updater)
	{
		if(entity == null || entity.getWorld() == null)
			return;
		
		if(_updater != null && updaters.contains(_updater, true))
		{
			updaters.removeValue(_updater, true);
			entity.getWorld().getUpdaterManager().removeUpdater(_updater);
		}
	}
	
	public void removeAllUpdaters()
	{
		if(entity == null || entity.getWorld() == null)
			return;
		
		for(int i = 0; i < updaters.size; ++i)
		{
			entity.getWorld().getUpdaterManager().removeUpdater(updaters.get(i));
		}
		updaters.clear();
	}
		
	protected void onAddToEntity()			{}
	
	protected void onRemoveFromEntity()		{}
	
	protected void onAddToWorld()			{}
	
	protected void onRemoveFromWorld() 		{}
	
	protected void update(float _fDt)		{}
}
