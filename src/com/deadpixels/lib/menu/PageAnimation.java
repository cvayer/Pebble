package com.deadpixels.lib.menu;

public abstract class PageAnimation 
{

	private boolean isRunning;
	private boolean notifyEnd;
	private Page page;
	
	private final EndFlag endFlag; // Convenience runnable to add to actions when the animation is finished

	public PageAnimation()
	{
		isRunning = false;
		notifyEnd = false;
		
		endFlag = new EndFlag();
	}
	
	public void notifyEnd() 
	{ 
		notifyEnd = true; 
	}
	
	public Page getPage()
	{
		return page;
	}
	
	public Runnable getEndFlag()
	{
		return endFlag;
	}
	
	protected boolean doesNeedToEnd()
	{
		return notifyEnd;
	}
	
	protected void start(Page _page)
	{
		if(!isRunning)
		{
			page = _page;
			isRunning = true;
			onStart();
		}
	}
	
	protected void end()
	{
		if(isRunning)
		{
			page = null;
			isRunning = false;
			notifyEnd = false;
			onEnd();
		}
	}
	
	public boolean isRunning()
	{
		return isRunning;
	}
	
	public void onAdd()
	{
		
	}
	
	public void onEnd()
	{
		
	}
	
	public void onUpdate(float _fDt)
	{
		
	}
	
	public abstract void onStart();
	
	
	public class EndFlag implements Runnable
	{
		@Override
		public void run() {
			notifyEnd();
		}
		
	}

	public void onRender(float _fDt) {
		
	}
	
}
