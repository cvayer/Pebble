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
package com.deadpixels.lib.constant;

import java.security.InvalidParameterException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.deadpixels.lib.directory.Directory;
import com.esotericsoftware.tablelayout.BaseTableLayout.Debug;
import com.esotericsoftware.tablelayout.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

/**
 * <p>ConstantEditor displays an UI overlay to tweak your game constants.
 * Abbreviated CE in further comments.</p>
 * <p> CE uses or create a {@link Skin}, you need to have these style in you Skin file : 
 * <ul>
 * <li>{@link TextButtonStyle} called "constantEditor", else it will take the default style.</li>
 * <li>{@link LabelStyle} called "constantEditor", else it will take the default style.</li>
 * <li>{@link TextFieldStyle} called "constantEditor", else it will take the default style.</li>
 * <ul>
 * You'll also need : 
 * <ul>
 * <li>A {@link NinePatch} called "constantEditor-pane", else it will try to get the one called "default-pane-noborder", if not you won't have a background in the flick panel.</li>
 * </ul>
 * </p>
 * <p>Don't forget to add to your android manifest the permission to save your constants : android.permission.WRITE_EXTERNAL_STORAGE.
 * </p>
 */
public class ConstantEditor  implements Disposable
{
	/** padding for the mainTable when the CE is closed */
	private final static int 			mainTablePadding = 5;
	/** minimal size for the buttons */
	private int 						buttonMinimalSize = 60;
	/** linked constant manager */
	private ConstantManager 			manager;
	/** true is the CE is open */
	private boolean 					open;
	/** true is a change has occured in the constants, and a save is need to validate them*/
	private boolean						needSave;
	/** listener for the CE, can be null */
	private ConstantEditorListener 		listener;
	/** true to debug the tables  */
	private boolean						debug;
	/** <p>Align of the openButton when the CE is closed. Uses TableLayout Align constants. Top-Right corner by default.</p> 
	 * @see BaseTableLayout
	 * */
	private int							openButtonAlign = Align.top | Align.right;
	// <UI variables>
	/** {@link Skin} used for the UI of the CE */
	private final 	Skin 					skin;
	/** true if the skin is created by the CE. False if a user uses it's own skin, so we don't dispose it. */
	private  		boolean					disposeSkin;
	/** {@link Stage} for the CE's UI */
	private final 	Stage					stage;
	/** Custom {@link CLickListener} to handle buttons click*/
	private final ButtonClickListener			buttonClickListener = new ButtonClickListener();
	/** Custom {@link TextFieldListener} to handle textField input*/
	private final ConstantTextFieldListener 	textFieldListener = new ConstantTextFieldListener();
	
	/** Main {@link Table}  of the CE, contains all the other tables */
	private final Table 						mainTable;
	/** {@link Table} containing utility buttons (Open, Back, Root, and Save). */
	private final Table 						optionsTable;
	/** {@link FlickScrollPane} to scroll the constants and directories.*/
	private final ScrollPane   					flickScrollpane;
	/** Widget of the FlickScrollPane. Contains all the {@link ConstantTable}. */
	private final Table 						flickTable;

	/** Current Directory to display in the flickTable. */
	private Directory<Constant> 		currentDirectory;
	
	/** Pool of {@link TextButton}  to avoid runtime garbage when switching of Directory. */
	private final Array<TextButton>			buttonPool;
	/** Pool of {@link ConstantTable} to avoid runtime garbage when switching of Directory. */
	private final Array<ConstantTable> 		constantTablePool;
	

	/** Open/Close {@link TextButton} for opening/closing the CE.*/
	private final TextButton					openButton;
	/** Back {@link TextButton}  to navigate backward in the Directory. */
	private final TextButton					backButton;
	/** Root {@link TextButton}  to return to the root in one go. */
	private final TextButton					rootButton;
	/** Save {@link TextButton}  to save the constants */
	private final TextButton					saveButton;
	// </UI variables>
	
	/**
	 * ConstantEditor constructor.
	 * @param _manager {@link ConstantManager} to display. Cannot be null.
	 * @param _skinPath String, path to the skin to use. Will create a new skin, disposed when the CE is disposed.
	 * @param _width Initial width of the Stage.
	 * @param _height Initial Height of the Stage.
	 */
	public ConstantEditor(ConstantManager _manager, String _skinPath, int _width, int _height)
	{
		this(_manager, new Skin(Gdx.files.internal(_skinPath + ".json")), _width, _height);
		// As we have created a new Skin we dispose when dispose is called it to avoid memory leak.
		disposeSkin = true;
	}
	
