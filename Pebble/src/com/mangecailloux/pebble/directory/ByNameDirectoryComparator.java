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

import java.util.Comparator;

/** A {@link Comparator}  to sort Directory by name.
*/
final class ByNameDirectoryComparator <E extends DirectoryElement>  implements Comparator<Directory<E>>
{
	@Override
	public int compare(Directory<E> _c1, Directory<E> _c2) {
		return _c1.name.compareTo(_c2.name);
	}
}