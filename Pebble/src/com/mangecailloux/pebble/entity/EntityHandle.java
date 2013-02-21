package com.mangecailloux.pebble.entity;

public class EntityHandle
{
	public static final int InvalidHandle = -1;
	
	private EntitiesManager manager;
	private int			  	handle;
	
	public EntityHandle()
	{
		manager = null;
		handle = InvalidHandle;
	}
	
	public void clear()
	{
		manager = null;
		handle = InvalidHandle;
	}
	
	public boolean equals(EntityHandle _handle)
	{
		return (handle == _handle.handle && manager == _handle.manager);
	}
	
	public boolean equals(Entity _entity)
	{
		return (get() == _entity);
	}
	
	public void copy(EntityHandle _handle)
	{
		if(_handle != null)
		{
			manager = _handle.manager;
			handle = _handle.handle;
		}
		else
		{
			clear();
		}
	}
	
	public void set(Entity _entity)
	{
		if(_entity != null)
		{
			manager = _entity.getEntityManager();
			handle = _entity.getHandle();
		}
		else
		{
			clear();
		}
	}
	
	public Entity get()
	{
		if(handle == InvalidHandle || manager == null)
			return null;
		
		return manager.getEntityByHandle(handle);
	}
}
