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

public abstract class PageAnimation 
{

	private boolean isRunning;
	private boolean notifyEnd;
	private Page page;
	
	private final EndFlag endFlag; // Convenience runnable to add to actions when the animation is finished

	public PageAnimation()
	{
		isRunning = false;
		notifyEnd = false;
		
		endFlag = new EndFlag();
	}
	
	public void notifyEnd() 
	{ 
		notifyEnd = true; 
	}
	
	public Page getPage()
	{
		return page;
	}
	
	public Runnable getEndFlag()
	{
		return endFlag;
	}
	
	protected boolean doesNeedToEnd()
	{
		return notifyEnd;
	}
	
	protected void start(Page _page)
	{
		if(!isRunning)
		{
			page = _page;
			isRunning = true;
			onStart();
		}
	}
	
	protected void end()
	{
		if(isRunning)
		{
			page = null;
			isRunning = false;
			notifyEnd = false;
			onEnd();
		}
	}
	
	public boolean isRunning()
	{
		return isRunning;
	}
	
	public void onAdd()
	{
		
	}
	
	public void onEnd()
	{
		
	}
	
	public void onUpdate(float _fDt)
	{
		
	}
	
	public abstract void onStart();
	
	
	public class EndFlag implements Runnable
	{
		@Override
		public void run() {
			notifyEnd();
		}
		
	}

	public void onRender(float _fDt) {
		
	}
	
}
