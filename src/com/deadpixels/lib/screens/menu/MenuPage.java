package com.deadpixels.lib.screens.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;

public abstract class MenuPage
{
	private	  final	Table	   table;
	protected final MenuScreen screen;
	protected 		MenuPage   backPage;
	private   		boolean	   hasBeenActivatedOnce;
	private			boolean	   activated;
	
	private final 	Array<MenuPageAnimation> 	pooledAnimations;
	private			MenuPageAnimation			currentAnimation;
	
	private final	BackAnimation				backAnimation;
	
	public MenuPage(MenuScreen _screen)
	{
		super();
		screen = _screen;
		hasBeenActivatedOnce = false;
		
		backPage = null;
		
		pooledAnimations = new Array<MenuPageAnimation>(true, 2);
		currentAnimation = null;
		
		backAnimation = new BackAnimation();
		
		table = new Table();
	}
	
	void setDefaultBackground(Drawable _background)
	{
		if(_background != null)
		{
			table.setBackground(_background);
		}
	}
	
	protected final Table table()
	{
		return table;
	}	
	
	final void resize(int _width, int _height)
	{
		table.setWidth(_width);
		table.setHeight(_height);
		onResize(_width, _height);
		table.invalidate();
	}
	
	final void update(float _fDt)
	{
		// We have a running animation
		if(currentAnimation != null)
		{
			if(currentAnimation.needToEnd())
			{
				currentAnimation.end();
				currentAnimation = null;
			}
		}
		
		if(currentAnimation == null)
		{
			if(pooledAnimations.size > 0)
			{
				currentAnimation = pooledAnimations.first();
				currentAnimation.start(this);
				pooledAnimations.removeIndex(0);
			}
		}
		
		onUpdate(_fDt);
	}
	
	private final void firstActivation()
	{
		if(!hasBeenActivatedOnce)
		{
			onFirstActivation();
			hasBeenActivatedOnce = true;
		}
	}
	
	final void activate(boolean _activate, MenuPage _backPage)
	{
		if(activated != _activate)
		{
			activated = _activate;
			
			if(activated)
			{
				if(_backPage != null)
					backPage = _backPage;
				firstActivation();
				onActivation();
			}
			else
			{
				clearAnimations();
				onDeactivation();
			}
		}
	}
	
	final void handleEvent(MenuEvent _event)
	{
		onEvent(_event);	
	}
	
	protected abstract void onResize(int _width, int _height);
	protected abstract void onFirstActivation();
	protected abstract void onUpdate(float _fDt);
	protected abstract void onDeactivation();
	protected abstract void onActivation();
	protected abstract void onEvent(MenuEvent _event);
	
	protected void onBack()
	{
		backAnimation.notifyEnd();
	}
	
	// Animations
	
	public final void addPageAnimation(MenuPageAnimation _animation)
	{
		if(_animation == null)
			return;
		
		pooledAnimations.add(_animation);
	}
	
	private final void clearAnimations()
	{
		pooledAnimations.clear();
		currentAnimation = null;
	}
	
	public void back()
	{
		if(backPage != null)
			addPageAnimation(backAnimation);
		else
			onBack();
	}
	
	protected MenuPageAnimation getBackAnimation()
	{
		return backAnimation;
	}
	
	class BackAnimation extends MenuPageAnimation
	{
		@Override
		public void onStart() {
			onBack();	
		}

		@Override
		public void onEnd() {
			screen.setCurrentPage(backPage, false);	
		}
	}
}
