package com.deadpixels.lib.screens.loading;

import com.deadpixels.lib.screens.Screen;
import com.deadpixels.lib.screens.ScreenManager;


public abstract class LoadingScreen extends Screen {
	
	protected final Screen 	screenToLoad;
	protected final boolean autoScreenChange;
	protected 		boolean	nextIsLoaded;
	private			float		timer;
	private final	float		duration;
	
	private LoadingScreen(String _name, ScreenManager _Manager, Screen _ToLoad, boolean _autoScreenChange, float _duration)
	{
		super(_name, _Manager);
		
		if(_ToLoad == null)
			throw new IllegalArgumentException("Screen to load must not be null");
		
		screenToLoad = _ToLoad;
		autoScreenChange = _autoScreenChange;
		nextIsLoaded = false;
		
		duration = _duration;
		timer = duration;
	}
	
	public LoadingScreen(String _name, ScreenManager _Manager, Screen _ToLoad, boolean _autoScreenChange)
	{
		this(_name, _Manager, _ToLoad, _autoScreenChange, -1.0f);
	}
	
	public LoadingScreen(String _name, ScreenManager _Manager, Screen _ToLoad, float _duration)
	{
		this(_name, _Manager, _ToLoad, true, _duration);
	}

	@Override
	protected void update(float _fDt) 
	{		
		if(screenToLoad != null && !nextIsLoaded)
		{
			if(manager.getAssetManager().update())
				nextIsLoaded = true;
		}
		
		if(timer >= 0.0f)
			timer -= _fDt;
		
		if(autoScreenChange && nextIsLoaded && timer < 0.0f)
			changeScreen();
	}
	
	@Override
	public void activate(boolean _activate)
    {
    	if(_activate)
    		timer = duration;
    	
    	super.activate(_activate);
    }
	
	protected void onLoad ()
	{
		if(screenToLoad != null)
			screenToLoad.load();
	}
	
	protected void onUnload ()
	{
		
	}
	
	protected void changeScreen()
	{
		manager.setScreen(screenToLoad);
	}
}
