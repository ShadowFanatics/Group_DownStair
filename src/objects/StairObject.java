package objects;

import android.content.res.Resources;

public class StairObject extends AnimateObject {
	private int floor = 0;
	public StairObject(Resources res, int imgResource, float x, float y, int f) {
		super(res, imgResource, x, y);
		floor = f;
	}
	
	public void setFloor(int f) {
		floor = f;
	}
	
	public int getFloor() {
		return floor;
	}
	
	public float getButtom() {
		if ( degree > 0 ) {
			return getY() + getWidth()*getSin();
		}
		return getY();
	}
}
