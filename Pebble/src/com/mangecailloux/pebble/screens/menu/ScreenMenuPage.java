package com.mangecailloux.pebble.screens.menu;

import com.mangecailloux.pebble.menu.PageAdapter;
import com.mangecailloux.pebble.screens.Screen;
import com.mangecailloux.pebble.screens.ScreenManager;

/** Helper class to have an easy access to screen, screen manager and assetManager */
public class ScreenMenuPage extends PageAdapter
{
	public ScreenMenuPage()
	{
		super();
	}
	
	public ScreenManager getScreenManager()
	{   
		Screen screen = getScreen();
		if(screen != null)
			return screen.getManager();
		return null;
	}
	
	public Screen getScreen()
	{
		if(menu != null)
			return ((ScreenMenu)menu).getScreen();
		return null;
	}
}
