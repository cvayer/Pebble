package com.mangecailloux.pebble.tools;

public class BitField {
	
	int mask;
	
	public static boolean contains(int _toTest, int _mask)
	{
		return ((_toTest & _mask) != 0);
	}
	
	public BitField()
	{
	}
	
	public BitField(int _mask)
	{
		mask = _mask;
	}
	
	public int getMask()
	{
		return mask;
	}
	
	public void clear()
	{
		mask = 0;
	}
	
	public void add(int _mask)
	{
		mask |= _mask;
	}
	
	public void remove(int _mask)
	{
		mask &= (~_mask);
	}
	
	public boolean contains(int _mask)
	{
		return contains(mask, _mask);
	}
	
	public void set(int _mask)
	{
		mask = _mask;
	}
	
	public void setBit(int _bit, boolean _value)
	{
		if(_value)
			addBit(_bit);
		else
			removeBit(_bit);
	}
	
	public void addBit(int _bit)
	{
		add((1<<_bit));
	}
	
	public void removeBit(int _bit)
	{
		remove((1<<_bit));
	}
	
	public boolean containsBit(int _bit)
	{
		return contains((1<<_bit));
	}
}
