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
package com.mangecailloux.pebble.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.audio.Music;
import com.mangecailloux.pebble.Pebble;

/**
 * AssetDescriptorParameter that must be used when using the {@link AssetsManager}. <br/>
 * Will automatically put the loaded music in the {@link MusicManager}.<br/>
 * Users can defined their own callback that will also be called.
 * @author clement.vayer
 *
 */
public class PebbleMusicLoaderParameter extends MusicLoader.MusicParameter
{
	/** user callback */
	private final LoadedCallback userCallBack;
	
	/**
	 * Default ctor with no user callback
	 */
	public PebbleMusicLoaderParameter()
	{
		this(null);
	}
	
	/**
	 * @param _userCallback user callback to process the music if needed. It will already be added to the manager.
	 */
	public PebbleMusicLoaderParameter(LoadedCallback _userCallback)
	{
		super();
		userCallBack = null;
		
		loadedCallback = new LoadedCallback() 
		{
			@SuppressWarnings("rawtypes")
			@Override
			public void finishedLoading(AssetManager assetManager, String fileName, Class type) {
				
				if(type == Music.class)
				{
					// Register the music to the manager is it's not already the case
					if(!Pebble.musics.contains(fileName))
					{
						Music sound = assetManager.get(fileName, Music.class);
						Pebble.musics.register(fileName, sound);
					}	
					
					// Call the user callback if needed.
					if(userCallBack != null)
						userCallBack.finishedLoading(assetManager, fileName, type);
				}
			}
		};
	}
	
}
