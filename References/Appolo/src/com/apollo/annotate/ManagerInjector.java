package com.apollo.annotate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.apollo.ApolloException;
import com.apollo.managers.Manager;

public abstract class ManagerInjector<T> {
	private final Class<? extends Annotation> clazz;

	public static ManagerInjector<Manager> injectorManager = new ManagerInjector<Manager>(InjectManager.class) {
		@Override
		Manager getInjectionObject(Manager manager, Class<Manager> type) {
			return manager.getWorld().getManager(type);
		}
	};

	public ManagerInjector(Class<? extends Annotation> clazz) {
		this.clazz = clazz;
	}
	
	public void inject(Manager manager, Field field) {
		Annotation annotation = field.getAnnotation(clazz);
		if(annotation!=null && clazz.isAssignableFrom(clazz)) {
			//Get the object to inject
			@SuppressWarnings("unchecked")
			Class<T> type = (Class<T>)field.getType();
			T object = getInjectionObject(manager, type);
			if(object==null) {
				//This will just inject a null object, but that is likely a problem in the code
				System.out.println("Warning! Autoinjection didn't find an object to inject! "+ field.getDeclaringClass() + " for " + field.getName());
			}
			try {
				field.setAccessible(true);
				field.set(manager, object);
			} catch (Exception e) {
				e.printStackTrace();
				throw new ApolloException("Failed to inject.", e);
			}
		}
	}
	
	abstract T getInjectionObject(Manager manager, Class<T> type);

}
