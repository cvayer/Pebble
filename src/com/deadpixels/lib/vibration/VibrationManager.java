package com.deadpixels.lib.vibration;

import com.badlogic.gdx.Gdx;

public class VibrationManager {

	// Singleton part
		public static  VibrationManager  get() {
	        if (null == m_Instance) { 
	        	m_Instance = new VibrationManager();
	        }
	        return m_Instance;
	    }
	    private static VibrationManager m_Instance;
	    
	    public static boolean getDefaultVibrationState()
	    {
	    	return true;
	    }

	    private final int hapticDuration = 100;
	    private boolean activated;
	    
	    private VibrationManager()
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
