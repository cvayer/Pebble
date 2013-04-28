/*******************************************************************************
 * Copyright 2013 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.mangecailloux.pebble.camera.ortho;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.mangecailloux.pebble.camera.ortho.behavior.OrthoMoveBehavior;
import com.mangecailloux.pebble.camera.ortho.behavior.OrthoZoomBehavior;

/**
 * Orthographic Camera Controller. 
 * @author clement.vayer
 *
 */
public class OrthoCameraController 
{
	protected  		OrthographicCamera  camera;
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
	
	public OrthoCameraController(OrthoZoomBehavior _zoom, OrthoMoveBehavior _move)
	{
		this(null, _zoom, _move);
	}
	
	public void setCamera(OrthographicCamera  _camera)
	{
		if(camera != _camera)
		{
			camera = _camera;
			move.init(this, camera);
			zoom.init(this, camera);
		}
	}
	
	public OrthoZoomBehavior getZoom()
	{
		return zoom;
	}
	
	public OrthoMoveBehavior getMove()
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
		if(camera != null)
		{
			camera.position.set(centerCamPosition);
			move.Move(0.0f, 0.0f);
			camera.update();
		}
	}
	
	public Vector3 getCenterPosition()
	{
		return centerCamPosition;
	}
	
	public void update(float _dt)
	{
		
	}
}
