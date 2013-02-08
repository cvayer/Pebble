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
package com.mangecailloux.pebble.audio;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Values;

/**
 * Manager used for handling {@link Music}.
 * @author clement.vayer
 */
public class MusicManager 
{  
	// Static ----------------------------------------------
	
	/** default activation state of the manager */
	private static boolean defaultActivation = true;
	
	/**
	 * @return the default activation state of the manager.
	 */
	public static boolean getDefaultActivation()
	{
		return defaultActivation;
	}
	
	/**
	 * Sets the default activation state, must be called before the creation of the manager. (e.g. Before the init() call of {@link ScreenManager} )
	 * @param _activated
	 */
	public static void setDefaultActivation(boolean _activated)
	{
		defaultActivation = _activated;
	}
	// /Static ----------------------------------------------
	
	/** if false, not music will be played */
	private boolean activated;
	/** all the loaded musics, when loaded through the {@link AssetsManager} the key is the filepath */
	private final 	ObjectMap<String, Music>	musics;
	
	// <Iterators> ----------------------------------
	/**
	 * Visitor used to stop all the music instances
	 */
	MusicVisitor allStop = new MusicVisitor() 
	{
		@Override
		public void visite(Music _music) 
		{
			if(_music.isPlaying())
				_music.stop();
		}
	};
	// </Iterators> ----------------------------------
   
	public MusicManager()
	{
		activated = getDefaultActivation();
		musics = new ObjectMap<String, Music>(2);
	}
	
	/**
	 * @param _activate if false, stop all playing musics, and future play calls will be ignored.
	 */
	public void activate(boolean _activate)
	{
		if(activated != _activate)
		{
			activated = _activate;	
			
			if(!activated)
			{
				// We stop all playing music
				visite(allStop);
			}
		}
	}
	
	/**
	 * Switch activation state.
	 */
	public void toggle()
	{
		activate(!activated);
	}
    
	/**
	 * @return true if activated
	 */
	public boolean isActivated()
	{
		return activated;
	}
	
	/**
	 * Iterate through all the musics
	 * @param _visitor used to perform a task on a music instance.
	 */
	private void visite(MusicVisitor _visitor)
	{
		if(_visitor == null)
			return;
		
		Values<Music> values = musics.values();
		while(values.hasNext())
    	{
			Music music = values.next();
			_visitor.visite(music);
    	}
	}
	
	/**
	 * Register the {@link Music} into the manager.
	 * {@link Asset} {@link #get(String)}
	 * @param _key ID of the music instance.
	 * @param _music music instance.
	 */
	public void register(String _key, Music _music)
	{
		if(_music == null)
			throw new IllegalArgumentException("MusicManager::register : _music must not be null");
		
		musics.put(_key, _music);
	}
	
	/** Remove a {@link Music} */
	public void unregister(String _key)
	{
		Music music = musics.get(_key);
		if(music != null && music.isPlaying())
			music.stop();
		musics.remove(_key);
	}
	
	/**
	 * Stop and remove all music instances. Be careful, this will not unload the musics.
	 */
	public void clear()
	{
		visite(allStop);
		musics.clear();
	}
	
	/**
	 * @param _key ID of the music.
	 * @return true if the music is referenced.
	 */
	public boolean contains(String _key)
	{
		return get(_key) != null;
	}
	
	/**
	 * @param _key ID of the music.
	 * @return the music is activated, else null.
	 */
	private Music get(String _key)
	{
		if(activated)
		 return musics.get(_key);
		return null;
	}
	
	/**
	 * Play the music.
	 * @param _key ID of the music.
	 * @param _looping if true, music will loop.
	 */
	public void play(String _key, boolean _looping)
	{
		Music music = get(_key);
		if(music != null && !music.isPlaying())
		{
			music.play();
			music.setLooping(_looping);
		}
	}
	
	/**
	 * Stop the Music.
	 * @param _key ID of the music.
	 */
	public void stop(String _key)
	{
		Music music = get(_key);
		if(music != null && music.isPlaying())
		{
			music.stop();
		}
	}
	
	/**
	 * @param _key ID of the music.
	 * @return true is the music is playing.
	 */
	public boolean isPlaying(String _key)
	{
		Music music = get(_key);
		if(music != null)
		{
			return music.isPlaying();
		}
		return false;
	}
	
	/**
	 * Convenience method to play a Music using an {@link AssetDescriptor} as a key. </br>
	 * Useful when using the {@link AssetsManager} as the music keys are the filename.
	 * @param _asset filename will be used as the key.
	 * @param _looping if true, music will loop.
	 */
	public void play(AssetDescriptor<Music> _asset, boolean _looping)
	{
		play(_asset.fileName, _looping);
	}
	
	/**
	 * Convenience method to stop a Music using an {@link AssetDescriptor} as a key. </br>
	 * Useful when using the {@link AssetsManager} as the music keys are the filename.
	 * @param _asset filename will be used as the key.
	 */
	public void stop(AssetDescriptor<Music> _asset)
	{
		stop(_asset.fileName);
	}
	
	/**
	 * Convenience method to know if a Music is playing using an {@link AssetDescriptor} as a key. </br>
	 * Useful when using the {@link AssetsManager} as the music keys are the filename.
	 * @param _asset filename will be used as the key.
	 */
	public boolean isPlaying(AssetDescriptor<Music> _asset)
	{
		return isPlaying(_asset);
	}

	//******************************************
	/**
	 * Interface to permof a task on each music stored
	 * @author clement.vayer
	 *
	 */
	private interface MusicVisitor
	{
		void visite(Music _music);
	}
}
