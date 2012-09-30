package com.deadpixels.lib.language;

import java.io.IOException;
import java.io.InputStream;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public abstract class LanguagesManager {       
     
        private ObjectMap<String, String> 	languageMap;
        private String 						languageName;
        private final String 				filePath;
        private final String 				defaultLanguage;
        private final XmlReader 			xmlReader;
        
        
        public LanguagesManager(String _path, String _defaultLanguage) {
                // Create language map
        	languageMap = new ObjectMap<String, String>();
            filePath = _path;
            defaultLanguage = _defaultLanguage;
            languageName = null;
            xmlReader = new XmlReader();
        }
        
        public String getLanguage() {
                return languageName;
        }
        
        public static String getSystemLanguage()
        {
        	return java.util.Locale.getDefault().toString();
        }

        public String getString(String _key) {
            if (languageMap != null) {
                    // Look for string in selected language
                    String string = languageMap.get(_key);
                    if (string != null)
                           return string;
            }
    
            // Key not found, return the key itself
            return _key;
        }
        
        public String getString(String _key, Object... _objects) {
           return String.format(getString(_key), _objects);
        }
        
        public void loadLanguage(String _languageName)
        {
            if (!_loadLanguage(_languageName))
                _loadLanguage(defaultLanguage);
        }
        
        protected boolean _loadLanguage(String _languageName) 
        {
    		if(_languageName == null)
    			return false;
    		
    		if(_languageName.equals(languageName))
    			return true;
    	
    		FileHandle fileHandle = Gdx.files.internal(filePath);
    		boolean success = false;
    		if(fileHandle != null && fileHandle.exists())
    		{
    			InputStream in = null;
    			try {
    				in = fileHandle.read();
    				Element rootXML = xmlReader.parse(in);
    				
    				int languageCount = rootXML.getChildCount();
    				
    				for(int i = 0; i < languageCount; ++i)
    				{
    					Element lang = rootXML.getChild(i);
    					
    					if(lang.getAttribute("name").equals(_languageName))
    					{
    						languageName = _languageName;
    						
                    		languageMap.clear();
                    		
                    		int stringCount = lang.getChildCount();
                    		
                    		for(int j = 0; j < stringCount; ++j)
            				{
                    			Element str = lang.getChild(j);
                    			
                    			if(str.getName().equals("string"))
                    			{
                    				 String key = str.getAttribute("key");
                                     String value = str.getAttribute("value");
                                     languageMap.put(key, value);
                    			}
            				}
                    		OnLoad();
                    		success = true;
    					}
    				}
    				
    			}
    			catch (IOException e) {
    				Gdx.app.log("LanguagesManager", "Cannot read internal file : "+ filePath +", load failed.");
    			} 
    			finally
    			{
    				if (in != null) {
    					try {
    						in.close();
    					} catch (IOException e) {
    						Gdx.app.log("LanguagesManager", "Cannot close internal file : "+ filePath +", load failed.");
    					}
    				}
    			}
    		}
    		else if(fileHandle != null)
    		{
    			Gdx.app.log("LanguagesManager", "Internal file : "+ filePath +" doesn't exists.");
    		}
	    		
	    	return success;
        }
        
        protected abstract void OnLoad();
}