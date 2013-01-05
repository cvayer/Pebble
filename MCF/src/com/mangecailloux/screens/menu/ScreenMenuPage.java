package com.mangecailloux.screens.menu;

import com.badlogic.gdx.assets.AssetManager;
import com.mangecailloux.menu.PageAdapter;
import com.mangecailloux.screens.Screen;
import com.mangecailloux.screens.ScreenManager;

/** Helper class to have an easy access to screen, screen manager and assetManager */
public class ScreenMenuPage extends PageAdapter
{
	public ScreenMenuPage()
	{
		super();
	}
	
	public AssetManager getAssetManager()
	{
		ScreenManager manager = getScreenManager();
		if(manager != null)
			return manager.getAssetManager();
		return null;
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
