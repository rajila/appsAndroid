package es.upm.android.rdajila.agendaapp.crudcontact;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import es.upm.android.rdajila.agendaapp.ListContactFragment;
import es.upm.android.rdajila.agendaapp.MainActivity;
import es.upm.android.rdajila.agendaapp.R;
import es.upm.android.rdajila.agendaapp.data.ContactBookDbHelper;
import es.upm.android.rdajila.agendaapp.entity.Contact;
import es.upm.android.rdajila.agendaapp.util.AppBarStateChangeListener;
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
    private String _nameDB = "";
    private int _colorDB = 000000;

    private Toolbar _toolbarApp;
    private TextView _valueAdress;
    private TextView _valueMobile;
    private TextView _valuePhone;
    private TextView _valueEmail;
    private TextView _valueName;

    private TextView _lblName;
    private TextView _lblAdress;
    private TextView _lblMobile;
    private TextView _lblPhone;
    private TextView _lblEmail;

    private FloatingActionButton _btnCall;
    private FloatingActionButton _btnEditL;
    private FloatingActionButton _btnDeleteL;

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
        int _orientation = getActivity().getResources().getConfiguration().orientation;
        _db = MainActivity._dbMain;
        // Inflate the layout for this fragment
        View _viewLayout = inflater.inflate(R.layout.fragment_detail_contact, container, false);

        _toolbarApp = (Toolbar) getActivity().findViewById(R.id._toolbarApp);
        if( _orientation == Configuration.ORIENTATION_PORTRAIT )
        {
            _toolbarApp.setTitle(R.string.title_detail_contact);
            ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        _valueAdress = (TextView) _viewLayout.findViewById(R.id._valueAdress);
        _valueMobile = (TextView) _viewLayout.findViewById(R.id._valueMobile);
        _valuePhone = (TextView) _viewLayout.findViewById(R.id._valuePhone);
        _valueEmail = (TextView) _viewLayout.findViewById(R.id._valueEmail);
        _valueName = (TextView) _viewLayout.findViewById(R.id._valueName);

        _lblName = (TextView) _viewLayout.findViewById(R.id._lblName);
        _lblAdress = (TextView) _viewLayout.findViewById(R.id._lblAdress);
        _lblMobile = (TextView) _viewLayout.findViewById(R.id._lblMobile);
        _lblPhone = (TextView) _viewLayout.findViewById(R.id._lblPhone);
        _lblEmail = (TextView) _viewLayout.findViewById(R.id._lblEmail);

        _btnCall = (FloatingActionButton)_viewLayout.findViewById(R.id._btnCall);
        _btnEditL = (FloatingActionButton)_viewLayout.findViewById(R.id._btnEditL);
        _btnDeleteL = (FloatingActionButton)_viewLayout.findViewById(R.id._btnDeleteL);
        if( _orientation == Configuration.ORIENTATION_PORTRAIT )
        {
            _btnEditL.hide();
            _btnDeleteL.hide();
        }
        if( _orientation == Configuration.ORIENTATION_LANDSCAPE )
        {
            _btnEditL.show();
            _btnDeleteL.show();
        }
        Drawable buttonDrawable = _btnCall.getBackground();
        buttonDrawable = DrawableCompat.wrap(buttonDrawable);
        DrawableCompat.setTint(buttonDrawable, Color.BLACK);
        _btnCall.setBackground(buttonDrawable);
        _btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { actionCall(); }
        });

        Drawable buttonDrawableEditL = _btnEditL.getBackground();
        buttonDrawableEditL = DrawableCompat.wrap(buttonDrawableEditL);
        DrawableCompat.setTint(buttonDrawableEditL, Color.BLACK);
        _btnEditL.setBackground(buttonDrawableEditL);
        _btnEditL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { actionEditContact();}
        });

        Drawable buttonDrawableDeleteL = _btnEditL.getBackground();
        buttonDrawableDeleteL = DrawableCompat.wrap(buttonDrawableDeleteL);
        DrawableCompat.setTint(buttonDrawableDeleteL, Color.BLACK);
        _btnDeleteL.setBackground(buttonDrawableDeleteL);
        _btnDeleteL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { new DeleteContactTask().execute(); }
        });

        // Carga los datos del contacto
        loadDetailContact();

        return _viewLayout;
    }

    /**
     * Función que ejecuta la tarea que carga los datos del contacto
     */
    private void loadDetailContact()
    {
        new GetContactByIdTask().execute();
    }

    /**
     * Actualiza el valor de los campos del contacto en la pantalla
     * @param data
     */
    private void showDetailContact(Contact data)
    {
        _idContact = Long.toString(data.get_id());
        _valueName.setText(data.get_name());
        _valueAdress.setText(data.get_adress());
        _valueMobile.setText(data.get_mobile());
        _valuePhone.setText(data.get_phone());
        _valueEmail.setText(data.get_email());

        _lblName.setTextColor(data.get_color());
        _lblAdress.setTextColor(data.get_color());
        _lblMobile.setTextColor(data.get_color());
        _lblPhone.setTextColor(data.get_color());
        _lblEmail.setTextColor(data.get_color());

        Drawable buttonDrawable = _btnCall.getBackground();
        buttonDrawable = DrawableCompat.wrap(buttonDrawable);
        DrawableCompat.setTint(buttonDrawable, data.get_color());
        _btnCall.setBackground(buttonDrawable);

        Drawable buttonDrawableL = _btnEditL.getBackground();
        buttonDrawableL = DrawableCompat.wrap(buttonDrawableL);
        DrawableCompat.setTint(buttonDrawableL, data.get_color());
        _btnEditL.setBackground(buttonDrawableL);

        Drawable buttonDrawableDL = _btnDeleteL.getBackground();
        buttonDrawableDL = DrawableCompat.wrap(buttonDrawableDL);
        DrawableCompat.setTint(buttonDrawableDL, data.get_color());
        _btnDeleteL.setBackground(buttonDrawableDL);
        _movilDB = data.get_mobile();
        _nameDB = data.get_name();
        _colorDB = data.get_color();
    }

    /**
     * Función que permite cargar la pantalla de Llamar
     */
    private void actionCall()
    {
        Intent i = new Intent(Intent.ACTION_DIAL);
        i.setData(Uri.parse("tel:" + _movilDB));
        startActivity(i);
    }

    /**
     * Función que levanta la pantalla para editar contacto
     */
    private void actionEditContact()
    {
        int _orientation = getActivity().getResources().getConfiguration().orientation;
        AddEditContactFragment _fragment = new AddEditContactFragment();
        Bundle args = new Bundle();
        args.putString(Constant._KEY_ID_CONTACT, _idContact);
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

    private void showListContactScreen(boolean requery)
    {
        int _orientation = getActivity().getResources().getConfiguration().orientation;
        if ( !requery )
            showDeleteContactDBError();
        else{
            showDeleteMessage();
            _toolbarApp.setTitle(R.string.app_name);
            ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            if( _orientation == Configuration.ORIENTATION_PORTRAIT ) getActivity().onBackPressed();
            if( _orientation == Configuration.ORIENTATION_LANDSCAPE )
            {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id._frgList, new ListContactFragment())
                        .commit();

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id._frgDynamic, new DetailContactFragment())
                        .commit();
            }
        }
    }

    /**
     * Mensaje de borrado exitoso
     */
    private void showDeleteMessage()
    {
        Toast.makeText(getActivity(), R.string.msn_delete_contact, Toast.LENGTH_SHORT).show();
    }

    /**
     * Mensaje OK de actualización de contacto
     */
    private void showUpdatedMessage()
    {
        Toast.makeText(getActivity(), R.string.msn_update_contact, Toast.LENGTH_SHORT).show();
    }

    /**
     * Mensaje de ERROR al cargar los datos
     */
    private void showLoadContactDBError()
    {
        Toast.makeText(getActivity(), R.string.msn_error_load_data, Toast.LENGTH_SHORT).show();
    }

    /**
     * Mensaje de ERROR  al eliminar un contacto
     */
    private void showDeleteContactDBError()
    {
        Toast.makeText(getActivity(), R.string.msn_error_delete_data, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        int _orientation = getActivity().getResources().getConfiguration().orientation;
        if( _orientation == Configuration.ORIENTATION_PORTRAIT )
        {
            menu.clear();
            super.onCreateOptionsMenu(menu, inflater);
            inflater.inflate(R.menu.menu_detail, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id._menuEdit: // Editar
                actionEditContact();
                break;
            case R.id._menuDelete: // Eliminar
                if(_idContact != null ) new DeleteContactTask().execute();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Clase gestiona la obtencion de los datos del contacto de DB
     */
    private class GetContactByIdTask extends AsyncTask<Void, Void, Cursor>
    {
        @Override
        protected Cursor doInBackground(Void... voids)
        {
            if(_idContact != null )
                return _db.getContactById(_idContact);
            else{
                return _db.getFirstContact();
            }
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor != null && cursor.moveToLast()) {
                showDetailContact(new Contact(cursor));
            } else {
                _btnEditL.hide();
                _btnDeleteL.hide();
                _btnCall.hide();
            }
        }
    }

    /**
     * Clase que gestiona la eliminación del contacto de DB
     */
    private class DeleteContactTask extends AsyncTask<Void, Void, Integer>
    {
        @Override
        protected Integer doInBackground(Void... voids) {
            return _db.deleteContact(_idContact);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            showListContactScreen(integer > 0 );
        }

    }
}