package com.mangecailloux.pebble.vibration;

import com.badlogic.gdx.Gdx;

public class VibrationManager {
	    
	    public static boolean getDefaultVibrationState()
	    {
	    	return true;
	    }

	    private final int hapticDuration = 20;
	    private boolean activated;
	    
	    public VibrationManager()
	    {
	    	activated = true;
	    }
	    
	    public void activate(boolean _activate)
	    {
	    	if(activated != _activate)
	    	{
	    		activated = _activate;
	    	}
	    }
	    
	    public void toggleVibrations()
	    {
	    	activate(!activated);
	    }
	    
	    public boolean isActivated()
	    {
	    	return activated;
	    }
	    
	    public void vibrate(int _millisec)
	    {
	    	if(isActivated())
	    		Gdx.input.vibrate(_millisec);
	    }
	    
	    public void haptic()
	    {
	    	vibrate(hapticDuration);
	    }
}
