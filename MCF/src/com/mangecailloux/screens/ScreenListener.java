package com.mangecailloux.screens;

public interface ScreenListener 
{
   public void onDispose ();
   public void onUpdate (float _fDt);
   public void onRender (float _fDt);
   public void onResize (int width, int height);
   public void onFirstActivation();
   public void onActivation();
   public void onDeactivation();
   public void onPause ();
   public void onResume ();
   
   public class ScreenListenerAdapter implements ScreenListener
   {
	@Override
	public void onDispose() {}

	@Override
	public void onUpdate(float _fDt) {}

	@Override
	public void onRender(float _fDt) {}

	@Override
	public void onResize(int width, int height) {}

	@Override
	public void onFirstActivation() {}

	@Override
	public void onActivation() {}

	@Override
	public void onDeactivation() {}

	@Override
	public void onPause() {}

	@Override
	public void onResume() {}
   
   }
}
