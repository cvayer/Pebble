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
package com.mangecailloux.pebble.camera.ortho.behavior.limiter;

import com.badlogic.gdx.math.Vector3;

public class OrthoMoveSquareLimiter extends OrthoMoveLimiter
{
	private float sideLenght;
	
	public OrthoMoveSquareLimiter()
	{
		this(-1.0f);
	}
	
	public OrthoMoveSquareLimiter(float _sideLenght)
	{
		sideLenght = _sideLenght;
	}
	
	public void SetSideLenght(float _lenght)
	{
		sideLenght = _lenght;
	}
	
	@Override
	public void limit(Vector3 _cameraPosition) 
	{
		if(sideLenght < 0.0f)
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

}
