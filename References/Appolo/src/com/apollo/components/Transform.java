package com.apollo.components;

import com.apollo.Component;
import com.apollo.utils.TrigLUT;
import com.apollo.utils.Utils;

public class Transform extends Component {
	public float x, y;
	public float rotation;

	public Transform() {
	}

	public Transform(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Transform(Transform t) {
		this.x = t.x;
		this.y = t.y;
	}
	
	public Transform(float x, float y, float rotation) {
		this.x = x;
		this.y = y;
		this.rotation = rotation;
	}

	public void addX(float x) {
		this.x += x;
	}
	
	public void addY(float y) {
		this.y += y;
	}
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void move(float distance, float heading) {
		x += (float) (TrigLUT.cos(heading) * distance);
		y += (float) (TrigLUT.sin(heading) * distance);
	}
	
	public void move(float distance) {
		x += (float) (TrigLUT.cos(rotation) * distance);
		y += (float) (TrigLUT.sin(rotation) * distance);
	}

	public void addRotation(float angle) {
		rotation = (rotation + angle) % 360;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public float getRotation() {
		return rotation;
	}
	
	public void setLocation(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void setLocation(Transform t) {
		this.x = t.x;
		this.y = t.y;
	}
	
	public float getRotationAsRadians() {
		return (float)Math.toRadians(rotation);
	}

	public float getDistanceTo(Transform t) {
		return Utils.distance(t.getX(), t.getY(), x, y);
	}

	public float getDistanceTo(float x, float y) {
		return Utils.distance(x, y, this.x, this.y);
	}
	
	public float angleInRadians(Transform t) {
		return Utils.angleInRadians(x, y, t.x, t.y);
	}
	
	public float angleInRadians(float x, float y) {
		return Utils.angleInRadians(this.x, this.y, x, y);
	}
	
	@Override
	public String toString() {
		return "x="+x+" y="+y+" rotation"+rotation;
	}

}
