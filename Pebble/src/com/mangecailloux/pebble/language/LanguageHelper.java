package com.mangecailloux.pebble.language;

import com.mangecailloux.pebble.Pebble;

public class LanguageHelper {

	public static String getOnOff(boolean _on)
	{
		if(_on)
    		return Pebble.languages.getString("On");
    	else
    		return Pebble.languages.getString("Off");		
	}
}
