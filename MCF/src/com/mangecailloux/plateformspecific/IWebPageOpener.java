package com.mangecailloux.plateformspecific;



public interface IWebPageOpener {
	
	public enum EMode
	{
		eURL,
		eMarket
	};
	
	public void open(String _url, EMode _mode);
}
