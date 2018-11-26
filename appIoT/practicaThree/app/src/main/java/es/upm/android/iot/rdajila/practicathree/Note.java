package es.upm.android.iot.rdajila.practicathree;

public class Note
{
    private int _frequency;
    private  int _duration;

    public Note(int frequency, int duration)
    {
        this._frequency = frequency;
        this._duration = duration;
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
}