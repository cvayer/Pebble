/*******************************************************************************
 * Copyright 2013 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
