package dev.zmq.duckseatinsect;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

public class SoundPlayer
{
    private static SoundPool soundPool;
    private static int hitSound;
    private static int overSound;
    private AudioAttributes audioAttributes;
    final  int SOUND_POOL_MAX=2;

    public  SoundPlayer(Context context)
    {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
        {
            audioAttributes=new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build();

            soundPool=new SoundPool.Builder().setAudioAttributes(audioAttributes)
                    .setMaxStreams(SOUND_POOL_MAX).build();
        }
        else
        {
            //SoundPool (int maxStram,int stramType,int srcQuality)
            soundPool=new SoundPool(SOUND_POOL_MAX, AudioManager.STREAM_MUSIC,0);


        }

        hitSound=soundPool.load(context,R.raw.eatting,1);
        overSound=soundPool.load(context,R.raw.killed,1);
    }
    public void playHitSound()
    {
    //	play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
        soundPool.play(hitSound,1.0f,1.0f,1,0,1.0f);
    }
    public void playOverSound()
    {
        //	play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
        soundPool.play(overSound,1.0f,1.0f,1,0,1.0f);
    }
}
