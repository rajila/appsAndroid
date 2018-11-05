package es.upm.android.rdajila.agendaapp.entity;


public class Contact
{
    private Long _id;
    private String _name;
    private String _direction;
    private String _mobile;
    private String _phone;
    private String _email;

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_direction() {
        return _direction;
    }

    public void set_direction(String _direction) {
        this._direction = _direction;
    }

    public String get_mobile() {
        return _mobile;
    }

    public void set_mobile(String _mobile) {
        this._mobile = _mobile;
    }

    public String get_phone() {
        return _phone;
    }

    public void set_phone(String _phone) {
        this._phone = _phone;
    }

    public String get_email() {
        return _email;
    }

    public void set_email(String _email) {
        this._email = _email;
    }
}