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

public class EntityArchetype 
{
	private final Array<Class<? extends Component>> componentTypes;
	
	public EntityArchetype()
	{
		componentTypes = new Array<Class<? extends Component>>(4);
	}
	
	@SuppressWarnings("unchecked")
	public EntityArchetype(Class<? extends Component>... _componentTypes)
	{
		this();
		
		AddComponent(_componentTypes);
	}
	
	public void AddComponent(Class<? extends Component> _componentType)
	{
		if(!componentTypes.contains(_componentType, true))
		{
			componentTypes.add(_componentType);
		}
		else
			throw new RuntimeException("EntityArchetype::AddComponent : you cannot have two components of the same type in one entity");
	}
	
	@SuppressWarnings("unchecked")
	public void AddComponent(Class<? extends Component>... _componentTypes)
	{
		for(int i=0; i < _componentTypes.length; ++i)
		{
			AddComponent(_componentTypes[i]);
		}
	}
	
	public Array<Class<? extends Component>> getComponentTypes()
	{
		return componentTypes;
	}
	
	public int getComponentTypesCount()
	{
		return componentTypes.size;
	}
}
