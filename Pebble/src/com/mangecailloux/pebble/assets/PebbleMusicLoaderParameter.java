package com.mangecailloux.pebble.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.audio.Music;
import com.mangecailloux.pebble.Pebble;

public class PebbleMusicLoaderParameter extends MusicLoader.MusicParameter
{
	public PebbleMusicLoaderParameter()
	{
		super();
		
		loadedCallback = new LoadedCallback() 
		{
			@Override
			public void finishedLoading(AssetManager assetManager, String fileName, Class type) {
				
				if(!Pebble.musics.contains(fileName))
				{
					Music sound = assetManager.get(fileName, type);
					Pebble.musics.register(fileName, sound);
				}	
			}
		};
	}
	
}
