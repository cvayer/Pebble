package com.deadpixels.lib.screens.menu;

import com.deadpixels.lib.menu.Menu;
import com.deadpixels.lib.screens.Screen;

/** Convenience class to access easily the current screen the menu is in */
public class ScreenMenu extends Menu {
	
	private final Screen	screen;
	
	public ScreenMenu(Screen _screen) 
	{
		screen = _screen;
	}
	
	public Screen getScreen()
	{
		return screen;
	}

}
