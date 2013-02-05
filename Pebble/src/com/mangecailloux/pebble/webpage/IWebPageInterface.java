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
 * This interface is handled by the {@link WebPageManager}. IWebPageInterface are instantiated per platform and can be null.
 * They are given to the WebPageManager through the {@link ScreenManagerParameters}.
 * </p>
 * <p>
 * Implementations are located in Pebble-Desktop and Pebble-Android.
 * </p>
 * @author clement.vayer
 */
public interface IWebPageInterface {
	
	/**
	 * Type of the page to open, eMarket will try to open the app page via Google Play
	 */
	public enum EType
	{
		eURL,
		eMarket
	};
	
	/**
	 * opens a webpage
	 * @param _url	URL to open, or app ID
	 * @param _type type for opening method
	 */
	public void open(String _url, EType _type);
}
