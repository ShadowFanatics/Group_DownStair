package data;

import java.util.ArrayList;
import java.util.Random;

import objects.AnimateObject;
import objects.StairObject;
import resource.Image;
import engine.Physical;
import android.content.SharedPreferences;
import android.util.Log;

public class GameData {
	public boolean gameOver = false;
	public int life = 3;
	public int lastFloor = 0;
	public int userFloor = 0;
	public int timeTotal = 0;
	public float lengthBetweenStair = 150;
	public float lastStairY = 100;
	public float gameSpeed = (float) 1.5; //not save yet
	
	public float playerLocation[] = new float[2];
	public float stairLocation[][] = new float[10][2];
	public int stairFloor[] = new int[10];
	private final String PREF_STAIR[] = { "stairLocation0", "stairLocation1",
			"stairLocation2", "stairLocation3", "stairLocation4",
			"stairLocation5", "stairLocation6", "stairLocation7", "stairLocation8", "stairLocation9" };

	public GameData(int screenWidth, float density) {
		playerLocation[0] = screenWidth / 2;
		playerLocation[1] = 250*density;
		for (int i = 0; i < 10; i++) {
			lastStairY += lengthBetweenStair;
			stairLocation[i][0] = getRandomInt(0, screenWidth - 100);
			stairLocation[i][1] = lastStairY;
			stairFloor[i] = lastFloor;
			lastFloor++;
		}
	}

	public void save(SharedPreferences settings,AnimateObject player,ArrayList<StairObject> stairs) {
		playerLocation[0] = player.getX();
		playerLocation[1] = player.getY();
		for (int i = 0; i < stairs.size(); i++) {
			stairLocation[i][0]  = stairs.get(i).getX();
			stairLocation[i][1]  = stairs.get(i).getY();
			stairFloor[i] = stairs.get(i).getFloor();
		}
		settings.edit().putBoolean("gameOver", gameOver).putInt("life", life)
				.putInt("lastFloor", lastFloor)
				.putFloat("lengthBetweenStair", lengthBetweenStair)
				.putFloat("lastStairY", lastStairY)
				.putInt("userFloor", userFloor)
				.putInt("timeTotal", timeTotal)
				.putFloat("playerLocationX", playerLocation[0])
				.putFloat("playerLocationY", playerLocation[1])
				.putFloat("gameSpeed", gameSpeed)
				.putFloat(PREF_STAIR[0] + "X", stairLocation[0][0])
				.putFloat(PREF_STAIR[0] + "Y", stairLocation[0][1])
				.putInt(PREF_STAIR[0] + "Floor", stairFloor[0])
				.putFloat(PREF_STAIR[1] + "X", stairLocation[1][0])
				.putFloat(PREF_STAIR[1] + "Y", stairLocation[1][1])
				.putInt(PREF_STAIR[1] + "Floor", stairFloor[1])
				.putFloat(PREF_STAIR[2] + "X", stairLocation[2][0])
				.putFloat(PREF_STAIR[2] + "Y", stairLocation[2][1])
				.putInt(PREF_STAIR[2] + "Floor", stairFloor[2])
				.putFloat(PREF_STAIR[3] + "X", stairLocation[3][0])
				.putFloat(PREF_STAIR[3] + "Y", stairLocation[3][1])
				.putInt(PREF_STAIR[3] + "Floor", stairFloor[3])
				.putFloat(PREF_STAIR[4] + "X", stairLocation[4][0])
				.putFloat(PREF_STAIR[4] + "Y", stairLocation[4][1])
				.putInt(PREF_STAIR[4] + "Floor", stairFloor[4])
				.putFloat(PREF_STAIR[5] + "X", stairLocation[5][0])
				.putFloat(PREF_STAIR[5] + "Y", stairLocation[5][1])
				.putInt(PREF_STAIR[5] + "Floor", stairFloor[5])
				.putFloat(PREF_STAIR[6] + "X", stairLocation[6][0])
				.putFloat(PREF_STAIR[6] + "Y", stairLocation[6][1])
				.putInt(PREF_STAIR[6] + "Floor", stairFloor[6])
				.putFloat(PREF_STAIR[7] + "X", stairLocation[7][0])
				.putFloat(PREF_STAIR[7] + "Y", stairLocation[7][1])
				.putInt(PREF_STAIR[7] + "Floor", stairFloor[7])
				.putFloat(PREF_STAIR[8] + "X", stairLocation[8][0])
				.putFloat(PREF_STAIR[8] + "Y", stairLocation[8][1])
				.putInt(PREF_STAIR[8] + "Floor", stairFloor[8])
				.putFloat(PREF_STAIR[9] + "X", stairLocation[9][0])
				.putFloat(PREF_STAIR[9] + "Y", stairLocation[9][1])
				.putInt(PREF_STAIR[9] + "Floor", stairFloor[9]).commit();
		
	}

	public boolean load(SharedPreferences settings) {
		if ( !settings.getBoolean("gameOver", true) ) {
			gameOver = false;
			life = settings.getInt("life", 3);
			lastFloor = settings.getInt("lastFloor", 0);
			userFloor = settings.getInt("userFloor", 0);
			timeTotal = settings.getInt("timeTotal", 0);
			lengthBetweenStair = settings.getFloat("lengthBetweenStair", 150);
			lastStairY = settings.getFloat("lastStairY", 100);
			gameSpeed = settings.getFloat("gameSpeed", (float) 1.5);
			playerLocation[0] = settings.getFloat("playerLocationX", 0);
			playerLocation[1] = settings.getFloat("playerLocationY", 0);
			for ( int i = 0; i < 10; i++ ) {
				stairLocation[i][0] = settings.getFloat(PREF_STAIR[i] + "X", 0);
				stairLocation[i][1] = settings.getFloat(PREF_STAIR[i] + "Y", 0);
				stairFloor[i] = settings.getInt(PREF_STAIR[i] + "Floor", 0);
			}
		}
		return false;
	}

	private int getRandomInt(int min, int max) {
		Random r = new Random();
		return r.nextInt(max - min + 1) + min;
	}

}
