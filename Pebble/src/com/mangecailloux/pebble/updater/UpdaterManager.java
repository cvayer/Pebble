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
package com.mangecailloux.pebble.updater;

import com.badlogic.gdx.utils.Array;

public class UpdaterManager 
{
	private final Array<UpdateGroup> groups;
	
	public UpdaterManager()
	{
		groups = new Array<UpdateGroup>(true, 4);
	}
	
	public void update(float _dt)
	{
		for(int i = 0; i < groups.size; ++i)
		{
			UpdateGroup group = groups.get(i);
			if(group != null)
				group.update(_dt);
		}
	}
	
	public void addUpdater(Updater _updater)
	{
		int index = _updater.getPriority().getIndex();
		
		// We resize the array is the priority is too high for the array
		if(index >= groups.size)
		{
			int diff = index - (groups.size - 1);
			groups.ensureCapacity(diff);
			
			// A resize method would be good here :)
			for(int i = 0; i < diff; ++i)
			{
				groups.add(null);
			}
		}
		
		UpdateGroup group = groups.get(index);
		if(group == null)
		{
			group = new UpdateGroup(_updater.getPriority());
			groups.set(index, group);
		}
		
		group.addUpdater(_updater);
	}
	
	public void removeUpdater(Updater _updater)
	{
		int index = _updater.getPriority().getIndex();
		if(index < groups.size)
		{
			UpdateGroup group = groups.get(index);
			if(group != null)
				group.removeUpdater(_updater);
		}
	}
	
	
	//------------------------------------------------------------
	//------------------------------------------------------------
	// UpdateGroup
	//------------------------------------------------------------
	//------------------------------------------------------------
	class UpdateGroup
	{
		protected final IUpdatePriority 	priority;
		private final Array<Updater>	updaters;
		private final Array<Updater>	toAdd;
		private final Array<Updater>	toRemove;
		
		public UpdateGroup(IUpdatePriority _priority)
		{
			priority 	= _priority;
			updaters 	= new Array<Updater>(false, 4);
			toAdd 		= new Array<Updater>(false, 4);
			toRemove 	= new Array<Updater>(false, 4);
		}
		
		public void update(float _dt)
		{
			if(toAdd.size > 0)
			{
				for(int i = 0; i < toAdd.size; ++i )
				{
					updaters.add(toAdd.get(i));
				}
				toAdd.clear();
			}
			
			if(toRemove.size > 0)
			{
				for(int i = 0; i < toRemove.size; ++i )
				{
					updaters.removeValue(toRemove.get(i), true);
				}
				toRemove.clear();
			}
			
			for(int i = 0; i < updaters.size; ++i )
			{
				if(!updaters.get(i).isPaused())
					updaters.get(i).update(_dt);
			}
		}
		
		public void addUpdater(Updater _updater)
		{
			if(_updater != null && _updater.getPriority().equals(priority))
				toAdd.add(_updater);
		}
		
		public void removeUpdater(Updater _updater)
		{
			if(_updater != null && _updater.getPriority().equals(priority))
				toRemove.add(_updater);
		}		
	};
}
