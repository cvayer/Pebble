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
	private final AdsHandler handler;
	
	public AdsInterface(AdsHandler _handler)
	{
		handler = _handler;
	}
	
	@Override
	public void showAds(boolean _show) 
	{
		handler.sendEmptyMessage(_show ? AdsHandler.SHOW_ADS : AdsHandler.HIDE_ADS);
	}

}
