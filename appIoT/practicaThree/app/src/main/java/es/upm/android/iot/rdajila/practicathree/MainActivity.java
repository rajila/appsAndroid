package es.upm.android.iot.rdajila.practicathree;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.widget.TextView;

import com.google.android.things.contrib.driver.pwmspeaker.Speaker;

import java.io.IOException;

public class MainActivity extends Activity
{
    private static final long PLAYBACK_NOTE_DELAY = 80L;

    private Speaker mSpeaker;
    private HandlerThread mHandlerThread;
    private Handler mHandler;

    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try {
            mSpeaker = new Speaker(Constantes.PINBUZZER);
            mSpeaker.stop(); // in case the PWM pin was enabled already
        } catch (IOException e) {
            Log.e(TAG, "RADC01 Error initializing speaker");
            return; // don't initilize the handler
        }

        mHandlerThread = new HandlerThread("pwm-playback");
        mHandlerThread.start();
        Log.i(TAG,"RADC HILO: "+mHandlerThread.getLooper().toString());
        mHandler = new Handler(mHandlerThread.getLooper());
        mHandler.post(mPlaybackRunnable);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if (mHandler != null) {
            mHandler.removeCallbacks(mPlaybackRunnable);
            mHandlerThread.quitSafely();
        }
        if (mSpeaker != null) {
            try {
                mSpeaker.stop();
                mSpeaker.close();
            } catch (IOException e) {
                Log.e(TAG, "RADC02 Error closing speaker", e);
            } finally {
                mSpeaker = null;
            }
        }
    }

    private Runnable mPlaybackRunnable = new Runnable()
    {

        private int index = 0;

        @Override
        public void run()
        {

            Log.i(TAG,"RADC INDEX: "+index);

            if (mSpeaker == null) {
                Log.i(TAG,"RADC NULL: "+index);
                return;
            }

            try {
                if (index == Constantes.DRAMATIC_THEME.length) {
                    Log.i(TAG,"RADC IGUAL: "+index);
                    // reached the end
                    mSpeaker.stop();
                } else {
                    double note = Constantes.DRAMATIC_THEME[index++];
                    Log.i(TAG,"RADC NOTA: "+note);
                    if (note > 0) {
                        mSpeaker.play(note);
                    } else {
                        mSpeaker.stop();
                    }
                    mHandler.postDelayed(this, PLAYBACK_NOTE_DELAY); // Se vuelve e ajecutar el Run, en un tiempo
                }
            } catch (IOException e) {
                Log.e(TAG, "RADC03 Error playing speaker", e);
            }
        }
    };
}