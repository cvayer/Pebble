package com.mangecailloux.pebble.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SoundLoader;
import com.badlogic.gdx.audio.Sound;
import com.mangecailloux.pebble.Pebble;

public class PebbleSoundLoaderParameter extends SoundLoader.SoundParameter
{
	public PebbleSoundLoaderParameter()
	{
		super();
		
		loadedCallback = new LoadedCallback() 
		{
			@Override
			public void finishedLoading(AssetManager assetManager, String fileName, Class type) {
				
				if(!Pebble.sounds.contains(fileName))
				{
					Sound sound = assetManager.get(fileName, type);
					Pebble.sounds.register(fileName, sound);
				}	
			}
		};
	}
	
}
