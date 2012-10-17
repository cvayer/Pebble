package com.deadpixels.lib.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Values;
import com.badlogic.gdx.utils.Pools;
import com.deadpixels.lib.screens.Screen;
import com.deadpixels.lib.screens.ScreenManager;

public abstract class MenuScreen extends Screen {
	
	protected final Stage							stage;
	protected final ObjectMap<String, MenuPage> 	pages;
	protected 	    MenuPage					  	currentPage;
	
	public MenuScreen(String _name, ScreenManager _Manager) {
		super(_name, _Manager);
		
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		inputMultiplexer.addProcessor(stage);
		
		pages = new ObjectMap<String, MenuPage>(4);
		currentPage = null;
	}
	
	public void addPage(String _name, MenuPage _page)
	{
		pages.put(_name, _page);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends MenuPage> T getPage(String _key, Class<T> _type)
	{
		MenuPage page = pages.get(_key);
		if(_type.isInstance(page))
			return (T)page;
		return null;
	}
	
	public void setCurrentPageByKey(String _name)
	{
		setCurrentPage(pages.get(_name));
	}
	
	public void setCurrentPage(MenuPage _page)
	{
		if(_page != currentPage)
		{
			if(currentPage != null)
			{
				currentPage.onDeactivation();
			}
			
			MenuPage oldPage  = currentPage;
			currentPage = _page;
			
			if(currentPage != null)
			{
				stage.clear();
				stage.addActor(currentPage);
				currentPage.resize((int)stage.getWidth(), (int)stage.getHeight());
				currentPage.firstActivation();
				currentPage.onActivation();
			}
			
			onPageChange(oldPage, currentPage);
		}
	}
	
	protected abstract String 	getStartPageKey();
	protected abstract void 	onPageChange(MenuPage _oldPage, MenuPage _newPage);
	protected abstract void 	renderPreStage(float _fDt);
	protected abstract void 	renderPostStage(float _fDt);

	@Override
	public void dispose() {
		stage.dispose();
	}

	@Override
	protected void update(float _fDt) {
		stage.act(_fDt);
		
		if(currentPage != null)
			currentPage.update(_fDt);
	}

	@Override
	protected void render(float _fDt) {
		renderPreStage(_fDt);
		stage.draw();
		Table.drawDebug(stage);
		renderPostStage(_fDt);
	}

	@Override
	protected void onResize(int width, int height) {
		stage.setViewport(width, height, false);
		
		if(currentPage != null)
		{
			currentPage.resize(width, height);
		}

	}

	@Override
	
	protected void onActivation() {
		setCurrentPageByKey(getStartPageKey());
	}

	@Override
	protected void onDeactivation() {
		setCurrentPage(null);
		stage.clear();
	}
	
	// Events	
	public void sendEvent(int _id, MenuEventParameters _parameters)
	{
		MenuEvent event = Pools.obtain(MenuEvent.class);
		event.setParameters(_id, _parameters);
		
		Values<MenuPage> values = pages.values();
		
		while(values.hasNext())
    	{
			MenuPage page = values.next();
			page.handleEvent(event);
    	}
		
		Pools.free(event);
	}
	
	public void sendEvent(int _id)
	{
		sendEvent(_id, null);
	}
}
