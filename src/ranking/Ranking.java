package ranking;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import sqlite.postDataDAO;

import com.group_downstair.main.MainActivity;
import com.group_downstair.main.R;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Ranking extends Activity{
		private static final int GAME_REQUEST = 0;	//need >= 0
		private static final int RANK_REQUEST = 1;
		private static final int TITLE_REQUEST = 2;
	 	private ListView lv;  
	    private List<RankData> mListlist;
		private static int images[] = {R.drawable.record_number_0,
			R.drawable.record_number_1, R.drawable.record_number_2, R.drawable.record_number_3,
			R.drawable.record_number_4, R.drawable.record_number_5, R.drawable.record_number_6,
			R.drawable.record_number_7, R.drawable.record_number_8, R.drawable.record_number_9,
			R.drawable.crown};
		private static int cup[] = {R.drawable.gold, R.drawable.silver, R.drawable.bronze};
		private int display_width, display_height;
		private long index = 0;
		private static final String TAG = "Ranking_activity";
	    /** Called when the activity is first created. */  
	    @Override  
	    public void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState);  
	        setContentView(R.layout.activity_rank);
	        
	        initData();
	        addData();
	        //list sort
	        Collections.sort(mListlist,new Comparator<RankData>() {
	        	

	        	public int compare(RankData lhs, RankData rhs) {
					// TODO Auto-generated method stub
					//Date date1 = stringToDate(lhs.getTSec());
					//Date date2 = stringToDate(rhs.getTSec());
					int n1 = Integer.parseInt(lhs.getScore());
					int n2 = Integer.parseInt(rhs.getScore());
					//由大排到小
					if(n1 < n2){
						return 1;
					}
					return -1;
				}
			});
	        //setIcon
	        Iterator<RankData> it = mListlist.listIterator();
	        int i = 1;//這變數要重寫
	        int j = 0;
	        while (it.hasNext()) {
	        	RankData rankData = it.next(); 
	        	if(j == 0 && i <= 3){
	        		rankData.setIcon1(getResources().getDrawable(cup[i-1]));
	        		rankData.setIcon2(getResources().getDrawable(images[i]));
	        	}
	        	else {
	        		rankData.setIcon1(getResources().getDrawable(images[j]));
	        		rankData.setIcon2(getResources().getDrawable(images[i]));
				}
				i++;
				if(i >= 10){
					j++;
					i = i / 10;
				}
			}
				
	        //setAdapter
	        lv.setAdapter(new MyAdapter(this, mListlist));
	        
	        lv.setOnItemClickListener(new OnItemClickListener(){


	        	public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					Toast.makeText(Ranking.this, "輕觸一下表單 回到主畫面", Toast.LENGTH_SHORT).show();
					
					Intent intent = new Intent();
					intent.setClass(Ranking.this, MainActivity.class);
					startActivityForResult(intent, TITLE_REQUEST);
					Ranking.this.finish();
									
				}
	        	
	        });
	        
	        this.setContentView(lv);
	          
	    }
	    
	    private void initData() {
	        lv = new ListView(this); 
	        mListlist = new ArrayList<RankData>();

			/*mListlist.add(new RankData("30","2015-05-07 00:30", "硬硬"));
			mListlist.add(new RankData("25","2015-05-07 01:25","邦弟"));
			mListlist.add(new RankData("37","2015-05-07 02:37","岳霖"));
			mListlist.add(new RankData("7","2015-05-07 03:67","熊貓"));
			mListlist.add(new RankData("11","2015-05-07 04:18","魚蛋"));*/
			readState();

			DisplayMetrics displayMetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
			display_width = displayMetrics.widthPixels;
			display_height = displayMetrics.heightPixels;
			//Toast.makeText(Ranking.this, display_width + ":width" + display_height + "height", Toast.LENGTH_LONG).show();;
		}
	    
	    private void addData() {
			Bundle bundle = getIntent().getExtras();
			if ( bundle.getBoolean("save")) {
				String score = bundle.getString("score");
				String date = bundle.getString("time");
				String name = bundle.getString("name");
				RankData newData = new RankData(index,score, date, name);
				mListlist.add(newData);
				postDataDAO localData = new postDataDAO(getApplicationContext());
				localData.insert(newData);
				localData.close();
				index++;
			}
		}
	    
		private void readState() {
			postDataDAO localData = new postDataDAO(getApplicationContext());
			List<RankData> messages = localData.getAll();
			index = messages.size();
			for (int i = 0; i < messages.size(); i++) {
				Log.e("!!",String.valueOf(i));
				long id =  messages.get(i).id;
				String name =  messages.get(i).getName();
				String date =  messages.get(i).getDate();
				String score =  messages.get(i).getScore();
				RankData item = new RankData(id,score, date, name);
				mListlist.add(item);
			}
			localData.close();
		}
	    
	    /*
	    public static Date stringToDate(String rankString) {
			ParsePosition position = new ParsePosition(0);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date dateValue = simpleDateFormat.parse(rankString, position);
	    	return dateValue;
		}*/	
	    public class MyAdapter extends BaseAdapter {  
	    	  
	        private Context mContext;  
	        private List<RankData> mList;  
	      
	        public MyAdapter(Context context, List<RankData> list) {  
	            this.mContext = context;  
	            this.mList = list;  
	        }  
	      
	        public int getCount() {  
	            return mList != null ? mList.size() : 0;  
	        }  
	      
	        public Object getItem(int position) {  
	            return mList.get(position);  
	        }  
	      
	        public long getItemId(int position) {  
	            return position;  
	        }  
	          
	        private class ViewHolder {  
	            private TextView textView1;  
	            private TextView textVeiw2;
	            private ImageView imageview1;
	            private ImageView imageview2;
	            private TextView timerView;
	            //private Button button;
	        }

			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
	            ViewHolder holder = null;  
	            if (convertView == null) {
	            	
	            	LayoutInflater li = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            	convertView = li.inflate(R.layout.activity_rank, parent, false);
	            	//convertView = (LinearLayout)LayoutInflater.from(mContext).inflate(R.layout.activity_rank, null); 
	                holder = new ViewHolder();  
	                holder.textView1 = (TextView) convertView.findViewById(R.id.smalltv);  
	                holder.textVeiw2 = (TextView) convertView.findViewById(R.id.bigtv);
	                holder.timerView = (TextView) convertView.findViewById(R.id.timertv);
	                holder.imageview1 = (ImageView)convertView.findViewById(R.id.iv_1);
	                holder.imageview2 = (ImageView)convertView.findViewById(R.id.iv_2);
	               // holder.button = (Button)convertView.findViewById(R.id.bn);
	                
	                convertView.setTag(holder);
	            } else {  
	                holder = (ViewHolder) convertView.getTag();  
	            }  
	      
	            holder.imageview1.setImageDrawable(mList.get(position).getIcon1());
	            holder.imageview2.setImageDrawable(mList.get(position).getIcon2());
	            holder.textView1.setText(mList.get(position).getDate());  
	            holder.textVeiw2.setText(mList.get(position).getName());
	            holder.timerView.setText(mList.get(position).getScore() + "層");
	            
	            holder.imageview1.getLayoutParams().height = display_height / 9;
	            holder.imageview1.getLayoutParams().width = display_width / 9;
	            holder.imageview2.getLayoutParams().height = display_height / 9;
	            holder.imageview2.getLayoutParams().width = display_width / 9;
	            /*holder.button.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
		                Toast.makeText(Ranking.this, "當前選中列表項為:sdfsdf", 
		                        Toast.LENGTH_SHORT).show();
		                Ranking.this.finish();
					}
				});*/
	            return convertView;  
			}
			
			
		    /*private Bitmap scaleBitmap(Bitmap bitmap){
		    		
		    	int width = bitmap.getWidth();
		    	int height = bitmap.getHeight();
		    	int new_width = display_width / 4 - 30;
		    	int new_height = display_height / 5;
		    	float scaleWidth = ((float) new_width ) / width;
		    	float scaleHeight = ((float) new_height ) / height;
		    	Matrix matrix = new Matrix();
		    	matrix.postScale(scaleWidth, scaleHeight);
		    	Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
		    	return newbmp;
		    };
		    
		    private Bitmap drawableToBitmap(Drawable drawable) {   
		        int w = drawable.getIntrinsicWidth();  
		        int h = drawable.getIntrinsicHeight();  
		   
		        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888  
		                : Bitmap.Config.RGB_565;  
		        Bitmap bitmap = Bitmap.createBitmap(w, h, config);  
		        Canvas canvas = new Canvas(bitmap);  
		        drawable.setBounds(0, 0, w, h);   
		        drawable.draw(canvas);
		        
		        return bitmap;  
		    */
	    }

		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if(keyCode == KeyEvent.KEYCODE_BACK){
				Intent intent = new Intent();
				intent.setClass(Ranking.this, MainActivity.class);
				startActivityForResult(intent, TITLE_REQUEST);
				Ranking.this.finish();
			}
			return super.onKeyDown(keyCode, event);
		}


}
