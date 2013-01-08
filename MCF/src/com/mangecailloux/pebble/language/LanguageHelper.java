package com.mangecailloux.pebble.language;

public class LanguageHelper {

	public static String getOnOff(boolean _on)
	{
		if(_on)
    		return LanguagesManager.get().getString("On");
    	else
    		return LanguagesManager.get().getString("Off");		
	}
}
