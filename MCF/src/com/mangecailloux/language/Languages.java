package com.mangecailloux.language;

public class Languages 
{
	public static final int ENG_ID = 0;
	public static final int FR_ID = 1;
	
	public static final String ENG = "en_UK";
	public static final String FR = "fr_FR";
	
	private static final String[] Languages = {ENG, FR};
	
	public static String getLanguage(int _id)
	{
		return Languages[_id];
	}
}
