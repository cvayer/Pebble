package com.mangecailloux.pebble.entity;

import com.mangecailloux.pebble.entity.manager.EntityGroup;
import com.mangecailloux.pebble.entity.manager.EntityGroupManager;

public class Entity 
{
	protected 		static int globalCounter = 0;
	
	private			int 			 id;
	private  		EntityWorld	   	 world;
	private final   ComponentSet	 components;
	private 		boolean			 deletePending;
	
	protected Entity()
	{
		// Entity should never be unallocated during runtime, so that should suffice to have an unique id
		id = globalCounter++;
		components = new ComponentSet(this);
		deletePending = false;
	}
	
	@Override
	public String toString()
	{
		return "Entity " + id;
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
	
	private void deinitComponents() {
		components.deinit();
	}
	
	public <C extends Component> C getComponent(Class<C> _type)
	{
		return components.getComponent(_type);
	}
	
	public void addToGroup(EntityGroup _group)
	{
		if(world != null)
		{
			EntityGroupManager manager = world.getManager(EntityGroupManager.class);
			if(manager != null)
				manager.addToGroup(this, _group);
		}
	}
	
	public void removeFromGroup(EntityGroup _group)
	{
		if(world != null)
		{
			EntityGroupManager manager = world.getManager(EntityGroupManager.class);
			if(manager != null)
				manager.removeFromGroup(this, _group);
		}
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
		deinitComponents();
	}
}
