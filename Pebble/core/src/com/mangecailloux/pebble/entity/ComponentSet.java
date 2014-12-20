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
import com.badlogic.gdx.utils.Pools;

public class ComponentSet 
{
	private	final	Entity			 entity;
	private final 	Array<Component> components;
	private final 	ObjectMap<Class<? extends Component>, Component> componentsByType;
	
	protected ComponentSet(Entity _entity)
	{
		entity = _entity;
		components = new Array<Component>(false, 2);
		componentsByType = new ObjectMap<Class<? extends Component>, Component>(8);
	}
	
	protected void init(EntityArchetype _archetype)
	{
		if(components.size != 0)
			throw new RuntimeException("ComponentSet::setup -> components must be empty");
		
		Array<Class<? extends Component>> componentTypes = _archetype.getComponentTypes();
		
		if(componentTypes.size == 0)
			throw new RuntimeException("ComponentSet::setup -> Archetype must not be empty");
		
		for(int i=0; i < componentTypes.size; i++)
		{
			Class<? extends Component> type = componentTypes.get(i);
			
			Component component = Pools.obtain(type);
			
			if(getComponent(type) != null)
				throw new RuntimeException("ComponentSet::setup -> Component is duplicated : " + type.getSimpleName());
			
			component.setEntity(entity);
			components.add(component);
			componentsByType.put(type, component);
			
			entity.info("Adding component : " + type.getSimpleName());
		}
	}
	
	private void deinit()
	{
		for(int i=0; i < components.size; ++i)
		{
			Component component = components.get(i);
			component.setEntity(null);
			
			entity.info("Removing component : " + component.getClass().getSimpleName());
			Pools.free(component);
		}
		components.clear();
		componentsByType.clear();
	}
	
	protected <C extends Component> C getComponent(Class<C> _type)
	{
		Component c = componentsByType.get(_type);
		// If we don't find a component we look for a super class
		if(c == null)
		{
			for(int i = 0; i < components.size; ++i)
			{
				Component c2 = components.get(i);
				
				if(_type.isInstance(c2))
				{
					// Is the component is instance of the super class we add it to the components by type to avoid researching  it
					componentsByType.put(_type, c2);
					return _type.cast(c2);
				}
			}
		}
		else
		{
			return _type.cast(c);
		}
				
		return null;
	}
	
	protected void onAddToWorld()
	{
		for(int i = 0; i < components.size; ++i)
		{
			components.get(i).onSetWorld(entity.getWorld());
		}
	}
	
	protected void onRemoveFromWorld()
	{
		for(int i = 0; i < components.size; ++i)
		{
			components.get(i).onSetWorld(null);
		}
		deinit();
	}
}
