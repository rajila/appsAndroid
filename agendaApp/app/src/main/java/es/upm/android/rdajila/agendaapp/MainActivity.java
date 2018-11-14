package es.upm.android.rdajila.agendaapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.RestrictTo;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity
{
    private RelativeLayout _layoutContenido;
    private RelativeLayout _layoutInicio;
    private RelativeLayout _layoutPrincipal;
    private Toolbar _toolbarApp;
    private FloatingActionButton _btnAdd;

    Handler _handler = new Handler();

    Runnable _runnable = new Runnable() {
        @Override
        public void run()
        {
            try {
                _layoutContenido.setVisibility(View.VISIBLE);
                _layoutInicio.setVisibility(View.GONE);
                _layoutPrincipal.setBackgroundColor(getResources().getColor(R.color.colorBlanco));
                _toolbarApp.setVisibility(View.VISIBLE);
                _btnAdd.show();

                getSupportFragmentManager().beginTransaction()
                        .add(R.id._contenidoLayout, new ListContactFragment())
                        .commit();
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
        _toolbarApp = (Toolbar) findViewById(R.id._toolbarApp);
        _btnAdd = (FloatingActionButton)findViewById(R.id._btnAdd);

        _toolbarApp.setTitle(R.string.app_name);
        _toolbarApp.inflateMenu(R.menu.menu_main);

        // Efecto de pantalla de inicio
        _handler.postDelayed(_runnable, 4000); //2000 is the timeout for the splash
    }
}