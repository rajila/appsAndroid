package es.upm.android.rdajila.agendaapp.crudcontact;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import es.upm.android.rdajila.agendaapp.R;
import es.upm.android.rdajila.agendaapp.data.ScheduleDbHelper;
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
    private ScheduleDbHelper _db;

    private static final String TAG = AddEditContactFragment.class.getSimpleName();

    private String _idContact;

    private FloatingActionButton _btnAdd;
    private Button _btnAceptar;

    private TextInputLayout _tilName;
    private EditText _fieldName;

    private TextInputLayout _tilDirection;
    private EditText _fieldDirection;

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

        if (getArguments() != null) {
            _idContact = getArguments().getString(Constant._KEY_ID_CONTACT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        _btnAdd = (FloatingActionButton)getActivity().findViewById(R.id._btnAdd);
        if( _btnAdd != null ) _btnAdd.hide();

        _db = new ScheduleDbHelper(getActivity());
        // Inflate the layout for this fragment
        View _viewLayout = inflater.inflate(R.layout.fragment_add_edit_contact, container, false);

        _btnAceptar = (Button) _viewLayout.findViewById(R.id.boton_aceptar);
        _btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { saveContact(); }
        });

        _tilName = (TextInputLayout)_viewLayout.findViewById(R.id._til_name);
        _fieldName = (EditText)_viewLayout.findViewById(R.id._field_name);

        _tilDirection = (TextInputLayout)_viewLayout.findViewById(R.id._til_direction);
        _fieldDirection = (EditText)_viewLayout.findViewById(R.id._field_direction);

        _tilMobile = (TextInputLayout)_viewLayout.findViewById(R.id._til_mobile);
        _fieldMobile = (EditText)_viewLayout.findViewById(R.id._field_mobile);

        _tilPhone = (TextInputLayout)_viewLayout.findViewById(R.id._til_phone);
        _fieldPhone = (EditText)_viewLayout.findViewById(R.id._field_phone);

        _tilEmail = (TextInputLayout)_viewLayout.findViewById(R.id._til_email);
        _fieldEmail = (EditText)_viewLayout.findViewById(R.id._field_email);

        return _viewLayout;
    }

    public void saveContact()
    {
        Log.i(TAG, "Save Contact!!");
    }

    public void cancelAddContact()
    {

    }
}