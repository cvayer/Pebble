package com.mangecailloux.pebble.android.ads;

import android.os.Handler;
import android.os.Message;
import android.view.View;

public class AdsHandler extends Handler
{
  	public final static int SHOW_ADS = 1;
  	public final static int HIDE_ADS = 0;
  	
  	private final View adView;
	
	public AdsHandler(View _adView)
	{
		adView = _adView;
	}
	
	 @Override
     public void handleMessage(Message msg) {
         switch(msg.what) {
             case SHOW_ADS:
             {
            	 if(adView != null)
            		 adView.setVisibility(View.VISIBLE);
                 break;
             }
             case HIDE_ADS:
             {
            	 if(adView != null)
            		 adView.setVisibility(View.GONE);
                 break;
             }
         }
     }
}
