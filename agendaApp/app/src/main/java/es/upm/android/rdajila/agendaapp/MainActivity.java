package es.upm.android.rdajila.agendaapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity
{
    RelativeLayout _layoutContenido;
    RelativeLayout _layoutInicio;
    RelativeLayout _layoutPrincipal;
    Toolbar _toolbarApp;
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

        // Efecto de pantalla de inicio
        _handler.postDelayed(_runnable, 6000); //2000 is the timeout for the splash
    }
}