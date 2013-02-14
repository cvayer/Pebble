package com.mangecailloux.pebble.android.webpage;

import android.os.Bundle;
import android.os.Message;

import com.mangecailloux.pebble.webpage.IWebPageInterface;

public class WebPageInterface implements IWebPageInterface
{
	private final WebPageHandler handler;
	
	public WebPageInterface(WebPageHandler _handler)
	{
		handler = _handler;
	}
	
	@Override
	public void open(String _url, EType _mode) 
	{	
		Bundle data = new Bundle();
		Message msg = new Message();
		
		if(_mode == EType.eURL)
			data.putInt("mode", 0);
		else if (_mode == EType.eMarket)
			data.putInt("mode", 1);
		else
			data.putInt("mode", 0);
			
		
		data.putString("url", _url);
		msg.setData(data);
		handler.sendMessage(msg);
	}

}