	/**
	 * ConstantEditor constructor.
	 * @param _manager _manager {@link ConstantManager} to display. Cannot be null.
	 * @param _skin {@link Skin} to use for the UI. 
	 * @param _width Initial width of the Stage.
	 * @param _height Initial Height of the Stage.
	 */
	public ConstantEditor(ConstantManager _manager, Skin _skin, int _width, int _height)
	{
		if(_manager == null)
			throw new InvalidParameterException("ConstantEditor ctor : _manager must not be null");
		
		manager = _manager;
		// initial directory is the root.
		currentDirectory = manager.getRoot();
		open = false;
		needSave = false;
		debug = false;
		
		skin = _skin;
		disposeSkin = false;
		
		// Stage creation
		stage = new Stage(_width, _height, false);
		
		// Pools creation
		buttonPool		= new Array<TextButton>(false, 4);
		constantTablePool 	= new Array<ConstantTable>(false, 4);
		
		// get the button style
		TextButtonStyle style = null;
		if(skin.has("constantEditor", TextButtonStyle.class))
			style = skin.get("constantEditor", TextButtonStyle.class);
		else
			style = skin.get(TextButtonStyle.class);
		
		openButton = new TextButton("Open", style);
		openButton.addListener(buttonClickListener);
		
		backButton = new TextButton("Back", style);
		backButton.addListener(buttonClickListener);
		backButton.setVisible(false);
		backButton.setTouchable(Touchable.disabled);
		
		rootButton = new TextButton("Root", style);
		rootButton.addListener(buttonClickListener);
		rootButton.setVisible(false);
		rootButton.setTouchable(Touchable.disabled);
		
		saveButton = new TextButton("Save", style);
		saveButton.addListener(buttonClickListener);
		saveButton.setVisible(false);
		saveButton.setTouchable(Touchable.disabled);
				
		mainTable = new Table();
		stage.addActor(mainTable);
		
		flickTable = new Table();
		flickTable.top();	
		
		NinePatch patch = skin.getPatch("constantEditor-pane");
		if(patch == null)
			patch = skin.getPatch("default-pane-noborder");
		
		flickTable.setBackground(new NinePatchDrawable(patch));
		
		flickScrollpane = new ScrollPane(flickTable);
		flickScrollpane.setupOverscroll(5, 10, 15);
		flickScrollpane.setScrollingDisabled(true, false);
		flickScrollpane.setFlingTime(0.25f);
		
		optionsTable = new Table();
		
		refreshOptionTable();
		
		mainTable.pad(mainTablePadding);
		mainTable.add(optionsTable).expand().fill();
	}
	
	/**Disposes the Stage and the Skin if it was created by the CE. **/
	@Override
	public void dispose() {
		stage.dispose();
		
		if(disposeSkin)
			skin.dispose();
	}
	
	/**
	 * Sets the Constant Manager to display.
	 * @param _manager ConstantManager to display.
	 */
	public void setManager(ConstantManager _manager)
	{
		if(_manager == null)
			throw new InvalidParameterException("ConstantEditor ctor : _manager must not be null");
		
		manager = _manager;
		currentDirectory = manager.getRoot();
		if(open)
			refreshFlickTable();
	}
	
	/**
	 * Sets the minimal size for the buttons and ConstantTables. Useful to tweak the size the CE will take on screen.
	 * @param _minimalSize _minimalSize for the UI components.
	 */
	public void setUIMinimalSizes(int _minimalSize)
	{
		buttonMinimalSize = _minimalSize;
		
		Cell<?> cell = optionsTable.getCell(openButton);
		if(cell != null)
			cell.minSize(buttonMinimalSize);
		
		cell = optionsTable.getCell(backButton);
		if(cell != null)
			cell.minSize(buttonMinimalSize);
		
		cell = optionsTable.getCell(rootButton);
		if(cell != null)
			cell.minSize(buttonMinimalSize);
		
		cell = optionsTable.getCell(saveButton);
		if(cell != null)
			cell.minSize(buttonMinimalSize);
		
		if(open)
			refreshFlickTable();
	}
	
	/**
	 * Align of the openButton when the CE is closed. Uses TableLayout Align constants. Top-Right corner by default.
	 * @param _align can be any combination of TableLayout.TOP, TableLayout.RIGHT, TableLayout.LEFT, TableLayout.BOTTOM, TableLayout.CENTER.
	 */
	public void setOpenButtonAlign(int _align)
	{
		if(openButtonAlign != _align)
		{
			openButtonAlign = _align;
			
			if(!open)
			{
				Cell<?> cell = optionsTable.getCell(openButton);
				if(cell != null)
					cell.align(openButtonAlign);
			}
		}
	}

