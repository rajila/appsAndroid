package es.upm.android.rdajila.agendaapp.crudcontact;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.upm.android.rdajila.agendaapp.R;
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
    private String _idContact;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            _idContact = getArguments().getString(Constant._KEY_ID_CONTACT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_edit_contact, container, false);
    }
}