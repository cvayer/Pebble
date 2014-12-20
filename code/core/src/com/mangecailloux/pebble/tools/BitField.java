/*******************************************************************************
 * Copyright 2013 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
