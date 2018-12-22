package es.upm.android.rdajila.agendaapp;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.RelativeLayout;
import android.support.v7.widget.Toolbar;

import es.upm.android.rdajila.agendaapp.crudcontact.DetailContactFragment;


public class MainActivity extends AppCompatActivity
{
    private static final String TAG = MainActivity.class.getSimpleName();

    private RelativeLayout _layoutContenido;
    private RelativeLayout _layoutInicio;
    private RelativeLayout _layoutPrincipal;
    private Toolbar _toolbarApp;

    private RelativeLayout _layoutFrgDynamic;

    Handler _handler = new Handler();

    /**
     * Hilo secuandario que gestiona la pantalla de inicio de la aplicación
     */
    Runnable _runnable = new Runnable() {
        @Override
        public void run()
        {
            try {
                _layoutContenido.setVisibility(View.VISIBLE);
                _layoutInicio.setVisibility(View.GONE);
                _layoutPrincipal.setBackgroundColor(getResources().getColor(R.color.colorBlanco));
                _toolbarApp.setVisibility(View.VISIBLE);
                showHorizontal();
                //getSupportFragmentManager().beginTransaction()
                //        .add(R.id._contenidoLayout, new ListContactFragment())
                //        .commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _layoutPrincipal = (RelativeLayout)findViewById(R.id._contenedorPrincipal);
        _layoutInicio = (RelativeLayout) findViewById(R.id._contenidoInicio);
        _layoutContenido = (RelativeLayout) findViewById(R.id._contenidoLayout);
        _layoutFrgDynamic = (RelativeLayout) findViewById(R.id._frgDynamic);
        _toolbarApp = (Toolbar) findViewById(R.id._toolbarApp);
        _toolbarApp.setTitle(R.string.app_name);
        setSupportActionBar(_toolbarApp);

        // Efecto de pantalla de inicio
        _handler.postDelayed(_runnable, 4000); //2000 is the timeout for the splash
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void showHorizontal()
    {
        int _orientation = this.getResources().getConfiguration().orientation;
        if( _orientation == Configuration.ORIENTATION_PORTRAIT )
            _layoutFrgDynamic.setVisibility(View.GONE);
        if( _orientation == Configuration.ORIENTATION_LANDSCAPE )
        {
            _layoutFrgDynamic.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id._frgDynamic, new DetailContactFragment())
                    .commit();
        }
    }
}