package es.upm.android.rdajila.agendaapp.crudcontact;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
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

        _tilAdress = (TextInputLayout)_viewLayout.findViewById(R.id._til_adress);
        _fieldAdress = (EditText)_viewLayout.findViewById(R.id._field_adress);

        _tilMobile = (TextInputLayout)_viewLayout.findViewById(R.id._til_mobile);
        _fieldMobile = (EditText)_viewLayout.findViewById(R.id._field_mobile);

        _tilPhone = (TextInputLayout)_viewLayout.findViewById(R.id._til_phone);
        _fieldPhone = (EditText)_viewLayout.findViewById(R.id._field_phone);

        _tilEmail = (TextInputLayout)_viewLayout.findViewById(R.id._til_email);
        _fieldEmail = (EditText)_viewLayout.findViewById(R.id._field_email);

        // Carga de datos
        if (_idContact != null) loadContactDB();

        return _viewLayout;
    }

    private void loadContactDB()
    {
        new GetContactByIdTask().execute();
    }

    private void saveContact()
    {
        Log.i(TAG, "Save Contact!!");

        String _name = _fieldName.getText().toString();
        String _adress = _fieldAdress.getText().toString();
        String _mobile = _fieldMobile.getText().toString();
        String _phone = _fieldPhone.getText().toString();
        String _email = _fieldEmail.getText().toString();

        Contact _contact = new Contact(_name,_adress,_mobile,_phone,_email);
        new AddEditContactTask().execute(_contact);
    }

    private void showListContactScreen(Boolean requery)
    {
        if ( !requery )
        {
            showAddEditError();
            getActivity().setResult(Activity.RESULT_CANCELED);
        } else getActivity().setResult(Activity.RESULT_OK);

        getActivity().finish();
    }

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
                showLoadContactDBError();
                getActivity().setResult(Activity.RESULT_CANCELED);
                getActivity().finish();
            }
        }
    }
}