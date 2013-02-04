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
package com.mangecailloux.pebble.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.mangecailloux.pebble.Pebble;
import com.mangecailloux.pebble.ads.AdsManager;
import com.mangecailloux.pebble.assets.AssetsManager;
import com.mangecailloux.pebble.audio.MusicManager;
import com.mangecailloux.pebble.audio.SoundManager;
import com.mangecailloux.pebble.debug.Debuggable;
import com.mangecailloux.pebble.language.LanguagesManager;
import com.mangecailloux.pebble.updater.UpdaterManager;
import com.mangecailloux.pebble.vibration.VibrationManager;
import com.mangecailloux.pebble.webpage.WebPageManager;

public abstract class ScreenManager extends Debuggable implements ApplicationListener 
{
    private 		Screen  	 			screen;
    private 		Screen  	 			nextScreen;
    private 		boolean		 			changeScreenPending;
    private 		boolean 	 			manageScreenDisposal;
    private 		boolean		 			appCreated;
    private final 	UpdaterManager 			updateManager;
    private final   ScreenManagerParameters parameters;
    
    public ScreenManager(ScreenManagerParameters _parameters)
    {
    	super();
    	
    	updateManager = new UpdaterManager();
    	manageScreenDisposal = true;
    	appCreated = false;
    	parameters = _parameters;
    }
    
    private void initPebble()
    {
    	// Pebble creation
    	Pebble.assets = new AssetsManager();
    	Pebble.musics = new MusicManager();
    	Pebble.sounds = new SoundManager();
    	Pebble.languages = new LanguagesManager();
    	Pebble.vibrations = new VibrationManager();
    	Pebble.ads = new AdsManager();
    	Pebble.webpages = new WebPageManager();
    	
    	Pebble.ads.setInterface(parameters.adsInterface);
		Pebble.webpages.setInterface(parameters.webPageInterface);
    }
    
    private void deinitPebble()
    {
    	Pebble.assets.dispose();
		
		Pebble.assets 		= null;
    	Pebble.musics 		= null;
    	Pebble.sounds 		= null;
    	Pebble.languages 	= null;
    	Pebble.vibrations 	= null;
    	Pebble.ads 			= null;
    	Pebble.webpages 	= null;
    }
    
    protected UpdaterManager getUpdateManager()
    {
    	return updateManager;
    }
    
    public void manageScreenDisposal(boolean _manage)
    {
    	manageScreenDisposal = _manage;
    }
    
    @Override
    protected 	void onDebug (boolean _debug) 
    {
    	if(Gdx.app != null)
    	{
	    	if(_debug)
	    		Gdx.app.setLogLevel(Application.LOG_DEBUG);
	    	else
	    		Gdx.app.setLogLevel(Application.LOG_ERROR);
    	}
    	
    	Pebble.assets.debug(_debug);
    }

    @Override
    public void dispose () {
    		applyNextScreen(null);
    		deinitPebble();
    }
    
    @Override
    public void create ()
    {
    	appCreated = true;
    	initPebble();
    	init();
    	setScreen(getInitialScreen());
    	onDebug (isDebug()); 
    }
    
    protected abstract void   init();
    protected abstract Screen getInitialScreen();

    @Override
    public void pause () {
            if (screen != null) 
            	screen.pause(true);
    }

    @Override
    public void resume () {
            if (screen != null) 
            	screen.pause(false);
    }
    
    public float getDt()
    {
    	return Gdx.graphics.getDeltaTime();
    }

    @Override
    public void render () {

    		if(changeScreenPending)
    		{
    			applyNextScreen(nextScreen);
    			changeScreenPending = false;
    		}
    		
    		updateManager.update(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void resize (int _width, int _height) {
            if (screen != null) 
            	screen.resize(_width, _height);
    }

    public void setScreen (Screen _screen) {
    	changeScreenPending = true;
    	nextScreen = _screen;
    }

    private void applyNextScreen (Screen _screen) {
    	if(appCreated && _screen != screen)
        {   
    		 if(_screen != null && !_screen.isLoaded())
             	_screen.load();
    		 
    		if (screen != null) 
            {
            	screen.activate(false);
            	screen.unload();
            	if(manageScreenDisposal)
            		screen.dispose();
            	screen.setManager(null);
            }
            
            screen = _screen;
            
            if(screen != null)
            {
	            Gdx.input.setInputProcessor(screen.getInputMultiplexer());
	            screen.setManager(this);
		        screen.activate(true);
		        screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            }
        }
    }

    public Screen getScreen () {
            return screen;
    }
}
