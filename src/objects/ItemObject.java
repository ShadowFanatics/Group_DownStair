package objects;

import android.content.res.Resources;

public class ItemObject extends AnimateObject {
	private int type = 0;
	public ItemObject(Resources res, int imgResource, float x, float y, int t) {
		super(res, imgResource, x, y);
		type = t;
	}
	
	public void setType(int t) {
		type = t;
	}
	
	public int getType() {
		return type;
	}
}
