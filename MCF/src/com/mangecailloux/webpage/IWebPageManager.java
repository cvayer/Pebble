package com.mangecailloux.webpage;



public interface IWebPageManager {
	
	public enum EMode
	{
		eURL,
		eMarket
	};
	
	public void open(String _url, EMode _mode);
}
