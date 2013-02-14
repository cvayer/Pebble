package com.mangecailloux.pebble.android.webpage;

import android.os.Bundle;
import android.os.Message;

import com.mangecailloux.pebble.webpage.IWebPageInterface;

public class WebPageInterface implements IWebPageInterface
{
	private int handlerIndex = 0;
	private final WebPageHandler handler1;
	private final WebPageHandler handler2;
	
	// Temp solution with two handlers to avoid crashes
	public WebPageInterface(WebPageHandler _handler1, WebPageHandler _handler2)
	{
		handler1 = _handler1;
		handler2 = _handler2;
	}
	
	@Override
	public void open(String _url, EType _mode) 
	{
		
		WebPageHandler handler = null;
		
		if(handlerIndex%2 == 0)
			handler = handler1;
		else
			handler = handler2;
		
		handlerIndex ++;
		
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
