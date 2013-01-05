package com.mangecailloux.tools;


public class Direction 
{
	//---------------------------------------------------------------------------------------------
	// STATIC PART
	
	public static final int START  	= 0;
	public static final int MAX	= 8;
	
	public static final int NONE  = 0;
	public static final int N  	= (1<<0);
	public static final int NW 	= (1<<1);
	public static final int W 	= (1<<2);
	public static final int SW 	= (1<<3);
	public static final int S 	= (1<<4);
	public static final int SE 	= (1<<5);
	public static final int E 	= (1<<6);
	public static final int NE 	= (1<<7);
	
	public static int getDirectionFromIndex(int _index)
	{
		if(_index < START || _index >= MAX)
			throw new RuntimeException("Direction::directionFromIndex -> wrong index : " + _index);
		
		return (1<<_index);
	}

	public static int getX(int _direction)
	{
		int x = 0;
		switch(_direction)
		{
		case N  	: x =  0; break;
		case NE  	: x =  1; break;
		case E  	: x =  1; break;
		case SE  	: x =  1; break;
		case S 		: x =  0; break;
		case SW 	: x = -1; break;
		case W 		: x = -1; break;
		case NW  	: x = -1; break;
		default : 
			throw new RuntimeException("Direction::getX -> tested direction is not a unitary direction : " + _direction);
		}
		return x;
	}
	
	public static int getY(int _direction)
	{
		int y = 0;
		switch(_direction)
		{
		case N  	: y = -1; break;
		case NE  	: y = -1; break;
		case E  	: y =  0; break;
		case SE  	: y =  1; break;
		case S 		: y =  1; break;
		case SW 	: y =  1; break;
		case W 		: y =  0; break;
		case NW  	: y = -1; break;
		default : 
			throw new RuntimeException("Direction::getY -> tested direction is not a unitary direction : " + _direction);
		}
		return y;
	}
	
	public static int getInvert(int _direction)
	{
		int dir = 0;
		switch(_direction)
		{
		case N  	: dir =  S; break;
		case NE  	: dir = SW; break;
		case E  	: dir =  W; break;
		case SE  	: dir = NW; break;
		case S 		: dir =  N; break;
		case SW 	: dir = NE; break;
		case W 		: dir =  E; break;
		case NW  	: dir = SE; break;
		default : 
			throw new RuntimeException("Direction::getInvert -> tested direction is not a unitary direction : " + _direction);
		}
		return dir;
	}
	
	public static boolean hasDirection(int _dirToTest, int _direction)
	{
		return BitField.contains(_dirToTest, _direction);
	}
	
	//---------------------------------------------------------------------------------------------
	//NON STATIC PART
	
	BitField direction;
	
	public Direction()
	{
		direction = new BitField();
	}
	
	public int get()
	{
		return direction.getMask();
	}
	
	public void clear()
	{
		direction.clear();
	}
	
	public void add(int _direction)
	{
		direction.add(_direction);
	}
	
	public void remove(int _direction)
	{
		direction.remove(_direction);
	}
	
	public int getX()
	{
		return getX(direction.getMask());
	}
	
	public int getY()
	{
		return getY(direction.getMask());
	}
	
	public  int getInvert()
	{
		return getInvert(direction.getMask());
	}
	
	public boolean hasDirection(int _direction)
	{
		return direction.contains(_direction);
	}
}
