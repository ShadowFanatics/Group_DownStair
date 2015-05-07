package com.group_downstair.main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import data.GameData;
import engine.Physical;
import objects.*;
import resource.Image;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.SensorManager;
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
import audio.AudioControl;

public class GameSceneActivity extends Activity {
	private View myPanel = null;
	private AnimateObject player;
	private ArrayList<AnimateObject> snakes = new ArrayList<AnimateObject>();
	private ArrayList<StairObject> stairs = new ArrayList<StairObject>();
	private ArrayList<ItemObject> items = new ArrayList<ItemObject>();
	private int screenWidth;
	private int screenHeight;
	private Resources res;
	private Thread paintThread;
	private final long fps = 60;
	private int frameCount = 0;
	private ArrayList<float[]> pastPlayerLocation = new ArrayList<float[]>();
	private final int snakeDelayFrame = 5;
	private boolean isPause = false;
	private MySensor mySensor;
	public static final String PREF = "DOWNSTAIR_PREF";
	public static final String PREF_LIFE = "DOWNSTAIR_LIFE";
	private GameData game;
	private Physical physical;

	private int timeTotal = 0;
	private final float deadLine = 85;
	private boolean playRedEffect = false;
	private int playerFloor = -1; //use to play sound

	private AudioControl audioControl;
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("created", "created");

		// 用來取得螢幕大小
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		
		screenWidth = displayMetrics.widthPixels;
		screenHeight = displayMetrics.heightPixels;

		game = new GameData(screenWidth);
		
		Bundle bundle = getIntent().getExtras();
		if(!bundle.getBoolean("isStart"))
		{
			restorePrefs();
		}
		
		physical = new Physical();
		myPanel = new Panel(this);
		setContentView(myPanel);

		
		initializeAudio();

