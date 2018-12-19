package es.upm.android.rdajila.agendaapp.crudcontact;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import es.upm.android.rdajila.agendaapp.R;
import es.upm.android.rdajila.agendaapp.util.Constant;

/**
 * Clase que gestiona la pantalla de Detalle del Contacto
 */
public class DetailContactActivity extends AppCompatActivity
{
    private static final String TAG = AddEditContact.class.getSimpleName();

    private Toolbar _toolbarApp;
    private String _idContact;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_contact);

        _toolbarApp = (Toolbar) findViewById(R.id._toolbarApp);
        _toolbarApp.setTitle(R.string.title_detail_contact);
        setSupportActionBar(_toolbarApp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        _idContact = getIntent().getStringExtra(Constant._KEY_ID_CONTACT);

        DetailContactFragment _fragment = new DetailContactFragment();
        Bundle args = new Bundle();
        args.putString(Constant._KEY_ID_CONTACT, _idContact);
        _fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .add(R.id._contenidoLayout, _fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }
}