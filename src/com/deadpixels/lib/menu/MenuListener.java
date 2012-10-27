package com.deadpixels.lib.menu;

//-------------------------------------------------------------------
//----  MenuListener
//-------------------------------------------------------------------
public interface MenuListener
{
	void onPageOpen(Page _page, PageDescriptor<? extends Page> _descriptor);
	void onPageClose(Page _page, PageDescriptor<? extends Page> _descriptor);
}
