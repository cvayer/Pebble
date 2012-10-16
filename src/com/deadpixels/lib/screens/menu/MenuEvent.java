package com.deadpixels.lib.screens.menu;

import com.badlogic.gdx.utils.Pool.Poolable;

public final class MenuEvent implements Poolable 
{
	private int					id;
	private MenuEventParameters parameters;
	
	public MenuEvent()
	{
		parameters = null;
	}
	
	@Override
	public void reset() {
		id = 0;
		parameters = null;
	}
	
	protected void setParameters(int _id, MenuEventParameters _parameters)
	{
		id = _id;
		parameters = _parameters;
	}
	
	public int getId()
	{
		return id;
	}
	
	@SuppressWarnings("unchecked")
	public <P extends MenuEventParameters> P getParameters(Class<P> _paramType)
	{
		if(parameters!= null && _paramType.isInstance(parameters))
			return (P)parameters;
		return null;
	}

}
