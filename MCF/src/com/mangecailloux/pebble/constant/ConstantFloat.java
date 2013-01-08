/*
 * Copyright 2011 Clément Vayer <cvayer@gmail.com>

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 *******************************************************************************/
package com.mangecailloux.pebble.constant;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

/** 
 * Constant storing a float, extends {@link Constant}.
*/
public final class ConstantFloat extends Constant {
	
	/**Actual value of the constant. **/
	protected float	  value;
	
	public ConstantFloat()
	{
		super();
	}
	
	@Override
	protected void setValue(String _value)  throws NumberFormatException
	{
		value = Float.parseFloat(_value);
	}
	
	@Override
	public String toString()
	{
		return Float.toString(value);
	}
	
	/**@return the stored float **/
	public float get() { return value; }

	@Override
	protected void internalToXML(XmlWriter _xml) throws IOException {
		_xml.attribute("value", toString());
	}

	@Override
	protected void internalFromXML(Element _element) {
		setValue(_element.getAttribute("value"));
	}
}
