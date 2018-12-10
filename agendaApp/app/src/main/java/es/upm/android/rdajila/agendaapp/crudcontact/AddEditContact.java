package es.upm.android.rdajila.agendaapp.crudcontact;

import android.app.Activity;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import es.upm.android.rdajila.agendaapp.ListContactFragment;
import es.upm.android.rdajila.agendaapp.R;
import es.upm.android.rdajila.agendaapp.util.Constant;

public class AddEditContact extends AppCompatActivity
{
    private static final String TAG = AddEditContact.class.getSimpleName();

    private Toolbar _toolbarApp;
    private String _idContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_contact);

        _toolbarApp = (Toolbar) findViewById(R.id._toolbarApp);
        setSupportActionBar(_toolbarApp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        _idContact = getIntent().getStringExtra(Constant._KEY_ID_CONTACT);

        setTitle( (_idContact == null)?R.string.title_add_contact:R.string.title_edit_contact );

        AddEditContactFragment _fragment = new AddEditContactFragment();
        Bundle args = new Bundle();
        args.putString(Constant._KEY_ID_CONTACT, _idContact);
        _fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .add(R.id._contenidoLayout, _fragment)
                .commit();
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }
}