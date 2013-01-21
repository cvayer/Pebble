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

package com.mangecailloux.pebble.menu;

import com.badlogic.gdx.utils.Pool.Poolable;

public final class MenuEvent implements Poolable 
{
	private int					id;
	private MenuEventParameters parameters;
	
	public MenuEvent()
	{
		parameters = null;
	}
	
	@Override
	public void reset() {
		id = 0;
		parameters = null;
	}
	
	protected void setParameters(int _id, MenuEventParameters _parameters)
	{
		id = _id;
		parameters = _parameters;
	}
	
	public int getId()
	{
		return id;
	}
	
	@SuppressWarnings("unchecked")
	public <P extends MenuEventParameters> P getParameters(Class<P> _paramType)
	{
		if(parameters!= null && _paramType.isInstance(parameters))
			return (P)parameters;
		return null;
	}

}
