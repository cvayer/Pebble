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
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Values;

/**
 * Manager used for handling {@link Sound}.
 * @author clement.vayer
 */
public class SoundManager 
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
	 * Sets the default activation state, must be called before the creation of the manager. (e.g. Before {@link ScreenManager#init()} )
	 * @param _activated
	 */
	public static void setDefaultActivation(boolean _activated)
	{
		defaultActivation = _activated;
	}
	// /Static ----------------------------------------------
	
	/** if false, no sound will be played */
	private boolean activated ;
	/** all the loaded sounds, when loaded through the {@link AssetsManager} the key is the filepath */
	private final 	ObjectMap<String, Sound>	sounds;
	
	// <Iterators> ----------------------------------
	/**
	 * Visitor used to stop all the stop instances
	 */
	SoundVisitor allStop = new SoundVisitor() 
	{
		@Override
		public void visite(Sound _sound) 
		{
			_sound.stop();
		}
	};
	// </Iterators> ----------------------------------
	
	public SoundManager()
	{
		activated = getDefaultActivation();
		sounds = new ObjectMap<String, Sound>(2);
	}

	/**
	 * @param _activate if false, stop all sounds and future play calls will be ignored.
	 */
	public void activate(boolean _activate)
	{
		if(activated != _activate)
		{
			activated = _activate;			
			if(!activated)
			{
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
	 * @param _visitor used to perform a task on a sound instance.
	 */
	private void visite(SoundVisitor _visitor)
	{
		if(_visitor == null)
			return;
		
		Values<Sound> values = sounds.values();
		while(values.hasNext())
    	{
			Sound sound = values.next();
			_visitor.visite(sound);
    	}
	}
	
	/**
	 * Register the {@link Sound} into the manager.
	 * @param _key ID of the sound instance.
	 * @param _sound sound instance.
	 */
	public void register(String _key, Sound _sound)
	{
		if(_sound == null)
			throw new IllegalArgumentException("SoundManager::register : _sound must not be null");
		
		sounds.put(_key, _sound);
	}
	
	/** Remove a {@link Sound}. This will not unload the music. 
	 * @param _key ID of the sound instance.
	 */
	public void unregister(String _key)
	{
		sounds.remove(_key);
	}
	
	/**
	 * Stop and remove all sounds instances. This will not unload the sounds.
	 */
	public void clear()
	{
		visite(allStop);
		sounds.clear();
	}
	
	/**
	 * @param _key ID of the sound.
	 * @return the sound if the manager activated, else null.
	 */
	private Sound get(String _key)
	{
		if(activated)
		 return sounds.get(_key);
		return null;
	}
	
	/**
	 * @param _key ID of the sound.
	 * @return true if the sound is referenced.
	 */
	public boolean contains(String _key)
	{
		return get(_key) != null;
	}
	
	// TODO : find a way to deal with sound handles to stop sounds instance, for now it's ok as we don't ever stop a particular sound instance
	// TODO : global volume ?
	/**
	 * Play the sound.
	 * @param _key ID of the sound.
	 */
	public void play(String _key)
	{
		play(_key, 1.0f);
	}
	
	/**
	 * Play the sound with a given volume
	 * @param _key ID of the sound.
	 * @param _volume desired volume, between 0.0 and 1.0
	 */
	public void play(String _key, float _volume)
	{
		Sound sound = get(_key);
		if(sound != null)
		{
			sound.play(_volume);
		}
	}
	
	/**
	 * Stop all instances of a sound.
	 * @param _key ID of the sound.
	 */
	public void stop(String _key)
	{
		Sound sound = get(_key);
		if(sound != null)
		{
			sound.stop();
		}
	}
	
	/**
	 * Convenience method to play a Sound using an {@link AssetDescriptor} as a key. </br>
	 * Useful when using the {@link AssetsManager} as the sound keys are the filename.
	 * @param _asset filename will be used as the key.
	 */
	public void play(AssetDescriptor<Sound> _asset)
	{
		play(_asset.fileName);
	}
	
	/**
	 * Convenience method to play a Sound using an {@link AssetDescriptor} as a key. </br>
	 * Useful when using the {@link AssetsManager} as the sound keys are the filename.
	 * @param _asset filename will be used as the key.
	 * @param _volume between 0.0 and 1.0
	 */
	public void play(AssetDescriptor<Sound> _asset, float _volume)
	{
		play(_asset.fileName, _volume);
	}
	
	/**
	 * Convenience method to stop all Sound instances using an {@link AssetDescriptor} as a key. </br>
	 * Useful when using the {@link AssetsManager} as the music keys are the filename.
	 * @param _asset filename will be used as the key.
	 */
	public void stop(AssetDescriptor<Sound> _asset)
	{
		stop(_asset.fileName);
	}
	

	//******************************************
	/**
	 * Interface to perform a task on each sound stored
	 * @author clement.vayer
	 */
	interface SoundVisitor
	{
		void visite(Sound _music);
	}
}