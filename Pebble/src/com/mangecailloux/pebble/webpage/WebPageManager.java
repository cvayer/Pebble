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
package com.mangecailloux.pebble.webpage;

/** <p>
 * This manager handles an {@link IWebPageInterface}. IWebPageInterface are instantiated per platform and can be null. 
 * It's given to the WebPageManager through the {@link ScreenManagerParameters}.
 * </p>
 * <p>
 * See Pebble-Android and Pebble-Desktop for an implementation examples.
 * </p>
 */
public class WebPageManager {
	
	/** the platform dependent implementation of the WebPage interface*/
	private final IWebPageInterface webpage;
	
	public WebPageManager(IWebPageInterface _webpage)
	{
		webpage = _webpage;
	}
	
	/** open a webpage
	 * 
	 * @param _url URL of the page, "http://" in front of the URL is optional, it will be added if missing.
	 */
	public void openPage(String _url)
	{
		if(webpage != null)
		{
			webpage.open(_url, IWebPageInterface.EType.eURL);
		}
	}
	
	public void openMarket(String _appID)
	{
		if(webpage != null)
		{
			webpage.open(_appID, IWebPageInterface.EType.eMarket);
		}
	}
}
