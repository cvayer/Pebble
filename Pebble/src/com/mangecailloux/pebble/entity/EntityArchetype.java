package com.mangecailloux.pebble.entity;

import com.badlogic.gdx.utils.Array;

public class EntityArchetype 
{
	private final Array<Class<? extends Component>> componentTypes;
	
	public EntityArchetype()
	{
		componentTypes = new Array<>(4);
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
	
	public Array<Class<? extends Component>> getComponentTypes()
	{
		return componentTypes;
	}
	
	public int getComponentTypesCount()
	{
		return componentTypes.size;
	}
}
