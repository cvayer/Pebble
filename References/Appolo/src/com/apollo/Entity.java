package com.apollo;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.apollo.utils.Bag;

public final class Entity {
	protected World world;
	private Bag<Component> components;
	private Map<Class<? extends Component>, Component> componentsByType;
	private Map<String,Bag<EventHandler>> handlersByEventType;
	private boolean deleted;

	public Entity(World world) {
		this.world = world;
		components = new Bag<Component>();
		componentsByType = new LinkedHashMap<Class<? extends Component>, Component>();
	}

	public void setComponent(Component component) {
		component.setOwner(this);
		components.add(component);
		componentsByType.put(component.getType(), component);
	}
	
	public void removeComponent(Class<? extends Component> clazz) {
		Component component = getComponent(clazz);
		if(component!=null) {
			removeComponent(component);
		}
	}
	
	public void removeComponent(Component component) {
		components.remove(component);
		componentsByType.remove(component);
		
		world.getEntityManager().removeComponent(component, this);
	}

	protected void initialize() {
		for (int i = 0, s = components.size(); s > i; i++) {
			components.get(i).initialize();
		}
	}
	
	protected void uninitialize() {
		for (int i = 0, s = components.size(); s > i; i++) {
			components.get(i).uninitialize();
		}
	}

	public void update(int delta) {
		for (int i = 0, s = components.size(); s > i; i++) {
			components.get(i).update(delta);
		}
	}

	public <T extends Component> T getComponent(Class<T> type) {
		return type.cast(componentsByType.get(type));
	}
	
	public Bag<Component> getComponents() {
		return components;
	}

	public World getWorld() {
		return world;
	}

	public boolean isDeleted() {
		return deleted;
	}
	
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	public void addEventHandler(String eventType, EventHandler listener) {
		if(handlersByEventType == null)
			handlersByEventType = new HashMap<String, Bag<EventHandler>>();
		
		Bag<EventHandler> listeners = handlersByEventType.get(eventType);
		if(listeners == null) {
			listeners = new Bag<EventHandler>();
			handlersByEventType.put(eventType,listeners);
		}
		listeners.add(listener);
	}
	
	public void fireEvent(String eventType) {
		if(handlersByEventType != null) {
			Bag<EventHandler> handlers = handlersByEventType.get(eventType);
			if(handlers != null) {
				for(int i = 0; handlers.size() > i; i++) {
					EventHandler handler = handlers.get(i);
					handler.handleEvent();
				}
			}
		}
	}
	
	public Map<String, Bag<EventHandler>> getAllEventHandlers() {
		return handlersByEventType;
	}
	
	public void addToWorld() {
		world.addEntity(this);
	}

}
