package com.mangecailloux.pebble.camera.ortho;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.mangecailloux.pebble.camera.ortho.behavior.OrthoMoveBehavior;
import com.mangecailloux.pebble.camera.ortho.behavior.OrthoZoomBehavior;

public class OrthoCameraController 
{
	protected final OrthographicCamera  camera;
	protected final Vector3			 	centerCamPosition;	
	
	protected final OrthoZoomBehavior	 zoom;
	protected final OrthoMoveBehavior	 move; 
	
	public OrthoCameraController(OrthographicCamera _camera, OrthoZoomBehavior _zoom, OrthoMoveBehavior _move)
	{
		camera = _camera;
		centerCamPosition = new Vector3();
		
		zoom = _zoom;
		zoom.init(this, camera);
		
		move = _move;
		move.init(this, camera);
	}
	
	public OrthoZoomBehavior GetZoom()
	{
		return zoom;
	}
	
	public OrthoMoveBehavior GetMove()
	{
		return move;
	}
	
	public void setCenterPosition(float _x, float _y)
	{
		centerCamPosition.set(_x, _y, 0.0f);
		if(move.getLimiter() != null)
		{
			move.getLimiter().SetCenter(centerCamPosition);
		}
	}
	
	public void setCameraToCenterPos()
	{
		camera.position.set(centerCamPosition);
		move.Move(0.0f, 0.0f);
		camera.update();
	}
	
	public Vector3 GetCenterPosition()
	{
		return centerCamPosition;
	}
}
