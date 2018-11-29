package es.upm.android.iot.rdajila.practicaiotfinal;

import android.app.Activity;
import android.os.Bundle;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.KeyEvent;

import com.google.android.things.pio.PeripheralManager;
import com.google.android.things.pio.Gpio;
import com.google.android.things.contrib.driver.pwmspeaker.Speaker;

import java.io.IOException;
import java.util.HashMap;

/**
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 *
 * Link referencia
 * @see <a href="https://github.com/survivingwithandroid/Surviving-with-android/blob/master/Things/app/src/main/java/com/survivingwithandroid/things/RGBThingActivity.java">https://github.com/survivingwithandroid/Surviving-with-android/blob/master/Things/app/src/main/java/com/survivingwithandroid/things/RGBThingActivity.java</a>
 */
public class MainActivity extends Activity
{
    private Speaker mSpeaker;
    private Gpio _redPin;
    private Gpio _greenPin;
    private Gpio _bluePin;
    private Gpio _yellowPin;
    private Gpio _whitePin;

    private PeripheralManager _manager;
    private HandlerThread _handlerThread;
    private Handler _handler;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        _manager = PeripheralManager.getInstance();
        initComponentsRPI();

        _handlerThread = new HandlerThread("pwm-playback");
        _handlerThread.start();
        _handler = new Handler(_handlerThread.getLooper());
        _handler.post(mPlaybackRunnable);
    }

    private void initComponentsRPI()
    {
        try
        {
            Log.i(TAG,"INIT RPI'S COMPONENTS");
            _redPin = _manager.openGpio(Constantes.PINRED);
            _redPin.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            _redPin.setValue(false);

            _greenPin = _manager.openGpio(Constantes.PINGREEN);
            _greenPin.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            _greenPin.setValue(false);

            _bluePin = _manager.openGpio(Constantes.PINBLUE);
            _bluePin.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            _bluePin.setValue(false);

            _yellowPin = _manager.openGpio(Constantes.PINYELLOW);
            _yellowPin.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            _yellowPin.setValue(false);

            _whitePin = _manager.openGpio(Constantes.PINWHITE);
            _whitePin.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            _whitePin.setValue(false);

            mSpeaker = new Speaker(Constantes.PINBUZZER);
            mSpeaker.stop(); // in case the PWM pin was enabled already

        } catch (IOException e){
            Log.e(TAG, "Error initializing components RPI");
            return; // don't initilize the handler
        }
    }

    private Runnable mPlaybackRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            final HashMap<Integer, Note> TONO_STAR_WARS = Constantes.initToneStarWars(_yellowPin,_bluePin,_redPin, _greenPin, _whitePin);
            if (mSpeaker == null) return;
            try
            {
                for(int i=0; i<TONO_STAR_WARS.size(); i++)
                {
                    Note _note = TONO_STAR_WARS.get(i);
                    int _frequency = _note.get_frequency();
                    int _duration = _note.get_duration();
                    if( _frequency > 0 )
                    {
                        mSpeaker.play(_frequency);
                        setLedValue(_note.get_ledPin(),true);
                        Thread.sleep(_duration);
                        setLedValue(_note.get_ledPin(),false);
                        mSpeaker.stop();
                        Thread.sleep(50);
                    }else{
                        Thread.sleep(_duration);
                    }
                }
            } catch (IOException e) {
                Log.e(TAG, "RADC03 Error playing speaker", e);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    private void setLedValue(Gpio objPin, boolean value)
    {
        try {
            objPin.setValue(value);
        } catch (IOException e) {
            Log.e(TAG, "Error updating GPIO value", e);
        }
    }

    private void closePinLed(Gpio objPin)
    {
        if (objPin != null)
        {
            try {
                objPin.close();
            } catch (IOException e) {
                //Log.w(TAG, "Unable to close GPIO", e);
            } finally{
                objPin = null;
            }
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        closePinLed(_redPin);
        closePinLed(_greenPin);
        closePinLed(_bluePin);
        closePinLed(_yellowPin);
        closePinLed(_whitePin);

        if (_handler != null) {
            _handler.removeCallbacks(mPlaybackRunnable);
            _handlerThread.quitSafely();
        }
        if (mSpeaker != null) {
            try {
                mSpeaker.stop();
                mSpeaker.close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing speaker", e);
            } finally {
                mSpeaker = null;
            }
        }
    }
}