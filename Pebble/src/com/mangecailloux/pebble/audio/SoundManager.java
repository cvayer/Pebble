package com.mangecailloux.pebble.audio;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.IntMap.Values;

public class SoundManager {	    
	public static boolean getDefaultActivation()
	{
		return true;
	}
	
	private boolean activated ;
	private final 	IntMap<Sound>	sounds;
	
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
		sounds = new IntMap<Sound>(2);
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
	
	// We don't dispose it as it's done by the assetManager : TODO : find a system to automatically register musics and sound to the manager via callbacks
	public void register(int _key, Sound _sound)
	{
		if(_sound == null)
			return;
		
		sounds.put(_key, _sound);
	}
	
	private Sound get(int _key)
	{
		if(activated)
		 return sounds.get(_key);
		return null;
	}
	
	// TODO : find a way to deal with sound handles to stop sounds instance, for now it's ok as we don't ever stop a particular sound instance
	public void play(int _key)
	{
		Sound sound = get(_key);
		if(sound != null)
		{
			sound.play();
		}
	}
	
	public void play(int _key, float _volume)
	{
		Sound sound = get(_key);
		if(sound != null)
		{
			sound.play(_volume);
		}
	}
	
	public void stop(int _key)
	{
		Sound sound = get(_key);
		if(sound != null)
		{
			sound.stop();
		}
	}
	

	//******************************************
	interface SoundVisitor
	{
		void visite(Sound _music);
	}
}