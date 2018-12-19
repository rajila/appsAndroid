package es.upm.android.rdajila.agendaapp.crudcontact;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import es.upm.android.rdajila.agendaapp.R;
import es.upm.android.rdajila.agendaapp.data.ContactBookDbHelper;
import es.upm.android.rdajila.agendaapp.entity.Contact;
import es.upm.android.rdajila.agendaapp.util.Constant;
import es.upm.android.rdajila.agendaapp.util.Util;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddEditContactFragment . OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddEditContactFragment # newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddEditContactFragment extends Fragment
{
    private ContactBookDbHelper _db;

    private static final String TAG = AddEditContactFragment.class.getSimpleName();

    private String _idContact = null;

    private Button _btnAceptar;

    private TextInputLayout _tilName;
    private EditText _fieldName;

    private TextInputLayout _tilAdress;
    private EditText _fieldAdress;

    private TextInputLayout _tilMobile;
    private EditText _fieldMobile;

    private TextInputLayout _tilPhone;
    private EditText _fieldPhone;

    private TextInputLayout _tilEmail;
    private EditText _fieldEmail;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            _idContact = getArguments().getString(Constant._KEY_ID_CONTACT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        _db = new ContactBookDbHelper(getActivity());
        // Inflate the layout for this fragment
        View _viewLayout = inflater.inflate(R.layout.fragment_add_edit_contact, container, false);

        _btnAceptar = (Button) _viewLayout.findViewById(R.id._btnSave);
        _btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { saveContact(); }
        });

        _tilName = (TextInputLayout)_viewLayout.findViewById(R.id._til_name);
        _fieldName = (EditText)_viewLayout.findViewById(R.id._field_name);
        _fieldName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { _tilName.setError(null); }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        _tilAdress = (TextInputLayout)_viewLayout.findViewById(R.id._til_adress);
        _fieldAdress = (EditText)_viewLayout.findViewById(R.id._field_adress);
        _fieldAdress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { _tilAdress.setError(null); }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        _tilMobile = (TextInputLayout)_viewLayout.findViewById(R.id._til_mobile);
        _fieldMobile = (EditText)_viewLayout.findViewById(R.id._field_mobile);
        _fieldMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { _tilMobile.setError(null); }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        _tilPhone = (TextInputLayout)_viewLayout.findViewById(R.id._til_phone);
        _fieldPhone = (EditText)_viewLayout.findViewById(R.id._field_phone);
        _fieldPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { _tilPhone.setError(null); }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        _tilEmail = (TextInputLayout)_viewLayout.findViewById(R.id._til_email);
        _fieldEmail = (EditText)_viewLayout.findViewById(R.id._field_email);
        _fieldEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { _tilEmail.setError(null); }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Carga de datos si existe el idContacto
        if (_idContact != null) loadContactDB();

        return _viewLayout;
    }

    /**
     * Carga los datos del contacto desde DB
     */
    private void loadContactDB()
    {
        new GetContactByIdTask().execute();
    }

    /**
     * Guarda el contacto
     */
    private void saveContact()
    {
        Log.i(TAG, "Save Contact!!");

        String _name = _fieldName.getText().toString();
        String _adress = _fieldAdress.getText().toString();
        String _mobile = _fieldMobile.getText().toString();
        String _phone = _fieldPhone.getText().toString();
        String _email = _fieldEmail.getText().toString();

        /**
         * Verificamos si los campos son correctos
         */
        boolean _stateValidateName = Util.isNameOK(_name);
        boolean _stateValidateAdress = Util.isAdressOK(_adress);
        boolean _stateValidateMobile = Util.isMobilePhoneOK(_mobile);
        boolean _stateValidatePhone = Util.isMobilePhoneOK(_phone);
        boolean _stateValidateEmail = Util.isEmailOK(_email);

        if( !_stateValidateName ) _tilName.setError(getResources().getString(R.string.form_name_error));
        else _tilName.setError(null);

        if( !_stateValidateAdress ) _tilAdress.setError(getResources().getString(R.string.form_adress_error));
        else _tilAdress.setError(null);

        if( !_stateValidateMobile ) _tilMobile.setError(getResources().getString(R.string.form_mobile_error));
        else _tilMobile.setError(null);

        if( !_stateValidatePhone ) _tilPhone.setError(getResources().getString(R.string.form_phone_error));
        else _tilPhone.setError(null);

        if( !_stateValidateEmail ) _tilEmail.setError(getResources().getString(R.string.form_email_error));
        else _tilEmail.setError(null);

        if( _stateValidateName && _stateValidateAdress && _stateValidateMobile && _stateValidatePhone && _stateValidateEmail )
        {
            Contact _contact = new Contact(_name, _adress, _mobile, _phone, _email);
            new AddEditContactTask().execute(_contact);
        }else
            Toast.makeText(getActivity(), R.string.form_general_error, Toast.LENGTH_SHORT).show();
    }

    /**
     * Carga la pantalla de listado de contactos
     * @param requery
     */
    private void showListContactScreen(Boolean requery)
    {
        if ( !requery )
        {
            showAddEditError();
            getActivity().setResult(Activity.RESULT_CANCELED);
        } else getActivity().setResult(Activity.RESULT_OK);

        getActivity().finish();
    }

    /**
     * Actualiza los valores del formulario con los datos de DB
     * @param data
     */
    private void loadDetailContact(Contact data)
    {
        _fieldName.setText(data.get_name());
        _fieldAdress.setText(data.get_adress());
        _fieldMobile.setText(data.get_mobile());
        _fieldPhone.setText(data.get_phone());
        _fieldEmail.setText(data.get_email());
    }

    private void showLoadContactDBError()
    {
        Toast.makeText(getActivity(), R.string.msn_error_load_data, Toast.LENGTH_SHORT).show();
    }

    private void showAddEditError()
    {
        Toast.makeText(getActivity(), R.string.msn_error_save_data, Toast.LENGTH_SHORT).show();
    }

    /**
     * Clase que gestiona la creación y actualizacin del contacto
     */
    private class AddEditContactTask extends AsyncTask<Contact, Void, Boolean>
    {
        @Override
        protected Boolean doInBackground(Contact... contacts) {
            if (_idContact != null)
            {
                Log.i(TAG,"Update Contact");
                return _db.updateContact(contacts[0], _idContact) > 0;
            } else {
                Log.i(TAG,"Create new Contact");
                return _db.insertContact(contacts[0]) > 0;
            }
        }

        @Override
        protected void onPostExecute(Boolean result)
        {
            Log.i(TAG,"Show List Contact Screen");
            showListContactScreen(result);
        }
    }

    /**
     * Clase que gestiona la obtención del contacto
     */
    private class GetContactByIdTask extends AsyncTask<Void, Void, Cursor>
    {
        @Override
        protected Cursor doInBackground(Void... voids) {
            return _db.getContactById(_idContact);
        }

        @Override
        protected void onPostExecute(Cursor cursor)
        {
            if (cursor != null && cursor.moveToLast()) {
                loadDetailContact(new Contact(cursor));
            } else {
                // Si hay error al cargar los datos
                showLoadContactDBError();
                getActivity().setResult(Activity.RESULT_CANCELED);
                getActivity().finish();
            }
        }
    }
}