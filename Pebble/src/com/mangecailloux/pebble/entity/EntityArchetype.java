package com.mangecailloux.pebble.entity;

import com.badlogic.gdx.utils.Array;

public class EntityArchetype 
{
	private final Array<Class<? extends Component>> componentTypes;
	
	public EntityArchetype()
	{
		componentTypes = new Array<Class<? extends Component>>(4);
	}
	
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
