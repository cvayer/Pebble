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
package com.mangecailloux.pebble.entity.manager;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;
import com.mangecailloux.pebble.entity.Entity;
import com.mangecailloux.pebble.entity.EntityManager;

public class EntityTagManager extends EntityManager
{
	private final ObjectMap<String, Entity> 		entityByTags;
	private final ObjectMap<Entity, Array<String>> 	tagsByEntity;
	private final Pool<Array<String>>				stringArrayPool;
	
	public EntityTagManager()
	{
		super();
		
		entityByTags = new ObjectMap<String, Entity>(8);
		tagsByEntity = new ObjectMap<Entity, Array<String>>(8);
		
		stringArrayPool = new Pool<Array<String>>()
		{
			@Override
			protected Array<String> newObject() {
				return new Array<String>(false, 4);
			}
		};
		
	}
	
	public void addTag(String _tag, Entity _entity)
	{
		entityByTags.put(_tag, _entity);
		
		Array<String> tags = tagsByEntity.get(_entity);
		
		if(tags == null)
		{
			tags = stringArrayPool.obtain();
			tagsByEntity.put(_entity, tags);
		}
		
		tags.add(_tag);
		
	}

	public void removeTag(String _tag)
	{
		Entity entity = entityByTags.remove(_tag);
		
		if(entity != null)
		{
			Array<String> tags = tagsByEntity.get(entity);
			if(tags != null)
			{
				tags.removeValue(_tag, false);
			}
			
			if(tags.size == 0)
			{
				tagsByEntity.remove(entity);
				stringArrayPool.free(tags);
			}
		}
		
		
	}

	public Entity getEntity(String _tag)
	{
		return entityByTags.get(_tag);
	}
	
	@Override
	public void onAddToWorld(Entity _entity) 
	{
		
	}
	
	@Override
	public	void onRemoveFromWorld(Entity _entity) 
	{
		Array<String> tags = tagsByEntity.remove(_entity);
		
		if(tags != null)
		{
			for(int i=0; i < tags.size; ++i)
			{
				entityByTags.remove(tags.get(i));
			}
			
			stringArrayPool.free(tags);
		}
		
	}	
}
