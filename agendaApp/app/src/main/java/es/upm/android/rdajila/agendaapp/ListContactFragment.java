package es.upm.android.rdajila.agendaapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import es.upm.android.rdajila.agendaapp.contract.ContactContract;
import es.upm.android.rdajila.agendaapp.crudcontact.AddEditContact;
import es.upm.android.rdajila.agendaapp.data.ScheduleDbHelper;
import es.upm.android.rdajila.agendaapp.util.Constant;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListContactFragment . OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListContactFragment # newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListContactFragment extends Fragment
{
    private static final String TAG = ListContactFragment.class.getSimpleName();

    private ScheduleDbHelper _db;
    private ListView _listContact;
    private ContactCursorAdapter _contactAdaptador;

    private FloatingActionButton _btnAdd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        _db = new ScheduleDbHelper(getActivity());
        // Inflate the layout for this fragment
        View _viewLayout = inflater.inflate(R.layout.fragment_list_contact, container, false);
        _listContact = (ListView) _viewLayout.findViewById(R.id._listData);
        _contactAdaptador = new ContactCursorAdapter(getActivity(),null);
        //_contactAdaptador = new ContactCursorAdapter(getActivity(), _db.getAllContacts());
        _listContact.setAdapter(_contactAdaptador);

        _btnAdd = (FloatingActionButton)_viewLayout.findViewById(R.id._btnAdd);
        _btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { actionAddContact(); }
        });

        loadContacts();

        return _viewLayout;
    }

    private void actionAddContact()
    {
        Intent _intentView = new Intent( getActivity(), AddEditContact.class );
        startActivityForResult( _intentView, Constant._REQUEST_ADD_CONTACT ); // Constant._REQUEST_ADD_CONTACT valor del resultCode
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (Activity.RESULT_OK == resultCode)
        {
            switch (requestCode)
            {
                case Constant._REQUEST_ADD_CONTACT:
                    showSavedMessage();
                    break;
                //case REQUEST_UPDATE_DELETE_LAWYER:
                //    loadLawyers();
                //    break;
            }
            loadContacts();
        }
    }

    private void showSavedMessage()
    {
        Toast.makeText(getActivity(), R.string.msn_save_contact, Toast.LENGTH_SHORT).show();
    }

    private void loadContacts()
    {
        new ContactLoadTask().execute();
    }

    private class ContactLoadTask extends AsyncTask<Void, Void, Cursor>
    {
        @Override
        protected Cursor doInBackground(Void... voids) {
            return _db.getAllContacts();
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor != null && cursor.getCount() > 0) {
                _contactAdaptador.swapCursor(cursor);
            } else {
                // Mostrar empty state
            }
        }
    }
}