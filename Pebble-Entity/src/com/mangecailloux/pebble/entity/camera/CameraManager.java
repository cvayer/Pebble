package com.mangecailloux.pebble.entity.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.mangecailloux.pebble.entity.Entity;
import com.mangecailloux.pebble.entity.EntityManager;

public class CameraManager extends EntityManager
{
	private final 	Array<OrthographicCameraComponent>	cameras;
	private			OrthographicCameraComponent			activeCamera;
	
	public CameraManager()
	{		
		cameras = new Array<OrthographicCameraComponent>(false, 2);
	}
	
	public OrthographicCamera getCamera()
	{
		if(activeCamera != null)
		{
			return activeCamera.getCamera();
		}
		return null;
	}
	
	public void register(OrthographicCameraComponent	_camera)
	{
		if(!cameras.contains(_camera, true))
		{
			cameras.add(_camera);
		}
	}
	
	public void unregister(OrthographicCameraComponent	_camera)
	{
		if(cameras.contains(_camera, true))
		{
			cameras.removeValue(_camera, true);
		}
	}
	
	public void setActive(OrthographicCameraComponent	_camera)
	{
		activeCamera = _camera;
	}
	
	@Override
	public void onAddToWorld(Entity _entity) {
		
		
	}

	@Override
	public void onRemoveFromWorld(Entity _entity) {

	}

	@Override
	public void dispose() {
		
	}
	
	
}
