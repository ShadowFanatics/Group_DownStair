package objects;

import java.util.ArrayList;

import resource.Image;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

public class AnimateObject {
	protected int ImgIndex = 0;
	protected int length = 0;
	protected Bitmap img[];
	protected boolean canAnimate = false;
	protected Matrix matrix = new Matrix();
	protected int width;
	protected int height;
	protected float degree;
	protected float preStatus[] = new float[9];
	protected float speedX = 0;
	protected float speedY = 0;
	protected boolean isGravity = false;
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
		matrix.reset();
		matrix.preTranslate(x, y);
		matrix.preRotate(degree);
		matrix.getValues(preStatus);
	}
 
	public void move(float dx, float dy) {
		matrix.getValues(preStatus);
		matrix.reset();
		matrix.preTranslate(preStatus[2] + dx, preStatus[5] + dy);
		matrix.preRotate(degree);
	}
	
	public void backToPreStatus() {
		matrix.setValues(preStatus);
	}
	
	public void setDegree(float d) {
		degree = d;
		matrix.preRotate(degree);
	}
	
	public float getDegree() {
		return degree;
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
	
	public float getSpeedX() {
		return speedX;
	}
	
	public void addSpeedX(float x) {
		speedX += x;
	}
	
	public void setSpeedX(float x) {
		speedX = x;
	}
	
	public float getSpeedY() {
		return speedY;
	}
	
	public void addSpeedY(float y) {
		speedY += y;
	}
	
	public void setSpeedY(float y) {
		speedY = y;
	}
	
	public void setGravity(boolean g) {
		isGravity = g;
	}
	
	public boolean isGravity() {
		return isGravity;
	}
	
	public float getCos() {
		float values[] = new float[9];
		matrix.getValues(values);
		return values[0];
	}
	
	public float getSin() {
		float values[] = new float[9];
		matrix.getValues(values);
		return values[3];
	}
}
