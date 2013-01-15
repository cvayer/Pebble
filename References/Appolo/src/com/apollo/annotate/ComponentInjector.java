package com.apollo.annotate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.apollo.ApolloException;
import com.apollo.Component;
import com.apollo.Entity;
import com.apollo.managers.Manager;
import com.apollo.managers.TagManager;

public abstract class ComponentInjector<T> {
	private final Class<? extends Annotation> clazz;

	
	
	public static ComponentInjector<Component> injectorComponent = new ComponentInjector<Component>(InjectComponent.class) {
		@Override
		Component getInjectionObject(Component component, Field field) {
			return component.getComponentFromOwner(Class.class.cast(field.getType()));
		}
	};
	
	public static ComponentInjector<Manager> injectorManager = new ComponentInjector<Manager>(InjectManager.class) {
		@Override
		Manager getInjectionObject(Component component, Field field) {
			return component.getWorld().getManager(Class.class.cast(field.getType()));
		}
	};
	
	public static ComponentInjector<Entity> injectorTaggedEntity = new ComponentInjector<Entity>(InjectTaggedEntity.class) {
		@Override
		Entity getInjectionObject(Component component, Field field) {
			InjectTaggedEntity annotation = field.getAnnotation(InjectTaggedEntity.class);
			String tag = annotation.value();
			TagManager tagManager = component.getWorld().getManager(TagManager.class);
			Entity entity = null;
			if(tagManager != null) {
				entity = tagManager.getEntity(tag);
			} else {
				System.out.println("Warning! Autoinjection didn't find tag manager when attempting to inject entity by tag. "+ field.getDeclaringClass() + " for " + field.getName());
			}
			return entity;
		}
	};
	
	

	public ComponentInjector(Class<? extends Annotation> clazz) {
		this.clazz = clazz;
	}
	
	public void inject(Field field, Component component) {
		Annotation annotation = field.getAnnotation(clazz);
		if(annotation!=null && clazz.isAssignableFrom(clazz)) {
			T object = getInjectionObject(component, field);
			if(object==null) {
				//This will just inject a null object, but that is likely a problem in the code
				System.out.println("Warning! Autoinjection didn't find an object to inject! "+ field.getDeclaringClass() + " for " + field.getName());
			}
			try {
				field.setAccessible(true);
				field.set(component, object);
			} catch (Exception e) {
				e.printStackTrace();
				throw new ApolloException("Failed to inject.", e);
			}
		}
	}
	
	abstract T getInjectionObject(Component component, Field field);

	public ComponentInjector<Component> getInjectorComponent() {
		return injectorComponent;
	}

}
