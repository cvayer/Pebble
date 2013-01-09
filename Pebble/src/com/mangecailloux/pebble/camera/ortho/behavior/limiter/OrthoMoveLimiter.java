package com.mangecailloux.pebble.camera.ortho.behavior.limiter;

import com.badlogic.gdx.math.Vector3;

public abstract class OrthoMoveLimiter 
{
	protected final Vector3 center;
	
	public OrthoMoveLimiter()
	{
		center = new Vector3();
	}
	
	public void SetCenter(Vector3 _center)
	{
		center.set(_center);
	}
	
	public abstract void limit(Vector3 _cameraPosition);
}
