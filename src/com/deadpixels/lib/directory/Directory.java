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
package com.deadpixels.lib.directory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Sort;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.badlogic.gdx.utils.XmlReader.Element;

/** 
 * <p>
 * Directory represents a node in a storage tree.
 * </p>
 * <p>
 * It contains children Directory and elements that extend of DirectoryElement.
 * </p>
*/
public class Directory <E extends DirectoryElement>
{
	/**Comparator used for the default sort for the elements.
	 * @see Directory#sort
	 * */
	@SuppressWarnings("rawtypes")
	private static final ByNameDirectoryElementComparator 	elementComparator 	= new ByNameDirectoryElementComparator();
	/**Comparator used for the default sort for the directories. 
	 * @see Directory#sort
	 * */
	@SuppressWarnings("rawtypes")
	private static final ByNameDirectoryComparator  directoryComparator = new ByNameDirectoryComparator();
	/** Separator regex used to split a constant path. */
	private static final String directorySeparator = "\\\\";
	
	/** {@link StringWriter} to format the directory into a String */
	private static final StringWriter stringWriter 	= new StringWriter();
	/** {@link XmlWriter} to format the directory into XML */
	private static final XmlWriter 	  xmlWriter 	= new XmlWriter(stringWriter);
	/** {@link XmlReader} to read a XML and create the directory tree*/
	private static final XmlReader 	  xmlReader 	= new XmlReader();
	
	/** Parent Directory, used for backward navigation */
	public		Directory<E> 							parent;	
	/** Children Directory, can be null */
	// TODO use sortable ObjectMap ?
	public		Array<Directory<E> > 					children;
	/** Constants store in that directory, can be null */
	// TODO use sortable ObjectMap ?
	public		Array<E>			 					elements;
	/** name of the directory */
	public 		String									name;
	/** true if locally sorted, to know if a directory is fully sorted (itself + children) use isSorted()*/
	private		boolean									sorted;
	/** enable logging or not */
	private 	boolean									logEnabled;

	protected Directory()
	{
		sorted = false;
		logEnabled = false;
	}
	
	/**
	 * 
	 * @param _enable enable the log or not.
	 */
	public void log(boolean _enable)
	{
		logEnabled = _enable;
	}
	
	/**
	 * 
	 * @return true is the directory is fully empty (no children nor elements).
	 */
	public boolean isEmpty()
	{
		int size = 0;
		if(elements != null)
			size += elements.size;
		if(children != null)
			size += children.size;

		return (size == 0);
	}
	
	/**
	 * Adds an element with the wanted path.
	 * @param _path  Relative path of the element within the directory.
	 * @param _type  Class of the wanted element.
	 * @return the element that have been added, can be null if failure.
	 */
	public <T extends E> T addElement(String _path, Class<T> _type)
	{
		// we split the path only once, to avoid more runtime allocation, we then use the same array and an index.
		String[] splitted = _path.split(directorySeparator);
		return addElement(splitted, 0, _type);
	}
	
	/**
	 * Gets an element with the wanted path.
	 * @param _path Relative path of the constant from the directory.
	 * @param _type Class of the constant you want.
	 * @return  the constant that match the wanted path, can be null if failure.
	 */
	public <T extends E> T getElement(String _path, Class<T> _type)
	{
		// we split the path only once, to avoid more runtime allocation, we then use the same array and an index.
		String[] splitted = _path.split(directorySeparator, 2);
		return getElement(splitted, 0, _type);
	}
	
	/**
	 * 
	 * @return true if the current directory is locally sorted and all its children are sorted.
	 */
	public boolean isSorted()
	{
		if(!sorted)
			return false;
		
		if(children != null)
		{
			for(Directory<E> child : children)
			{
				if(!child.isSorted())
					return false;
			}
		}
		return true;
	}
	
	/**
	 * default sort, sort constants and child by name. Uses the static comparators.
	 */
	@SuppressWarnings("unchecked")
	public void sort()
	{
		sort(directoryComparator, elementComparator);
	}
	
	/**
	 * Parameterized sort function, to customize the wanted sort ( by value, by constant number, etc. )
	 * @param _directoryComparator Custom ConstantDirectory comparator.
	 * @param _elementComparator Custom Constant comparator.
	 */
	public void sort(Comparator<Directory<E>> _directoryComparator, Comparator<E> _elementComparator)
	{
		// we sort locally our children and constants
		if(sorted == false)
		{
			if(children != null)
				Sort.instance().sort(children, _directoryComparator);
			if(elements != null)
				Sort.instance().sort(elements, _elementComparator);
			sorted = true;
		}
		
		// we make our children sort themselves.E
		if(children != null)
		{
			for(Directory<E> child : children)
			{
				child.sort();
			}
		}
	}
	
	/**
	 * Convert the directory into XML string.
	 * @return the XML representing the directory.
	 */
	public String toXML()
	{
		stringWriter.flush();
		toXML(xmlWriter);
		return stringWriter.getBuffer().toString();
	}
	
	/**
	 * Build the directory from a XML input stream.
	 * @param _in The InputStream of the XML file.
	 * @throws IOException
	 */
	public void fromXML(InputStream _in) throws IOException
	{
		clear();
		Element rootXML = xmlReader.parse(_in);
		fromXML(rootXML);
	}
	
