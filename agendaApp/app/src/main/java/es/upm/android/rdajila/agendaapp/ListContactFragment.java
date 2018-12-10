package es.upm.android.rdajila.agendaapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import es.upm.android.rdajila.agendaapp.contract.ContactContract;
import es.upm.android.rdajila.agendaapp.crudcontact.AddEditContact;
import es.upm.android.rdajila.agendaapp.crudcontact.DetailContactActivity;
import es.upm.android.rdajila.agendaapp.data.ContactBookDbHelper;
import es.upm.android.rdajila.agendaapp.util.Constant;
import es.upm.android.rdajila.agendaapp.util.Util;


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

    private ContactBookDbHelper _db;
    private ListView _listContact;
    private ContactCursorAdapter _contactAdaptador;

    private FloatingActionButton _btnAdd;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        _db = new ContactBookDbHelper(getActivity());
        // Inflate the layout for this fragment
        View _viewLayout = inflater.inflate(R.layout.fragment_list_contact, container, false);
        _listContact = (ListView) _viewLayout.findViewById(R.id._listData);
        _contactAdaptador = new ContactCursorAdapter(getActivity(),null);
        _listContact.setAdapter(_contactAdaptador);

        _listContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor _currentData = (Cursor) _contactAdaptador.getItem(i);
                showDetailScreen(_currentData.getString(_currentData.getColumnIndex(ContactContract._ID)));
            }
        });

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

    private void showDetailScreen(String idContact)
    {
        Intent intent = new Intent(getActivity(), DetailContactActivity.class);
        intent.putExtra(Constant._KEY_ID_CONTACT, idContact);
        startActivityForResult(intent, Constant._REQUEST_SHOW_CONTACT);
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
                case Constant._REQUEST_SHOW_CONTACT:
                    showDeleteMessage();
                    break;
            }
        }else if(Constant._REQUEST_EDIT_CONTACT == resultCode){
            showUpdatedMessage();
        }
        loadContacts();
    }

    private void showSavedMessage()
    {
        Toast.makeText(getActivity(), R.string.msn_save_contact, Toast.LENGTH_SHORT).show();
    }

    private void showUpdatedMessage()
    {
        Toast.makeText(getActivity(), R.string.msn_update_contact, Toast.LENGTH_SHORT).show();
    }

    private void showDeleteMessage()
    {
        Toast.makeText(getActivity(), R.string.msn_delete_contact, Toast.LENGTH_SHORT).show();
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
            _contactAdaptador.swapCursor(cursor);
            //if (cursor != null && cursor.getCount() > 0) {
            //    _contactAdaptador.swapCursor(cursor);
            //} else {
                // Mostrar empty state
            //}
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ( item.getItemId() )
        {
            case R.id.action_import:
                Log.i(TAG,"ACTION IMPORT");
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) || Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY))
                {
                    if( checkPermission() )
                    {
                        if( Util.importContactsJSON(_db) )
                        {
                            loadContacts();
                            Toast.makeText(getActivity(), R.string.msn_import_ok, Toast.LENGTH_SHORT).show();
                        }else
                            Toast.makeText(getActivity(), R.string.msn_import_error, Toast.LENGTH_SHORT).show();
                    }
                } else Toast.makeText(getActivity(), R.string.msn_access_sd_error, Toast.LENGTH_SHORT).show();

                break;
            case R.id.action_export:
                Log.i(TAG,"ACTION EXPORT");
                if ( Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) )
                {
                    if( checkPermission() )
                    {
                        if( Util.exportContactsJSON(_db.getAllContacts()) )
                            Toast.makeText(getActivity(), R.string.msn_export_ok, Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getActivity(), R.string.msn_export_error, Toast.LENGTH_SHORT).show();
                    }
                } else Toast.makeText(getActivity(), R.string.msn_access_sd_error, Toast.LENGTH_SHORT).show();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean checkPermission()
    {
        boolean _permisosOK = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                _permisosOK = false;
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constant._PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }

        return _permisosOK;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case Constant._PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE://Manifest.permission.WRITE_EXTERNAL_STORAGE:{
                // La peticion ha sido cancelada  si el array esta vacio
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    //expOrImpContacts();
                    Log.i(TAG,"Permission OK");
                } else {
                    Toast.makeText(getActivity(), R.string.msn_permission_ko, Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}