package com.mangecailloux.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mangecailloux.menu.MenuListener;
import com.mangecailloux.menu.Page;
import com.mangecailloux.menu.PageDescriptor;
import com.mangecailloux.screens.Screen;
import com.mangecailloux.screens.ScreenManager;

public abstract class MenuScreen extends Screen implements MenuListener {
	
	protected final Stage							stage;
	protected final ScreenMenu						menu;
	
	public MenuScreen(String _name, ScreenManager _Manager) {
		super(_name, _Manager);
		
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		
		menu = new ScreenMenu(this);
		
		menu.addToStage(stage);
		
		menu.setListener(this);
		
		inputMultiplexer.addProcessor(stage);
		inputMultiplexer.addProcessor(menu.getInputListener());
	}
	
	public ScreenMenu getMenu()
	{
		return menu;
	}
	
	public Stage getStage()
	{
		return stage;
	}
	
	@Override
	protected void onDebug (boolean _debug)
	{
	}
	
	protected abstract PageDescriptor<? extends Page> 	getStartPageDescriptor();
	
	@Override
	public void onPageOpen(Page _Page, PageDescriptor<? extends Page> _descriptor)
	{
		
	}
	
	@Override
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
		menu.render(_fDt);
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
