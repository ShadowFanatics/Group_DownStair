package com.group_downstair.main;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;


public class InfoActivity  extends Activity {
	private LinearLayout layout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info);
		initializeViews();
	}
	private void initializeViews()
	{
		
		
		layout = (LinearLayout)findViewById(R.id.infoActivity_layout);
		//背景圖片
		layout.setBackground(this.getResources().getDrawable(R.drawable.drawble_title));
		
	}
}
