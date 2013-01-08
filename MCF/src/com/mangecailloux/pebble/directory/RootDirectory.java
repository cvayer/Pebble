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
package com.mangecailloux.pebble.directory;

/** 
 * Used as entry point for a directory tree, extends {@link Directory}.
*/
public class RootDirectory <E extends DirectoryElement> extends Directory<E>
{
	/** 
	 * @param _name name of the root directory.
	*/
	public RootDirectory(String _name)
	{
		super();
		name = _name;
	}
}
