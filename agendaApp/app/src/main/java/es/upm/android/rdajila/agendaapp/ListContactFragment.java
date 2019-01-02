package es.upm.android.rdajila.agendaapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import es.upm.android.rdajila.agendaapp.contract.ContactContract;
import es.upm.android.rdajila.agendaapp.crudcontact.AddEditContactFragment;
import es.upm.android.rdajila.agendaapp.crudcontact.DetailContactFragment;
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
        _db = MainActivity._dbMain;
        // Inflate the layout for this fragment
        View _viewLayout = inflater.inflate(R.layout.fragment_list_contact, container, false);
        _listContact = (ListView) _viewLayout.findViewById(R.id._listData);
        _contactAdaptador = new ContactCursorAdapter(getActivity(),null);
        _listContact.setAdapter(_contactAdaptador);
        _listContact.setDivider(getActivity().getResources().getDrawable(R.drawable.divider));
        _listContact.setDividerHeight(1);

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

    /**
     * Función que levanta la actividad New/Edit Contacto
     */
    private void actionAddContact()
    {
        //Intent _intentView = new Intent( getActivity(), AddEditContact.class );
        //startActivityForResult( _intentView, Constant._REQUEST_ADD_CONTACT ); // Constant._REQUEST_ADD_CONTACT valor del resultCode
        int _orientation = getActivity().getResources().getConfiguration().orientation;
        AddEditContactFragment _fragment = new AddEditContactFragment();
        if( _orientation == Configuration.ORIENTATION_PORTRAIT )
            loadFragmentScreenWithBackStack(_fragment, R.id._frgList);
        if( _orientation == Configuration.ORIENTATION_LANDSCAPE )
            loadFragmentScreenWithOutBackStack(_fragment, R.id._frgDynamic);
    }



    /**
     * Función que levanta la actividad Detalle Contacto
     * @param idContact
     */
    private void showDetailScreen(String idContact)
    {
        int _orientation = getActivity().getResources().getConfiguration().orientation;
        DetailContactFragment _fragment = new DetailContactFragment();
        Bundle args = new Bundle();
        args.putString(Constant._KEY_ID_CONTACT, idContact);
        _fragment.setArguments(args);

        if( _orientation == Configuration.ORIENTATION_PORTRAIT )
            loadFragmentScreenWithBackStack(_fragment, R.id._frgList);
        if( _orientation == Configuration.ORIENTATION_LANDSCAPE )
            loadFragmentScreenWithOutBackStack(_fragment, R.id._frgDynamic);
    }

    private void loadFragmentScreenWithBackStack(Fragment fragment, int resource)
    {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(resource, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void loadFragmentScreenWithOutBackStack(Fragment fragment, int resource)
    {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(resource, fragment)
                .commit();
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

    /**
     * Mensaje de guardado exitoso
     */
    private void showSavedMessage()
    {
        Toast.makeText(getActivity(), R.string.msn_save_contact, Toast.LENGTH_SHORT).show();
    }

    /**
     * Mensaje de actualización exitosa
     */
    private void showUpdatedMessage()
    {
        Toast.makeText(getActivity(), R.string.msn_update_contact, Toast.LENGTH_SHORT).show();
    }

    /**
     * Mensaje de borrado exitoso
     */
    private void showDeleteMessage()
    {
        Toast.makeText(getActivity(), R.string.msn_delete_contact, Toast.LENGTH_SHORT).show();
    }

    /**
     * Carga el listado de contactos
     */
    private void loadContacts()
    {
        new ContactLoadTask().execute();
    }

    /**
     * Clase que controla la carga de todos los contactos
     */
    private class ContactLoadTask extends AsyncTask<Void, Void, Cursor>
    {
        @Override
        protected Cursor doInBackground(Void... voids) {
            return _db.getAllContacts(); // Consulta todos los contactos
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            _contactAdaptador.swapCursor(cursor); // El adaptador de la lista pinta los datos en la lista
            //if (cursor != null && cursor.getCount() > 0) {
            //    _contactAdaptador.swapCursor(cursor);
            //} else {
                // Mostrar empty state
            //}
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        int _orientation = getActivity().getResources().getConfiguration().orientation;
        if( _orientation == Configuration.ORIENTATION_PORTRAIT )
        {
            super.onCreateOptionsMenu(menu, inflater);
            menu.clear();
            inflater.inflate(R.menu.menu_main, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ( item.getItemId() )
        {
            case R.id.action_import: // Importar los contactos
                Log.i(TAG,"ACTION IMPORT");
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) || Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY))
                {
                    if( checkPermission() ) // Verifica que haya permisos de escritura en memo externa
                    {
                        if( Util.importContactsJSON(_db) ) // Verifica que la importación se realizco correctamente
                        {
                            loadContacts();
                            Toast.makeText(getActivity(), R.string.msn_import_ok, Toast.LENGTH_SHORT).show();
                        }else
                            Toast.makeText(getActivity(), R.string.msn_import_error, Toast.LENGTH_SHORT).show();
                    }
                } else Toast.makeText(getActivity(), R.string.msn_access_sd_error, Toast.LENGTH_SHORT).show();

                break;
            case R.id.action_export: // Exporta los contactos
                Log.i(TAG,"ACTION EXPORT");
                if ( Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) )
                {
                    if( checkPermission() ) // Verifica que haya permisos de escritura en memo externa
                    {
                        if( Util.exportContactsJSON(_db.getAllContacts()) ) // Verifica que la expertación se haya realizado correctamene
                            Toast.makeText(getActivity(), R.string.msn_export_ok, Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getActivity(), R.string.msn_export_error, Toast.LENGTH_SHORT).show();
                    }
                } else Toast.makeText(getActivity(), R.string.msn_access_sd_error, Toast.LENGTH_SHORT).show();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Verifica los permisos necesarios para la Exportación e Importación de contactos
     * @return
     */
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