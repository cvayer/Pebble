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
package com.mangecailloux.pebble.tools;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class GeomUtils 
{
	private static Vector2 tmp = new Vector2();
	private static Vector2 tmp2 = new Vector2();
	/**
	 * @param _center origin of the 2D cone
	 * @param _dir direction of the 2D cone
	 * @param _point point to check
	 * @param _angle angle of the cone
	 * @param _degrees true if _angle is in degrees
	 * @return true if _point is inside the cone defined by _center, _dir and _angle
	 * */
	public static boolean isInCone(Vector2 _center, Vector2 _dir, Vector2 _point, float _angle, boolean _degrees)
	{
		tmp2.set(_dir).nor();
		float dot = tmp.set(_point).sub(_center).nor().dot(tmp2);
		float angle = (float) Math.acos(dot);
		
		if(_degrees)
			angle *= MathUtils.radiansToDegrees;
		
		return Math.abs(angle) <= _angle/2.0f;
	}
}
