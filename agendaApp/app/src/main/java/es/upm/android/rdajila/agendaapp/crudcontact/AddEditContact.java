package es.upm.android.rdajila.agendaapp.crudcontact;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import es.upm.android.rdajila.agendaapp.ListContactFragment;
import es.upm.android.rdajila.agendaapp.R;
import es.upm.android.rdajila.agendaapp.util.Constant;

public class AddEditContact extends AppCompatActivity
{
    private Toolbar _toolbarApp;
    private String _idContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_contact);

        _toolbarApp = (Toolbar) findViewById(R.id._toolbarApp);
        _toolbarApp.setTitle(R.string.app_name);
        //_toolbarApp.inflateMenu(R.menu.menu_main);
        setSupportActionBar(_toolbarApp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        _idContact = getIntent().getStringExtra(Constant._KEY_ID_CONTACT);

        AddEditContactFragment _fragment = new AddEditContactFragment();
        Bundle args = new Bundle();
        args.putString(Constant._KEY_ID_CONTACT, _idContact);
        _fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .add(R.id._contentAddEditLayout, _fragment)
                .commit();
    }
}