		mySensor = new MySensor(getSystemService(SENSOR_SERVICE));
	}
	
	@Override
	protected void onDestroy()
	{
		//釋放資源
		audioControl.releaseAll();
		
		super.onDestroy();
	}
	
	private void initializeAudio()
	{
		//一開始就會播BGM
		audioControl = new AudioControl(GameSceneActivity.this);
	}

	public void gameRun() {
		player.setSpeedX(-mySensor.getForceX()*game.gameSpeed);
		if (player.getY() < 100 ) {
			if ( game.life > 0) {
				snakes.remove(snakes.size()-1);
				game.life--;
				player.setLocation(player.getX(), 150);
			}
			else {
				game.gameOver = true;
			}
			playRedEffect = true;
		}
		if (player.getY() > screenHeight) {
			game.gameOver = true;
		}
		if (player.getX() < 0 && player.getSpeedX() < 0) {
			player.setSpeedX((float) (-player.getSpeedX()));
		}
		if (player.getX() > screenWidth - player.getWidth()
				&& player.getSpeedX() > 0) {
			player.setSpeedX((float) (-player.getSpeedX()));
		}
		// run all pbject physical
		int floor = physical.runObjects2();
		if (floor != -1 && floor > game.userFloor) {
			game.userFloor = floor;
		}
		if (floor == -1) {
			playerFloor = -1;
		}
		else {
			if (floor != playerFloor) {
				//TODO play jump sound here
			}
		}

		// add snake
		float loc[] = new float[9];
		player.getMatrix().getValues(loc);
		pastPlayerLocation.add(loc);
		Matrix temp = new Matrix();
		for (int i = 0; i < snakes.size(); i++) {
			int indexOfLocation = pastPlayerLocation.size() - (i + 1)
					* snakeDelayFrame;
			if (pastPlayerLocation.size() > (i + 1) * snakeDelayFrame) {
				temp.setValues(pastPlayerLocation.get(indexOfLocation));
				snakes.get(i).setMatrix(temp);
				snakes.get(i).move(0, (0 - snakeDelayFrame * (i + 1))*game.gameSpeed);
				if ( snakes.get(i).getY() < deadLine ) {
					snakes.remove(i);
					game.life--;
					playRedEffect = true;
				}
				if (i == snakes.size() - 1) {
					pastPlayerLocation.remove(0);
				}
			}
		}

		frameCount++;
		if (frameCount > 50) {
			frameCount = 0;
			timeTotal++;
		}
		// generate item
		if (timeTotal % 20 == 1 && frameCount == 0) {
			ItemObject tempItem = new ItemObject(res, Image.itemLife,
					getRandomInt(0, screenWidth), screenHeight, 1);
			tempItem.addSpeedY(-game.gameSpeed);
			items.add(tempItem);
		}
		if (timeTotal % 10 == 1 && frameCount == 0) {
			ItemObject tempItem = new ItemObject(res, Image.itemBomb,
					getRandomInt(0, screenWidth), screenHeight, 2);
			tempItem.addSpeedY(-game.gameSpeed);
			items.add(tempItem);
		}
		
		// item move collide eat
		for (int i = 0; i < items.size(); i++) {
			ItemObject tempItem = items.get(i);
			tempItem.move(tempItem.getSpeedX(), tempItem.getSpeedY());
			if (physical.isCollide(tempItem, player)) {
				eatItem(tempItem.getType());
				items.remove(i);
			}
			if (tempItem.getY() < deadLine ) {
				items.remove(i);
			}
		}
		// stair put to bottom.
		float max = 0;
		int index = -1;
		for (int i = 0; i < stairs.size(); i++) {
			if (stairs.get(i).getButtom() > max) {
				max = stairs.get(i).getY();
			}
			if (stairs.get(i).getY() < deadLine) {
				index = i;
			}
		}
		if (index != -1) {
			game.lastStairY = max + game.lengthBetweenStair;
			stairs.get(index).setLocation(getRandomInt(0, screenWidth - 100),
					game.lastStairY);
			// stairs.get(index).setDegree(getRandomInt(-45, 45));
			stairs.get(index).setFloor(game.lastFloor);
			game.lastFloor++;
		}
		
		//add speed while game process
		if (timeTotal % 10 == 1 && frameCount == 0) {
			game.gameSpeed += 0.5;
			physical.addGravity((float) 0.1);
			for (int i = 0; i < stairs.size(); i++) {
				stairs.get(i).setSpeedY(-game.gameSpeed);
				
			}
			for (int i = 0; i < items.size(); i++) {
				items.get(i).setSpeedY(-game.gameSpeed);
			}
		}
	}

	private void eatItem(int type) {
		switch (type) {
		case 1: // life
			snakes.add(new AnimateObject(res, Image.snakeBody, player.getX(),
					player.getY()));
			game.life++;
			break;
		case 2: // bomb
			if ( game.life > 0) {
				snakes.remove(snakes.size()-1);
				game.life--;
			}
			else {
				game.gameOver = true;
			}
			playRedEffect = true;
			break;
		default:
			break;
		}
	}

	class Panel extends SurfaceView implements SurfaceHolder.Callback,
			Runnable, Serializable {
		private SurfaceHolder surfaceHolder;
		private Bitmap background,status;

		public Panel(Context context) {
			super(context);
			this.setId(123);
			res = getResources();
			surfaceHolder = this.getHolder();
			surfaceHolder.addCallback(this);
			background = BitmapFactory
					.decodeResource(res, Image.gameBackground);
			status = BitmapFactory
					.decodeResource(res, Image.statusBack);
		}

		public void addObjects() {
			player = new AnimateObject(res, Image.snakeHead,
					game.playerLocation[0], game.playerLocation[1]);
			player.setGravity(true);
			physical.addObject(player);
			for (int i = 0; i < game.life; i++) {
				snakes.add(new AnimateObject(res, Image.snakeBody,
						game.playerLocation[0], game.playerLocation[1]));
			}
			for (int i = 0; i < 10; i++) {
				StairObject temp = new StairObject(res, Image.stair,
						game.stairLocation[i][0], game.stairLocation[i][1],
						game.stairFloor[i]);
				temp.setSpeedY(-game.gameSpeed);
				stairs.add(temp);
				physical.addObject(temp);
			}
		}

		public void draw() {
			Canvas canvas = null;
			try {
				canvas = surfaceHolder.lockCanvas(null);
				synchronized (surfaceHolder) {
					canvas.drawColor(Color.GREEN);
					canvas.drawBitmap(background, 0, deadLine, null);
					// canvas.drawBitmap(background, new Rect(0,0,540,960), new
					// Rect(0,0,screenWidth,screenHeight), null);
					for (int i = 0; i < snakes.size(); i++) {
						canvas.drawBitmap(snakes.get(i).getImg(), snakes.get(i)
								.getMatrix(), null);
					}
					canvas.drawBitmap(player.getImg(), player.getMatrix(), null);
					for (int i = 0; i < stairs.size(); i++) {
						canvas.drawBitmap(stairs.get(i).getImg(), stairs.get(i)
								.getMatrix(), null);
					}
					for (int i = 0; i < items.size(); i++) {
						canvas.drawBitmap(items.get(i).getImg(), items.get(i)
								.getMatrix(), null);
					}
					canvas.drawBitmap(status, 0, 0, null);
					if ( playRedEffect ) {
						canvas.drawColor(Color.RED);
						playRedEffect = false;
					}
					Paint paint = new Paint();
					paint.setColor(Color.WHITE);
					paint.setTextSize(30);
					canvas.drawText("Floor: " + String.valueOf(game.userFloor), 5, 50,
							paint);
					canvas.drawText("Time: " + String.valueOf(timeTotal) + "s", screenWidth - 150, 50,
							paint);
				}
			} finally {
				if (canvas != null) {
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}

		}

		public void run() {
			while (!isPause && !game.gameOver) {
				gameRun();
				draw();
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					Log.e("thread", e.toString());
				}
			}
			//TODO gameover here
			draw();
			finish();
		}

		public void surfaceCreated(SurfaceHolder holder) {
			addObjects();
			paintThread = new Thread(this);
			paintThread.start();
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
		}
	}

	private void restorePrefs() {
		game.load(getSharedPreferences(PREF, 0));
	}

	@Override
	protected void onPause() {
		super.onPause();
		isPause = true;
		mySensor.onPause();
		game.save(getSharedPreferences(PREF, 0), player, stairs);
		finish();
	}

	private int getRandomInt(int min, int max) {
		Random r = new Random();
		return r.nextInt(max - min + 1) + min;
	}
}
