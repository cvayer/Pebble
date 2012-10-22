package com.deadpixels.lib.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deadpixels.lib.menu.Menu;
import com.deadpixels.lib.menu.Page;
import com.deadpixels.lib.screens.Screen;
import com.deadpixels.lib.screens.ScreenManager;

public abstract class MenuScreen extends Screen implements Menu.MenuListener {
	
	protected final Stage							stage;
	protected final ScreenMenu						menu;
	
	public MenuScreen(String _name, ScreenManager _Manager) {
		super(_name, _Manager);
		
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		
		menu = new ScreenMenu(stage, this);
		
		menu.setListener(this);
		
		inputMultiplexer.addProcessor(stage);
		inputMultiplexer.addProcessor(menu.getInputListener());
	}
	
	public ScreenMenu getMenu()
	{
		return menu;
	}
	
	protected abstract String 	getStartPageKey();
	
	public void onPageChange(Page _oldPage, Page _newPage)
	{
		
	}

	@Override
	public void onDispose() {
		menu.dispose();
		stage.dispose();
	}

	@Override
	protected void onUpdate(float _fDt) {
		stage.act(_fDt);
		
		menu.update(_fDt);
	}

	//! Renders the stage
	@Override
	protected void onRender(float _fDt) {
		stage.draw();
		// TODO  : remove the drawdebug
		Table.drawDebug(stage);
	}

	@Override
	protected void onResize(int width, int height) {
		stage.setViewport(width, height, false);
		menu.resize(width, height);
	}
	
	@Override
	protected void onFirstActivation() {
	}

	@Override
	protected void onActivation() {
		menu.setCurrentPageByKey(getStartPageKey());
	}

	@Override
	protected void onDeactivation() {
		menu.setCurrentPage(null);
		stage.clear();
	}
}
