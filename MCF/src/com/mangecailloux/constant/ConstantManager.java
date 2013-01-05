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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mangecailloux.directory.Directory;
import com.mangecailloux.directory.RootDirectory;

/** 
 * <p>
 * ConstantManager handles storing, loading and saving constants.
 * </p>
*/
public class ConstantManager
{
	/** File path used for external storage and loading */
	private final String 						externalFilePath;
	/** File path used for internal loading */
	private final String 						internalFilePath;
	
	/** Root directory to store all the constants*/
	private final RootDirectory<Constant>		root;
	
	/** Enable/Disable logging. */
	private boolean 							logEnabled;
	
	/**
	 * Create the ConstantManager, will try to load the constants.
	 * @param _internalFileName File path used for internal loading, without the extension.
	 * @param _externalFileName File path used for external storage and loading, without the extension.
	 */
	public ConstantManager(String _internalFileName, String _externalFileName)
	{
		this(_internalFileName, _externalFileName, false);
	}
	
	/**
	 * Create the ConstantManager, will try to load the constants.
	 * @param _internalFileName File path used for internal loading, without the extension.
	 * @param _externalFileName File path used for external storage and loading, without the extension.
	 * @param _log Enable logging right away, to be able to log the constant loading.
	 */
	public ConstantManager(String _internalFileName, String _externalFileName, boolean _log)
	{
		externalFilePath = _externalFileName + ".cst";
		internalFilePath = _internalFileName + ".cst";
		root = new RootDirectory<Constant>("Root");
		logEnabled = _log;
		loadConstants();	
	}
	
	/** Enable/Disable logging. */
	public void log(boolean _enable)
	{
		logEnabled = _enable;
		root.log(_enable);
	}
	
	/**
	 * <p>Define a {@link ConstantInt}. </p>
	 * <p>If a constant is loaded from the *.cst file but never defined it will be marked as "unused" an will be removed during the next save.</p>
	 * @param _path Full path to store the constant.
	 * @param _default Default value for the constant, used if the constant is created, ignored if it had been already loaded.
	 * @return the wanted ConstantInt.
	 */
	public ConstantInt defineConstantInt(String _path, int _default)
	{
		// Try to get the constant from the root directory, in case it has been already loaded.
		ConstantInt constant = root.getElement(_path, ConstantInt.class);
		if(constant == null)
		{
			// if not, it's a new constant, so we add it and set the default value.
			constant = root.addElement(_path, ConstantInt.class);
			constant.value = _default;
		}
		else
		{
			// if we find it but the constant is already used, that mean we tried to define two constants with the same path.
			if(constant.used)
				throw new RuntimeException("The constant with the path : " + _path + " was already defined");
		}
		// sets it to used.
		constant.used = true;
		return constant;
	}
	
	/**
	 * <p>Define a {@link ConstantFloat}. </p>
	 * <p>If a constant is loaded from the *.cst file but never defined it will be marked as "unused" an will be removed during the next save.</p>
	 * @param _path Full path to store the constant.
	 * @param _default Default value for the constant, used if the constant is created, ignored if it had been already loaded.
	 * @return the wanted ConstantFloat.
	 */
	public ConstantFloat defineConstantFloat(String _path, float _default)
	{
		// Try to get the constant from the root directory, in case it has been already loaded.
		ConstantFloat constant = root.getElement(_path, ConstantFloat.class);
		if(constant == null)
		{
			// if not, it's a new constant, so we add it and set the default value.
			constant = root.addElement(_path, ConstantFloat.class);
			constant.value = _default;
		}
		else
		{
			// if we find it but the constant is already used, that mean we tried to define two constants with the same path.
			if(constant.used)
				throw new RuntimeException("The constant with the path : " + _path + " was already defined");
		}
		// sets it to used.
		constant.used = true;	
		return constant;
	}
	
	/**
	 * Remove all unused constants and sort them by name;
	 */
	public void clean()
	{
		removeUnusedConstants();
		root.sort();
	}
	