	/**
	 * True to debug the tables.
	 * @param _debug true to debug.
	 */
	public void debug(boolean _debug)
	{
		if(_debug != debug)
		{
			debug = _debug;
			
			if(debug)
			{
				mainTable.debug(Debug.all);
				flickTable.debug(Debug.all);
				optionsTable.debug(Debug.all);
			}
			else
			{
				mainTable.debug(Debug.none);
				flickTable.debug(Debug.none);
				optionsTable.debug(Debug.none);
			}
		}
	}
	
	/**
	 * Must be called whenever your application is resizing. In the resize() method when using libGdx.
	 * @param _width New width of your stage.
	 * @param _height New height of your stage.
	 * @param _stretch Stretch parameter for the Stage. See {@link Stage#setViewport}.
	 */
	public void resize(int _width, int _height, boolean _stretch)
	{
		stage.setViewport(_width, _height, _stretch);
		
		mainTable.setWidth(_width);
		mainTable.setHeight(_height);
		mainTable.invalidate();
	}
	
	/**
	 * Adds a listener to the CE to callback some events.
	 * @param _listener {@link ConstantEditorListener} to listen to events. Can be null.
	 */
	public void setListener(ConstantEditorListener _listener)
	{
		listener = _listener;
	}
	/**
	 * The CE InputProcessor should be added to the list of InputProcessor of the application.
	 * @return the InputProcessor for the CE.
	 */
	public InputProcessor getInputProcessor()
	{
		return stage;
	}
	
	/**
	 * Render function must be called in the Render function of your application. At the end to have an overlay.
	 */
	public void render(float _fDt)
	{
		stage.act(_fDt);
		stage.draw();
		
		if(debug)
			Table.drawDebug(stage);
	}
	
	/**
	 * <p>Update "touchability" and visibility on Root and Back buttons depending on the state of the CE.</p>
	 * <p>They are visible only if the current displayed Directory is not the Root of the linked manager.</p>
	 */
	private void updateRootBackButtonsVisibility()
	{
		if(currentDirectory == manager.getRoot())
		{
			backButton.setVisible(false);
			rootButton.setVisible(false);
			backButton.setTouchable(Touchable.disabled);
			rootButton.setTouchable(Touchable.disabled);
		}
		else
		{
			backButton.setVisible(true);
			rootButton.setVisible(true);
			backButton.setTouchable(Touchable.enabled);
			rootButton.setTouchable(Touchable.enabled);
		}
	}
	
	/**
	 *  <p>Update "touchability" and visibility on Save button depending on the state of the CE.</p>
	 *  <p>It's displayed only if the CE has made a change on one of the constant.</p>
	 */
	private void updateSaveButtonsVisibility()
	{
		if(needSave)
		{
			saveButton.setVisible(true);
			saveButton.setTouchable(Touchable.enabled);
		}
		else
		{
			saveButton.setVisible(false);
			saveButton.setTouchable(Touchable.disabled);
		}
	}
	
	/**
	 * <p>Refresh the flickTable according to the state of the CE.</p>
	 */
	private void refreshFlickTable()
	{
		flickTable.clear();
		// if the CE is not open we just clear the table.
		if(open)
		{
			updateRootBackButtonsVisibility();
				
			int buttonPoolIndex = 0;
			int constantTablePoolIndex = 0;
			if(currentDirectory.children != null)
			{
				for (int i = 0; i < currentDirectory.children.size; i++) 
				{
					TextButton button = null;
					
					// No more free button remaining in the pool, we need to add new button
					if(buttonPoolIndex == buttonPool.size)
					{
						TextButtonStyle style = null;
						if(skin.has("constantEditor", TextButtonStyle.class))
							style = skin.get("constantEditor", TextButtonStyle.class);
						else
							style = skin.get(TextButtonStyle.class);
						
						button = new TextButton(currentDirectory.children.get(i).name, style);
						button.setName("directory" + i);
						button.addListener(buttonClickListener);
						buttonPool.add(button);
					}
					else // we get the button in the pool.
					{
						button = buttonPool.get(buttonPoolIndex);
						button.getLabel().setText(currentDirectory.children.get(i).name);
					}
					
					buttonPoolIndex++;
					
					// we add the button to the table.
					if(button != null)
					{
						flickTable.row();
						flickTable.add(button).top().expandX().fill().minSize(buttonMinimalSize, buttonMinimalSize);
					}
				}
			}
			
			if(currentDirectory.elements != null)
			{
				for (int i = 0; i < currentDirectory.elements.size; i++) 
				{
					ConstantTable constantTable = null;
					// No more free ConstantTable remaining in the pool, we need to add new one
					if(constantTablePoolIndex == constantTablePool.size)
					{
						constantTable = new ConstantTable(skin, constantTablePoolIndex, buttonMinimalSize / 2, textFieldListener);
						constantTablePool.add(constantTable);
					}
					else // we get the ConstantTable in the pool.
					{
						constantTable = constantTablePool.get(constantTablePoolIndex);
					}
					constantTablePoolIndex++;
					
					if(constantTable != null)
					{
						constantTable.initFromConstant(currentDirectory.elements.get(i));
						// layout
						flickTable.row();
						flickTable.add(constantTable).center().expandX().fillX().minSize(buttonMinimalSize, buttonMinimalSize);
					}
				}
			}
		}
	}
	
