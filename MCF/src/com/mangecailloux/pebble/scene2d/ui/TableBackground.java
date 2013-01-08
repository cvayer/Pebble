package com.mangecailloux.pebble.scene2d.ui;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class TableBackground {
	/** Optional. */
	public Drawable background;

	public TableBackground () {
	}

	public TableBackground (Drawable background) {
		this.background = background;
	}

	public TableBackground (TableBackground tableBackground) {
		this.background = tableBackground.background;
	}
}