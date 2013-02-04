/*******************************************************************************
 * Copyright 2013 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.mangecailloux.pebble.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mangecailloux.pebble.menu.MenuListener;
import com.mangecailloux.pebble.menu.Page;
import com.mangecailloux.pebble.menu.PageDescriptor;
import com.mangecailloux.pebble.screens.Screen;
import com.mangecailloux.pebble.screens.ScreenUpdatePriority;
import com.mangecailloux.pebble.updater.Updater;
import com.mangecailloux.pebble.updater.impl.ConstantUpdater;

public abstract class MenuScreen extends Screen implements MenuListener {
	
	protected final Stage							stage;
	protected final ScreenMenu						menu;
	
	public MenuScreen(String _name) 
	{
		super(_name);
		
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

	protected void onUpdate(float _fDt) {
		stage.act(_fDt);	
		menu.update(_fDt);
	}

	//! Renders the stage
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
	protected void onFirstActivation() 
	{
		addUpdater(update);
		addUpdater(render);
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
	
	Updater update = new ConstantUpdater(ScreenUpdatePriority.BeforeRender) 
	{
		@Override
		public void doUpdate(float _dt) 
		{
			onUpdate(_dt);
		}
	};
	
	Updater render = new Updater(ScreenUpdatePriority.Render) 
	{
		@Override
		public void update(float _dt) {
			onRender(_dt);
		}
	};
}
