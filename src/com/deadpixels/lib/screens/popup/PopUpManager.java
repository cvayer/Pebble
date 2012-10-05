package com.deadpixels.lib.screens.popup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Values;


public class PopUpManager {
	// Singleton part
	public static  PopUpManager  get() {
        if (null == m_Instance) { 
        	m_Instance = new PopUpManager();
        }
        return m_Instance;
    }
    private static PopUpManager m_Instance;
    
    private		  boolean 			isOpen;
    private 	  PopUpListener 	listener;
    
    private final ObjectMap<Class<?>, PopUp> 	popUps;
    private		  PopUpDescriptor<?>			currentDescriptor;
    private		  PopUp							currentPopUp;
    private		  Stage							stage;
    private		  Skin							skin;
    private 	  AssetManager					assetManager;
        
    private PopUpManager()
    {
    	isOpen = false;
    	listener  = null;
    	
    	popUps = new ObjectMap<Class<?>, PopUp>(4);
    	currentDescriptor = null;
    	currentPopUp = null;
    }
    
    public void setup(Stage _stage, Skin _skin,  AssetManager _assetManager)
    {
    	stage = _stage;
    	skin = _skin;
    	assetManager = _assetManager;
    }
    
    public AssetManager getAssetmanager()
    {
    	return assetManager;
    }
    
    public void addPopUp(PopUp _popUp)
    {
    	if(_popUp != null)
    	{
    		_popUp.setManager(this);
    		popUps.put(_popUp.getClass(), _popUp);
    	}
    }
    
    public void removePopUp(Class<?> _key)
    {
    	if(_key != null)
    	{
    		PopUp popUp = popUps.get(_key);
    		if(popUp != null)
    		{
    			popUp.setManager(this);
	    		popUps.remove(_key);
    		}
    	}
    }
    
    public void clearPopUps()
    {
    	Values<PopUp> values = popUps.values();
    	
    	while(values.hasNext())
    	{
    		PopUp popUp = values.next();
    		popUp.setManager(null);
    	}
    	
    	popUps.clear();
    }
    
    public void open(PopUpDescriptor<?> _descriptor)
    {
    	if(_descriptor != null && !isOpen)
    	{
    		currentDescriptor = _descriptor;
    		currentPopUp = popUps.get(currentDescriptor.type);
    		if(currentPopUp != null)
    		{
    			isOpen = true;
    			stage.addActor(currentPopUp.getRoot());
    			currentPopUp.init(currentDescriptor, skin);
    			currentPopUp.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    			currentPopUp.open();
    		}	
    	}
    }    
    
    public void setListener(PopUpListener _listener)
    {
    	listener = _listener;
    }
    
    public boolean isOpen()
    {
    	return isOpen;
    }
    
   public void update(float _fDt)
   {
	   
   }
    
    public void resize(int _width, int _height)
    {
    	if(currentPopUp != null)
    		currentPopUp.resize(_width, _height);
    }
    
	protected void onPopUpClosed(PopUp _popUp)
	{
		if(_popUp == currentPopUp)
		{
			isOpen = false;
			stage.getRoot().removeActor(currentPopUp.getRoot());
	        currentPopUp = null;
	        PopUpDescriptor<?> closing = currentDescriptor;
	        currentDescriptor = null;
	        if(listener != null)
	        	listener.onClose(closing);
		}
	}
}
