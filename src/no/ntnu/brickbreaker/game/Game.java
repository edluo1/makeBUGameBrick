package no.ntnu.brickbreaker.game;
	
	import java.util.Observable;
import java.util.Observer;
	
	import no.ntnu.brickbreaker.GameHolder;
import no.ntnu.brickbreaker.models.Ball;
import no.ntnu.brickbreaker.models.Brick;
import no.ntnu.brickbreaker.models.Paddle;
	
	import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.extension.input.touch.controller.MultiTouch;
import org.anddev.andengine.extension.input.touch.controller.MultiTouchController;
import org.anddev.andengine.extension.input.touch.controller.MultiTouchException;
	
	import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings.Global;
import android.view.Display;
import android.widget.Toast;
	
	public class Game extends BaseGameActivity implements IOnSceneTouchListener, Observer{
		private static int CAMERA_HEIGHT = 480;
		private static int CAMERA_WIDTH = 800;
		private Camera mCamera;
	
	
		private TiledTextureRegion ballTextureRegion;
	
		private Texture paddleTexture;
		private Texture ballTexture;
		private Paddle paddle;
		private GameHolder gameHolder;
		private Object mCurViewMode;
		private Paddle paddle2;
		private Paddle paddle1;
		
		//Keeps track of player scoes
		private int Player1_Score = 0;
		private int Player2_Score = 0;
	
	
	
		public Engine onLoadEngine() {
	
			final Display defaultDisplay = getWindow().getWindowManager().getDefaultDisplay();
			CAMERA_WIDTH = defaultDisplay.getWidth();
			CAMERA_HEIGHT = defaultDisplay.getHeight();
			this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
			this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
            final Engine engine = new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera));

            try {
                    if(MultiTouch.isSupported(this)) {
                            engine.setTouchController(new MultiTouchController());
                            if(MultiTouch.isSupportedDistinct(this)) {
                                    Toast.makeText(this, "MultiTouch detected --> Drag multiple Sprites with multiple fingers!", Toast.LENGTH_LONG).show();
                            } else {
                                    Toast.makeText(this, "MultiTouch detected --> Drag multiple Sprites with multiple fingers!\n\n(Your device might have problems to distinguish between separate fingers.)", Toast.LENGTH_LONG).show();
                            }
                    } else {
                            Toast.makeText(this, "Sorry your device does NOT support MultiTouch!\n\n(Falling back to SingleTouch.)", Toast.LENGTH_LONG).show();
                    }
            } catch (final MultiTouchException e) {
                    Toast.makeText(this, "Sorry your Android Version does NOT support MultiTouch!\n\n(Falling back to SingleTouch.)", Toast.LENGTH_LONG).show();
            }

            return engine;
    }
		
		
	
		@Override
		public void onLoadResources() {
			
			gameHolder = GameHolder.getInstance();
			gameHolder.addObserver(this);
			gameHolder.setGameActivity(this);
			
			this.ballTexture = new Texture(64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
			this.paddleTexture = new Texture(1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
	
			TextureRegionFactory.setAssetBasePath("gfx/");
	
			this.ballTextureRegion = TextureRegionFactory.createTiledFromAsset(this.ballTexture, this, "ball.png", 0, 0, 1, 1);
	
			this.mEngine.getTextureManager().loadTexture(this.paddleTexture);
			this.mEngine.getTextureManager().loadTexture(this.ballTexture);
		}
	
	
	
	
		@Override
		public Scene onLoadScene() {
	
	
			this.mEngine.registerUpdateHandler(new FPSLogger());
	
			final Scene scene = new Scene(1);
			scene.setBackground(new ColorBackground(0f, 0f, 0f));
			scene.setOnSceneTouchListener(this);
	
			//creates player balls
			//ball 1 is blue
			final Ball ball1 = new Ball(CAMERA_WIDTH/2, CAMERA_HEIGHT-30-16, this.ballTextureRegion, mEngine, 1);
			ball1.setVelocity(100.0f, 100.0f);
			
			//ball 2 is red
			final Ball ball2 = new Ball(CAMERA_WIDTH/2, CAMERA_HEIGHT-770-784, this.ballTextureRegion, mEngine, 2);
			ball2.setVelocity(100.0f, 100.0f);
	
			scene.getTopLayer().addEntity(ball1);
			scene.getTopLayer().addEntity(ball2);
			
			//creates player paddles
			paddle1 = new Paddle(CAMERA_WIDTH/2, CAMERA_HEIGHT-45, CAMERA_HEIGHT/6, CAMERA_WIDTH/17) {
			    
				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
			    	switch(pSceneTouchEvent.getAction()) {
			    	case TouchEvent.ACTION_DOWN:
			    		this.setPosition(pSceneTouchEvent.getX() - this.getWidth()/2, CAMERA_HEIGHT-45);
			    		break;
			    	case TouchEvent.ACTION_MOVE:
			    		this.setPosition(pSceneTouchEvent.getX() - this.getWidth()/2, CAMERA_HEIGHT-45);
			    		break;
			    	}
			    	return true;
			    }
			    
			};
		    scene.registerTouchArea(paddle1);
			
			paddle2 = new Paddle(CAMERA_WIDTH/2, 30, CAMERA_HEIGHT/6, CAMERA_WIDTH/17) {
			    public boolean onAreaTouched(TouchEvent pSceneTouchEvent,float pTouchAreaLocalX, float pTouchAreaLocalY) {
			    	switch(pSceneTouchEvent.getAction()) {
			    	case TouchEvent.ACTION_DOWN:
			    		this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2, 30);
			    		break;
			    	case TouchEvent.ACTION_MOVE:
			    		this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2, 30);
			    		break;
			    	}
			    	return true;
			    }
			};
		    scene.registerTouchArea(paddle2);
			
			//sets brick properties
			int num_bricks_across = 5;
			int num_bricks_down = 10;
			int brick_width = (CAMERA_HEIGHT/16);
			int brick_height = (CAMERA_WIDTH/32);
			
			//implements matrix of bricks
			final Brick[][] bricks1 = new Brick[num_bricks_across][num_bricks_down];
			
			final Brick[][] bricks2 = new Brick[num_bricks_across][num_bricks_down];
		
			
			
			//adds Player1 bricks to scene
			for (int i = 0; i < bricks1.length; i++) {
				for (int j = 0; j < bricks1[0].length; j++) {
					bricks1[i][j]= new Brick( ( 10+j*(brick_width+2)) , ( ((CAMERA_HEIGHT/2) + 50) + (i*(brick_height+2)) ) , brick_width, brick_height, 2);
					scene.getTopLayer().addEntity(bricks1[i][j]);
				}
			}
			
			//adds PLayer2 bricks to scene
			for (int i = 0; i < bricks2.length; i++) {
				for (int j = 0; j < bricks2[0].length; j++) {
					bricks2[i][j]= new Brick( ( 10+j*(brick_width+2)) , ( ((CAMERA_HEIGHT/2) - 50) - (i*(brick_height+2)) ) , brick_width, brick_height, 1);
					scene.getTopLayer().addEntity(bricks2[i][j]);
				}
			}
			
			//adds entities to the screen
			scene.getTopLayer().addEntity(paddle1);
			scene.getTopLayer().addEntity(ball1);
			scene.getTopLayer().addEntity(paddle2);
			scene.getTopLayer().addEntity(ball2);
			scene.registerTouchArea(paddle1);
			scene.registerTouchArea(paddle2);
	
			/* The actual collision-checking. */
			scene.registerUpdateHandler(new IUpdateHandler() {
				public void reset() { }
	
				public void onUpdate(final float pSecondsElapsed) {
						if (ball1.collidesWith(paddle1)) {
							ball1.bounceWithPaddle(paddle1);
						} else if (ball1.collidesWith(paddle2)) {
							ball1.bounceWithPaddle(paddle2);
						} else if (ball2.collidesWith(paddle2)) {
							ball2.bounceWithPaddle(paddle2);
						} else if (ball2.collidesWith(paddle1)) {
							ball2.bounceWithPaddle(paddle1);
						} else if ( (ball1.getY() >= Game.getCAMERA_HEIGHT() - 30) || (ball2.getY() >= Game.getCAMERA_HEIGHT() - 30) ) {
							scene.setBackground(new ColorBackground(255f, 0f, 0f));
							Player2_Score++;
						} else if ( (ball1.getY() <= 30) || (ball2.getY() <= 30) ) {
							scene.setBackground(new ColorBackground(0f, 0f, 255f));
							Player1_Score++;
						} else {
							for (int i = 0; i < bricks1.length; i++) {
								for (int j = 0; j < bricks1[0].length; j++) {
									scene.setBackground(new ColorBackground(0f, 0f, 0f));
									if ( ball1.collidesWith(bricks1[i][j]) ) {
										bricks1[i][j].setPosition(CAMERA_HEIGHT+20, CAMERA_WIDTH+20);
										scene.getTopLayer().removeEntity(bricks1[i][j]);
										ball1.bounceWithBrick(bricks1[i][j]);
									}
									if ( ball2.collidesWith(bricks2[i][j])) {
										bricks2[i][j].setPosition(CAMERA_HEIGHT+20, CAMERA_WIDTH+20);
										scene.getTopLayer().removeEntity(bricks2[i][j]);
										ball2.bounceWithBrick(bricks2[i][j]);
									}
								}
							}
						}
						
				
			}});
	
			return scene;
		}
	
		public static int getCAMERA_HEIGHT() {
			return CAMERA_HEIGHT;
		}
		public static int getCAMERA_WIDTH() {
			return CAMERA_WIDTH;
		}
	
		@Override
		public void onLoadComplete() {
			// TODO Auto-generated method stub
	
		}
	
		@Override
		public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
			/*
			paddle1.setPosition(pSceneTouchEvent.getX() - paddle1.getWidth() / 2, Game.getCAMERA_HEIGHT()-30);
			paddle2.setPosition(pSceneTouchEvent.getX() - paddle2.getWidth() / 2, 30);
			*/
			return true;
		}
		
		protected void onPause() {
			super.onPause();
			gameHolder.setGameState(gameHolder.getPausedGameState());
		}
		
	
		@Override
		public void update(Observable observable, Object data) {
			// TODO Auto-generated method stub
			
		}
	}
