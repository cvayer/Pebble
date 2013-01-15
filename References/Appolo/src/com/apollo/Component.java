package com.apollo;

import java.lang.reflect.Field;

import com.apollo.annotate.ComponentInjector;
import com.apollo.managers.EntityManager;

public abstract class Component {
	protected Entity owner;

	public World getWorld() {
		return owner.getWorld();
	}

	public EntityManager getEntityManager() {
		return owner.getWorld().getEntityManager();
	}

	public void setOwner(Entity owner) {
		this.owner = owner;
	}

	public Entity getOwner() {
		return owner;
	}

	public Class<? extends Component> getType() {
		return this.getClass();
	}

	public void initialize() {
	}

	public void update(int delta) {
	}

	public <T extends Component> T getComponentFromOwner(Class<T> family) {
		return owner.getComponent(family);
	}

	public void uninitialize() {
	}

	public void applyAnnotations() {
		Class<?> clazz = this.getClass();
		do {
			Field[] fields = clazz.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				ComponentInjector.injectorComponent.inject(fields[i], this);
				ComponentInjector.injectorManager.inject(fields[i], this);
				ComponentInjector.injectorTaggedEntity.inject(fields[i], this);
			}
			clazz = clazz.getSuperclass();
		} while (clazz != Component.class);
	}

}
