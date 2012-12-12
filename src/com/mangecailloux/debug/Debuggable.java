package com.mangecailloux.debug;

public class Debuggable 
{
	private boolean debug;
	
	public Debuggable()
	{
		debug = false;
	}
	
	public final void debug(boolean _debug)
	{
		if(debug != _debug)
		{
			debug = _debug;
			onDebug(debug);
		}
	}
	
	public boolean isDebug() 
	{ 
		return debug; 
	}
	
	protected void onDebug(boolean _debug) {}
}
