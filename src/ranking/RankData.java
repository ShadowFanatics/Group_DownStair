package ranking;

import android.graphics.drawable.Drawable;

public class RankData {
	private String name;
	private String tsec;
	private String date;
	//private String time_string;
	private Drawable icon1;
	private Drawable icon2;
	
	public String getName() {
		return name;
	}
	
	public String getTSec() {
		return tsec;
	}
	
	public String getDate() {
		return date;
	}
	
	public Drawable getIcon1(){
		return icon1;
	}
	
    public void setIcon1(Drawable icon) {
        this.icon1 = icon;
    }
    
	public Drawable getIcon2(){
		return icon2;
	}
	
    public void setIcon2(Drawable icon) {
        this.icon2 = icon;
    }
	
	public RankData(String tsec,String date, String name) {
		this.tsec = tsec;
		this.date = date;
		this.name = name;
	}
}
