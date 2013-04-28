package com.mangecailloux.pebble.entity;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.mangecailloux.pebble.entity.Entity;
import com.mangecailloux.pebble.entity.EntityManager;

public class InputManager extends EntityManager
{
	private InputMultiplexer multiplexer;
	
	public InputManager(InputMultiplexer _multiplexer)
	{
		multiplexer = _multiplexer;
	}
	
	public void addProcessor (InputProcessor processor) {
		multiplexer.addProcessor(processor);
	}

	public void removeProcessor (InputProcessor processor) {
		multiplexer.removeProcessor(processor);
	}
	
	@Override
	public void onAddToWorld(Entity _entity) {
		
		
	}

	@Override
	public void onRemoveFromWorld(Entity _entity) {
	
		multiplexer.clear();
	}

	@Override
	public void dispose() {
		
	}
}
