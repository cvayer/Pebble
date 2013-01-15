package com.apollo.managers;

import java.util.HashMap;
import java.util.Map;

import com.apollo.Entity;

public class PlayerManager extends Manager {
	private Map<Entity, Player> playerByEntity;

	public PlayerManager() {
		playerByEntity = new HashMap<Entity, Player>();
	}

	public void setPlayer(Entity e, Player player) {
		playerByEntity.put(e, player);
	}

	public Player getPlayer(Entity e) {
		return playerByEntity.get(e);
	}
	
}
