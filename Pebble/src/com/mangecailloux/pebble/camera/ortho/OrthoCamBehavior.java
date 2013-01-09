package com.mangecailloux.pebble.camera.ortho;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class OrthoCamBehavior 
{
	protected OrthographicCamera  	camera;
	protected OrthoCameraController controller;
	protected boolean				needUpdate;
	
	public OrthoCamBehavior()
	{
		
	}
	
	protected void init(OrthoCameraController _controller, OrthographicCamera _camera)
	{
		camera = _camera;
		controller = _controller;
	}
	
	protected boolean NeedUpdate()
	{
		return needUpdate;
	}
	
	protected void SetNeedUpdate(boolean _needUpdate)
	{
		needUpdate = _needUpdate;
	}
}
