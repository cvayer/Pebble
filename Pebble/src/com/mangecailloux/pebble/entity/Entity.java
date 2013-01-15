package com.mangecailloux.pebble.entity;

import com.badlogic.gdx.utils.Array;

public class Entity 
{
	private final Array<Component> components;
	
	protected Entity()
	{
		components = new Array<Component>(false, 2);
	}
	
	public void addComponent(Component _component)
	{
		if(_component != null && !components.contains(_component, true))
		{
			_component.setEntity(this);
			components.add(_component);
		}
	}
	
	public void update(float _fDt)
	{
		for(int i = 0; i < components.size; ++i)
		{
			components.get(i).update(_fDt);
		}
	}
}