	/**
	 * Update the optionsTable according the the state of the CE.
	 */
	private void refreshOptionTable()
	{
		optionsTable.clear();
		if(open)
		{
			// If we are opened we add all the buttons
			optionsTable.add(openButton).minSize(buttonMinimalSize).top().right();
			optionsTable.row();
			optionsTable.add(backButton).minSize(buttonMinimalSize).top().right().expandX();
			optionsTable.row();
			optionsTable.add(rootButton).minSize(buttonMinimalSize).top().right().expandX();
			optionsTable.row();
			optionsTable.add(saveButton).minSize(buttonMinimalSize).top().right().expand();
			
			openButton.getLabel().setText("Close");
			
			updateRootBackButtonsVisibility();
			updateSaveButtonsVisibility();
		}
		else
		{
			// if we are closed we add only the Open Button.
			optionsTable.add(openButton).minSize(buttonMinimalSize).align(openButtonAlign).expand();
			
			openButton.getLabel().setText("Open");
			
			updateRootBackButtonsVisibility();
			updateSaveButtonsVisibility();
		}
	}
	
	//------------------------------------------------------------------
	// <ButtonClickListener>
	//------------------------------------------------------------------
	/**
	 * Custom ClickListener to handle all buttons click in the CE.
	 */
	class ButtonClickListener extends ClickListener
	{
		//------------------------------------------------------------------
		// <ActionCompletedListener>
		//------------------------------------------------------------------
		class ActionCompletedListener implements Runnable
		{
			public static final int NONE = 0;
			public static final int OPEN = 1;
			public static final int CLOSE = 2;
			
			private int m_mode = NONE;
			
			Runnable setMode(int _mode)
			{
				m_mode = _mode;
				return this;
			}
			
			public void run() {
				
				if(m_mode == OPEN)
				{
					mainTable.setY(0.0f);
					mainTable.pad(0);
					mainTable.invalidate();
				}
				else if( m_mode == CLOSE)
				{
					//When the anim is finished we effectively do the closing.
					open = false;
					
					refreshOptionTable();
					refreshFlickTable();
					
					// We add only the optionTable
					mainTable.clear();
					mainTable.pad(mainTablePadding);
					mainTable.add(optionsTable).expand().fill();
					
					if(listener != null)
						listener.onClosing();
					
					mainTable.setX(0.0f);
					mainTable.setY(0.0f);
				}
				
			}
		}
		
		/** Listener to now if an actor action is completed */
		private final ActionCompletedListener m_ActionListener = new ActionCompletedListener();
		
		@Override
		public void clicked (InputEvent event, float x, float y) {
			
			Actor actor = event.getListenerActor();
			if(actor == openButton)
			{
				if(!open)
					open();
				else
					close();
			}
			else if(actor == backButton)
			{
				back();
			}
			else if(actor == rootButton)
			{
				backToRoot();
			}
			else if(actor == saveButton)
			{
				save();
			}
			else
			{
				handleDirectoryClick(actor);
			}
		}
		
