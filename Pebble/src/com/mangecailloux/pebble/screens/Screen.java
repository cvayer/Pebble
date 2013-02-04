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
package com.mangecailloux.pebble.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.mangecailloux.pebble.debug.Debuggable;
import com.mangecailloux.pebble.updater.Updater;
import com.mangecailloux.pebble.updater.Updaters;

public abstract class Screen  extends Debuggable {
	
	protected final String 				name;
	protected       ScreenManager 		manager;
	protected 		boolean				loaded;
	protected final InputMultiplexer 	inputMultiplexer;
	protected		boolean				hasBeenActivated;
	private   final Updaters			updaters;
	
	public Screen(String _name)
	{
		super();
		
		if(_name != null)
			name = _name;
		else
			name = this.getClass().getSimpleName();
		
		manager = null;
		updaters = new Updaters();
    	loaded = false;
    	hasBeenActivated = false;
    	inputMultiplexer = new InputMultiplexer();
	}
	
	public ScreenManager getManager()
	{
		return manager;
	}
	
	protected void setManager(ScreenManager _manager)
	{
		if(manager != _manager)
		{
			manager = _manager;
			if(manager != null)
			{
				updaters.setManager(manager.getUpdateManager());
			}
			else
			{
				updaters.setManager(null);
			}
		}
	}
	
	public void addUpdater(Updater _updater)
	{
		updaters.addUpdater(_updater);
	}
    
    public Boolean isLoaded()
    {
    	return loaded;
    }
    
    public InputMultiplexer getInputMultiplexer()
    {
    	return inputMultiplexer;
    }
    
    public void log(String _message)
    {
    	if(isDebug())
    		Gdx.app.log("Screen : " + name, _message);
    }
    
    public final void load()
    {
    	if(!loaded)
    	{
    		log("onLoad");
    		onLoad();
    		loaded = true;
    	}
    }
    
    public final void unload()
    {
    	if(loaded)
    	{
    		log("onUnload");
    		onUnload();
    		loaded = false;
    	}
    }
    
    public void resize(int _width, int _height)
    {
    	log("onResize (" + _width + ", " + _height + ")");
    	onResize(_width, _height);
    }
    
    public final void activate(boolean _activate)
    {
    	if(_activate)
    	{
    		log("onActivation");
    		if(!hasBeenActivated)
    		{
    			onFirstActivation();
    			hasBeenActivated = true;
    		}
    		onActivation();
    	}
    	else
    	{
    		log("onDeactivation");
    		onDeactivation();
    	}
    }
    
    public final void pause(boolean _pause)
    {
    	if(_pause)
    	{
    		log("onPause");
    		onPause();
    	}
    	else
    	{
    		log("onResume");
    		onResume();
    	}
    }
    
    public final void dispose()
    {
    	log("onDispose");
    	onDispose();
    }
    
    protected 		abstract void onDispose ();
    protected		abstract void onLoad ();
    protected		abstract void onUnload ();
    protected 		abstract void onResize (int width, int height);
    protected 		abstract void onFirstActivation();
    protected 		abstract void onActivation();
    protected 		abstract void onDeactivation();
    protected 		abstract void onPause ();
    protected 		abstract void onResume ();
}
