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
	float velocityX;
	float velocityY;
	int i =0;
	private Engine mEngine;
	
	public Ball(float positionX, float positionY, TiledTextureRegion positionTextureRegion, Engine mEngine) {
		super(positionX, positionY, positionTextureRegion);
		this.mEngine = mEngine;
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
		float centerVertical = brick.getX();
		float westWall = centerVertical - brick.getWidth()/2-this.getWidth();
		float eastWall = centerVertical + brick.getWidth()/2+this.getWidth();
		float centerHorizontal = brick.getY();
		float northWall = centerHorizontal - brick.getHeight()/2;
		float southWall = centerHorizontal + brick.getHeight()/2;
		
		if (ballPositionY >= southWall || ballPositionY <= northWall) { // hit brick from vertical
			this.setVelocityY(-1*this.getVelocityY());
		}
		
		if (ballPositionX <= westWall || ballPositionX >= eastWall) { // hit brick moving horizontal
			this.setVelocityX(-1*this.getVelocityX());
		} 

	}

	public void bounceWithPaddle(Paddle paddle) {
		float ballPositionX = this.getX();
		float centerPaddle = paddle.getX();
		float paddleLeft = centerPaddle - paddle.getWidth()/2;
		float paddleRight = centerPaddle + paddle.getWidth()/2;
		
		this.setVelocityX(ballPositionX-centerPaddle);
		
		this.setVelocityY(-1*Math.abs(this.getVelocityY()));
		
	}
	
}
