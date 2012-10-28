package com.deadpixels.lib.language;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.ObjectMap;

public abstract class LanguagesManager {       
     
        private ObjectMap<String, String> 	languageMap;
        private String 						languageName;
        private final String 				filePath;
        private final String 				defaultLanguage;

		private final DocumentBuilderFactory dbf;
        private 	  DocumentBuilder db;
        
        
        public LanguagesManager(String _path, String _defaultLanguage) {
                // Create language map
        	languageMap = new ObjectMap<String, String>();
            filePath = _path;
            defaultLanguage = _defaultLanguage;
            languageName = null;
            
            dbf = DocumentBuilderFactory.newInstance();
            try {
				db = dbf.newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				Gdx.app.log("LanguagesManager", "News Document Error.");
			}
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

                     Document doc;
                     doc = db.parse(in);

                     Element root = doc.getDocumentElement();
                     
                     NodeList languages = root.getElementsByTagName("language");
                     int numLanguages = languages.getLength();
                     
                     for (int i = 0; i < numLanguages; ++i)
                     {
                    	Node language = languages.item(i);    
                        if (language.getAttributes().getNamedItem("name").getTextContent().equals(_languageName)) 
                        {
                    	 	languageName = _languageName;
                    	 	languageMap.clear();
                            Element languageElement = (Element)language;
                            NodeList strings = languageElement.getElementsByTagName("string");
                            int numStrings = strings.getLength();
                             
							for (int j = 0; j < numStrings; ++j) 
							{
								NamedNodeMap attributes = strings.item(j).getAttributes();
								String key = attributes.getNamedItem("key").getTextContent();
								String value = attributes.getNamedItem("value").getTextContent();
								value = value.replace("<br/>", "\n");
								value = value.replace("\\n", "\n");
								languageMap.put(key, value);
							}
                         
                         	OnLoad();
                    		success = true;
                        }
                   }   	
    			}
    			catch (IOException e) {
    				Gdx.app.log("LanguagesManager", "Cannot read internal file : "+ filePath +", load failed.");
    			} 
    			catch (SAXException e) {
    				Gdx.app.log("LanguagesManager", "Parse Error.");
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