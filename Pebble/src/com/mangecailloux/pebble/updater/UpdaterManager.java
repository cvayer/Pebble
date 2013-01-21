package com.mangecailloux.pebble.updater;

import java.util.Comparator;

import com.badlogic.gdx.utils.Array;

public class UpdaterManager 
{
	public UpdaterManager()
	{
		
	}
	
	
	
	class UpdateGroup
	{
		protected final UpdatePriority 	priority;
		private final Array<IUpdater>	updaters;
		private final Array<IUpdater>	toAdd;
		private final Array<IUpdater>	toRemove;
		
		public UpdateGroup(UpdatePriority _priority)
		{
			priority = _priority;
			updaters = new Array<IUpdater>(false, 4);
			toAdd = new Array<IUpdater>(false, 4);
			toRemove = new Array<IUpdater>(false, 4);
		}
		
		public void update(float _dt)
		{
			if(toAdd.size > 0)
			{
				for(int i = 0; i < toAdd.size; ++i )
				{
					updaters.add(toAdd.get(i));
				}
				toAdd.clear();
			}
			
			if(toRemove.size > 0)
			{
				for(int i = 0; i < toRemove.size; ++i )
				{
					updaters.removeValue(toRemove.get(i), true);
				}
				toRemove.clear();
			}
			
			for(int i = 0; i < updaters.size; ++i )
			{
				updaters.get(i).update(_dt);
			}
		}
		
		public void addUpdater(IUpdater _updater)
		{
			if(_updater != null)
				toAdd.add(_updater);
		}
		
		public void removeUpdater(IUpdater _updater)
		{
			if(_updater != null)
				toRemove.add(_updater);
		}
		
		@Override
		public boolean equals(Object _o)
		{
			if(_o instanceof UpdateGroup)
				return equals(UpdateGroup.class.cast(_o));
			return false;
		}
		
		public boolean equals(UpdateGroup _group)
		{
			return priority.ordinal() == _group.priority.ordinal();
		}
	};
	
	class UpdateGroupComparator implements Comparator<UpdateGroup>
	{
		@Override
		public int compare(UpdateGroup o1, UpdateGroup o2) {
			return o1.priority.ordinal() - o2.priority.ordinal();
		}
		
	}
}
