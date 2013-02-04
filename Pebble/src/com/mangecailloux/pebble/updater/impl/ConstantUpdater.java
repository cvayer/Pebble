package com.mangecailloux.pebble.updater.impl;

import com.badlogic.gdx.math.MathUtils;
import com.mangecailloux.pebble.updater.IUpdatePriority;
import com.mangecailloux.pebble.updater.Updater;

public abstract class ConstantUpdater extends Updater
{
	private float updateDt;
    private float maxUpdateDt;
    private float updateTimer;
	    
	public ConstantUpdater(IUpdatePriority _priority) 
	{
		super(_priority);
		
		updateTimer = 0.0f;
    	updateDt = -1.0f;
    	maxUpdateDt = 1/24.0f;
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
    
    public float getUpdateDt()
    {
    	return updateDt;
    }
    
    public float getMaxUpdateDt()
    {
    	return maxUpdateDt;
    }

	@Override
	public final void update(float _dt) 
	{
		float maxedDt = MathUtils.clamp(_dt, 0.0f, maxUpdateDt);
		if(updateDt <= 0.0f)
    	{
			doUpdate(maxedDt);
    	}
    	else
    	{
    		updateTimer -= maxedDt;
        	if(updateTimer <= 0.0f)
        	{
        		updateTimer += updateDt;
        		doUpdate(updateDt);	
        	}
    	}
	}
	
	public abstract void doUpdate(float _dt);

}
