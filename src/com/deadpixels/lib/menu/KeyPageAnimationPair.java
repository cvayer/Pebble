package com.deadpixels.lib.menu;

import com.badlogic.gdx.utils.Pool.Poolable;

public class KeyPageAnimationPair implements Poolable
{
	public String key;
	public PageAnimation animation;
	
	public KeyPageAnimationPair()
	{
		reset();
	}

	@Override
	public void reset() {
		key = null;
		animation = null;
	}
}