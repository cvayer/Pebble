package com.mangecailloux.pebble.entity;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class Entity 
{
	private  		EntityWorld	   	 world;
	private final 	Array<Component> components;
	private final 	ObjectMap<Class<? extends Component>, Component> componentsPerType;
	
	protected Entity()
	{
		components = new Array<Component>(false, 2);
		
		componentsPerType = new ObjectMap<Class<? extends Component>, Component>(8);
	}
	
	protected void setWorld(EntityWorld _world)
	{
		world = _world;
	}
	
	public EntityWorld getWorld()
	{
		return world;
	}
	
	public void addComponent(Component _component)
	{
		if(_component != null && !components.contains(_component, true))
		{
			_component.setEntity(this);
			components.add(_component);
			
			componentsPerType.put(_component.getClass(), _component);
		}
	}
	
	public <C extends Component> C getComponent(Class<C> _type)
	{
		return _type.cast(componentsPerType.get(_type));
	}
	
	public void update(float _fDt)
	{
		for(int i = 0; i < components.size; ++i)
		{
			components.get(i).update(_fDt);
		}
	}
}
