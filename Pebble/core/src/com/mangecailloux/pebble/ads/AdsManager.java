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

package com.mangecailloux.pebble.ads;

/** <p>
 * This manager handles an {@link IAdsInterface}. IAdsInterface are instantiated per platform and can be null,
 * for instance on the Desktop. They are given to the AdsManager through the {@link ScreenManagerParameters}.
 * </p>
 * <p>
 * See Pebble-Android for an implementation example.
 * </p>
 * @author clement.vayer
 */
public class AdsManager {
	    
    private final IAdsInterface ads;
    
    public AdsManager(IAdsInterface _ads)
    {
    	ads = _ads;
    }
    
    /**@param _show show or hide the ads*/
    public void showAds(boolean _show)
    {
    	if(ads != null)
    	{
    		ads.showAds(_show);
    	}
    }
}
