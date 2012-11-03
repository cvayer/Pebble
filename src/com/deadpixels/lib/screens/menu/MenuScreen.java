package com.deadpixels.lib.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deadpixels.lib.menu.MenuListener;
import com.deadpixels.lib.menu.Page;
import com.deadpixels.lib.menu.PageDescriptor;
import com.deadpixels.lib.screens.Screen;
import com.deadpixels.lib.screens.ScreenManager;

public abstract class MenuScreen extends Screen implements MenuListener {
	
	protected final Stage							stage;
	protected final ScreenMenu						menu;
	
	public MenuScreen(String _name, ScreenManager _Manager) {
		super(_name, _Manager);
		
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		
		menu = new ScreenMenu(this);
		
		stage.addActor(menu.getRoot());
		
		menu.setListener(this);
		
		inputMultiplexer.addProcessor(stage);
		inputMultiplexer.addProcessor(menu.getInputListener());
	}
	
	public ScreenMenu getMenu()
	{
		return menu;
	}
	
	protected abstract PageDescriptor<? extends Page> 	getStartPageDescriptor();
	
	public void onPageOpen(Page _Page, PageDescriptor<? extends Page> _descriptor)
	{
		
	}
	
	public void onPageClose(Page _Page, PageDescriptor<? extends Page> _descriptor)
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
		menu.open(getStartPageDescriptor());
	}

	@Override
	protected void onDeactivation() {
		menu.clear();
		stage.clear();
	}
}
