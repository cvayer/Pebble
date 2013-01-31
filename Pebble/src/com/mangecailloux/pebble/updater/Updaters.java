package com.mangecailloux.pebble.updater;

import com.badlogic.gdx.utils.Array;

public class Updaters 
{
	private final Array<Updater> updaters;
	private 	  UpdaterManager manager;
	
	public Updaters()
	{
		updaters = new Array<Updater>(false, 2);
	}
	
	public void setManager(UpdaterManager _manager)
	{
		if(manager != null)
		{
			unregisterAllUpdaters();
		}
		
		manager = _manager;
		
		if(manager != null)
		{
			registerAllUpdaters();
		}
	}
	
	public void addUpdater(Updater _updater)
	{
		if(_updater != null && !updaters.contains(_updater, true))
		{
			updaters.add(_updater);
			if(manager != null)
				manager.addUpdater(_updater);
		}
	}
	
	private void registerAllUpdaters()
	{
		if(manager == null)
			throw new RuntimeException("UpdaterOwner::registerAllUpdaters : manager is null");
		
		for(int i = 0; i < updaters.size; ++i)
		{
			manager.addUpdater(updaters.get(i));
		}
	}
	
	private void unregisterAllUpdaters()
	{
		if(manager == null)
			throw new RuntimeException("UpdaterOwner::unregisterAllUpdaters : manager is null");
		
		for(int i = 0; i < updaters.size; ++i)
		{
			manager.removeUpdater(updaters.get(i));
		}
	}
}
