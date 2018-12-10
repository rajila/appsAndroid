package es.upm.android.rdajila.agendaapp.util;

import android.database.Cursor;
import android.os.Environment;
import android.util.Log;
import android.util.Patterns;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.regex.Pattern;

import es.upm.android.rdajila.agendaapp.contract.ContactContract;
import es.upm.android.rdajila.agendaapp.data.ContactBookDbHelper;
import es.upm.android.rdajila.agendaapp.entity.Contact;

public class Util
{
    private static final String TAG = Util.class.getSimpleName();

    public static boolean isNameOK(String name)
    {
        return Pattern.compile("^[a-zA-ZÑáéíóúñ ]+$").matcher(name).matches() && name.length() <= Constant._MAX_CHARACTER;
    }

    public static boolean isAdressOK(String adress)
    {
        return adress.length() <= Constant._MAX_CHARACTER && !adress.isEmpty();
    }

    public static boolean isMobilePhoneOK(String data)
    {
        return Patterns.PHONE.matcher(data).matches();
    }

    public static boolean isEmailOK(String email)
    {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean exportContactsJSON(Cursor cursor)
    {
        boolean _stateExport = false;
        JSONArray _dataJSON = new JSONArray();
        JSONObject _rowJSON;

        cursor.moveToFirst();
        while ( !cursor.isAfterLast() )
        {
            _rowJSON = new JSONObject();
            for (int i = 0; i < cursor.getColumnCount(); i++)
            {
                if (cursor.getColumnName(i) != null)
                {
                    try
                    {
                        if (cursor.getString(i) != null)
                            _rowJSON.put(cursor.getColumnName(i), cursor.getString(i));
                        else
                            _rowJSON.put(cursor.getColumnName(i), Constant._STRING_EMPTY);
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            }
            _dataJSON.put(_rowJSON);
            cursor.moveToNext();
        }
        cursor.close();

        try {
            File _path = Environment.getExternalStorageDirectory();
            File _file = new File(_path.getAbsolutePath(), Constant._NAME_FILE);
            OutputStreamWriter _fileWrite = new OutputStreamWriter(new FileOutputStream(_file));
            _fileWrite.write(_dataJSON.toString());
            _fileWrite.close();
            _stateExport = !_stateExport;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return _stateExport;
    }

    public static boolean importContactsJSON(ContactBookDbHelper db)
    {
        boolean _stateExport = false;

        JSONArray _dataJSON;

        try
        {
            File _path = Environment.getExternalStorageDirectory();
            File _file = new File(_path.getAbsolutePath(), Constant._NAME_FILE);
            BufferedReader _fileRead = new BufferedReader(new InputStreamReader(new FileInputStream(_file)));
            String _dataJSONString = _fileRead.readLine();
            _dataJSON = new JSONArray(_dataJSONString);
            if (_dataJSON.length() > 0)
            {
                for (int i = 0; i < _dataJSON.length(); i++)
                {
                    JSONObject _blockJSON = _dataJSON.getJSONObject(i);
                    Contact _contact = new Contact( _blockJSON.getString(ContactContract._NAME),
                                                    _blockJSON.getString(ContactContract._ADRESS),
                                                    _blockJSON.getString(ContactContract._MOBILE),
                                                    _blockJSON.getString(ContactContract._PHONE),
                                                    _blockJSON.getString(ContactContract._EMAIL) );

                    Cursor _cursorDB = db.getContactById(Integer.toString(_blockJSON.getInt(ContactContract._ID)));

                    if ( _cursorDB != null && _cursorDB.moveToLast() )
                        db.updateContact(_contact,Integer.toString(_blockJSON.getInt(ContactContract._ID)));
                    else
                        db.insertContact(_contact);
                }
            }
            _stateExport = !_stateExport;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        return _stateExport;
    }
}