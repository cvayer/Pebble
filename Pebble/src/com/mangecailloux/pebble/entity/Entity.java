package com.mangecailloux.pebble.entity;

public class Entity 
{
	private  		EntityWorld	   	 world;
	private final   ComponentSet	 components;
	private 		boolean			 deletePending;
	
	protected Entity()
	{
		components = new ComponentSet();
		deletePending = false;
	}
	
	protected void setDeletePending(boolean	_deletePending)
	{
		deletePending = _deletePending;
	}
	
	protected boolean isDeletePending()
	{
		return deletePending;
	}
	
	protected void setWorld(EntityWorld _world)
	{
		world = _world;
	}
	
	public EntityWorld getWorld()
	{
		return world;
	}
	
	protected void initComponents(EntityArchetype _archetype)
	{
		components.init(_archetype);
	}
	
	protected void deinitComponents() {
		components.deinit();
	}
	
	public <C extends Component> C getComponent(Class<C> _type)
	{
		return components.getComponent(_type);
	}
	
	protected void update(float _fDt)
	{
		components.update(_fDt);
	}
	
	protected void onAddToWorld()
	{
		components.onAddToWorld();
	}
	
	protected void onRemoveFromWorld()
	{
		components.onRemoveFromWorld();
	}
}
