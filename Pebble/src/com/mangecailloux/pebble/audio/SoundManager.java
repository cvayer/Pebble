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

public class SoundManager {	    
	public static boolean getDefaultActivation()
	{
		return true;
	}
	
	private boolean activated ;
	private final 	ObjectMap<String, Sound>	sounds;
	
	//------------------------------------------
	// Iterators
	//------------------------------------------
	
	SoundVisitor allStop = new SoundVisitor() {
		
		@Override
		public void visite(Sound _sound) {
			_sound.stop();
		}
	};
   
	public SoundManager()
	{
		activated = getDefaultActivation();
		sounds = new ObjectMap<String, Sound>(2);
	}

	
	public void activate(boolean _activate)
	{
		if(activated != _activate)
		{
			activated = _activate;			
			if(!activated)
			{
				//TODO We stop all playing sounds that are looping
				//	visite(allStop);
			}
		}
	}
	
	@SuppressWarnings("unused")
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
	    
	public void toggle()
	{
		activate(!activated);
	}
    
	public boolean isActivated()
	{
		return activated;
	}
	
	public void register(String _key, Sound _sound)
	{
		if(_sound == null)
			throw new IllegalArgumentException("SoundManager::register : _sound must not be null");
		
		sounds.put(_key, _sound);
	}
	
	public void unregister(String _key)
	{
		sounds.remove(_key);
	}
	
	public void clear()
	{
		sounds.clear();
	}
	
	private Sound get(String _key)
	{
		if(activated)
		 return sounds.get(_key);
		return null;
	}
	
	public boolean contains(String _key)
	{
		return get(_key) != null;
	}
	
	// TODO : find a way to deal with sound handles to stop sounds instance, for now it's ok as we don't ever stop a particular sound instance
	public void play(String _key)
	{
		Sound sound = get(_key);
		if(sound != null)
		{
			sound.play();
		}
	}
	
	public void play(String _key, float _volume)
	{
		Sound sound = get(_key);
		if(sound != null)
		{
			sound.play(_volume);
		}
	}
	
	public void stop(String _key)
	{
		Sound sound = get(_key);
		if(sound != null)
		{
			sound.stop();
		}
	}
	
	public void play(AssetDescriptor<Sound> _asset)
	{
		play(_asset.fileName);
	}
	
	public void play(AssetDescriptor<Sound> _asset, float _volume)
	{
		play(_asset.fileName, _volume);
	}
	
	public void stop(AssetDescriptor<Sound> _asset)
	{
		stop(_asset.fileName);
	}
	

	//******************************************
	interface SoundVisitor
	{
		void visite(Sound _music);
	}
}