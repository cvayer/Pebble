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
			component.onAddToEntity();
		}
	}
	
	protected void deinit()
	{
		for(int i=0; i < components.size; ++i)
		{
			Component component = components.get(i);
			component.onRemoveFromEntity();
			Pools.free(component);
		}
		components.clear();
		componentsByType.clear();
	}
	
	protected <C extends Component> C getComponent(Class<C> _type)
	{
		return _type.cast(componentsByType.get(_type));
	}
	
	protected void update(float _fDt)
	{
		for(int i = 0; i < components.size; ++i)
		{
			components.get(i).update(_fDt);
		}
	}
	
	public void onAddToWorld()
	{
		for(int i = 0; i < components.size; ++i)
		{
			components.get(i).onAddToWorld();
		}
	}
	
	public void onRemoveFromWorld()
	{
		for(int i = 0; i < components.size; ++i)
		{
			components.get(i).onRemoveFromWorld();
		}
	}
}
