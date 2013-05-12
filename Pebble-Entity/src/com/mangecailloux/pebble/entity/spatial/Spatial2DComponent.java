package com.mangecailloux.pebble.entity.spatial;

import com.badlogic.gdx.math.Vector2;
import com.mangecailloux.pebble.entity.Component;

public class Spatial2DComponent extends Component {

	protected final Vector2 position;
	protected 		float	rotation;
	
	public Spatial2DComponent()
	{
		position = new Vector2();
		rotation = 0.0f;
	}
	
	@Override
	protected void onAddToWorld() {
		position.set(0.0f, 0.0f);
		rotation = 0.0f;
	}

	@Override
	protected void onRemoveFromWorld() {
		
	}
	
	public Vector2 getPosition()
	{
		return position;
	}
	
	public float getRotation()
	{
		return rotation;
	}
	
	public void setRotation(float _rotation)
	{
		rotation = _rotation;
	}
}
