package es.upm.android.rdajila.agendaapp.entity;

import android.content.ContentValues;
import android.database.Cursor;

import es.upm.android.rdajila.agendaapp.contract.ContactContract;

public class Contact
{
    private Long _id;
    private String _name;
    private String _direction;
    private String _mobile;
    private String _phone;
    private String _email;

    public Contact(String name, String direction, String mobile, String phone, String email)
    {
        _name = name;
        _direction = direction;
        _mobile = mobile;
        _phone = phone;
        _email = email;
    }

    public Contact(Cursor cursor)
    {
        _id = Long.parseLong(cursor.getString(cursor.getColumnIndex(ContactContract._ID)));
        _name = cursor.getString(cursor.getColumnIndex(ContactContract._NAME));
        _direction = cursor.getString(cursor.getColumnIndex(ContactContract._DIRECTION));
        _mobile = cursor.getString(cursor.getColumnIndex(ContactContract._MOBILE));
        _phone = cursor.getString(cursor.getColumnIndex(ContactContract._PHONE));
        _email = cursor.getString(cursor.getColumnIndex(ContactContract._EMAIL));
    }

    public ContentValues createContentValue()
    {
        ContentValues _contentVal = new ContentValues();
        _contentVal.put(ContactContract._NAME, _name);
        _contentVal.put(ContactContract._DIRECTION, _direction);
        _contentVal.put(ContactContract._MOBILE, _mobile);
        _contentVal.put(ContactContract._PHONE, _phone);
        _contentVal.put(ContactContract._EMAIL, _email);
        return _contentVal;
    }

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