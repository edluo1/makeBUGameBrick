package no.ntnu.brickbreaker.models;

import org.anddev.andengine.entity.primitive.Rectangle;

/**
 * 	
 * @author kristoffer
 * To register paddle use: scene.registerTouchArea(a paddle);
 */
public class Paddle extends Rectangle{
	
	public int Player_Num;
	public Paddle(float pX, float pY, float pWidth, float pHeight, int Player) {
		super(pX, pY, pWidth, pHeight);
		this.Player_Num = Player;
		// TODO Auto-generated constructor stub
	}


}
