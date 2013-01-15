package com.mangecailloux.pebble.entity;

public class Component 
{
	public static final int InvalidID = -1;
	
	private Entity 	entity;
	
	public Component()
	{
		entity = null;
	}
	
	public Entity getEntity()
	{
		return entity;
	}
	
	protected void setEntity(Entity _entity)
	{
		entity = _entity;
	}
	
	public void update(float _fDt)
	{
		
	}
}
