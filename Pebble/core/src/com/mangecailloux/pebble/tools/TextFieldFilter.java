package com.mangecailloux.pebble.tools;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class TextFieldFilter 
{
	/** TextFieldFilter to filter int  */
	public static final IntTextFieldFilter		intFilter = new IntTextFieldFilter();
	/** TextFieldFilter to filter float  */
	public static final FloatTextFieldFilter	floatFilter = new FloatTextFieldFilter();
	
	/** 
	 * <p>
	 * Custom TextFieldFilter , it extends {@link TextField.TextFieldFilter}.
	 * </p>
	 * <p>
	 * It allows : digits, backspace, dots, comma and minus characters.
	 * 
	 * </p>
	*/
	public static class FloatTextFieldFilter implements TextField.TextFieldFilter
	{
		// Allows digits, backspace (8), dot(44) , comma(46) and minus (45)
		@Override
		public boolean acceptChar(TextField _textField, char key) {
			return Character.isDigit(key) || key == 8 || key == 44 || key == 46 || key == 45;
		}
	}
	
	/** 
	 * <p>
	 * Custom TextFieldFilter , it extends {@link TextField.TextFieldFilter}.
	 * </p>
	 * <p>
	 * It allows : digits, backspace and minus characters.
	 * 
	 * </p>
	*/
	public static class IntTextFieldFilter implements TextField.TextFieldFilter
	{
		// Allows digits, backspace (8) and minus (45)
		public boolean acceptChar(TextField textField, char key) {
			return Character.isDigit(key) || key == 8 || key == 45;
		}
	}
}
