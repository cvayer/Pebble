package com.mangecailloux.pebble.ads;



public class AdsManager {
	    
    IAdsInterface ads;
    
    public AdsManager()
    {
    	
    }
    
    public void setInterface(IAdsInterface _ads)
    {
    	ads = _ads;
    }
    
    public void showAds(boolean _show)
    {
    	if(ads != null)
    	{
    		ads.showAds(_show);
    	}
    }
}
