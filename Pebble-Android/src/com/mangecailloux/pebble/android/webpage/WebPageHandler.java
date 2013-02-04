package com.mangecailloux.pebble.android.webpage;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class WebPageHandler extends Handler
{
	private final Activity activity;
	
	public WebPageHandler(Activity _activity)
	{
		activity = _activity;
	}
	
	 @Override
     public void handleMessage(Message msg) {
		Bundle data = msg.getData();
    	String url = data.getString("url");
    	int mode = data.getInt("mode");
    	
    	String finalUrl = url;
    	if(mode == 0)
		{
        	if (!finalUrl.startsWith("http://") && !finalUrl.startsWith("https://")) 
        		finalUrl = "https://" + url;
		}
		else if(mode == 1)
		{
			finalUrl = "market://details?id=" + url;
		}   
    	
    	try 
    	{		
			activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(finalUrl)));
		} 
    	catch (android.content.ActivityNotFoundException anfe) 
    	{
			if(mode == 1)
			{
				finalUrl = "http://play.google.com/store/apps/details?id=" + url;
				activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(finalUrl)));
			}
		}
     }
}
