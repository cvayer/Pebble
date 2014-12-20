package com.mangecailloux.pebble.screens;

import com.mangecailloux.pebble.updater.IUpdatePriority;

public enum ScreenUpdatePriority implements IUpdatePriority
{
	BeforeRender,
	Render,
	AfterRender;

	@Override
	public int getIndex() 
	{
		return ordinal();
	}

}
