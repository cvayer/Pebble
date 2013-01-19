package com.mangecailloux.pebble.audio;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Values;

public class MusicManager {
	    
	public static boolean getDefaultActivation()
	{
		return true;
	}
	
	private boolean activated ;
	private final 	ObjectMap<String, Music>	musics;
	
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
		musics = new ObjectMap<String, Music>(2);
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
	
	public void register(String _key, Music _music)
	{
		if(_music == null)
			throw new IllegalArgumentException("MusicManager::register : _music must not be null");
		
		musics.put(_key, _music);
	}
	
	public void unregister(String _key)
	{
		musics.remove(_key);
	}
	
	public void clear()
	{
		musics.clear();
	}
	
	public boolean contains(String _key)
	{
		return get(_key) != null;
	}
	
	private Music get(String _key)
	{
		if(activated)
		 return musics.get(_key);
		return null;
	}
	
	public void play(String _key, boolean _looping)
	{
		Music music = get(_key);
		if(music != null && !music.isPlaying())
		{
			music.play();
			music.setLooping(_looping);
		}
	}
	
	public void stop(String _key)
	{
		Music music = get(_key);
		if(music != null)
		{
			music.stop();
		}
	}
	
	public boolean isPlaying(String _key)
	{
		Music music = get(_key);
		if(music != null)
		{
			return music.isPlaying();
		}
		return false;
	}
	
	public void play(AssetDescriptor<Music> _asset, boolean _looping)
	{
		play(_asset.fileName, _looping);
	}
	
	public void stop(AssetDescriptor<Music> _asset)
	{
		stop(_asset.fileName);
	}
	
	public boolean isPlaying(AssetDescriptor<Music> _asset)
	{
		return isPlaying(_asset);
	}

	//******************************************
	interface MusicVisitor
	{
		void visite(Music _music);
	}
}
