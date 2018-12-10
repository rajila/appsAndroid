package es.upm.android.rdajila.agendaapp.contract;

import android.provider.BaseColumns;

public abstract class ContactContract implements BaseColumns
{
    public static final String _TABLE_NAME = "contact";
    public static final String _ID = "_id";
    public static final String _NAME = "name";
    public static final String _ADRESS = "adress";
    public static final String _MOBILE = "mobile";
    public static final String _PHONE = "phone";
    public static final String _EMAIL = "email";
    public static final String _COLOR = "color";

    public static final String[] _GET_ALL_DATA = {_ID, _NAME, _ADRESS, _MOBILE, _PHONE, _EMAIL, _COLOR};
}