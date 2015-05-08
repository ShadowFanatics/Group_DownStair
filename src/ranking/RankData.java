package ranking;

import android.graphics.drawable.Drawable;

public class RankData {
	public long id = 0;
	private String name;
	private String score;
	private String date;
	//private String time_string;
	private Drawable icon1;
	private Drawable icon2;
	
	public String getName() {
		return name;
	}
	
	public String getScore() {
		return score;
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
	
	public RankData(long id, String score,String date, String name) {
		this.id = id;
		this.score = score;
		this.date = date;
		this.name = name;
	}
}
