package com.mangecailloux.menu;

import com.badlogic.gdx.utils.Pool.Poolable;


public class MenuLayer implements Poolable
{
	private		  	PageDescriptor<? extends Page>			currentDescriptor;
	protected 	    Page					  				currentPage;
	private			MenuListener							listener;
	private			Menu									menu;
	private			int										index;
	
	public MenuLayer()
	{
		reset();
	}
	
	@Override
	public void reset() {
		menu = null;
		currentDescriptor = null;
		currentPage = null;
		listener = null;
		index = 0;
	}
	
	void init(Menu _menu, int _index)
	{
		menu = _menu;
		index = _index;
	}
	
	void setListener(MenuListener _listener)
	{
		listener = _listener;
	}
	
	boolean canBeRemoved()
	{
		return currentDescriptor == null;
	}
	
	public PageDescriptor<? extends Page> getCurrentPage()
	{
		return currentDescriptor;
	}
	
	void setCurrentPage(PageDescriptor<? extends Page> _descriptor, boolean _backPage)
	{
		if(menu == null)
			return;
		
		if(_descriptor != currentDescriptor)
		{
			if(currentDescriptor != null && currentPage != null)
			{
				menu.getRoot().removeActor(currentPage.group());
				currentPage.deactivate();
				if(listener != null)
					listener.onPageClose(currentPage, currentDescriptor);
			}
			
			PageDescriptor<? extends Page> oldDesc  = currentDescriptor;
			currentDescriptor = _descriptor;
			if(currentDescriptor != null)
				currentPage = menu.getPage(currentDescriptor.getType());
			else
				currentPage = null;
			
			if(currentDescriptor != null && currentPage != null)
			{
				menu.getRoot().addActorAt(index, currentPage.group());
				
				PageDescriptor<? extends Page> backDesc = _backPage ? oldDesc : null;
				currentPage.activate(this, currentDescriptor, backDesc);
				currentPage.resize((int)menu.getRoot().getWidth(), (int)menu.getRoot().getHeight());
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
	
	private void open(PageDescriptor<? extends Page> _descriptor, boolean _isBackPage)
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
	
	void update(float _fDt)
	{
		if(currentPage != null)
			currentPage.update(_fDt);
	}
	
	void render(float _fDt)
	{
		if(currentPage != null)
			currentPage.render(_fDt);
	}
	
	void resize(int width, int height) 
	{
		if(currentPage != null)
		{
			currentPage.resize(width, height);
		}
	}
	
	public boolean back()
	{
		if(currentPage != null)
		{
			currentPage.back();
			return true;
		}
		return false;
	}
}
