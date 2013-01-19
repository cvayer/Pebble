package com.mangecailloux.pebble.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Logger;
import com.mangecailloux.pebble.Pebble;

public class AssetsManager 
{
	private final AssetManager assetManager;
	
	@SuppressWarnings("unchecked")
	public static <T> AssetDescriptor<T> newDescriptor(String _fileName, Class<T> _type, AssetLoaderParameters<T> _params)
	{
		if(_type == Sound.class)
		{
			if(_params != null && !(_params instanceof PebbleSoundLoaderParameter))
				throw new IllegalArgumentException("AssetsManager::newDescriptor : sound parameter must be a PebbleSoundLoaderParameter");
			
			_params = (AssetLoaderParameters<T>) new PebbleSoundLoaderParameter();
		}
		
		if(_type == Music.class)
		{
			if(_params != null && !(_params instanceof PebbleMusicLoaderParameter))
				throw new IllegalArgumentException("AssetsManager::newDescriptor : music parameter must be a PebbleMusicLoaderParameter");
			
			_params = (AssetLoaderParameters<T>) new PebbleMusicLoaderParameter();
		}
		
		return new AssetDescriptor<T>(_fileName, _type, _params);
	}
	
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
