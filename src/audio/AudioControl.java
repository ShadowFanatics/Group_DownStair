package audio;

import com.group_downstair.main.R;

import android.content.Context;
import android.media.MediaPlayer;

public class AudioControl
{
	/**
	 * 相關檔案固定放在res/raw => R.raw.xxx
	 */
	
	private Context context;
	private MediaPlayer bgmPlayer;
	private MediaPlayer starPlayer;
	private MediaPlayer sound1Player;
	private MediaPlayer sound2Player;
	private MediaPlayer sound3Player;
	
	public AudioControl(Context context)
	{
		this.context = context;
		
		initializeMediaPlayers();
	}
	
	private void initializeMediaPlayers()
	{
		//bgmPlayer
		bgmPlayer = MediaPlayer.create(context, R.raw.bubble_bobble);
		bgmPlayer.setLooping(true);
		bgmPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
		{

			public void onPrepared(MediaPlayer mp)	//當音訊準備好(可被播放)時執行
			{
				bgmPlayer.start();
				System.out.println("bgmPlayer onPrepared start");
			}
		});
		
		//starPlayer
		starPlayer = MediaPlayer.create(context, R.raw.mario_star);
		starPlayer.setLooping(true);
		
		//sound1Player
		sound1Player = MediaPlayer.create(context, R.raw.heal);
		sound1Player.setLooping(false);
		
		//sound2Player
		sound2Player = MediaPlayer.create(context, R.raw.jump);
		sound2Player.setLooping(false);
		
		//sound3Player
		sound3Player = MediaPlayer.create(context, R.raw.hurt);
		sound3Player.setLooping(false);
	}
	
	public void playBGM()
	{
		if(!bgmPlayer.isPlaying())
		{
			bgmPlayer.start();
//			System.out.println("play BGM");
		}
	}
	
	public void pauseBGM()
	{
		if(bgmPlayer.isPlaying())
		{
			bgmPlayer.pause();
//			System.out.println("pause BGM");
		}
	}
	
	public void changeBGM()
	{
		if(bgmPlayer.isPlaying())
		{
			bgmPlayer.pause();
			starPlayer.start();
		}
		else if(starPlayer.isPlaying())
		{
			starPlayer.pause();
			bgmPlayer.start();
		}
	}
	
	public void playSound1()
	{
		if(!sound1Player.isPlaying())
		{
			sound1Player.start();
//			System.out.println("play Sound1");
		}
	}
	
	public void playSound2()
	{
		if(!sound2Player.isPlaying())
		{
			sound2Player.start();
//				System.out.println("play Sound2");
		}
	}
	
	public void playSound3()
	{
		if(!sound3Player.isPlaying())
		{
			sound3Player.start();
//			System.out.println("play Sound3");
		}
	}
	
	public void releaseAll()
	{
		if(bgmPlayer != null)
		{
			bgmPlayer.release();
			bgmPlayer = null;
//			System.out.println("release BGM");
		}
		
		if(starPlayer != null)
		{
			starPlayer.release();
			starPlayer = null;
//			System.out.println("release star");
		}
		
		if(sound1Player != null)
		{
			sound1Player.release();
			sound1Player = null;
//			System.out.println("release Sound1");
		}
		
		if(sound2Player != null)
		{
			sound2Player.release();
			sound2Player = null;
//			System.out.println("release Sound2");
		}
		
		if(sound3Player != null)
		{
			sound3Player.release();
			sound3Player = null;
//			System.out.println("release Sound2");
		}
	}
}
