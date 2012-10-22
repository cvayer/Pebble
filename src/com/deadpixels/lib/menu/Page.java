package com.deadpixels.lib.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

public abstract class Page
{
	private	  final	Table	   table;
	protected  		Menu 	   menu;
	protected 		Page   backPage;
	private   		boolean	   hasBeenActivatedOnce;
	private			boolean	   activated;
	
	private final 	Array<PageAnimation> 	pooledAnimations;
	private			PageAnimation			currentAnimation;
	
	private final	BackAnimation				backAnimation;
	
	public Page()
	{
		super();
		
		hasBeenActivatedOnce = false;
		
		backPage = null;
		
		pooledAnimations = new Array<PageAnimation>(true, 2);
		currentAnimation = null;
		
		backAnimation = new BackAnimation();
		
		table = new Table();
	}
	
	protected void setMenu(Menu _menu)
	{
		if(_menu == null)
			throw new GdxRuntimeException("Menu::SetMenu : _menu must not be null");
		menu = _menu;
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
	
	final void activate(boolean _activate, Page _backPage)
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
	
	public final void sendEvent(int _id, MenuEventParameters _parameters)
	{
		menu.sendEvent(_id, _parameters);
	}
	
	public final void sendEvent(int _id)
	{
		menu.sendEvent(_id);
	}
	
	final void handleEvent(MenuEvent _event)
	{
		onEvent(_event);	
	}
	
	protected abstract void onDispose();
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
	
	public final void addPageAnimation(PageAnimation _animation)
	{
		if(_animation == null)
			return;
		
		pooledAnimations.add(_animation);
		_animation.onAdd();
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
	
	protected PageAnimation getBackAnimation()
	{
		return backAnimation;
	}
	
	class BackAnimation extends PageAnimation
	{
		@Override
		public void onStart() {
			onBack();	
		}

		@Override
		public void onEnd() {
			menu.setCurrentPage(backPage, false);	
		}

		@Override
		public void onAdd() {
			
		}
	}
}
