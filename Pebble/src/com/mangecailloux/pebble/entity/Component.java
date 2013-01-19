package com.mangecailloux.pebble.entity;

public abstract class Component 
{
	public static final int InvalidID = -1;
	
	private Entity 	entity;
	
	public Component()
	{
		entity = null;
	}
	
	protected void setEntity(Entity _entity)
	{
		entity = _entity;
	}
	
	public Entity getEntity()
	{
		return entity;
	}
	
	public <T extends Entity> T getEntity(Class<T> type)
	{
		return type.cast(entity);
	}
	
	public <C extends Component> C getComponent(Class<C> _type)
	{
		if(entity != null)
			return entity.getComponent(_type);
		return null;
	}
		
	protected void onAddToEntity()			{}
	
	protected void onRemoveFromEntity()		{}
	
	protected void onAddToWorld()			{}
	
	protected void onRemoveFromWorld() 		{}
	
	protected void update(float _fDt)		{}
}
