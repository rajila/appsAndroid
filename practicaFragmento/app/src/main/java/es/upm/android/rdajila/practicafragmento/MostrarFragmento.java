package es.upm.android.rdajila.practicafragmento;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MostrarFragmento extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_fragmento);

        loadFragmento();
    }

    private void loadFragmento()
    {
        FragmentManager _fragmentMan = getSupportFragmentManager();
        FragmentTransaction _fragmentTrans = _fragmentMan.beginTransaction();
        _fragmentTrans.add(R.id._layoutContenedor, new FragmentoOne());
        _fragmentTrans.commit();
    }
}