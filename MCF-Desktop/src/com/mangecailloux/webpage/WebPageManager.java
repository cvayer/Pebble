package com.mangecailloux.webpage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.mangecailloux.webpage.IWebPageManager;

public class WebPageManager implements IWebPageManager
{
	@Override
	public void open(String _url, EMode _mode) {
		if(_mode == EMode.eURL)
		{
			if (!_url.startsWith("http://") && !_url.startsWith("https://")) 
				_url = "http://" + _url;
			
			URI uri;
			try {
				uri = new URI(_url);
				try {
					java.awt.Desktop.getDesktop().browse(uri);
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			} catch (URISyntaxException e) {
				
				e.printStackTrace();
			}
		}
	}
}
