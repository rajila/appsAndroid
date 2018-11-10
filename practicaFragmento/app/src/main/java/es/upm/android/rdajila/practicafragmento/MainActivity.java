package es.upm.android.rdajila.practicafragmento;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void actionMostrarFragmento(View view)
    {
        Intent _loadLayout = new Intent(getApplicationContext(),MostrarFragmento.class);
        startActivity(_loadLayout);
    }

    public void actionIntercambiarFragmento(View view)
    {
        Intent _loadLayout = new Intent(getApplicationContext(),IntercambiarFragmento.class);
        startActivity(_loadLayout);
    }
}