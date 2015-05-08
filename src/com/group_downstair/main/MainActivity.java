package com.group_downstair.main;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
	
	private static final int GAME_REQUEST = 0;	//need >= 0
	private static final int RANK_REQUEST = 1;
	private static int drawble_title = R.drawable.drawble_title;
	private Button startButton;
	private Button continueButton;
	private Button rankButton;
	private Button exitButton;
	private Button infoButton;
	private LinearLayout layout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initializeViews();
	}
	
	
	private void initializeViews()
	{
		startButton = (Button) findViewById(R.id.button_start);
		startButton.setOnClickListener(startButtonListener);
		continueButton = (Button) findViewById(R.id.button_continue);
		continueButton.setOnClickListener(continueButtonListener);
		//continueButton.setEnabled(false);
		rankButton = (Button) findViewById(R.id.button_rank);
		rankButton.setOnClickListener(rankButtonListener);
		exitButton = (Button) findViewById(R.id.button_exit);
		exitButton.setOnClickListener(exitButtonListener);
		infoButton = (Button) findViewById(R.id.button_Info);
		infoButton.setOnClickListener(infoButtonListener);
		
		
		layout = (LinearLayout)findViewById(R.id.MainActivity_layout);
		//背景圖片
		layout.setBackground(this.getResources().getDrawable(drawble_title));
		
	}
	
	private Button.OnClickListener startButtonListener = new Button.OnClickListener()
	{
		
		public void onClick(View v)
		{
			Bundle bundle = new Bundle();
			bundle.putBoolean("isStart", true);
			
			Intent intent = new Intent();
			intent.putExtras(bundle);
			intent.setClass(MainActivity.this, GameSceneActivity.class);
			startActivity(intent);
		}
	};
	
	private Button.OnClickListener continueButtonListener = new Button.OnClickListener()
	{

		public void onClick(View v)
		{
			Bundle bundle = new Bundle();
			bundle.putBoolean("isStart", false);
			
			Intent intent = new Intent();
			intent.putExtras(bundle);
			intent.setClass(MainActivity.this, GameSceneActivity.class);
			startActivity(intent);
		}
	};
	
	private Button.OnClickListener rankButtonListener = new Button.OnClickListener()
	{

		public void onClick(View v)
		{	
			Bundle bundle = new Bundle();
			bundle.putBoolean("save", false);
			Intent intent = new Intent();
			intent.putExtras(bundle);
			intent.setClass(MainActivity.this, ranking.Ranking.class);
			startActivityForResult(intent, RANK_REQUEST);
			MainActivity.this.finish();
			
		}
	};
	
	private Button.OnClickListener infoButtonListener = new Button.OnClickListener()
	{

		public void onClick(View v)
		{	
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, InfoActivity.class);
			startActivity(intent);
			
		}
	};
	
	private Button.OnClickListener exitButtonListener = new Button.OnClickListener()
	{

		public void onClick(View v)
		{
			MainActivity.this.finish();
		}
	};
	
	
	
}
