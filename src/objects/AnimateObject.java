package objects;

import java.util.ArrayList;

import resource.Image;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

public class AnimateObject {
	private int ImgIndex = 0;
	private int length = 0;
	private Bitmap img[];
	private boolean canAnimate = false;
	private Matrix matrix = new Matrix();
	private int width;
	private int height;
	
	public AnimateObject(Resources res, int imgResource, float x, float y) {
		img = new Bitmap[1];
		img[0] = BitmapFactory.decodeResource(res,
				imgResource);
		width = img[0].getWidth();
		height = img[0].getHeight();
		setLocation(x, y);
		canAnimate = false;
	}
	
	public AnimateObject(Resources res, int imgResource[], float x, float y) {
		length = imgResource.length;
		img = new Bitmap[length];
		ImgIndex = 0;
		for (int i = 0; i < length; i++) {
			img[i] = BitmapFactory.decodeResource(res, imgResource[i]);
		}
		width = img[0].getWidth();
		height = img[0].getHeight();
		setLocation(x, y);
		canAnimate = true;
	}
	
	public void animate() {
		if ( canAnimate ) {
			if ( ImgIndex < length - 1) {
				ImgIndex++;
			}
			else {
				ImgIndex = 0;
			}
		}
	}
	
	public void setLocation(float x, float y) {
		matrix.setTranslate(x, y);
	}

	public void move(float dx, float dy) {
		float values[] = new float[9];
		matrix.getValues(values);
		matrix.setTranslate(values[2] + dx, values[5] + dy);
	}

	public Bitmap getImg() {
		return img[ImgIndex];
	}

	public Matrix getMatrix() {
		return matrix;
	}
	
	public float getX() {
		float values[] = new float[9];
		matrix.getValues(values);
		return values[2];
	}
	
	public float getY() {
		float values[] = new float[9];
		matrix.getValues(values);
		return values[5];
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}
