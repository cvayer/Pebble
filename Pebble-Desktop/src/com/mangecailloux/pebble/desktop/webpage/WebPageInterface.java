package com.mangecailloux.pebble.desktop.webpage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.mangecailloux.pebble.webpage.IWebPageInterface;

public class WebPageInterface implements IWebPageInterface
{
	private final String playStoreHTTP = "http://play.google.com/store/apps/details?id=";
	private final String playStoreHTTPS = "https://play.google.com/store/apps/details?id=";
	
	private final String HTTP = "http://";
	private final String HTTPS = "https://";
	
	@Override
	public void open(String _url, EMode _mode) {
		if(_mode == EMode.eURL)
		{
			if (!_url.startsWith(HTTP) && !_url.startsWith(HTTPS)) 
				_url = HTTPS + _url;
		}
		else
		{	
			if (!_url.startsWith(playStoreHTTP) && !_url.startsWith(playStoreHTTPS)) 
				_url = playStoreHTTPS  + _url;
		}
		
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