	/**
	 * Remove all elements and children from the directory.
	 */
	public void clear()
	{
		if(children != null)
			children.clear();
		
		if(elements != null)
			elements.clear();
		
		sorted = true;
	}
	
	
	/**
	 * Gets the wanted element. Recursive function.
	 * @param _splittedPath Full path that has been splitted.
	 * @param _index		Current index to process
	 * @param _type			Class to return.
	 * @return the wanted element, can be null;
	 */
	public <T extends E> T getElement(String[] _splittedPath, int _index, Class<T> _type)
	{
		// we are at the end of the array, so we try to get the matching local element.
		if(_index == _splittedPath.length - 1)
		{
			return getLocalElement(_splittedPath[_index], _type);
		}
		else  // We are not at the end of the array
		{
			// we get the matching local child, and try to find the element in it
			Directory<E> child = getLocalChild(_splittedPath[_index]);
			if(child != null)
				return child.getElement(_splittedPath, _index + 1, _type);
			
			return null;
		}
	}
	
	/**
	 * Adds the wanted element. Recursive function.
	 * @param _splittedPath Full path that has been splitted.
	 * @param _index Current index to process
	 * @param _type Class to return.
	 * @return the added element, can be null;
	 */
	public <T extends E> T addElement(String[] _splittedPath, int _index, Class<T> _type)
	{
		// we are at the end of the array, so we try to add the new local element.
		if(_index == _splittedPath.length - 1)
		{
			return addLocalElement(_splittedPath[_index], _type);
		}
		else // We are not at the end of the array
		{
			// we get the matching local child
			Directory<E> child = getLocalChild(_splittedPath[_index]);
			// if the child doesn't exist we add it to the children list
			if(child == null)
			{
				createChildrenArray();
				child = new Directory<E>();
				child.name = _splittedPath[_index];
				child.parent = this;
				children.add(child);
				sorted = false;
			}
			
			// we add the element to the matching child.
			return child.addElement(_splittedPath, _index + 1, _type);
		}
	}
	
	//------------------------------------------------------------------------------
	// private functions
	/**
	 * 
	 * @param _xml XmlWriter to convert the directory into XML. Recursive function.
	 */
	private void toXML(XmlWriter _xml)
	{
		try {
			// add the current directory to the XML
			_xml.element("directory");
			_xml.attribute("name", name);
			
			// If we have children we add them too
			if(children != null)
			{
				for(Directory<E> child : children)
				{
			 		child.toXML(_xml);
				}
			}
			
			// If we have constants we add them too
			if(elements != null)
			{
				for(DirectoryElement element : elements)
				{
					element.toXML(_xml);
				}
			}
			
			_xml.pop();
			
		} catch (IOException e) {
			
			if(logEnabled)
				Gdx.app.log("Directory", e.getMessage());	
		}
	}
	
	/**
	 * Fill in the directory from the current {@link Element }. Recursive function.
	 * @param _element Current Element.
	 */
	private void fromXML(Element _element)
	{
		// we init the current directory.
		name = _element.getAttribute("name");
		sorted = true;
		
		// we add all the childs
		int childcount = _element.getChildCount();
		for(int i=0; i< childcount; ++i)
		{
			Element child = _element.getChild(i);
			
			String childName = child.getName();
			
			// if it's a directory
			if(childName.equals("directory"))
			{
				createChildrenArray();
				Directory<E> childDir= new Directory<E>();
				childDir.parent = this;
				children.add(childDir);
				// We init the child with the current child Element.
				childDir.fromXML(child);
			}
			// if it's a constant
			else if(childName.equals("element"))
			{
				createElementArray();
				try 
				{
					E element = DirectoryElement.fromXML(child);
					if(element != null)
						elements.add(element);
					
				} catch (Exception e) {
					if(logEnabled)
						Gdx.app.log("Directory", e.getMessage());	
				}
			}
		}
	}
	
	/**
	 * Create the DirectoryElement Array if we don't have one already
	 */
	private void createElementArray()
	{
		if(elements == null)
			elements = new Array<E>(false, 4);
	}
	
	/**
	 *  Create the Directory Array if we don't have one already
	 */
	private void createChildrenArray()
	{
		if(children == null)
			children = new Array<Directory<E>>(false, 4);
	}
	
	/**
	 * Adds an element to the directory local element list, if it doesn't exist already.
	 * @param _name Name of the element to add.
	 * @param _type Class of the element to return.
	 * @return the newly added element.
	 */
	private <T extends E> T addLocalElement(String _name, Class<T> _type)
	{
		T element  = getLocalElement(_name, _type);
		// if we don't already have that constant
		if(element == null)
		{
			try {
				createElementArray();
				element = _type.newInstance();
				element.name = _name;
				elements.add(element);	
				sorted = false;
			} catch (InstantiationException e) {
				if(logEnabled)
					Gdx.app.log("CDirectory", e.getMessage());	
			} catch (IllegalAccessException e) {
				if(logEnabled)
					Gdx.app.log("Directory", e.getMessage());	
			}
		}
		
		return element;
	}
	
	/**
	 * Get the wanted local element. If the element is not of the right Class an exception is thrown.
	 * @param _name Name of the wanted element.
	 * @param _type Class of the element to return.
	 * @return the element with the right name and the right Class. Can be null.
	 */
	@SuppressWarnings("unchecked")
	private <T extends E> T getLocalElement(String _name, Class<T> _type)
	{
		if(elements != null)
		{
			for(E constant : elements)
			{
				if(constant.name.equals(_name))
				{
					if(_type.isInstance(constant))
						return (T)constant;
					else
						throw new RuntimeException("The constant with the name : " + _name + " was not registered with that type");
				}
			}
		}
		return null;
	}
	
	/** 
	 * @param _name Name of the wanted directory.
	 * @return the wanted local Directory. Can be null.
	 */
	private Directory<E> getLocalChild(String _name)
	{
		if(children != null)
		{
			for(Directory<E> child : children)
			{
				if(child.name.equals(_name))
				{
					return child;
				}
			}
		}
		return null;
	}
	
	// private functions
	//------------------------------------------------------------------------------
	
}



