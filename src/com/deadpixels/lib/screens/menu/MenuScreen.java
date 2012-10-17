package com.deadpixels.lib.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;
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
	private	  final InputListener					inputListener;
	
	public MenuScreen(String _name, ScreenManager _Manager) {
		super(_name, _Manager);
		
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		
		inputListener = new InputListener();
		
		inputMultiplexer.addProcessor(stage);
		inputMultiplexer.addProcessor(inputListener);
		
		Gdx.input.setCatchBackKey(true);
		
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
		setCurrentPage(_page, true);
	}
	
	protected void setCurrentPage(MenuPage _page, boolean _backPage)
	{
		if(_page != currentPage)
		{
			if(currentPage != null)
			{
				stage.getRoot().removeActor(currentPage.table());
				currentPage.activate(false, null);
			}
			
			MenuPage oldPage  = currentPage;
			currentPage = _page;
			
			if(currentPage != null)
			{
				stage.addActor(currentPage.table());
				currentPage.resize((int)stage.getWidth(), (int)stage.getHeight());
				MenuPage backPage = _backPage ? oldPage : null;
				currentPage.activate(true, backPage);
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

		public boolean keyUp (int keycode) {
			return false;
		}
		
	}
}
