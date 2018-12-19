package es.upm.android.rdajila.agendaapp.entity;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;

import java.util.Random;

import es.upm.android.rdajila.agendaapp.contract.ContactContract;

/**
 * Clase Contacto
 */
public class Contact
{
    private Long _id;
    private String _name;
    private String _adress;
    private String _mobile;
    private String _phone;
    private String _email;
    private int _color;

    /**
     * Contructor que crea un contacto sin ID
     * @param name
     * @param adress
     * @param mobile
     * @param phone
     * @param email
     */
    public Contact(String name, String adress, String mobile, String phone, String email)
    {
        Random random = new Random();
        _name = name;
        _adress = adress;
        _mobile = mobile;
        _phone = phone;
        _email = email;
        // Definimos un color para el contacto
        _color = Color.argb(255, random.nextInt(250), random.nextInt(150), random.nextInt(200));
    }

    /**
     * Contructor que crea un contacto a partir de base de datos
     * @param cursor
     */
    public Contact(Cursor cursor)
    {
        _id = Long.parseLong(cursor.getString(cursor.getColumnIndex(ContactContract._ID)));
        _name = cursor.getString(cursor.getColumnIndex(ContactContract._NAME));
        _adress = cursor.getString(cursor.getColumnIndex(ContactContract._ADRESS));
        _mobile = cursor.getString(cursor.getColumnIndex(ContactContract._MOBILE));
        _phone = cursor.getString(cursor.getColumnIndex(ContactContract._PHONE));
        _email = cursor.getString(cursor.getColumnIndex(ContactContract._EMAIL));
        _color = cursor.getInt(cursor.getColumnIndex(ContactContract._COLOR));
    }

    /**
     * Función que prepara el contenedor con los respectivos valores para insertar en base de datos
     * @return
     */
    public ContentValues createContentValue()
    {
        ContentValues _contentVal = new ContentValues();
        _contentVal.put(ContactContract._NAME, _name);
        _contentVal.put(ContactContract._ADRESS, _adress);
        _contentVal.put(ContactContract._MOBILE, _mobile);
        _contentVal.put(ContactContract._PHONE, _phone);
        _contentVal.put(ContactContract._EMAIL, _email);
        _contentVal.put(ContactContract._COLOR, _color);
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

    public String get_adress() {
        return _adress;
    }

    public void set_adress(String _adress) {
        this._adress = _adress;
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

    public int get_color() {
        return _color;
    }

    public void set_color(int _color) {
        this._color = _color;
    }
}