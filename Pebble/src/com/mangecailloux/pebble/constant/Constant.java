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

import com.mangecailloux.pebble.directory.DirectoryElement;


/** 
 * Abstract class, represents a constant
*/
public abstract class Constant extends DirectoryElement
{
	/**Marks an obsolete constant, if used == false when saving, the constant will be removed from the file **/
	protected boolean  used;
	
	protected Constant()
	{
		used = false;
	}
	
	/**@return the value stored in the constant as a String **/
	public abstract String toString();
	
	/** Sets the value stored in the constant
	 * @param _value value to store, as a String
	 * **/
	protected abstract void setValue(String _value)  throws NumberFormatException;
}
