package com.mangecailloux.pebble.android.webpage;

import android.os.Bundle;
import android.os.Message;

import com.mangecailloux.pebble.webpage.IWebPageInterface;

public class WebPageInterface implements IWebPageInterface
{
	private final Message msg;
	private final Bundle data;
	private final WebPageHandler handler;
	
	public WebPageInterface(WebPageHandler _handler)
	{
		msg = new Message();
		data = new Bundle();
		handler = _handler;
	}
	
	@Override
	public void open(String _url, EMode _mode) {
		
		if(_mode == EMode.eURL)
			data.putInt("mode", 0);
		else if (_mode == EMode.eMarket)
			data.putInt("mode", 1);
		else
			data.putInt("mode", 0);
			
		
		data.putString("url", _url);
		msg.setData(data);
		handler.sendMessage(msg);
	}

}
