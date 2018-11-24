package es.upm.android.iot.rdajila.practicaiottwo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;

import com.google.android.things.contrib.driver.button.Button;
import com.google.android.things.contrib.driver.button.ButtonInputDriver;
import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManager;

import java.io.IOException;
import java.util.Random;

/**
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 *
 * Link referencia
 * @see <a href="https://github.com/survivingwithandroid/Surviving-with-android/blob/master/Things/app/src/main/java/com/survivingwithandroid/things/RGBThingActivity.java">https://github.com/survivingwithandroid/Surviving-with-android/blob/master/Things/app/src/main/java/com/survivingwithandroid/things/RGBThingActivity.java</a>
 */
public class MainActivity extends Activity
{
    private static final String TAG = MainActivity.class.getSimpleName();

    private Gpio _redPin;
    private Gpio _greenPin;
    private Gpio _bluePin;

    private ButtonInputDriver _btnPin;

    private PeripheralManager _manager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        _manager = PeripheralManager.getInstance();
        initComponentsRPI();
    }

    private void initComponentsRPI()
    {
        try
        {
            _redPin = _manager.openGpio(Constantes.PINRED);
            _redPin.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            _redPin.setValue(false);

            _greenPin = _manager.openGpio(Constantes.PINGREEN);
            _greenPin.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            _greenPin.setValue(false);

            _bluePin = _manager.openGpio(Constantes.PINBLUE);
            _bluePin.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            _bluePin.setValue(false);

            _btnPin = new ButtonInputDriver(Constantes.PINBUTTON, Button.LogicState.PRESSED_WHEN_LOW, KeyEvent.KEYCODE_SPACE);
            _btnPin.register();

        } catch (IOException e){ }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_SPACE) return turnOnRGB();
        return super.onKeyDown(keyCode, event);
    }

    private boolean turnOnRGB()
    {
        Random _r = new Random();
        int _val = _r.nextInt(5)+1;
        switch ( _val )
        {
            case 1: // RED TURN ON
                setRGBValue(_redPin,true);
                setRGBValue(_greenPin,false);
                setRGBValue(_bluePin,false);
                break;
            case 2: // GREEN TURN ON
                setRGBValue(_redPin,false);
                setRGBValue(_greenPin,true);
                setRGBValue(_bluePin,false);
                break;
            case 3: // BLUE TURN ON
                setRGBValue(_redPin,false);
                setRGBValue(_greenPin,false);
                setRGBValue(_bluePin,true);
                break;
            case 4: // ALL TURN ON
                setRGBValue(_redPin,true);
                setRGBValue(_greenPin,true);
                setRGBValue(_bluePin,true);
                break;
            case 5: // ALL TURN OFF
                setRGBValue(_redPin,false);
                setRGBValue(_greenPin,false);
                setRGBValue(_bluePin,false);
                break;
            default:
                break;
        }
        return true;
    }

    private void setRGBValue(Gpio objPin, boolean value)
    {
        try {
            objPin.setValue(value);
        } catch (IOException e) {
            Log.e(TAG, "Error updating GPIO value", e);
        }
    }

    private void closePinRGB(Gpio objPin)
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

    private void closePinButton(ButtonInputDriver objPin)
    {
        if (objPin != null)
        {
            objPin.unregister();
            try {
                objPin.close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing Button driver", e);
            } finally{
                objPin = null;
            }
        }
    }

    @Override
    protected void onStop()
    {
        closePinRGB(_redPin);
        closePinRGB(_greenPin);
        closePinRGB(_bluePin);
        closePinButton(_btnPin);
        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        closePinRGB(_redPin);
        closePinRGB(_greenPin);
        closePinRGB(_bluePin);
        closePinButton(_btnPin);
        super.onDestroy();
    }
}