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
package com.mangecailloux.constant;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;
/** 
 * <p>
 * Custom TextFieldFilter , it extends {@link TextField.TextFieldFilter}.
 * </p>
 * <p>
 * It allows : digits, backspace and minus characters.
 * 
 * </p>
*/
class IntTextFieldFilter implements TextField.TextFieldFilter
{
	// Allows digits, backspace (8) and minus (45)
	public boolean acceptChar(TextField textField, char key) {
		return Character.isDigit(key) || key == 8 || key == 45;
	}
}