package com.deadpixels.lib.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.ObjectMap.Values;

public class Menu {
	
	private final 	WidgetGroup								root;
	private final	ObjectMap<Class<? extends Page>, Page> 	pages;
	private		  	PageDescriptor<? extends Page>			currentDescriptor;
	protected 	    Page					  				currentPage;
	private	  final InputListener							inputListener;
	private   		Drawable 								defaultPageBackground;
	private			MenuListener							listener;
	
	public Menu() 
	{
		root = new WidgetGroup();
		inputListener = new InputListener();
	
		setCatchBackKey(true);
		
		pages = new ObjectMap<Class<? extends Page>, Page>(4);
		currentPage = null;
		currentDescriptor = null;
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
	
	public void addPage(Page _page)
	{
		if(_page == null)
			return;
		if(defaultPageBackground != null && _page.table().getBackground() == null)
			_page.table().setBackground(defaultPageBackground);
		
		_page.setMenu(this);
		pages.put(_page.getClass(), _page);
	}
	
	@SuppressWarnings("unchecked")
	private <T extends Page> T getPage(Class<T> _type)
	{
		if(pages.containsKey(_type))
		{
			return (T)pages.get(_type);
		}
		return null;
	}
	
	protected void setCurrentPage(PageDescriptor<? extends Page> _descriptor, boolean _backPage)
	{
		if(_descriptor != currentDescriptor)
		{
			if(currentDescriptor != null && currentPage != null)
			{
				root.removeActor(currentPage.table());
				currentPage.activate(false, null, null);
				if(listener != null)
					listener.onPageClose(currentPage, currentDescriptor);
			}
			
			PageDescriptor<? extends Page> oldDesc  = currentDescriptor;
			currentDescriptor = _descriptor;
			if(currentDescriptor != null)
				currentPage = getPage(currentDescriptor.getType());
			else
				currentPage = null;
			
			if(currentDescriptor != null && currentPage != null)
			{
				root.addActor(currentPage.table());
				PageDescriptor<? extends Page> backDesc = _backPage ? oldDesc : null;
				currentPage.activate(true, currentDescriptor, backDesc);
				currentPage.resize((int)root.getWidth(), (int)root.getHeight());
				if(listener != null)
					listener.onPageOpen(currentPage, currentDescriptor);
				currentPage.playAnimation(Page.OPEN);
			}
		}
	}
	
	public void open(PageDescriptor<? extends Page> _descriptor)
	{
		open(_descriptor, true);
	}
	
	public void close()
	{
		open(null, false);
	}
	
	public void clear()
	{
		setCurrentPage(null, false);	
	}
	
	protected void open(PageDescriptor<? extends Page> _descriptor, boolean _isBackPage)
	{
		if(_descriptor == currentDescriptor)
			return;
		
		// there is no current Page
		if(currentDescriptor == null)
		{
			setCurrentPage(_descriptor, _isBackPage);		
		}
		else
		{
			// is there is no Close animation on the current page we set it
			if(!currentPage.playAnimation(Page.CLOSE))
				setCurrentPage(_descriptor, _isBackPage);	
			else // There is a close animation we store the nextPage
			{
				currentPage.setNextPage(_descriptor);
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
