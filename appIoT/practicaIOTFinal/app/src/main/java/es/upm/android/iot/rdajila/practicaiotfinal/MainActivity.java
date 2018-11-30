package es.upm.android.iot.rdajila.practicaiotfinal;

import android.app.Activity;
import android.os.Bundle;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.KeyEvent;

import com.google.android.things.contrib.driver.button.Button;
import com.google.android.things.pio.PeripheralManager;
import com.google.android.things.pio.Gpio;
import com.google.android.things.contrib.driver.pwmspeaker.Speaker;

import com.google.android.things.contrib.driver.button.ButtonInputDriver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

/**
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 *
 * Link referencia
 * @see <a href="https://github.com/survivingwithandroid/Surviving-with-android/blob/master/Things/app/src/main/java/com/survivingwithandroid/things/RGBThingActivity.java">https://github.com/survivingwithandroid/Surviving-with-android/blob/master/Things/app/src/main/java/com/survivingwithandroid/things/RGBThingActivity.java</a>
 */
public class MainActivity extends Activity
{
    private Speaker mSpeaker;

    private ButtonInputDriver _btnPin;

    private Gpio _redPin;
    private Gpio _greenPin;
    private Gpio _bluePin;
    private Gpio _yellowPin;
    private Gpio _whitePin;

    private PeripheralManager _manager;
    private HandlerThread _handlerThread;
    private Handler _handler;

    private static final String TAG = MainActivity.class.getSimpleName();

    private boolean _flagButtonPress = false;
    private HashMap<Integer, Note> TONO_STAR_WARS;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        _manager = PeripheralManager.getInstance();
        initComponentsRPI();
        TONO_STAR_WARS = Constantes.initToneStarWars(_yellowPin,_bluePin,_redPin, _greenPin, _whitePin);

        _handlerThread = new HandlerThread("pwm-playback");
        _handlerThread.start();
        _handler = new Handler(_handlerThread.getLooper());
    }

    private void initComponentsRPI()
    {
        try
        {
            Log.i(TAG,"INIT COMPONENTS");
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

            _btnPin = new ButtonInputDriver(Constantes.PINBUTTON, Button.LogicState.PRESSED_WHEN_LOW, KeyEvent.KEYCODE_SPACE);
            _btnPin.register();

        } catch (IOException e){
            Log.e(TAG, "Error initializing RPI components");
            return; // don't initilize the handler
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_SPACE)
        {
            if( _flagButtonPress ) return true;
            _handler.post(mPlaybackRunnable);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private Runnable mPlaybackRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            if (mSpeaker == null) return;
            try
            {
                _flagButtonPress = !_flagButtonPress;
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
                _flagButtonPress = !_flagButtonPress;
            } catch (IOException e) {
                Log.e(TAG, "Error RPI speaker", e);
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
            Log.e(TAG, "Error updating state of LED GPIO", e);
        }
    }

    private void closePinLed(Gpio objPin)
    {
        if (objPin != null)
        {
            try {
                objPin.close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing LED GPIO", e);
            } finally{
                objPin = null;
            }
        }
    }

    private void closePinButton(ButtonInputDriver objPin)
    {
        if (objPin != null)
        {
            objPin.unregister();
            try {
                objPin.close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing RPI Button", e);
            } finally{
                objPin = null;
            }
        }
    }

    private void closePinSpeaker()
    {
        if (mSpeaker != null) {
            try {
                mSpeaker.stop();
                mSpeaker.close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing RPI speaker", e);
            } finally {
                mSpeaker = null;
            }
        }
    }

    private void removeCallbacks()
    {
        if (_handler != null) {
            _handler.removeCallbacks(mPlaybackRunnable);
            _handlerThread.quitSafely();
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
        closePinButton(_btnPin);

        removeCallbacks();
        closePinSpeaker();
    }
}