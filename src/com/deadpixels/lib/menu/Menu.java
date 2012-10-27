package com.deadpixels.lib.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.ObjectMap.Values;

public class Menu {
	
	protected final WidgetGroup								root;
	protected final ObjectMap<String, Page> 				pages;
	protected 	    Page					  				currentPage;
	private	  final InputListener							inputListener;
	private   		Drawable 								defaultPageBackground;
	private			MenuListener							listener;
	
	public Menu() 
	{
		root = new WidgetGroup();
		inputListener = new InputListener();
	
		setCatchBackKey(true);
		
		pages = new ObjectMap<String, Page>(4);
		currentPage = null;
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
		listener = _listener;
	}
	
	public void addPage(String _name, Page _page)
	{
		if(_page == null)
			return;
		if(defaultPageBackground != null && _page.table().getBackground() == null)
			_page.table().setBackground(defaultPageBackground);
		
		_page.setMenu(this);
		pages.put(_name, _page);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Page> T getPage(String _key, Class<T> _type)
	{
		Page page = pages.get(_key);
		if(_type.isInstance(page))
			return (T)page;
		return null;
	}
	
	protected void setCurrentPage(Page _page, boolean _backPage)
	{
		if(_page != currentPage)
		{
			if(currentPage != null)
			{
				root.removeActor(currentPage.table());
				currentPage.activate(false, null);
				if(listener != null)
					listener.onPageClose(currentPage);
			}
			
			Page oldPage  = currentPage;
			currentPage = _page;
			
			if(currentPage != null)
			{
				root.addActor(currentPage.table());
				Page backPage = _backPage ? oldPage : null;
				currentPage.activate(true, backPage);
				currentPage.resize((int)root.getWidth(), (int)root.getHeight());
				if(listener != null)
					listener.onPageOpen(currentPage);
				currentPage.playAnimation(Page.OPEN);
			}
		}
	}
	
	public void open(String _name)
	{
		open(pages.get(_name), true);
	}
	
	public void close()
	{
		open(null, false);
	}
	
	public void clear()
	{
		setCurrentPage(null, false);	
	}
	
	protected void open(Page _page, boolean _isBackPage)
	{
		if(_page == currentPage)
			return;
		
		// there is no current Page
		if(currentPage == null)
		{
			setCurrentPage(_page, _isBackPage);		
		}
		else
		{
			// is there is no Close animation on the current page we set it
			if(!currentPage.playAnimation(Page.CLOSE))
				setCurrentPage(_page, _isBackPage);	
			else // There is a close animation we store the nextPage
			{
				currentPage.setNextPage(_page);
			}
		}
	}
		
	public void setDefaultPagesBackground(Drawable _background)
	{
		if(defaultPageBackground == _background)
			return;
		
		Values<Page> values = pages.values();
		
		while(values.hasNext())
    	{
			Page page = values.next();
			
			// We want to set the default background
			if(_background != null)
			{
				// We set it if the table has not a background already
				if(page.table().getBackground() == null)
					page.table().setBackground(_background);
			}
			else // We want to remove the default background
			{
				// We remove it only if the table has the default background as a background
				if(page.table().getBackground() == defaultPageBackground)
					page.table().setBackground(null);
			}
    	}

		defaultPageBackground = _background;	
	}
	
	public void update(float _fDt) 
	{
		if(currentPage != null)
			currentPage.update(_fDt);
	}
	
	public void resize(int width, int height) 
	{
		if(currentPage != null)
		{
			currentPage.resize(width, height);
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
				if(currentPage != null)
				{
					currentPage.back();
					return true;
				}
			}
			return false;
		}		
	}
}
