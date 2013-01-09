package com.mangecailloux.pebble.camera.ortho.behavior;

import com.mangecailloux.pebble.camera.ortho.OrthoCamBehavior;
import com.mangecailloux.pebble.camera.ortho.behavior.limiter.OrthoMoveLimiter;

public class OrthoMoveBehavior extends OrthoCamBehavior
{
	protected OrthoMoveLimiter limiter;
	
	public OrthoMoveBehavior()
	{
		this(null);
	}
	
	public OrthoMoveBehavior(OrthoMoveLimiter _limiter)
	{
		super();
		limiter = _limiter;
	}
	
	public OrthoMoveLimiter getLimiter()
	{
		return limiter;
	}
	
	public final float radius = 200.f;
	
	public void Move(float _x, float _y)
	{
		camera.position.add(_x, _y, 0.0f);
		
		if(limiter != null)
		{
			limiter.limit(camera.position);
		}
		
		camera.update();
	}
}
