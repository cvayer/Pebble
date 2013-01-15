package com.apollo.utils;

public class RecoilInterpolator {
	private enum State {
		RECOILING, RECOVERING, DONE
	}
	private State state;
	private int recoilDistance;
	private float recoilSpeed;
	private float recoverSpeed;
	private float offset;
	
	public RecoilInterpolator(int recoilDistance, float recoilSpeed, float recoverSpeed) {
		this.recoilDistance = recoilDistance;
		this.recoilSpeed = recoilSpeed;
		this.recoverSpeed = recoverSpeed;
		this.offset = 0;
		state = State.DONE;
	}
	
	public void update(int delta) {
		if(!isDone()) {
			if(state == State.RECOILING) {
				offset += recoilSpeed * delta;
				if(offset > recoilDistance) {
					offset = recoilDistance;
					state = State.RECOVERING;
				}
			}
			else if(state == State.RECOVERING) {
				offset -= recoverSpeed * delta;
				if(offset < 0) {
					offset = 0;
					state = State.DONE;
				}
			}
		}
	}
	
	public float getOffset() {
		return offset;
	}
	
	public void recoil() {
		state = State.RECOILING;
	}
	
	public boolean isDone() {
		return state == State.DONE;
	}

}
