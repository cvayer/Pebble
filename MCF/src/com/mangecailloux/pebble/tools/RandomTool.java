package com.mangecailloux.pebble.tools;

import java.util.Random;

public class RandomTool {
	
	//-----------------------------------------------------------
	// Singleton part
	public static synchronized RandomTool Get() {
        if (null == m_Instance) { 
        	m_Instance = new RandomTool();
        }
        return m_Instance;
    }
    private static RandomTool m_Instance;
    
	//-----------------------------------------------------------
	
	private final Random m_Random;
	
	private RandomTool()
	{
		m_Random = new Random();
	}
	
	public float getFloat()
	{
		return m_Random.nextFloat();
	}
	
	public float getFloat(float _min, float _max)
	{
		float 	Rand 	= m_Random.nextFloat();
		float 	Range 	= _max - _min;
		float 	Ret 	= Rand*Range + _min;
		return 	Ret;
	}
	
	public int getInt()
	{
		return m_Random.nextInt();
	}
	
	public int getInt(int _min, int _max)
	{
		int 	Range 	= _max - _min;
		return 	getInt(Range) + _min;
	}
	
	public int getInt(int _max)
	{
		return m_Random.nextInt(_max + 1);
	}
}
