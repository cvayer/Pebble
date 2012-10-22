package com.deadpixels.lib.popup;

public final class  PopUpDescriptor<T extends PopUp> 
{
	protected final Class<T> type;
	private	  final PopUpParameters parameters;
	
	public PopUpDescriptor(Class<T> _type, PopUpParameters _parameters)
	{
		type = _type;
		parameters = _parameters;
	}
	
	@SuppressWarnings("unchecked")
	public <P extends PopUpParameters> P getParameters(Class<P> _paramType)
	{
		if(parameters!= null && _paramType.isInstance(parameters))
			return (P)parameters;
		return null;
	}
}
