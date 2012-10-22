package com.deadpixels.lib.screens.menu;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deadpixels.lib.menu.Menu;
import com.deadpixels.lib.screens.Screen;

/** Convenience class to access easily the current screen the menu is in */
public class ScreenMenu extends Menu {
	
	private final Screen	screen;
	
	public ScreenMenu(Screen _screen) 
	{
		this(null, _screen);
	}
	
	public ScreenMenu(Stage _stage, Screen _screen) 
	{
		super(_stage);
		screen = _screen;
	}
	
	public Screen getScreen()
	{
		return screen;
	}

}
