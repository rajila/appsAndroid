package es.upm.android.iot.rdajila.practicaiotone;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManager;

import java.io.IOException;

/**
 * Skeleton of an Android Things activity.
 * <p>
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 *
 * <pre>{@code
 * PeripheralManagerService service = new PeripheralManagerService();
 * mLedGpio = service.openGpio("BCM6");
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * mLedGpio.setValue(true);
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 *
 * Link referencia
 * @see <a href="https://github.com/survivingwithandroid/Surviving-with-android/blob/master/Things/app/src/main/java/com/survivingwithandroid/things/RGBThingActivity.java">https://github.com/survivingwithandroid/Surviving-with-android/blob/master/Things/app/src/main/java/com/survivingwithandroid/things/RGBThingActivity.java</a>
 */
public class MainActivity extends Activity
{
    private Gpio _redPin;
    private Gpio _greenPin;
    private Gpio _bluePin;

    private PeripheralManager _manager;

    private Switch _switchRed;
    private Switch _switchGreen;
    private Switch _switchBlue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _manager = PeripheralManager.getInstance();
        initComponentsRPI();
        initComponentsLayout();
    }

    private void initComponentsRPI()
    {
        initLed(_redPin, Constantes.PINRED);
        initLed(_greenPin, Constantes.PINGREEN);
        initLed(_bluePin, Constantes.PINBLUE);
    }

    private void initLed(Gpio objPin, String valPin)
    {
        try
        {
            objPin = _manager.openGpio(valPin);
            objPin.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            objPin.setValue(false);
        } catch (IOException e){ }
    }

    private void initComponentsLayout()
    {
        _switchRed = (Switch) findViewById(R.id.switchRed);
        _switchRed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    _redPin.setValue(!isChecked);
                }
                catch (IOException e) {
                    //Log.w(TAG, "Red GPIO Error", e);
                }
            }
        });

        _switchGreen = (Switch) findViewById(R.id.switchGreen);
        _switchGreen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    _greenPin.setValue(!isChecked);
                }
                catch (IOException e) {
                    //Log.w(TAG, "Red GPIO Error", e);
                }
            }
        });

        _switchBlue = (Switch) findViewById(R.id.switchBlue);
        _switchBlue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    _bluePin.setValue(!isChecked);
                }
                catch (IOException e) {
                    //Log.w(TAG, "Red GPIO Error", e);
                }
            }
        });
    }

    private void closePin(Gpio objPin)
    {
        if (objPin != null)
        {
            try {
                objPin.close();
                objPin = null;
            } catch (IOException e) {
                //Log.w(TAG, "Unable to close GPIO", e);
            }
        }
    }

    @Override
    protected void onDestroy()
    {
        closePin(_redPin);
        closePin(_greenPin);
        closePin(_bluePin);
        super.onDestroy();
    }
}