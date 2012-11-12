package com.mangecailloux.screens.loading;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.mangecailloux.screens.Screen;
import com.mangecailloux.screens.ScreenManager;

public class MangeCaillouxSplashScreen extends LoadingScreen
{
	private  	final	 Stage		stage;
	private    	final	 Table		table;
	private    	final	 Table		whiteTable;
	
	private 	final 	 Image 		caillouImage;
	private 	final 	 Image 		textImage;
	private 	final 	 Image 		whiteImage;
	
	private 	final 	 TextureAtlas atlas;
	
	private				 boolean	startAnimation;
	private				 boolean	readyToChangeScreen;
	
	private 	final	 TextureRegionDrawable eatenCaillouDrawable;
	private 	final	 TextureRegionDrawable caillouDrawable;
	
	public MangeCaillouxSplashScreen(ScreenManager _Manager, Screen _ToLoad) {
		super("MangeCaillouxSplashScreen", _Manager, _ToLoad, false);
		
		String folder = "HD/";
		if(Gdx.graphics.getWidth() < (800 + 480)/2)
			folder = "SD/";
		
		atlas = new TextureAtlas("data/splash/" + folder + "MCG-Splash.pack");
		
		
		textImage = new Image(atlas.findRegion("MangeCailloux"));
		whiteImage = new Image(atlas.findRegion("white"));
		
		caillouDrawable = new TextureRegionDrawable(atlas.findRegion("caillou"));
		eatenCaillouDrawable = new TextureRegionDrawable(atlas.findRegion("caillou_mange"));
		
		caillouImage = new Image(caillouDrawable);
		
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		
		caillouImage.setScaling(Scaling.fit);
		textImage.setScaling(Scaling.fit);
		
		table = new Table();
		
		table.setFillParent(true);
		
		stage.addActor(table);
		
		whiteTable = new Table();
		whiteTable.setFillParent(true);
		
		whiteTable.add(whiteImage).expand().fill();
		
		stage.addActor(whiteTable);
		
		inputMultiplexer.addProcessor(stage);
		
		whiteTable.addListener(new ClickListener()
		{
			public void clicked (InputEvent event, float x, float y) {
				readyToChangeScreen = true;
			}
		});
		
	//	table.debug();
	}
	
	public void initUI()
	{
		table.clear();
		
		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();
		
		float leftRightPadding = width*0.15f;
		
		float topPadding = height*0.05f;
		float inBetweenPadding = height*0.05f;
		float bottomPadding = height*0.1f;
		
		float textHeight = height*0.25f;
		
		table.add(caillouImage).pad(topPadding, leftRightPadding, 0.0f, leftRightPadding).prefHeight(width - 2.0f*leftRightPadding);
		table.row();
		table.add(textImage).pad(inBetweenPadding, leftRightPadding, bottomPadding, leftRightPadding).prefHeight(textHeight);
	}
	
	private final float firstDelay = 0.5f;
	private final float caillouFadeInDuration = 0.25f;
	private final float whiteDelay = 0.5f;
	private final float whiteHalfDuration = 0.2f;
	private final float delayBeforeText = 0.5f;
	private final float textFadeInDuration = 0.5f;
	private final float delayBeforeGlobalFade = 1.0f;
	private final float globalFadeDuration = 0.5f;
	private final float delayBeforeScreenSwap = 1.0f;
	
	private Runnable swapImage = new Runnable() {
		
		@Override
		public void run() {
			caillouImage.setDrawable(eatenCaillouDrawable);
		}
	};
	
	private Runnable changeScreen = new Runnable() {
		
		@Override
		public void run() {
			readyToChangeScreen = true;
		}
	};
	
	private Runnable globalFade = new Runnable() {
		
		@Override
		public void run() {
			SequenceAction action = Actions.sequence(	Actions.delay(delayBeforeGlobalFade),
														Actions.alpha(0.0f, globalFadeDuration, Interpolation.sineOut),
														Actions.delay(delayBeforeScreenSwap),
														Actions.run(changeScreen)
				);

			table.addAction(action);
		}
	};
	
	private Runnable textAnimation = new Runnable() {
		
		@Override
		public void run() {
			SequenceAction action = Actions.sequence(	Actions.delay(delayBeforeText),
														Actions.alpha(0.0f),
														Actions.show(),
														Actions.alpha(1.0f, textFadeInDuration, Interpolation.sineIn),
														Actions.run(globalFade)
													);

			textImage.addAction(action);
		}
	};
	
	private Runnable whiteAnimation = new Runnable() {
		
		@Override
		public void run() {
			SequenceAction action = Actions.sequence(	Actions.alpha(0.0f),
														Actions.show(),
														Actions.delay(whiteDelay),
														Actions.alpha(1.0f, whiteHalfDuration, Interpolation.sineIn),
														Actions.run(swapImage),
														Actions.alpha(0.0f, whiteHalfDuration, Interpolation.sineOut),
														Actions.hide(),
														Actions.run(textAnimation)
													);

			whiteTable.addAction(action);
		}
	};
	
	private void startAnimation()
	{
		SequenceAction action = Actions.sequence(	Actions.alpha(0.0f),
													Actions.show(),
													Actions.delay(firstDelay),
													Actions.alpha(1.0f, caillouFadeInDuration, Interpolation.sineIn),
													Actions.run(whiteAnimation)
												);

		caillouImage.addAction(action);
	}
	
	@Override
	protected void onActivation() 
	{
		initUI();
		whiteTable.setVisible(false);
		textImage.setVisible(false);
		caillouImage.setVisible(false);
		startAnimation = true;
		readyToChangeScreen = false;
	}

	@Override
	protected void onDeactivation() {
		
	}

	@Override
	public void onDispose() {
		stage.dispose();

		atlas.dispose();
	}
	
	@Override
	protected void onUpdate(float _fDt) {
		super.onUpdate(_fDt);
		
		if(startAnimation)
		{
			startAnimation = false;
			startAnimation();
		}
		
		if(readyToChangeScreen && nextIsLoaded)
			changeScreen();
	}

	@Override
	protected void onRender(float _fDt) {
		
		GL20 gl = Gdx.graphics.getGL20();
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gl.glClearColor(0.0f, 0.0f,0.0f, 1.0f);
		
		stage.act(_fDt);
		stage.draw();
//		Table.drawDebug(stage);
	}

	@Override
	protected void onResize(int width, int height) {
		stage.setViewport(width, height, false);
		initUI();
	}

	@Override
	protected void onFirstActivation() {
		
	}

	@Override
	protected void onPause() {
		
	}

	@Override
	protected void onResume() {
		
	}
}
