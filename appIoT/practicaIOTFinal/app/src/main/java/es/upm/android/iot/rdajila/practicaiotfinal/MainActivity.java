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

/**
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 *
 * Link referencia
 * @see <a href="https://github.com/survivingwithandroid/Surviving-with-android/blob/master/Things/app/src/main/java/com/survivingwithandroid/things/RGBThingActivity.java">https://github.com/survivingwithandroid/Surviving-with-android/blob/master/Things/app/src/main/java/com/survivingwithandroid/things/RGBThingActivity.java</a>
 */
public class MainActivity extends Activity
{
    // Objeto que hace referencia al Buzzer
    private Speaker _mSpeaker;

    // Objeto que hace referencia al button
    private ButtonInputDriver _btnPin;

    // Objetos que hacen referencia a los LEDs
    private Gpio _redPin;
    private Gpio _greenPin;
    private Gpio _bluePin;
    private Gpio _yellowPin;
    private Gpio _whitePin;

    // Objeto que administra la comunicación con los perifericos del hardware
    private PeripheralManager _manager;
    // Hilo para ejecutar la acción del button en un hilo secuandario
    private HandlerThread _handlerThread;
    private Handler _handler;

    private static final String TAG = MainActivity.class.getSimpleName();

    // Variable que nos ayuda a controlar la ejecución de la melodia.
    private boolean _flagButtonPress = false;

    // Lista de los tonos de la Nota
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

    /**
     * Función que inicializa las referencias de los objetos con el hardware
     */
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

            _mSpeaker = new Speaker(Constantes.PINBUZZER);
            _mSpeaker.stop(); // in case the PWM pin was enabled already

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
            // Verifica si el boton ha sido presionado y la melodia aún esta sonando
            // Treue --> La melodia aún esta sonando
            // False --> La melodia ha terminado de sonar
            if( _flagButtonPress ) return true;
            _handler.post(mPlaybackRunnable); // Ejecuta el hilo sencundario
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Función que gestiona el sonido de la melodia
     */
    private Runnable mPlaybackRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            if (_mSpeaker == null) return;
            try
            {
                // Actualiza el valor de la bandera
                _flagButtonPress = !_flagButtonPress;
                // Reoorre los tonos de la melodia
                for(int i=0; i<TONO_STAR_WARS.size(); i++)
                {
                    // Obtiene la nota
                    Note _note = TONO_STAR_WARS.get(i);
                    int _frequency = _note.get_frequency();
                    int _duration = _note.get_duration();
                    if( _frequency > 0 )
                    {
                        _mSpeaker.play(_frequency); // Inicia a sonar el tono
                        setLedValue(_note.get_ledPin(),true); // enciende el led correspondiene al tono
                        Thread.sleep(_duration); // tiempo de vida del tono
                        setLedValue(_note.get_ledPin(),false); // apaga el led
                        _mSpeaker.stop(); // deja de sonar el tono
                        Thread.sleep(50); // tiempo de espera para que inicie a sonar el siguiente tono
                    }else{
                        Thread.sleep(_duration);
                    }
                }
                _flagButtonPress = !_flagButtonPress; // Cambia el estado para estar listo para volver a escuchar el tono
            } catch (IOException e) {
                Log.e(TAG, "Error RPI speaker", e);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * Actualiza el estado de los LEDs
     * @param objPin --> Objeto referencial al LED
     * @param value True --> Enciende LED, False --> Apaga LED
     */
    private void setLedValue(Gpio objPin, boolean value)
    {
        try {
            objPin.setValue(value);
        } catch (IOException e) {
            Log.e(TAG, "Error updating state of LED GPIO", e);
        }
    }

    /**
     * Elimina todas las referencias de los objetos a los LEDs
     * @param objPin
     */
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

    /**
     * Elimina la referencia del objeto al Boton
     * @param objPin
     */
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

    /**
     * Elimina la referencia del objeto al Buzzer
     */
    private void closePinSpeaker()
    {
        if (_mSpeaker != null) {
            try {
                _mSpeaker.stop();
                _mSpeaker.close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing RPI speaker", e);
            } finally {
                _mSpeaker = null;
            }
        }
    }

    /**
     * Elimina la referencia del hilo secuandario
     */
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