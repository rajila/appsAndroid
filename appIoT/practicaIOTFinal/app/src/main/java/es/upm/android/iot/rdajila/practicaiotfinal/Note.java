package es.upm.android.iot.rdajila.practicaiotfinal;

import com.google.android.things.pio.Gpio;

public class Note
{
    private int _frequency;
    private  int _duration;
    private Gpio _ledPin;

    public Note(int frequency, int duration, Gpio ledPin)
    {
        this._frequency = frequency;
        this._duration = duration;
        this._ledPin = ledPin;
    }

    public int get_frequency() {
        return _frequency;
    }

    public void set_frequency(int _frequency) {
        this._frequency = _frequency;
    }

    public int get_duration() {
        return _duration;
    }

    public void set_duration(int _duration) {
        this._duration = _duration;
    }

    public Gpio get_ledPin() {
        return _ledPin;
    }

    public void set_ledPin(Gpio _ledPin) {
        this._ledPin = _ledPin;
    }
}