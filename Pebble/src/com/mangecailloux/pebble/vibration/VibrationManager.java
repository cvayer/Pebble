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
