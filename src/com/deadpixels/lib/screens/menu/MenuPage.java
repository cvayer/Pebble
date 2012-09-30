package com.deadpixels.lib.screens.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

public abstract class MenuPage extends Table
{
	protected final MenuScreen screen;
	private   		boolean	   hasBeenActivated;
	
	public MenuPage(MenuScreen _screen)
	{
		super();
		screen = _screen;
		hasBeenActivated = false;
	}
	
	public void firstActivation()
	{
		if(!hasBeenActivated)
		{
			onFirstActivation();
			hasBeenActivated = true;
		}
	}
	
	protected 	abstract void onFirstActivation();
	public 		abstract void update(float _fDt);
	public 		abstract void onDeactivation();
	public 		abstract void onActivation();	
}
