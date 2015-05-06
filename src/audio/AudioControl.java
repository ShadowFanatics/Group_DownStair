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
	private MediaPlayer sound1Player;
	private MediaPlayer sound2Player;
	
	//因為MediaPlayer.create(...)也會作prepare
	//用boolean判斷,避免一開始prepare完畢就播放
	private boolean sound1Flag = false;
	private boolean sound2Flag = false;
	
	public AudioControl(Context context)
	{
		this.context = context;
		
		initializeMediaPlayers();
	}
	
	private void initializeMediaPlayers()
	{
		//bgmPlayer
		bgmPlayer = MediaPlayer.create(context, R.raw.final_match);
		bgmPlayer.setLooping(true);
		bgmPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
		{
			@Override
			public void onPrepared(MediaPlayer mp)	//當音訊準備好(可被播放)時執行
			{
				bgmPlayer.start();
				System.out.println("bgmPlayer onPrepared start");
			}
		});
		
		//sound1Player
		sound1Player = MediaPlayer.create(context, R.raw.heal);
		sound1Player.setLooping(false);
		sound1Player.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
		{
			@Override
			public void onPrepared(MediaPlayer mp)
			{
				if(sound1Flag)
				{
					sound1Player.start();
					System.out.println("play Sound1");
				}
			}
		});
		
		//sound2Player
		sound2Player = MediaPlayer.create(context, R.raw.jump);
		sound2Player.setLooping(false);
		sound2Player.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
		{
			@Override
			public void onPrepared(MediaPlayer mp)
			{
				if(sound2Flag)
				{
					sound2Player.start();
					System.out.println("play Sound2");
				}
			}
		});
	}
	
	public void playBGM()
	{
		if(!bgmPlayer.isPlaying())
		{
			bgmPlayer.start();
			System.out.println("play BGM");
		}
	}
	
	public void pauseBGM()
	{
		if(bgmPlayer.isPlaying())
		{
			bgmPlayer.pause();
			System.out.println("pause BGM");
		}
	}
	
	public void playSound1()
	{
		if(sound1Flag)
		{
			if(!sound1Player.isPlaying())
			{
				sound1Player.start();
				System.out.println("play Sound1");
			}
		}
		else
		{
			sound1Player.start();
			sound1Flag = true;
			System.out.println("play Sound1");
		}
	}
	
	public void playSound2()
	{
		if(sound2Flag)
		{
			if(!sound2Player.isPlaying())
			{
				sound2Player.start();
				System.out.println("play Sound2");
			}
		}
		else
		{
			sound2Player.start();
			sound2Flag = true;
			System.out.println("play Sound2");
		}
	}
	
	public void releaseAll()
	{
		if(bgmPlayer != null)
		{
			bgmPlayer.release();
			bgmPlayer = null;
			System.out.println("release BGM");
		}
		
		if(sound1Player != null)
		{
			sound1Player.release();
			sound1Player = null;
			System.out.println("release Sound1");
		}
		
		if(sound2Player != null)
		{
			sound2Player.release();
			sound2Player = null;
			System.out.println("release Sound2");
		}
	}
}
