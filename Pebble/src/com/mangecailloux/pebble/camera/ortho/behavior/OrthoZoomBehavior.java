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
package com.mangecailloux.pebble.camera.ortho.behavior;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.mangecailloux.pebble.camera.ortho.OrthoCamBehavior;

public class OrthoZoomBehavior extends OrthoCamBehavior
{
	private float 			max;
	private float 			min;
	private float 			step;
	private Interpolation 	interpolation;
	private float 			multiplier;
	
	public OrthoZoomBehavior()
	{
		this(1.0f, 4.0f, 0.25f, Interpolation.linear);
	}
	
	public OrthoZoomBehavior(float _min, float _max, float _step, Interpolation _interpolation)
	{
		super();
		SetMinMultiplier(_min);
		SetMaxMultiplier(_max);
		SetStep(_step);
		SetInterpolation(_interpolation);
	}
	
	public float GetMultiplier()
	{
		return multiplier;
	}
	
	public float GetMinMultiplier()
	{
		return min;
	}
	
	public float GetMaxMultiplier()
	{
		return max;
	}
	
	public float GetStep()
	{
		return step;
	}
	
	public void SetMultiplier(float _multiplier)
	{
		multiplier = MathUtils.clamp(_multiplier, min, max);
	}
	
	public void SetMinMultiplier(float _min)
	{
		if(_min <= 0.0f)
			_min = 1.0f;
		
		min = _min;
		SetMultiplier(multiplier);
		SetStep(step);
	}
	
	public void SetMaxMultiplier(float _max)
	{
		if(_max < min)
			_max = min;
		
		max = _max;
		SetMultiplier(multiplier);
		SetStep(step);
	}
	
	public void SetStep(float _step)
	{
		if(_step < 0.0f)
			_step *= -1.0f;
		
		if(_step < (max - min))
		{
			step = _step;
		}
		else 
		{
			step = (max - min);
		}
	}
	
	public void SetInterpolation(Interpolation _interpolation)
	{
		interpolation = _interpolation;
	}
	
	public float GetMultiplierCursor()
	{
		return (multiplier - min) / (max - min);
	}
	
	protected float GetZoom()
	{
		if(min == max)
		{
			return 1.0f/min;
		}
		else
		{
			float zoomCursor = GetMultiplierCursor();
			return interpolation.apply(1.0f/min, 1.0f/max, zoomCursor);
		}
	}
	
	protected float GetZoomIn()
	{
		SetMultiplier(multiplier + step);
		return GetZoom();
	}
	
	protected float GetZoomOut()
	{
		SetMultiplier(multiplier - step);
		return GetZoom();
	}
	
	public void ZoomIn()
	{
		camera.zoom  = GetZoomIn();	
		camera.update();	
	}
	
	public void ZoomOut()
	{
		camera.zoom  = GetZoomOut();
		camera.update();
	}
}
