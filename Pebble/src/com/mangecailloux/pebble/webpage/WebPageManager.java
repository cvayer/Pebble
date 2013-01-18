package com.mangecailloux.pebble.webpage;

public class WebPageManager {
	
	private IWebPageInterface pageOpener;
	
	public WebPageManager()
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
