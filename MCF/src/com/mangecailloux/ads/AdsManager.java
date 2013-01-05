package com.mangecailloux.ads;



public class AdsManager {
	
	// Singleton part
	public static  AdsManager  get() {
        if (null == m_Instance) { 
        	m_Instance = new AdsManager();
        }
        return m_Instance;
    }
    private static AdsManager m_Instance;
    
    IAdsInterface ads;
    
    private AdsManager()
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
