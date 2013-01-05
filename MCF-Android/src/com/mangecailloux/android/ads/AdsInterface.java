package com.mangecailloux.android.ads;

import com.mangecailloux.ads.IAdsInterface;

public class AdsInterface implements IAdsInterface
{
	private final AdsHandler handler;
	
	public AdsInterface(AdsHandler _handler)
	{
		handler = _handler;
	}
	
	@Override
	public void showAds(boolean _show) {
		handler.sendEmptyMessage(_show ? AdsHandler.SHOW_ADS : AdsHandler.HIDE_ADS);
	}

}
