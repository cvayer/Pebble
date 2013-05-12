package com.mangecailloux.pebble.entity.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.mangecailloux.pebble.entity.Component;

public class OrthographicCameraComponent extends Component
{
	protected final 	OrthographicCamera 		camera;
	protected 			boolean					isActive;
	protected			CameraManager			manager;
	
	public OrthographicCameraComponent()
	{
		camera = new OrthographicCamera();
	}
	
	public OrthographicCamera getCamera()
	{
		return camera;
	}
	
	public boolean isActive()
	{
		return isActive;
	}
	
	public void setActive(boolean _active)
	{
		if(_active != isActive)
		{
			if(manager != null)
			{
				isActive = _active;
				if(isActive)
				{
					manager.setActive(this);
				}
				else
				{
					manager.setActive(null);
				}
			}
		}
	}
	
	@Override
	protected void onAddToWorld() 
	{
		manager = getManager(CameraManager.class);
		if(manager != null)
		{
			manager.register(this);
		}
	}

	@Override
	protected void onRemoveFromWorld() {
		
		setActive(false);
		
		if(manager != null)
		{
			manager.unregister(this);
		}
		manager = null;
	}
}
