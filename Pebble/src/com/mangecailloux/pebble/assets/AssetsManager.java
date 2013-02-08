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
	/** LibGdx {@link AssetManager } to store and load all assets */
	private final AssetManager assetManager;
	
	/**
	 * Convenience method for creating {@link AssetDescriptor}. <br/> {@link PebbleSoundLoaderParameter} and {@link PebbleMusicLoaderParameter} will
	 * be automatically checked, or added if needed.
	 * @param _fileName path to the asset, relative to the Assets folder in the Android project
	 * @param _type	type of the asset
	 * @param _params optionnal parameters, can be null
	 * @return the newly created AssetDescriptor. Store it somewhere for easy access to the asset.
	 */
	@SuppressWarnings("unchecked")
	public static <T> AssetDescriptor<T> newDescriptor(String _fileName, Class<T> _type, AssetLoaderParameters<T> _params)
	{
		// if we load a sound
		if(_type == Sound.class)
		{
			//
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
	
	/**
	 * Dispose of all assets, clear {@link SoundManager} and {@link MusicManager}
	 */
	public synchronized void dispose () 
	{
		assetManager.dispose();
		Pebble.sounds.clear();
		Pebble.musics.clear();
	}
	
	/**
	 * @param _level is the ones set in {@link Logger}.<br/>
	 * Can be Logger.DEBUG, Logger.INFO, Logger.ERROR, or Logger.NONE
	 */
	public void setLogLevel(int _level)
	{
		assetManager.getLogger().setLevel(_level);
	}
	
	/**
	 * @param _descriptor {@link AssetDescriptor} of the asset you want to retrieve.
	 * @return the wanted asset if found, else null.
	 */
	public synchronized <T> T get (AssetDescriptor<T> _descriptor) 
	{
		return assetManager.get(_descriptor.fileName, _descriptor.type);
	}
	
	/**
	 * Adds the Asset to the loading queue.<br/>
	 * The queue must then be processed by calling processLoadingQueue() every frame until it returns true.
	 * @param _descriptor {@link AssetDescriptor} of the asset you want to load.
	 */
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
	
	/**
	 * Unload an asset. <br/>
	 * Note : Assets are refcounted, so depending on dependencies the asset might still be loaded in memory.<br/>
	 * If no more instance of a {@link Sound} or {@link Music} are loaded, it will be removed from the matching manager.
	 * @param _descriptor {@link AssetDescriptor} of the asset you want to unload.
	 */
	public synchronized void unload (AssetDescriptor<?> _descriptor) 
	{
		// Unregister the Sound and Music from the managers before effectively unloading it.
		if(_descriptor.type == Sound.class)
		{
			// Remove the Sound only if it will not be loaded anymore (refcount will be 0 after the next unload call)
			if(assetManager.getReferenceCount(_descriptor.fileName) == 1)
			{
				Pebble.sounds.unregister(_descriptor.fileName);
			}
		}
		else if(_descriptor.type == Music.class)
		{
			// Remove the Music only if it will not be loaded anymore (refcount will be 0 after the next unload call)
			if(assetManager.getReferenceCount(_descriptor.fileName) == 1)
			{
				Pebble.musics.unregister(_descriptor.fileName);
			}
		}
		
		assetManager.unload(_descriptor.fileName);
	}
	
	/**
	 * Must be called every frame after call to {@link #load(AssetDescriptor)}. When it returns true, you can stop calling it every frame.
	 * @return true if all assets in the loading queue are loaded.
	 */
	public boolean processLoadingQueue()
	{
		return assetManager.update();
	}
	
	/**
	 * Set the loader to use for a given type.
	 * @param _type Type of the Asset to process.
	 * @param _loader Associated {@link AssetLoader}.
	 */
	public synchronized <T, P extends AssetLoaderParameters<T>> void setLoader (Class<T> _type, AssetLoader<T, P> _loader) {
		assetManager.setLoader(_type, _loader);
	}
	
}
