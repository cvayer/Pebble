package com.apollo.managers;

import java.util.HashMap;
import java.util.Map;

import com.apollo.Component;
import com.apollo.Entity;
import com.apollo.utils.Bag;
import com.apollo.utils.ImmutableBag;

public class EntityManager extends Manager {
	private Bag<Entity> entities;
	private Map<Class<? extends Component>, Bag<Entity>> entitiesByComponentType;

	public EntityManager() {
		entities = new Bag<Entity>();
		entitiesByComponentType = new HashMap<Class<? extends Component>, Bag<Entity>>();
	}

	public ImmutableBag<Entity> getEntities() {
		return entities;
	}

	@Override
	public void added(Entity entity) {
		entities.add(entity);

		Bag<Component> components = entity.getComponents();
		for (int i = 0, s = components.size(); s > i; i++) {
			Component component = components.get(i);
			Bag<Entity> entities = entitiesByComponentType.get(component.getType());
			if (entities == null) {
				entities = new Bag<Entity>();
				entitiesByComponentType.put(component.getType(), entities);
			}
			entities.add(entity);
		}
	}

	public void applyComponentAnnotations(Entity entity) {
		for (Component component : entity.getComponents()) {
			component.applyAnnotations();
		}
	}

	@Override
	public void removed(Entity e) {
		entities.remove(e);

		Bag<Component> components = e.getComponents();
		for (int i = 0, s = components.size(); s > i; i++) {
			Component component = components.get(i);
			entitiesByComponentType.get(component.getType()).remove(e);
		}
	}

	public void removeComponent(Component component, Entity entity) {
		Bag<Entity> entitiesByComponent = getEntitiesByComponentType(component.getClass());
		entitiesByComponent.remove(entity);
	}

	public Bag<Entity> getEntitiesByComponentType(Class<? extends Component> type) {
		Bag<Entity> entities = entitiesByComponentType.get(type);
		if (entities == null) {
			entities = new Bag<Entity>();
			entitiesByComponentType.put(type, entities);
		}
		return entities;
	}

	@Override
	public void update(int delta) {
	}

}
