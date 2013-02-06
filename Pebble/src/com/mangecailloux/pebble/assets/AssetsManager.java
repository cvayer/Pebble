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

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Logger;
import com.mangecailloux.pebble.Pebble;

/**
 * <p>
 * That class is basically a wrapper around libGdx {@link AssetManager}, it allows loading, unloading and get of assets.
 * </p>
 * <p>
 * For {@link Sound} and {@link Music}, {@link PebbleSoundLoaderParameter} and {@link PebbleMusicLoaderParameter} should be used 
 * , so that they will be automatically added to the {@link SoundManager} and the {@link MusicManager}.
 * </p>
 * @author clement.vayer
 */
public class AssetsManager 
{
	private final AssetManager assetManager;
	
	/**
	 * Convenience method for creating {@link AssetDescriptor}. <br/> {@link PebbleSoundLoaderParameter} and {@link PebbleMusicLoaderParameter} will
	 * be automatically checked, or added if needed.
	 * @param _fileName path to the asset, relative to the Assets folder in the Android project
	 * @param _type	type of the asset
	 * @param _params optionnal parameters, can be null
	 * @return the newly created AssetDescriptor. Store it somewhere for easy access to the asset.
	 */
	public static <T> AssetDescriptor<T> newDescriptor(String _fileName, Class<T> _type, AssetLoaderParameters<T> _params)
	{
		if(_type == Sound.class)
		{
			if(_params != null && !(_params instanceof PebbleSoundLoaderParameter))
				throw new IllegalArgumentException("AssetsManager::newDescriptor : sound parameter must be a PebbleSoundLoaderParameter");
			
			if(_params == null)
				_params =  (AssetLoaderParameters<T>) new PebbleSoundLoaderParameter();
		}
		
		if(_type == Music.class)
		{
			if(_params != null && !(_params instanceof PebbleMusicLoaderParameter))
				throw new IllegalArgumentException("AssetsManager::newDescriptor : music parameter must be a PebbleMusicLoaderParameter");
			
			if(_params == null)
				_params = (AssetLoaderParameters<T>) new PebbleMusicLoaderParameter();
		}
		
		return new AssetDescriptor<T>(_fileName, _type, _params);
	}
	
	/**
	 * Convenience method for creating {@link AssetDescriptor}. <br/> {@link PebbleSoundLoaderParameter} and {@link PebbleMusicLoaderParameter} will
	 * be automatically added.
	 * @param _fileName path to the asset, relative to the Assets folder in the Android project
	 * @param _type	type of the asset
	 * @return the newly created AssetDescriptor. Store it somewhere for easy access to the asset.
	 */
	public static <T> AssetDescriptor<T> newDescriptor(String _fileName, Class<T> _type)
	{
		return newDescriptor(_fileName, _type, null);
	}
	
	
	public AssetsManager()
	{
		assetManager = new AssetManager();
	}
	
	public synchronized void dispose () 
	{
		assetManager.dispose();
		Pebble.sounds.clear();
		Pebble.musics.clear();
	}
	
	public void debug(boolean _debug)
	{
		if(_debug)
			assetManager.getLogger().setLevel(Logger.DEBUG);
		else
			assetManager.getLogger().setLevel(Logger.ERROR);
	}
	
	public synchronized <T> T get (AssetDescriptor<T> _descriptor) 
	{
		return assetManager.get(_descriptor.fileName, _descriptor.type);
	}
	
	public synchronized void load (AssetDescriptor<?> _descriptor) 
	{
		if(_descriptor.type == Sound.class)
		{
			if(_descriptor.params == null || !(_descriptor.params instanceof PebbleSoundLoaderParameter))
				throw new IllegalArgumentException("AssetsManager::get : sound parameter must be a PebbleSoundLoaderParameter");
		}
		
		if(_descriptor.type == Music.class)
		{
			if(_descriptor.params == null || !(_descriptor.params instanceof PebbleMusicLoaderParameter))
				throw new IllegalArgumentException("AssetsManager::get : music parameter must be a PebbleMusicLoaderParameter");
		}
		
		assetManager.load(_descriptor);
	}
	
	public synchronized void unload (AssetDescriptor<?> _descriptor) 
	{
		assetManager.unload(_descriptor.fileName);
		
		if(_descriptor.type == Sound.class)
		{
			// no more sound instance
			if(! assetManager.isLoaded(_descriptor.fileName))
			{
				Pebble.sounds.unregister(_descriptor.fileName);
			}
		}
		
		if(_descriptor.type == Music.class)
		{
			// no more music instance
			if(! assetManager.isLoaded(_descriptor.fileName))
			{
				Pebble.musics.unregister(_descriptor.fileName);
			}
		}
	}
	
	public boolean isFinishedLoading()
	{
		return assetManager.update();
	}
	
	public synchronized <T, P extends AssetLoaderParameters<T>> void setLoader (Class<T> _type, AssetLoader<T, P> _loader) {
		assetManager.setLoader(_type, _loader);
	}
	
}
