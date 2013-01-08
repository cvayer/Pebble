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

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/** 
 * <p>
 * ConstantTable is a custom Table to display a {@link Constant}, it extends {@link Table}.
 * </p>
 * <p>
 * It contains  : 
 * a {@link Label} to display the constant's name and
 * a {@link TextField} to display and edit the constant's value.
 * </p>
*/
class ConstantTable extends Table
{
	/** TextFieldFilter to filter int  */
	private static final IntTextFieldFilter		textFieldIntListener = new IntTextFieldFilter();
	/** TextFieldFilter to filter float  */
	private static final FloatTextFieldFilter	textFieldFloatListener = new FloatTextFieldFilter();
	
	/** {@link Label} to display the Constant name  */
	private final Label 		label;
	/** {@link Label} to display the Constant name  */
	private final TextField 	textField;

	/** @param _name Name of the {@link Table}, @see Actor 
	 *  @param _skin {@link Skin} to use for the {@link Label} and {@link TextField}. 
	 *  @param _id Unique ID, used to have unique names for the Table, Label and TextField.
	 *  @param _minimalHeight minimal height for the Label and TextField.
	 *  @param _listener {@link TextFieldListener} to know when a TextField is focused.
	 * */
	protected ConstantTable(Skin _skin, int _id, int _minimalHeight, TextFieldListener _listener)
	{
		super();
		LabelStyle labelstyle = null;
		// Try to get the constantEditor style per default, else get the default one.
		if(_skin.has("constantEditor", LabelStyle.class))
			labelstyle = _skin.get("constantEditor", LabelStyle.class);
		else
			labelstyle = _skin.get(LabelStyle.class);
		
		// Create new Label, set the right name with the _id
		label = new Label("", labelstyle);
		
		TextFieldStyle textFieldStyle = null;
		// Try to get the constantEditor style per default, else get the default one.
		if(_skin.has("constantEditor", TextFieldStyle.class))
			textFieldStyle = _skin.get("constantEditor", TextFieldStyle.class);
		else
			textFieldStyle = _skin.get(TextFieldStyle.class);
		
		// Create new Label, set the right name with the _id
		textField = new TextField("", textFieldStyle);
		textField.setName("textField" + _id);
		// set the listener
		textField.setTextFieldListener(_listener);
		
		// Label is above the TextField, each have the same min Height to have a symmetrical table.
		add(label).expand().minHeight(_minimalHeight);
		row();
		add(textField).expand().fill().minHeight(_minimalHeight);
	}
	
	/** Initializes the ConstantTable from the constant, sets the text and the textFieldListener.
	 *  @param _constant {@link Constant} used to initialize the ConstantTable. 
	 * */
	protected void initFromConstant(Constant _constant)
	{
		// sets the texts
		label.setText(_constant.name);
		textField.setText(_constant.toString());
		
		//sets the filter
		if(_constant instanceof ConstantFloat)
			textField.setTextFieldFilter(textFieldFloatListener);
		else if(_constant instanceof ConstantInt)
			textField.setTextFieldFilter(textFieldIntListener);
	}
}
