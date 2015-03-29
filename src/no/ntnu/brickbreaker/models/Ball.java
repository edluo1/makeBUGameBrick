package no.ntnu.brickbreaker.models;

import no.ntnu.brickbreaker.game.Game;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.util.constants.TimeConstants;


public class Ball extends AnimatedSprite {
	final float baseVelocity = 200;
	float velocity = 200;
	
	public boolean scoring;

	int i =0;
	private Engine mEngine;
	
	public Ball(float positionX, float positionY, TiledTextureRegion positionTextureRegion, Engine mEngine, int Color) {
		super(positionX, positionY, positionTextureRegion);
		this.mEngine = mEngine;
		if (Color == 1) {
			this.setColor(0.5f, 0.5f, 1f);
		} else {
			this.setColor(1f, 0f, 0f);
		}
	}

	
	protected void onManagedUpdate(final float pSecondsElapsed) {
		if(this.mX < 0) {
			this.setVelocityX(-1*this.getVelocityX());
		} else if(this.mX + this.getWidth() > Game.getCAMERA_WIDTH()) {
			this.setVelocityX(-1*this.getVelocityX());
		}

		if(this.mY < 0) {
			this.setVelocityY(velocity);
		} else if(this.mY + this.getHeight() > Game.getCAMERA_HEIGHT()) {
			this.setVelocityY(-velocity);
		}

		super.onManagedUpdate(pSecondsElapsed);
	}
	
	public void bounceWithBrick(Brick brick){
    	float ballPositionX = this.getX();
		float ballPositionY = this.getY();
		float westWall = brick.getX();
		float eastWall = westWall + brick.getWidth();
		float northWall = brick.getY();
		float centerHorizontal = northWall - brick.getHeight()/2;
		float southWall = northWall - brick.getHeight();
		
		if (ballPositionY >= southWall || ballPositionY <= northWall) { // hit brick from vertical
			this.setVelocityY(-1*this.getVelocityY());
		}
		
		else if (ballPositionX <= westWall || ballPositionX >= eastWall) { // hit brick moving horizontal
			this.setVelocityX(-1*this.getVelocityX());
		} 

	}

	public void bounceWithPaddle(Paddle paddle) {
		float ballPositionX = this.getX();
		float paddleLeft = paddle.getX();
		float centerPaddle = paddleLeft + paddle.getWidth()/2;
		float paddleRight = paddleLeft + paddle.getWidth();
		
		this.setVelocityX(2*(ballPositionX-centerPaddle));
		if (paddle.Player_Num == 1) {
			this.setVelocityY(-1*Math.abs(this.getVelocityY()));
		} else {
			this.setVelocityY(Math.abs(this.getVelocityY()));
		}
	}
	
}
