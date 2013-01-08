package com.mangecailloux.pebble.webpage;

public class WebPageManager {
	// Singleton part
	public static  WebPageManager  get() 
	{
	    if (null == m_Instance) 
	    { 
	    	m_Instance = new WebPageManager();
	    }
	    return m_Instance;
	}
	private static WebPageManager m_Instance;
	
	private IWebPageInterface pageOpener;
	
	private WebPageManager()
	{
		
	}
	
	public void setInterface(IWebPageInterface _pageOpener)
	{
		pageOpener = _pageOpener;
	}
	
	public void openPage(String _url)
	{
		if(pageOpener != null)
		{
			pageOpener.open(_url, IWebPageInterface.EMode.eURL);
		}
	}
	
	public void openMarket(String _appID)
	{
		if(pageOpener != null)
		{
			pageOpener.open(_appID, IWebPageInterface.EMode.eMarket);
		}
	}
}
