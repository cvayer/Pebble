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
	
	protected boolean needToEnd()
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
	
	public abstract void onAdd();
	public abstract void onStart();
	public abstract void onEnd();
	
	public class EndFlag implements Runnable
	{
		@Override
		public void run() {
			notifyEnd();
		}
		
	}
	
}
