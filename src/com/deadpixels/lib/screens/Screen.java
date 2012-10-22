package com.deadpixels.lib.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;

public abstract class Screen {
	
	protected final String 				name;
	protected final ScreenManager 		manager;
	protected 		boolean				loaded;
	protected final InputMultiplexer 	inputMultiplexer;
	protected		boolean				hasBeenActivated;
	
    private float updateDt;
	
	public Screen(String _name, ScreenManager _Manager)
	{
		if(_Manager == null) 
			throw new IllegalArgumentException("Screen : ScreenManager must not be null");
		
		if(_name != null)
			name = _name;
		else
			name = this.getClass().getSimpleName();
		manager = _Manager;
    	updateDt = -1.0f;
    	loaded = false;
    	hasBeenActivated = false;
    	inputMultiplexer = new InputMultiplexer();
	}
	
	public ScreenManager getManager()
	{
		return manager;
	}
	
    public void setUpdateFPS(int _FPS)
    {
    	updateDt = 1.0f / _FPS;
    }
    
    public Boolean isLoaded()
    {
    	return loaded;
    }
    
    public float getUpdateDt()
    {
    	return updateDt;
    }
    
    public InputMultiplexer getInputMultiplexer()
    {
    	return inputMultiplexer;
    }
    
    private void log(String _message)
    {
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
    
    public void activate(boolean _activate)
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
    
    public void pause(boolean _pause)
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
    
    public void dispose()
    {
    	log("onDispose");
    	onDispose();
    }
    
    protected 		abstract void onDispose ();
    protected 		abstract void onUpdate (float _fDt);
    protected 		abstract void onRender (float _fDt);
    protected		abstract void onLoad ();
    protected		abstract void onUnload ();
    protected 		abstract void onResize (int width, int height);
    protected 		abstract void onFirstActivation();
    protected 		abstract void onActivation();
    protected 		abstract void onDeactivation();
    protected 		abstract void onPause ();
    protected 		abstract void onResume ();
}
