package com.mangecailloux.pebble.entity;

public class Entity 
{
	private  		EntityWorld	   	 world;
	private final   ComponentSet	 components;
	
	protected Entity()
	{
		components = new ComponentSet();
	}
	
	protected void setWorld(EntityWorld _world)
	{
		world = _world;
	}
	
	public EntityWorld getWorld()
	{
		return world;
	}
	
	public void setup(EntityArchetype _archetype)
	{
		
	}
	
	public void addComponent(Component _component)
	{
		components.addComponent(_component);
	}
	
	public <C extends Component> C getComponent(Class<C> _type)
	{
		return components.getComponent(_type);
	}
	
	public void update(float _fDt)
	{
		components.update(_fDt);
	}
}
