package com.apollo.managers;

import com.apollo.Entity;
import com.apollo.components.spatial.Spatial;
import com.apollo.utils.Bag;
import com.apollo.utils.ImmutableBag;

public class RenderManager<T> extends Manager {
	private Bag<Bag<Spatial<T>>> buckets;
	private T context;

	public RenderManager(T context) {
		this.context = context;
		buckets = new Bag<Bag<Spatial<T>>>();
	}

	public T getContext() {
		return context;
	}

	public void render(T context) {
		renderBuckets(context);
	}

	private void resetBuckets() {
		for (int i = 0; buckets.size() > i; i++) {
			Bag<Spatial<T>> bag = buckets.get(i);
			if (bag != null) {
				bag.clear();
			}
		}
	}

	private void addSpatialsToBuckets() {
		EntityManager em = world.getEntityManager();
		ImmutableBag<Entity> spatialEntities = em.getEntitiesByComponentType(Spatial.class);

		for (int i = 0; spatialEntities.size() > i; i++) {
			Entity entity = spatialEntities.get(i);
			addEntitySpatialsToBuckets(entity);
		}
	}

	private void addEntitySpatialsToBuckets(Entity entity) {
		Spatial<T> spatial = entity.getComponent(Spatial.class);
		if (spatial != null) {
			spatial.addToRenderBuckets(buckets);
		}
	}

	private void renderBuckets(T context) {
		for (int i = 0; buckets.size() > i; i++) {
			Bag<Spatial<T>> bag = buckets.get(i);
			if (bag != null) {
				for (int a = 0; bag.size() > a; a++) {
					Spatial<T> spatial = bag.get(a);
					spatial.render(context);
				}
			}
		}
	}

	@Override
	public void added(Entity e) {
		//resetBuckets();
		//addSpatialsToBuckets();
		addEntitySpatialsToBuckets(e);
	}

	@Override
	public void removed(Entity e) {
		resetBuckets();
		addSpatialsToBuckets();
	}

}
