package com.group_downstair.main;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import engine.Physical;

import objects.*;
import resource.Image;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class GameSceneActivity extends Activity {
	private View myPanel;
	private AnimateObject player;
	private ArrayList<StairObject> stairs = new ArrayList<StairObject>();
	private int screenWidth;
	private int screenHeight;
	private Resources res;
	private float lastStairY = 100;
	private int lastFloor = 0;
	private final float lengthBetweenStair = 150;
	private Thread paintThread;
	private final long fps = 60;
	private boolean gameOver = false;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 用來取得螢幕大小
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

		screenWidth = displayMetrics.widthPixels;
		screenHeight = displayMetrics.heightPixels;
		myPanel = new Panel(this);
		setContentView(myPanel);

	}

	public void gameRun() {

		Physical.runObjects2();
		
		if (player.getY() > screenHeight ) {
			//gameOver = true;
			player.setLocation(player.getX(), 0);
		}
		if (player.getX() < 0 ) {
			player.setSpeedX((float) (-player.getSpeedX()));
		}
		if (player.getX() > screenWidth - player.getWidth() ) {
			player.setSpeedX((float) (-player.getSpeedX()));
		}
		
		float max = 0;
		int index = -1;
		for (int i = 0; i < stairs.size(); i++) {
			if (stairs.get(i).getButtom() > max) {
				max = stairs.get(i).getY();
			}
			if (stairs.get(i).getY() < -20) {
				index = i;
			}
		}
		if (index != -1) {
			lastStairY = max + lengthBetweenStair;
			stairs.get(index).setLocation(getRandomInt(0, screenWidth - 100),
					lastStairY);
			//stairs.get(index).setDegree(getRandomInt(-45, 45));
			stairs.get(index).setFloor(lastFloor);
			lastFloor++;
		}
	}

	class Panel extends SurfaceView implements SurfaceHolder.Callback, Runnable {
		private SurfaceHolder surfaceHolder;

		public Panel(Context context) {
			super(context);
			res = getResources();
			surfaceHolder = this.getHolder();
			surfaceHolder.addCallback(this);
			paintThread = new Thread(this);

			player = new AnimateObject(res, Image.player, screenWidth / 2, 0);
			player.setGravity(true);
			Physical.addObject(player);
			for (int i = 0; i < 10; i++) {
				lastStairY += lengthBetweenStair;
				StairObject temp = new StairObject(res, Image.stair, getRandomInt(0,
						screenWidth - 100), lastStairY, lastFloor);
				temp.addSpeedY(-1);
				stairs.add(temp);
				Physical.addObject(temp);
				lastFloor++;
			}
		}

		public void draw() {
			Canvas canvas = null;
			try {
				canvas = surfaceHolder.lockCanvas(null);
				synchronized (surfaceHolder) {
					canvas.drawColor(Color.BLUE);
					canvas.drawBitmap(player.getImg(), player.getMatrix(), null);
					for (int i = 0; i < stairs.size(); i++) {
						canvas.drawBitmap(stairs.get(i).getImg(), stairs.get(i)
								.getMatrix(), null);
					}
				}
			} finally {
				if (canvas != null) {
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}

		}

		public void run() {
			while (!gameOver) {
				gameRun();
				draw();
				try {
					Thread.sleep(17);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					Log.e("thread", e.toString());
				}
			}
		}

		public void surfaceCreated(SurfaceHolder holder) {
			paintThread.start();
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { // 確定按下退出鍵and防止重複按下退出鍵
			finish();
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) { // 確定按下退出鍵and防止重複按下退出鍵
			player.setSpeedX(-5);
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) { // 確定按下退出鍵and防止重複按下退出鍵
			player.setSpeedX(5);
		}
		return false;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private int getRandomInt(int min, int max) {
		Random r = new Random();
		return r.nextInt(max - min + 1) + min;
	}
}
