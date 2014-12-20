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
package com.mangecailloux.pebble.updater;

public abstract class Updater 
{
	private final IUpdatePriority priority;
	private 	  boolean paused;
	
	public Updater(IUpdatePriority _priority)
	{
		if(_priority == null)
			throw new IllegalArgumentException("Updater : _priority must not be null");
		
		priority = _priority;
		paused = false;
	}
	
	public IUpdatePriority getPriority()
	{
		return priority;
	}
	
	public void pause()
	{
		paused = true;
	}
	
	public void resume()
	{
		paused = false;
	}
	
	public boolean isPaused()
	{
		return paused;
	}
	
	public abstract void update(float _dt);
}
