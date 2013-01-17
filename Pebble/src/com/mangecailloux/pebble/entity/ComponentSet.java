package com.mangecailloux.pebble.entity;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class ComponentSet 
{
	private			Entity			 entity;
	private final 	Array<Component> components;
	private final 	ObjectMap<Class<? extends Component>, Component> componentsPerType;
	
	protected ComponentSet()
	{
		components = new Array<Component>(false, 2);
		componentsPerType = new ObjectMap<Class<? extends Component>, Component>(8);
	}
	
	protected void setEntity(Entity	_entity)
	{
		entity = _entity;
	}
	
	protected void addComponent(Component _component)
	{
		if(_component != null && !components.contains(_component, true))
		{
			_component.setEntity(entity);
			components.add(_component);
			
			componentsPerType.put(_component.getClass(), _component);
		}
	}
	
	protected <C extends Component> C getComponent(Class<C> _type)
	{
		return _type.cast(componentsPerType.get(_type));
	}
	
	protected void update(float _fDt)
	{
		for(int i = 0; i < components.size; ++i)
		{
			components.get(i).update(_fDt);
		}
	}
}
