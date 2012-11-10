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
package com.mangecailloux.directory;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlWriter;
import com.badlogic.gdx.utils.XmlReader.Element;

/**
 * <p>Base class for elements to store in {@link Directory}</p>
 *  <p>Can be loaded from XML file. </p>
 *  <p>Can be converted to XML.</p>
 * */
public abstract class DirectoryElement {
	/**Name of the element **/
	public String 	name;
	
	 /**
	  * Convert the directoryElement to XML.
	 * @param _xml XmlWriter to convert the directoryElement into XML.
	 * @throws IOException 
	 */
	protected void toXML(XmlWriter _xml) throws IOException
	{
		_xml.element("element");
		_xml.attribute("type", getClass().getName());
		_xml.attribute("name", name);
		internalToXML(_xml);
		_xml.pop();
	}
	
	/**
	* Initialize a DirectoryElement from an XML Element.
	* @param _element XML Element to load the DirectoryElement from.
	* @throws IOException 
	*/
	protected static <E extends DirectoryElement> E fromXML(Element _element) throws Exception
	{
		Class<?> elementClass = null;
		elementClass = Class.forName(_element.getAttribute("type"));
		if(elementClass != null)
		{
			@SuppressWarnings("unchecked")
			E element = (E) elementClass.newInstance();
			if(element!= null)
			{
				element.name = _element.getAttribute("name");
				element.internalFromXML(_element);
				return element;
			}
		}
		return null;
	}
	
	/**
	 *  <p>Add data to the XML node representing the element.</p>
	  * <p>Overridden in child classes. </p>
	  * <p>"name" should not be added in child classes as is it already done in DirectoryElement.</p>
	  * */
	abstract protected void internalToXML(XmlWriter _xml) throws IOException; 
	
	/**
	 * <p>Initialize a DirectoryElement from an XML Element.</p>
	 * <p>Overridden in child classes. </p>
	 * @param _element XML Element to load the DirectoryElement from.
	 */
	abstract protected void internalFromXML(Element _element);
}
