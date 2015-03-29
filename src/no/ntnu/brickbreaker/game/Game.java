package no.ntnu.brickbreaker.game;
	
	import java.util.Observable;
import java.util.Observer;

	import no.ntnu.brickbreaker.GameHolder;
import no.ntnu.brickbreaker.models.Ball;
import no.ntnu.brickbreaker.models.Brick;
import no.ntnu.brickbreaker.models.Paddle;

	import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.camera.hud.HUD;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.BuildableTexture;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.builder.ITextureBuilder;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.entity.text.*;
import org.anddev.andengine.opengl.font.*;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.extension.input.touch.controller.MultiTouch;
import org.anddev.andengine.extension.input.touch.controller.MultiTouchController;
import org.anddev.andengine.extension.input.touch.controller.MultiTouchException;
 


	import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
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
		private Font  fontP1;
		private ChangeableText textP1;
		private Texture mFontTexture1;
		private Font  fontP2;
		private ChangeableText textP2;
		private Texture mFontTexture2;
		private ChangeableText textP3;
		private ChangeableText textP4;
		
		//Keeps track of player scores
		private int Player1_Score = 0;
		private int Player2_Score = 0;
		
		
		public Engine onLoadEngine() {
	
			final Display defaultDisplay = getWindow().getWindowManager().getDefaultDisplay();
			CAMERA_WIDTH = defaultDisplay.getWidth();
			CAMERA_HEIGHT = defaultDisplay.getHeight();
			this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
			final Engine engine = new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera));
		
			//beginMultiTouch Initialization here
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
			
			  this.mFontTexture1 = new Texture(512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
              this.fontP1 = new Font(this.mFontTexture1, Typeface.DEFAULT_BOLD, 36, true, Color.BLUE);
              this.textP1 = new ChangeableText((CAMERA_WIDTH - 50), (CAMERA_HEIGHT - 50), fontP1, "00");
              
              
              this.mFontTexture2 = new Texture(512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
              this.fontP2 = new Font(this.mFontTexture2, Typeface.DEFAULT_BOLD, 36, true, Color.RED);
              this.textP2 = new ChangeableText(10, 5, fontP2, "00");
              textP2.setScale(-1f, -1f);
              
              this.textP3 = new ChangeableText(10, 5, fontP1, "00");
              this.textP4 = new ChangeableText(10, 5, fontP2, "00");
              textP4.setScale(-1f, -1f);
   		
	
			TextureRegionFactory.setAssetBasePath("gfx/");
	
			this.ballTextureRegion = TextureRegionFactory.createTiledFromAsset(this.ballTexture, this, "ball.png", 0, 0, 1, 1);
	
			this.mEngine.getTextureManager().loadTexture(this.paddleTexture);
			this.mEngine.getTextureManager().loadTexture(this.ballTexture);
			this.mEngine.getTextureManager().loadTexture(this.mFontTexture1);
			this.mEngine.getTextureManager().loadTexture(this.mFontTexture2);
			mEngine.getFontManager().loadFont(fontP1);
			mEngine.getFontManager().loadFont(fontP2);
			
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
			ball1.setSize(16, 16);
			//ball 2 is red
			final Ball ball2 = new Ball(CAMERA_WIDTH/2, CAMERA_HEIGHT-1000, this.ballTextureRegion, mEngine, 2);
			ball2.setVelocity(100.0f, 100.0f);
			ball2.setSize(16, 16);
			
			
			//creates player paddles
			paddle1 = new Paddle(CAMERA_WIDTH/2, (CAMERA_HEIGHT - ((CAMERA_WIDTH/17) + 20)) , CAMERA_HEIGHT/6, CAMERA_WIDTH/17, 1) {
			       
			    @Override
			    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
			        switch(pSceneTouchEvent.getAction()) {
			        case TouchEvent.ACTION_DOWN:
			         this.setPosition(pSceneTouchEvent.getX() - this.getWidth()/2, (CAMERA_HEIGHT - ((CAMERA_WIDTH/17) + 20)) );
			         break;
			        case TouchEvent.ACTION_MOVE:
			         this.setPosition(pSceneTouchEvent.getX() - this.getWidth()/2, (CAMERA_HEIGHT - ((CAMERA_WIDTH/17) + 20)) );
			         break;
			        }
			        return true;
			       }
			       
			   };
			      scene.registerTouchArea(paddle1);
			   
			   paddle2 = new Paddle(CAMERA_WIDTH/2, 20, CAMERA_HEIGHT/6, CAMERA_WIDTH/17, 2) {
			       public boolean onAreaTouched(TouchEvent pSceneTouchEvent,float pTouchAreaLocalX, float pTouchAreaLocalY) {
			        switch(pSceneTouchEvent.getAction()) {
			        case TouchEvent.ACTION_DOWN:
			         this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2, 20 );
			         break;
			        case TouchEvent.ACTION_MOVE:
			         this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2, 20 );
			         break;
			        }
			        return true;
			       }
			   };
			      scene.registerTouchArea(paddle2);
			   
			      
			//sets brick array properties
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
			scene.getTopLayer().addEntity(this.textP1);
			scene.getTopLayer().addEntity(this.textP2); 
			scene.getTopLayer().addEntity(ball1);
			scene.getTopLayer().addEntity(ball2);
			scene.getTopLayer().addEntity(paddle1);
			scene.getTopLayer().addEntity(ball1);
			scene.getTopLayer().addEntity(paddle2);
			scene.getTopLayer().addEntity(ball2);
			scene.registerTouchArea(paddle1);
			scene.registerTouchArea(paddle2);
	
			//adding variables to make collision checking easier
			final int pointUp = (Game.getCAMERA_HEIGHT() - 20);
			
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
						} 
						if ( (ball1.getY() + ball1.getHeight()) >= pointUp && ball1.scoring == false) {
							scene.setBackground(new ColorBackground(255f, 0f, 0f));
							ball1.scoring = true;
							Player2_Score++;
							if (Player2_Score >= 10) {
								P2_win();
							}
							textP2.setText(String.valueOf(Player2_Score));
							
						} else if ( (ball1.getY()) <= 4 && ball1.scoring == false) {
							scene.setBackground(new ColorBackground(0f, 0f, 255f));
							ball1.scoring = true;
							Player1_Score++;
							if (Player1_Score >= 10) {
								P1_win();
							}
							textP1.setText(String.valueOf(Player1_Score));
							
						} else if ((ball1.getY()) > 4 && (ball1.getY() + ball1.getHeight()) < pointUp) {
							ball1.scoring = false;
						}
						if ( (ball2.getY() + ball2.getHeight()) >= pointUp && ball2.scoring == false) {
							scene.setBackground(new ColorBackground(255f, 0f, 0f));
							ball2.scoring = true;
							Player2_Score++;
							if (Player2_Score >= 10) {
								P2_win();
							}
							textP2.setText(String.valueOf(Player2_Score));
							
							
						}else if ( (ball2.getY()) <= 4 && ball2.scoring == false) {
							scene.setBackground(new ColorBackground(0f, 0f, 255f));
							ball2.scoring = true;
							Player1_Score++;
							if (Player1_Score >= 10) {
								P1_win();
							}
							textP1.setText(String.valueOf(Player1_Score));	
							
							
						} else {
							ball2.scoring = false;
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
		
		//Implements win screens
		private void P1_win () {
			final Scene scene1 = new Scene(1);
			scene1.setBackground(new ColorBackground(0f, 0f, 0f));
			scene1.setOnSceneTouchListener(this);
			
			textP3.setText("WINNER");
			textP4.setText("LOSER");
			
			textP3.setScale(2.0f);
			textP4.setScale(2.0f);

			textP3.setPosition(CAMERA_WIDTH/2 - textP3.getWidth()/2, ((CAMERA_HEIGHT/2 - textP3.getHeight()/2) + 50) );
			textP4.setPosition(CAMERA_WIDTH/2 - textP4.getWidth()/2, ((CAMERA_HEIGHT/2 - textP4.getHeight()/2)) - 50);

			
            scene1.getTopLayer().addEntity(textP3);
            scene1.getTopLayer().addEntity(textP4);
            
            scene1.clearTouchAreas();
		}
		
		private void P2_win () {
			final Scene scene2 = new Scene(1);
			scene2.setBackground(new ColorBackground(0f, 0f, 0f));
			scene2.setOnSceneTouchListener(this);			
			
			textP3.setText("LOSER");
			textP4.setText("WINNER");
			
			textP3.setScale(2.0f);
			textP4.setScale(2.0f);
			
			textP3.setPosition(CAMERA_WIDTH/2 - textP3.getWidth()/2, ((CAMERA_HEIGHT/2 - textP3.getHeight()/2) + 50) );
			textP4.setPosition(CAMERA_WIDTH/2 - textP4.getWidth()/2, ((CAMERA_HEIGHT/2 - textP4.getHeight()/2) - 50) );
			
            scene2.getTopLayer().addEntity(textP3);
            scene2.getTopLayer().addEntity(textP4);
            

            scene2.clearTouchAreas();
		}
	
		private Texture getFontManager() {
			// TODO Auto-generated method stub
			return null;
		}

		private Typeface getTextureManager() {
			// TODO Auto-generated method stub
			return null;
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
