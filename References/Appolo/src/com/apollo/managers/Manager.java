package com.apollo.managers;

import java.lang.reflect.Field;

import com.apollo.Entity;
import com.apollo.World;
import com.apollo.annotate.ManagerInjector;

public abstract class Manager {
	protected World world;

	public void added(Entity e) {
	}

	public void removed(Entity e) {
	}

	public void update(int delta) {
	}

	public void initialize() {
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public World getWorld() {
		return world;
	}

	public void applyAnnotations() {
		Class<? extends Manager> clazz = this.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			ManagerInjector.injectorManager.inject(this, fields[i]);
		}
	}

}
