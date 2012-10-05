package com.deadpixels.lib.screens.popup;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public abstract class PopUp 
{
	private PopUpManager manager;
	
	public abstract Actor getRoot();
	public abstract void  init(PopUpDescriptor<?> _desc, Skin _skin);
	public abstract void  open();
	public abstract void  close();
	public abstract void  resize(int _width, int _height);
	
	protected void closed()
	{
		if(manager != null)
			manager.onPopUpClosed(this);
	}
	
	protected void setManager(PopUpManager _manager)
	{
		manager = _manager;
	}
	
	protected PopUpManager getManager()
	{
		return manager;
	}
}