		/**
		 * Opens the CE.
		 */		
		void open()
		{
			open = true;
			
			mainTable.clear();
			
			refreshOptionTable();
			refreshFlickTable();
			
			if(listener != null)
				listener.onOpening();
			
			// if we are open we add the two parts optionTable and flickPane
			mainTable.add(optionsTable).expand().fill();
			mainTable.add(flickScrollpane).expand().fill();
			
			// we launch the openAnimation on the Table
			float xOffset = stage.getWidth() * 0.65f;
			mainTable.setX(xOffset);
		    mainTable.addAction(Actions.sequence(Actions.moveBy(-xOffset, mainTablePadding, 0.15f, Interpolation.pow3In), Actions.run(m_ActionListener.setMode(ActionCompletedListener.OPEN))));
		}
		
		/**
		 * Closes the CE.
		 */
		void close()
		{
			// We launch the close animation.
			float xOffset = stage.getWidth() - openButton.getX() - openButton.getWidth() - mainTablePadding;
			mainTable.addAction(Actions.sequence(Actions.moveBy(xOffset, -mainTablePadding, 0.15f, Interpolation.pow3In), Actions.run(m_ActionListener.setMode(ActionCompletedListener.CLOSE))));
		}
		
		/**
		 * Save the constants if needed.
		 */
		void save()
		{
			if(needSave)
			{
				if(manager.saveConstants())
				{
					needSave = false;
					updateSaveButtonsVisibility();
				}
			}
		}
		
		/**
		 * Return to the root directory.
		 */
		void backToRoot()
		{
			if(currentDirectory.parent != null)
			{
				currentDirectory = manager.getRoot();
				refreshFlickTable();
			}
		}
		
		/**
		 * Navigates one step backward in the Directory.
		 */
		void back()
		{
			if(currentDirectory.parent != null)
			{
				currentDirectory = currentDirectory.parent;
				refreshFlickTable();
			}
		}
		
		/**
		 * Navigates in the Directory when click on a child Directory Button.
		 * @param actor Actor the click occured on.
		 */
		void handleDirectoryClick(Actor actor)
		{
			String name = actor.getName();
			
			if(name != null)
			{
				String[] splitted = name.split("directory");
				if(splitted.length == 2)
				{
					// gets the index of the directory.
					int index = Integer.parseInt(splitted[1]);
					
					if(index>=0 && index < currentDirectory.children.size)
					{
						currentDirectory = currentDirectory.children.get(index);
						refreshFlickTable();
					}
				}
			}
		}
	}
	//------------------------------------------------------------------
	// </ButtonClickListener>
	//------------------------------------------------------------------
	
	//------------------------------------------------------------------
	// <ConstantTextFieldListener>
	//------------------------------------------------------------------
	/**
	 * Custom TextFieldListener to handle all textField input in the CE.
	 */
	class ConstantTextFieldListener implements TextFieldListener
	{
		@Override
		public void keyTyped(TextField textField, char key) {
			handleTextFieldClick(textField, key);
		}
		
		/**
		 * <p>Handles input in the constant textfields.</p>
		 * <p>Note : For now it is handled every time an key is inputed in a textField. We should only do it when a textField is unfocused.<p>
		 * @param textField Inputed textField.
		 * @param key Key entered.
		 */
		void handleTextFieldClick(TextField textField, char key)
		{
			String name = textField.getName();
			String text = textField.getText();
			if(name != null)
			{
				String[] splitted = name.split("textField");
				if(splitted.length == 2)
				{
					// we get the index of the textfield.
					int index = Integer.parseInt(splitted[1]);
					
					if(index>=0 && index < currentDirectory.elements.size)
					{
						Constant constant = currentDirectory.elements.get(index);
						
						if(text != null && text.length() != 0)
						{
							// We try to apply the change.
							boolean reverse = false;
							if(constant instanceof ConstantFloat)
							{
								ConstantFloat constantFloat = (ConstantFloat)constant;	
								try
								{
									constantFloat.value = Float.parseFloat(text);
								}
								catch(NumberFormatException e)
								{
									reverse = true;
								}
							}
							else if(constant instanceof ConstantInt)
							{
								ConstantInt constantInt = (ConstantInt)constant;
								
								try
								{
									constantInt.value = Integer.parseInt(textField.getText());
								}
								catch(NumberFormatException e)
								{
									reverse = true;
								}
								
							}
							
							// if the change cannot be applied, we restore the current value of the constant in the textField.
							if(reverse)
							{
								textField.setText(constant.toString());
							}
							else
							{
								// We notify the saving and the change on the constant.
								needSave = true;
								updateSaveButtonsVisibility();
								if(listener != null)
									listener.onConstantChanged(constant);
							}
							
						}
					}
				}
			}
		}
	}
	//------------------------------------------------------------------
	// </ConstantTextFieldListener>
	//------------------------------------------------------------------
}
