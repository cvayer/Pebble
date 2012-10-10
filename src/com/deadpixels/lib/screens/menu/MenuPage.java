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
	
	public void resize(int _width, int _height)
	{
		setWidth(_width);
		setHeight(_height);
		onResize(_width, _height);
		invalidate();
	}
	
	protected 	abstract void onResize(int _width, int _height);
	protected 	abstract void onFirstActivation();
	public 		abstract void update(float _fDt);
	public 		abstract void onDeactivation();
	public 		abstract void onActivation();	
}
