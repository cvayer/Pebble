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
import com.mangecailloux.pebble.vibration.VibrationManager;
import com.mangecailloux.pebble.webpage.WebPageManager;

public abstract class ScreenManager extends Debuggable implements ApplicationListener {
    private Screen  	 screen;
    private Screen  	 nextScreen;
    private boolean		 changeScreenPending;
    private boolean 	 manageScreenDisposal;
    private boolean		 appCreated;
    
    public ScreenManager(ScreenManagerParameters _parameters)
    {
    	super();
    	// Pebble creation
    	Pebble.assets = new AssetsManager();
    	Pebble.musics = new MusicManager();
    	Pebble.sounds = new SoundManager();
    	Pebble.languages = new LanguagesManager();
    	Pebble.vibrations = new VibrationManager();
    	Pebble.ads = new AdsManager();
    	Pebble.webpages = new WebPageManager();
    
    	manageScreenDisposal = true;
    	appCreated = false;
    	
		Pebble.ads.setInterface(_parameters.adsInterface);
		Pebble.webpages.setInterface(_parameters.webPageInterface);
    }
    
    public void manageScreenDisposal(boolean _Manage)
    {
    	manageScreenDisposal = _Manage;
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
    		Pebble.assets.dispose();
    		
    		Pebble.assets 		= null;
        	Pebble.musics 		= null;
        	Pebble.sounds 		= null;
        	Pebble.languages 	= null;
        	Pebble.vibrations 	= null;
        	Pebble.ads 			= null;
        	Pebble.webpages 	= null;
    }
    
    @Override
    public void create ()
    {
    	appCreated = true;
    	setScreen(getInitialScreen());
    	onDebug (isDebug()); 
    }
    
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
    	
            if (screen != null)
            {
            	screen.render();
            }
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
            }
            
            screen = _screen;
            
            if(screen != null)
            {
	            Gdx.input.setInputProcessor(screen.getInputMultiplexer());
		        screen.activate(true);
		        screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            }
        }
    }

    public Screen getScreen () {
            return screen;
    }
}
