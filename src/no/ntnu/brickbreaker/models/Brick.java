package no.ntnu.brickbreaker.models;

import org.anddev.andengine.entity.primitive.Rectangle;

public class Brick extends Rectangle{
	
	public int Player_Num = 0;

	public Brick(float pX, float pY, float pWidth, float pHeight, int Player) {
		super(pX, pY, pWidth, pHeight);
		Player_Num = Player;
		if (Player_Num == 1) {
			this.setColor(1.0f, 0.0f, 0.0f);
		} else {
			this.setColor(0.0f, 0.0f, 1.0f);
		}
			
		
		// TODO Auto-generated constructor stub
	}

	protected void onManagedUpdate(final float pSecondsElapsed) {
//			if(this.collidesWith(pOtherShape))

	}

}
