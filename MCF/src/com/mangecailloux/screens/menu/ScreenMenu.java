package com.mangecailloux.screens.menu;

import com.mangecailloux.menu.Menu;
import com.mangecailloux.screens.Screen;

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
