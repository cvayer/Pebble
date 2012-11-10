package com.mangecailloux.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pools;

public abstract class Page
{
	public static final String OPEN 	= "Open";
	public static final String CLOSE 	= "Close";
	public static final String BACK 	= "Back";
	
	private	  final	Table	   	table;
	protected  		Menu 	   	menu;
	protected		MenuLayer	menuLayer;
	protected 		PageDescriptor<? extends Page>   		backDescriptor;
	protected 		PageDescriptor<? extends Page>   		nextDescriptor;
	private   		boolean	   	hasBeenActivatedOnce;
	private			boolean	   	activated;
	
	private final 	Array<KeyPageAnimationPair> 					pooledAnimations;
	private			KeyPageAnimationPair							currentAnimation;
	private	final 	ObjectMap<String, PageAnimation> 				animations;
	
	private			boolean											canUseDefaultBackground;
	
	public Page()
	{
		this(true);
	}
	
	public Page(boolean _fillStage)
	{
		super();
		
		hasBeenActivatedOnce = false;
		canUseDefaultBackground = true;
		
		backDescriptor = null;
		nextDescriptor = null;
		
		animations = new ObjectMap<String, PageAnimation>(4);
		
		pooledAnimations = new Array<KeyPageAnimationPair>(true, 2);
		currentAnimation = null;
		
		table = new Table();
		table.setFillParent(_fillStage);
	}
	
	public void setCanUseDefaultBackground(boolean _canUse)
	{
		canUseDefaultBackground = _canUse;
	}
	
	public boolean canUseDefaultBackground()
	{
		return canUseDefaultBackground;
	}
	
	public void registerAnimation(String _key, PageAnimation _animation)
	{
		if(_key == null || _animation == null)
			return;
		animations.put(_key, _animation);
	}
	
	public void unregisterAnimation(String _key)
	{
		if(_key == null)
			return;
		
		PageAnimation animation = getAnimation(_key);
		if(animation != null)
		{
			animations.put(_key, null);
		}
	}
	
	protected void setNextPage(PageDescriptor<? extends Page> _next)
	{
		nextDescriptor = _next;
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
		onResize(_width, _height);
	}
	
	final void update(float _fDt)
	{
		// We have a running animation
		if(currentAnimation != null)
		{
			currentAnimation.animation.onUpdate(_fDt);
			if(currentAnimation.animation.doesNeedToEnd())
			{
				currentAnimation.animation.end();
				
				String key = currentAnimation.key;
				
				Pools.free(currentAnimation);
				currentAnimation = null;
				
				if(menuLayer != null)
				{
					if(key == CLOSE)
					{
						menuLayer.setCurrentPage(nextDescriptor, true);
					}
					else if(key == BACK)
					{
						menuLayer.setCurrentPage(backDescriptor, false);
					}
				}
			}
		}
		
		if(currentAnimation == null)
		{
			if(pooledAnimations.size > 0)
			{
				currentAnimation = pooledAnimations.first();
				currentAnimation.animation.start(this);
				if(currentAnimation.key == BACK)
				{
					onBack();
				}
				pooledAnimations.removeIndex(0);
			}
		}
		
		onUpdate(_fDt);
	}
	
	final void render(float _fDt)
	{
		onRender(_fDt);
		if(currentAnimation != null)
		{
			currentAnimation.animation.onRender(_fDt);
		}
	}
	
	private final void firstActivation(PageDescriptor<? extends Page> _descriptor)
	{
		if(!hasBeenActivatedOnce)
		{
			onFirstActivation(_descriptor);
			hasBeenActivatedOnce = true;
		}
	}
	
	final void activate(MenuLayer _menuLayer, PageDescriptor<? extends Page> _descriptor, PageDescriptor<? extends Page> _backDescriptor)
	{
		if(!activated)
		{
			activated = true;

			if(_backDescriptor != null)
				backDescriptor = _backDescriptor;
			
			menuLayer = _menuLayer;
			firstActivation(_descriptor);
			onActivation(_descriptor);

		}
	}
	
	final void deactivate()
	{
		if(activated)
		{
			menuLayer = null;
			activated = false;
			clearCurrentAnimations();
			onDeactivation();
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
	
	protected final void dispose()
	{
		clearCurrentAnimations();
		onDispose();
	}
	
	protected abstract void onDispose();
	protected abstract void onResize(int _width, int _height);
	protected abstract void onFirstActivation(PageDescriptor<? extends Page> _descriptor);
	protected abstract void onUpdate(float _fDt);
	protected abstract void onRender(float _fDt);
	protected abstract void onDeactivation();
	protected abstract void onActivation(PageDescriptor<? extends Page> _descriptor);
	protected abstract void onEvent(MenuEvent _event);
	protected abstract void onBack();
	
	// Animations

	protected PageAnimation getAnimation(String _key)
	{
		if(animations.containsKey(_key))
			return animations.get(_key);
		return null;
	}
	
	public final  boolean isAnAnimationRunning()
	{
		return currentAnimation != null;
	}
	
	public final boolean playAnimation(String _key)
	{
		if(_key == null)
			return false;
		
		PageAnimation animation = getAnimation(_key);
		
		if(animation == null)
			return false;
		
		KeyPageAnimationPair pair = Pools.obtain(KeyPageAnimationPair.class);
		pair.key = _key;
		pair.animation = animation;
		pooledAnimations.add(pair);
		animation.onAdd();
		return true;
	}
	
	private final void clearCurrentAnimations()
	{
		for(int i=0; i < pooledAnimations.size; ++i)
		{
			Pools.free(pooledAnimations.get(i));
		}

		pooledAnimations.clear();
		if(currentAnimation != null)
		{
			Pools.free(currentAnimation);
			currentAnimation = null;
		}
	}
	
	public void back()
	{
		if(backDescriptor != null)
		{
			if( ! playAnimation(BACK) )
			{
				onBack();
				if(menuLayer != null)
					menuLayer.setCurrentPage(backDescriptor, false);
			}
		}
		else
		{
			onBack();
		}
	}	
}
