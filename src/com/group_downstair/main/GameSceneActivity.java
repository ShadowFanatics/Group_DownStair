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
	private Timer gameTimer;
	private AnimateObject player;
	private ArrayList<AnimateObject> stairs = new ArrayList<AnimateObject>();
	private int screenWidth;
	private int screenHeight;
	private Resources res;
	private float lastStairY = 100;
	private final float lengthBetweenStair = 100;
	private boolean playerFall = true;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 用來取得螢幕大小
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		//preferSize = displayMetrics.widthPixels / sudokuSize;
		screenWidth = displayMetrics.widthPixels;
		screenHeight = displayMetrics.heightPixels;
		myPanel = new Panel(this);
		setContentView(myPanel);
		
		gameTimer = new Timer();
		gameTimer.schedule(timer_task, 0, 20);
	}
	
	public void run() {
		if ( playerFall ) {
			player.move(0, 5);
		}
		float max = 0;
		int outScreenIndex = -1;
		for (int i = 0; i < stairs.size(); i++) {
			stairs.get(i).move(0, -1);
			if ( Physical.isCollide(stairs.get(i),player) ) {
				player.setLocation(player.getX(), stairs.get(i).getY()-player.getHeight());
			}
			float tempY = stairs.get(i).getY();
			if ( tempY > max ) {
				max = tempY;
			}
			if( stairs.get(i).getY() < -20 ) {
				outScreenIndex = i;
			}
        }
		if ( outScreenIndex != -1 ) {
			lastStairY = max + lengthBetweenStair;
			stairs.get(outScreenIndex).setLocation(getRandomInt(0, screenWidth - 100), lastStairY);
		}
	}
	
	class Panel extends View {
		public Panel(Context context) {
			super(context);
			res = getResources();
			player = new AnimateObject(res, Image.player, screenWidth/2, 0);
			for ( int i = 0 ; i < 10 ; i++ ) {
				lastStairY += lengthBetweenStair;
				stairs.add(new AnimateObject(res, Image.stair, getRandomInt(0, screenWidth - 100), lastStairY));
			}
		}

		public void onDraw(Canvas canvas) {
			canvas.drawColor(Color.WHITE);
			canvas.drawBitmap(player.getImg(), player.getMatrix(), null);
			for (int i = 0; i < stairs.size(); i++) {
				canvas.drawBitmap(stairs.get(i).getImg(), stairs.get(i).getMatrix(), null);
	        }
			//canvas.drawBitmap(test.getImg(), test.getMatrix(), null);
		}

	}
	
	public boolean onKeyDown(int keyCode,KeyEvent event){
	      if(keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0){   //確定按下退出鍵and防止重複按下退出鍵
	    	  finish();
	      }
	      return false;
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private TimerTask timer_task = new TimerTask() {
		public void run() {
			Message msg = new Message();
			msg.what = 1;
			timer_handler.sendMessage(msg);
		}
	};

	private Handler timer_handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			run();
			myPanel.invalidate();
		}
	};
	
	private int getRandomInt(int min, int max) {
		Random r = new Random();
		return r.nextInt(max - min + 1) + min;
	}
}