	/**
	 *  Remove all unused constants from the root.
	 */
	protected void removeUnusedConstants()
	{
		removeUnusedConstants(root);
	}
	/**
	 * <p>Remove all constants that are not used anymore (used == false).</p>
	 * <p>If a directory gets empty after removing its unused constants it will be removed too</p>
	 * @param _directory Directory to clean.
	 */
	private void removeUnusedConstants(Directory<Constant> _directory)
	{
		if(_directory == null)
			return;
		
		// remove unused constants locally
		if(_directory.elements != null)
		{
			Iterator<Constant> it = _directory.elements.iterator();
			while(it.hasNext())
			{
				Constant constant = it.next();
				if(constant.used == false)
					it.remove();
			}
		}
		
		if(_directory.children != null)
		{
			// remove unused constants of our childs
			Iterator<Directory<Constant>> itChild = _directory.children.iterator();
			while(itChild.hasNext())
			{
				Directory<Constant> child = itChild.next();
				removeUnusedConstants(child);
				// if a child gets empty we remove it
				if(child.isEmpty() == true)
					itChild.remove();
			}
		}
	}	
	
	@Override
	public String toString()
	{
		return root.toXML();
	}
	
	/**
	 * <p>Save the constants, used the external file path.</p>
	 * @return true if the save occurred, false if it failed. Logging will give more infos.
	 */
	public boolean saveConstants()
	{
		// We check if the external storage is available.
		if(!Gdx.files.isExternalStorageAvailable())
		{
			if(logEnabled)
				Gdx.app.log("ConstantManager", "No external storage available, save failed.");
			return false;
		}
		
		FileHandle handle = Gdx.files.external(externalFilePath);
		OutputStreamWriter out = null;
		try {
			OutputStream outStream = handle.write(false);
			if(outStream != null)
			{
				// clean the constants before saving them to have only the latest sorted data.
				clean();
				String xml = root.toXML();
				out = new OutputStreamWriter(outStream);
				out.write(xml);
			}
		}
		catch (IOException e) {
			if(logEnabled)
				Gdx.app.log("ConstantManager", "Cannot write file : "+ externalFilePath +", save failed.");
		} 
		finally
		{
			if (out != null) {
				try {
					out.close();
					return true;
				} catch (IOException e) {
					if(logEnabled)
						Gdx.app.log("ConstantManager", "Cannot close file : "+ externalFilePath +", save failed.");
				}
			}
		}
		return false;
	}
	
	/**
	 * @return the root directory of the constants.
	 */
	protected Directory<Constant> getRoot()
	{
		return root;
	}
	
	/**
	 * <p>Loads the constant from the wanted file.</p>
	 * @param _handle Handle of the wanted file.
	 * @param _internal true is the file is internal, useful for logging.
	 * @return true is loading is successful.
	 */
	private boolean loadConstants(FileHandle _handle, boolean _internal)
	{
		// if the file exist.
		if(_handle != null && _handle.exists())
		{
			InputStream in = null;
			try {
				in = _handle.read();
				root.fromXML(in);
			}
			catch (IOException e) {
				if(logEnabled)
				{
					if(_internal)
						Gdx.app.log("ConstantManager", "Cannot read internal file : "+ internalFilePath +", load failed.");
					else
						Gdx.app.log("ConstantManager", "Cannot read external file : "+ externalFilePath +", load failed.");
				}
			} 
			finally
			{
				if (in != null) {
					try {
						in.close();
						return true;
					} catch (IOException e) {
						
						if(logEnabled)
						{
							if(_internal)
								Gdx.app.log("ConstantManager", "Cannot close internal file : "+ internalFilePath +", load failed.");
							else
								Gdx.app.log("ConstantManager", "Cannot close external file : "+ externalFilePath +", load failed.");
						}
					}
				}
			}
		}
		else if(_handle != null)
		{
			if(logEnabled)
			{
				if(_internal)
					Gdx.app.log("ConstantManager", "Internal file : "+ internalFilePath +" doesn't exists.");
				else
					Gdx.app.log("ConstantManager", "External file : "+ externalFilePath +" doesn't exists.");
			}
		}
		return false;
	}
	
	/**
	 * <p>Loads the constant from the external and internal files.</p>
	 * <p>Will try to load from the external first as it is more likely to be the most recent file. If it fails, it will try to load from the internal file.</p>
	 */
	private void loadConstants()
	{
		FileHandle externalHandle = null;
	
		// try to load external file first.
		if(Gdx.files.isExternalStorageAvailable())
		{
			externalHandle = Gdx.files.external(externalFilePath);
		}
		else if(logEnabled)
			Gdx.app.log("ConstantManager", "Load : No external storage available, trying internal file");
	
		if(!loadConstants(externalHandle, false))
		{
			// if external loading fail, try to load the internal one.
			FileHandle internalHandle = null;
			internalHandle = Gdx.files.internal(internalFilePath);
			loadConstants(internalHandle, true);
		}
	}
}
