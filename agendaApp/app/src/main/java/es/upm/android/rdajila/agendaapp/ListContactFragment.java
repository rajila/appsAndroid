package es.upm.android.rdajila.agendaapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import es.upm.android.rdajila.agendaapp.crudcontact.AddEditContactFragment;
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
    private ScheduleDbHelper _db;
    private ListView _listContact;
    private ContactCursorAdapter _contactAdaptador;

    private FloatingActionButton _btnAdd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //getActivity().deleteDatabase(ContactContract._TABLE_NAME);
        _db = new ScheduleDbHelper(getActivity());
        // Inflate the layout for this fragment
        View _viewLayout = inflater.inflate(R.layout.fragment_list_contact, container, false);
        _listContact = (ListView) _viewLayout.findViewById(R.id._listData);
        //_contactAdaptador = new ContactCursorAdapter(getActivity(),null);
        _contactAdaptador = new ContactCursorAdapter(getActivity(), _db.getAllContacts());
        _listContact.setAdapter(_contactAdaptador);

        _btnAdd = (FloatingActionButton)getActivity().findViewById(R.id._btnAdd);
        _btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { actionAddContact(); }
        });

        //loadContacts();

        return _viewLayout;
    }

    private void actionAddContact()
    {
        //Intent _intentView = new Intent(getActivity(),AddEditContact.class);
        //startActivityForResult(_intentView, Constant._REQUEST_ADD_CONTACT);
        AddEditContactFragment _fragment = new AddEditContactFragment();
        Bundle args = new Bundle();
        args.putString(Constant._KEY_ID_CONTACT, null);
        _fragment.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id._contenidoLayout, _fragment)
                .commit();
    }

    /*private void loadContacts()
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
    }*/
}