package com.mangecailloux.pebble.audio;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.IntMap.Values;

public class MusicManager {
	    
	public static boolean getDefaultActivation()
	{
		return true;
	}
	
	private boolean activated ;
	private final 	IntMap<Music>	musics;
	
	//------------------------------------------
	// Iterators
	//------------------------------------------
	
	MusicVisitor allStop = new MusicVisitor() {
		
		@Override
		public void visite(Music _music) {
			if(_music.isPlaying())
				_music.stop();
		}
	};
   
	public MusicManager()
	{
		activated = getDefaultActivation();
		musics = new IntMap<Music>(2);
	}
	   
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
	    
	public void toggle()
	{
		activate(!activated);
	}
    
	public boolean isActivated()
	{
		return activated;
	}
	
	// We don't dispose it as it's done by the assetManager : TODO : find a system to automatically register musics and sound to the manager via callbacks
	public void register(int _key, Music _music)
	{
		if(_music == null)
			return;
		
		musics.put(_key, _music);
	}
	
	private Music get(int _key)
	{
		if(activated)
		 return musics.get(_key);
		return null;
	}
	
	public void play(int _key, boolean _looping)
	{
		Music music = get(_key);
		if(music != null && !music.isPlaying())
		{
			music.play();
			music.setLooping(_looping);
		}
	}
	
	public void stop(int _key)
	{
		Music music = get(_key);
		if(music != null)
		{
			music.stop();
		}
	}
	
	public boolean isPlaying(int _key)
	{
		Music music = get(_key);
		if(music != null)
		{
			return music.isPlaying();
		}
		return false;
	}
	

	//******************************************
	interface MusicVisitor
	{
		void visite(Music _music);
	}
}
