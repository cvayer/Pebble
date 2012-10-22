package com.deadpixels.lib.popup;

public interface PopUpListener 
{
	public void onOpen(PopUpDescriptor<?> _popUp);
	public void onClose(PopUpDescriptor<?> _popUp);
}
