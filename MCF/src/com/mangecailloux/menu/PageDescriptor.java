package com.mangecailloux.menu;

public final class  PageDescriptor<T extends Page> 
{
	private	  final Class<T> type;
	private	  final PageParameters parameters;
	
	public PageDescriptor(Class<T> _type, PageParameters _parameters)
	{
		type = _type;
		parameters = _parameters;
	}
	
	@SuppressWarnings("unchecked")
	public <P extends PageParameters> P getParameters(Class<P> _paramType)
	{
		if(parameters!= null && _paramType.isInstance(parameters))
			return (P)parameters;
		return null;
	}
	
	public Class<T> getType()
	{
		return type;
	}
}
