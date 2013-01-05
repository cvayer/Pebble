package com.mangecailloux.webpage;



public interface IWebPageInterface {
	
	public enum EMode
	{
		eURL,
		eMarket
	};
	
	public void open(String _url, EMode _mode);
}
