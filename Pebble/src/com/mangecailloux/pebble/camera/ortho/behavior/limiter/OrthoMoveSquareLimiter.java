package com.mangecailloux.pebble.camera.ortho.behavior.limiter;

import com.badlogic.gdx.math.Vector3;

public class OrthoMoveSquareLimiter extends OrthoMoveLimiter
{
	private float sideLenght;
	
	public OrthoMoveSquareLimiter()
	{
		sideLenght = -1.0f;
	}
	
	public void SetSideLenght(float _lenght)
	{
		sideLenght = _lenght;
	}
	
	@Override
	public void limit(Vector3 _cameraPosition) 
	{
		if(_cameraPosition.x <  center.x - sideLenght)
			_cameraPosition.x = center.x - sideLenght;
		
		if(_cameraPosition.x >  center.x + sideLenght)
			_cameraPosition.x = center.x + sideLenght;
		
		if(_cameraPosition.y <  center.y - sideLenght)
			_cameraPosition.y = center.y - sideLenght;
		
		if(_cameraPosition.y >  center.y + sideLenght)
			_cameraPosition.y = center.y + sideLenght;
	}

}
