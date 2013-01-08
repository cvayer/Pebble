package com.mangecailloux.pebble.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.math.MathUtils;
import com.mangecailloux.pebble.debug.Debuggable;

public abstract class Screen  extends Debuggable {
	
	protected final String 				name;
	protected final ScreenManager 		manager;
	protected 		boolean				loaded;
	protected final InputMultiplexer 	inputMultiplexer;
	protected		boolean				hasBeenActivated;
	
    private 		float updateDt;
    private 		float maxUpdateDt;
    private 		float updateTimer;
	
	public Screen(String _name, ScreenManager _Manager)
	{
		super();
		
		if(_Manager == null) 
			throw new IllegalArgumentException("Screen : ScreenManager must not be null");
		
		if(_name != null)
			name = _name;
		else
			name = this.getClass().getSimpleName();
		manager = _Manager;
		
		updateTimer = 0.0f;
    	updateDt = -1.0f;
    	maxUpdateDt = 1/24.0f;
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
    	if(_FPS > 0)
    		updateDt = 1.0f / _FPS;
    }
    
    public void setMaxUpdateFPS(int _FPS)
    {
    	if(_FPS > 0)
    		maxUpdateDt = 1.0f / _FPS;
    }
    
    public Boolean isLoaded()
    {
    	return loaded;
    }
    
    public float getUpdateDt()
    {
    	return updateDt;
    }
    
    public float getMaxUpdateDt()
    {
    	return maxUpdateDt;
    }
    
    public InputMultiplexer getInputMultiplexer()
    {
    	return inputMultiplexer;
    }
    
    public void render()
    {
    	float fDt = manager.getDt();
    	
    	if(updateDt <= 0.0f)
    	{
    		float maxedDt = MathUtils.clamp(fDt, 0.0f, maxUpdateDt);
    		onUpdate(maxedDt);
    	}
    	else
    	{
    		float maxedDt = MathUtils.clamp(fDt, 0.0f, maxUpdateDt);
    		updateTimer -= maxedDt;
        	if(updateTimer <= 0.0f)
        	{
        		updateTimer += updateDt;
        		onUpdate(updateDt);	
        	}
    	}
    		
    	onRender(fDt);
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
    		updateTimer = -1.0f;
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
