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

import com.mangecailloux.pebble.ads.IAdsInterface;

public class AdsInterface implements IAdsInterface
{
	private int handlerIndex = 0;
	private final AdsHandler handler1;
	private final AdsHandler handler2;
	
	// Temp solution with two handlers to avoid crashes
	public AdsInterface(AdsHandler _handler1, AdsHandler _handler2)
	{
		handler1 = _handler1;
		handler2 = _handler2;
	}
	
	@Override
	public void showAds(boolean _show) 
	{
		AdsHandler handler = null;
		
		if(handlerIndex%2 == 0)
			handler = handler1;
		else
			handler = handler2;
		
		handlerIndex ++;
		
		handler.sendEmptyMessage(_show ? AdsHandler.SHOW_ADS : AdsHandler.HIDE_ADS);
	}

}
