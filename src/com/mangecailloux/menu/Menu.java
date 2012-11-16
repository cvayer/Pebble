package com.mangecailloux.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.ObjectMap.Values;

public class Menu {
	
	private final 	WidgetGroup								root;
	private final	ObjectMap<Class<? extends Page>, Page> 	pages;
	private	  final InputListener							inputListener;
	private   		Drawable 								defaultPageBackground;
	private			MenuListener							listener;
	
	private final	Array<MenuLayer>						layers;
	
	public Menu() 
	{
		root = new WidgetGroup();
		root.setTouchable(Touchable.childrenOnly);
		inputListener = new InputListener();
	
		setCatchBackKey(true);
		
		pages = new ObjectMap<Class<? extends Page>, Page>(4);
		
		layers = new Array<MenuLayer>(true, 2);

		defaultPageBackground = null;
		
		root.setFillParent(true);
	}
	
	public WidgetGroup getRoot()
	{
		return root;
	}
	
	public InputListener getInputListener()
	{
		return inputListener;
	}
	
	public void setCatchBackKey(boolean _catch)
	{
		Gdx.input.setCatchBackKey(_catch);
	}
	
	public void setListener(MenuListener _listener)
	{
		if(listener != _listener)
		{
			listener = _listener;
			for(int i=0; i < layers.size; ++i)
			{
				MenuLayer layer = layers.get(i);
				if(layer !=null)
					layer.setListener(listener);
			}
		}
	}
	
	public void addPage(Page _page)
	{
		if(_page == null)
			return;
		
		setDefaultPageBackground(defaultPageBackground, _page);
		
		_page.setMenu(this);
		pages.put(_page.getClass(), _page);
	}
	
	@SuppressWarnings("unchecked")
	<T extends Page> T getPage(Class<T> _type)
	{
		if(pages.containsKey(_type))
		{
			return (T)pages.get(_type);
		}
		return null;
	}
	
	void addNewLayer()
	{
		MenuLayer layer = Pools.obtain(MenuLayer.class);
		
		layers.add(layer);
		
		layer.init(this, (layers.size - 1));
		layer.setListener(listener);
	}
	
	public void open(PageDescriptor<? extends Page> _descriptor)
	{
		open(_descriptor, false);
	}
	
	public void openPopUp(PageDescriptor<? extends Page> _descriptor)
	{
		open(_descriptor, true);
	}
	
	private void open(PageDescriptor<? extends Page> _descriptor, boolean _popUp)
	{
		if(layers.size == 0 || _popUp)
		{
			addNewLayer();
		}
		
		MenuLayer layer = layers.peek();
		if(layer != null)
			layer.open(_descriptor);
	}
	
	public PageDescriptor<? extends Page> getCurrentPage()
	{
		if(layers.size > 0)
		{
			MenuLayer layer = layers.peek();
			if(layer != null)
				return layer.getCurrentPage();
		}
		return null;
	}
	
	public void close()
	{	
		if(layers.size > 0)
		{
			MenuLayer layer = layers.peek();
			if(layer != null)
				layer.close();
		}
	}
	
	public void back()
	{	
		if(layers.size > 0)
		{
			MenuLayer layer = layers.peek();
			if(layer != null)
				layer.back();
		}
	}
	
	public void closeAll()
	{	
		for(int i=0; i < layers.size; ++i)
		{
			MenuLayer layer = layers.get(i);
			if(layer !=null)
				layer.close();
		}
	}
	
	public void clear()
	{	
		for(int i=0; i < layers.size; ++i)
		{
			MenuLayer layer = layers.get(i);
			if(layer !=null)
				layer.setCurrentPage(null, false);
			
			Pools.free(layer);
		}
		layers.clear();
	}
	
	protected void setDefaultPageBackground(Drawable _background, Page _page)
	{
		if(_page != null && _page.canUseDefaultBackground())
		{
			// We want to set the default background
			if(_background != null)
			{
				// We set it if the table has not a background already
				if(_page.background().getBackground() == null)
					_page.background().setBackground(_background);
			}
			else // We want to remove the default background
			{
				// We remove it only if the table has the default background as a background
				if(_page.background().getBackground() == defaultPageBackground)
					_page.background().setBackground(null);
			}
		}
	}
	
	public Drawable getDefaultPagesBackground()
	{
		return defaultPageBackground;
	}
		
	public void setDefaultPagesBackground(Drawable _background)
	{
		if(defaultPageBackground == _background)
			return;
		
		Values<Page> values = pages.values();
		
		while(values.hasNext())
    	{
			Page page = values.next();
			
			setDefaultPageBackground(_background, page);
    	}

		defaultPageBackground = _background;	
	}
	
	public void update(float _fDt) 
	{	
		for(int i=0; i < layers.size; ++i)
		{
			MenuLayer layer = layers.get(i);
			
			if(layer.canBeRemoved())
			{
				layers.removeIndex(i);
				Pools.free(layer);
				--i;
			}
		}
		
		for(int i=0; i < layers.size; ++i)
		{
			MenuLayer layer = layers.get(i);
			if(layer !=null)
				layer.update(_fDt);
		}
	}
	
	public void render(float _fDt) 
	{			
		for(int i=0; i < layers.size; ++i)
		{
			MenuLayer layer = layers.get(i);
			if(layer !=null)
				layer.render(_fDt);
		}
	}
	
	public void resize(int width, int height) 
	{		
		for(int i=0; i < layers.size; ++i)
		{
			MenuLayer layer = layers.get(i);
			if(layer !=null)
				layer.resize(width, height);
		}
	}
	
	public void dispose()
	{
		clear();
		Values<Page> values = pages.values();
		
		while(values.hasNext())
    	{
			Page page = values.next();
			page.dispose();
    	}
		pages.clear();
	}
	
	// Events	
	public final void sendEvent(int _id, MenuEventParameters _parameters)
	{
		MenuEvent event = Pools.obtain(MenuEvent.class);
		event.setParameters(_id, _parameters);
		
		Values<Page> values = pages.values();
		
		while(values.hasNext())
    	{
			Page page = values.next();
			page.handleEvent(event);
    	}
		
		Pools.free(event);
	}
	
	public final void sendEvent(int _id)
	{
		sendEvent(_id, null);
	}
		
	//-------------------------------------------------------------------
	//----  InputListener
	//-------------------------------------------------------------------
	class InputListener extends InputAdapter
	{
		public boolean keyDown (int keycode) {
			
			if(keycode == Input.Keys.BACK || keycode == Keys.BACKSPACE)
			{
				if(layers.size > 0)
				{
					return layers.peek().back();
				}
			}
			return false;
		}		
	}
}
