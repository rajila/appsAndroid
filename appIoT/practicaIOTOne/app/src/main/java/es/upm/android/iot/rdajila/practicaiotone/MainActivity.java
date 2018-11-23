package es.upm.android.iot.rdajila.practicaiotone;

import android.app.Activity;
import android.os.Bundle;
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

    private void initComponentLayout()
    {

    }
}