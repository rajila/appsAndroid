package es.upm.android.rdajila.agendaapp.crudcontact;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import es.upm.android.rdajila.agendaapp.R;
import es.upm.android.rdajila.agendaapp.data.ContactBookDbHelper;
import es.upm.android.rdajila.agendaapp.entity.Contact;
import es.upm.android.rdajila.agendaapp.util.Constant;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailContactFragment . OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailContactFragment # newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailContactFragment extends Fragment
{
    private ContactBookDbHelper _db;

    private static final String TAG = DetailContactFragment.class.getSimpleName();

    private String _idContact = null;
    private String _movilDB = "";

    private Toolbar _toolbarApp;

    private CollapsingToolbarLayout _mCollapsingView;
    private TextView _valueAdress;
    private TextView _valueMobile;
    private TextView _valuePhone;
    private TextView _valueEmail;

    private FloatingActionButton _btnCall;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            _idContact = getArguments().getString(Constant._KEY_ID_CONTACT);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        _db = new ContactBookDbHelper(getActivity());
        // Inflate the layout for this fragment
        View _viewLayout = inflater.inflate(R.layout.fragment_detail_contact, container, false);

        _mCollapsingView = (CollapsingToolbarLayout) getActivity().findViewById(R.id.toolbar_layout);
        _toolbarApp = (Toolbar) getActivity().findViewById(R.id._toolbarApp);

        _valueAdress = (TextView) _viewLayout.findViewById(R.id._valueAdress);
        _valueMobile = (TextView) _viewLayout.findViewById(R.id._valueMobile);
        _valuePhone = (TextView) _viewLayout.findViewById(R.id._valuePhone);
        _valueEmail = (TextView) _viewLayout.findViewById(R.id._valueEmail);

        _btnCall = (FloatingActionButton)getActivity().findViewById(R.id._btnCall);
        _btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { actionCall(); }
        });

        loadDetailContact();

        return _viewLayout;
    }

    private void loadDetailContact()
    {
        new GetContactByIdTask().execute();
    }

    private void showDetailContact(Contact data)
    {
        _mCollapsingView.setTitle(data.get_name());
        _valueAdress.setText(data.get_adress());
        _valueMobile.setText(data.get_mobile());
        _valuePhone.setText(data.get_phone());
        _valueEmail.setText(data.get_email());
        _mCollapsingView.setBackgroundResource(R.color.colorG);
        _movilDB = data.get_mobile();
    }

    private void actionCall()
    {
        Intent i = new Intent(Intent.ACTION_DIAL);
        i.setData(Uri.parse("tel:" + _movilDB));
        startActivity(i);
    }

    private void actionEditContact()
    {
        Intent intent = new Intent(getActivity(), AddEditContact.class);
        intent.putExtra(Constant._KEY_ID_CONTACT, _idContact);
        startActivityForResult(intent, Constant._REQUEST_EDIT_CONTACT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == Constant._REQUEST_EDIT_CONTACT) {
            if (resultCode == Activity.RESULT_OK)
            {
                getActivity().setResult(Constant._REQUEST_EDIT_CONTACT);
                getActivity().finish();
            }
        }
    }

    private void showListContactScreen(boolean requery)
    {
        if ( !requery )
            showDeleteContactDBError();
        else{
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        }
    }

    private void showUpdatedMessage()
    {
        Toast.makeText(getActivity(), R.string.msn_update_contact, Toast.LENGTH_SHORT).show();
    }

    private void showLoadContactDBError()
    {
        Toast.makeText(getActivity(), R.string.msn_error_load_data, Toast.LENGTH_SHORT).show();
    }

    private void showDeleteContactDBError()
    {
        Toast.makeText(getActivity(), R.string.msn_error_delete_data, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id._menuEdit:
                actionEditContact();
                break;
            case R.id._menuDelete:
                new DeleteContactTask().execute();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class GetContactByIdTask extends AsyncTask<Void, Void, Cursor>
    {
        @Override
        protected Cursor doInBackground(Void... voids) {
            return _db.getContactById(_idContact);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor != null && cursor.moveToLast()) {
                showDetailContact(new Contact(cursor));
            } else {
                showLoadContactDBError();
            }
        }
    }

    private class DeleteContactTask extends AsyncTask<Void, Void, Integer>
    {

        @Override
        protected Integer doInBackground(Void... voids) {
            return _db.deleteContact(_idContact);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            showListContactScreen(integer > 0);
        }

    }
}