package objects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ItemObject extends AnimateObject {
	private int type = 0;
	private boolean isVisible  =false;
	public ItemObject(Resources res, int imgResource, float x, float y, int t) {
		super(res, imgResource, x, y);
		type = t;
	}
	
	public ItemObject(Resources res, int imgResource[], float x, float y, int t) {
		super(res, imgResource, x, y);
		type = t;
	}
	
	public void setType(int t) {
		type = t;
	}
	
	public int getType() {
		return type;
	}
	
	public void setVisible(boolean v) {
		isVisible = v;
	}
	
	public boolean getVisible() {
		return isVisible;
	}
}
