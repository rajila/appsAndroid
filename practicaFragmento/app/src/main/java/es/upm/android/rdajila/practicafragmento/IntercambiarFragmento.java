package es.upm.android.rdajila.practicafragmento;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class IntercambiarFragmento extends AppCompatActivity
{
    private boolean _flagChangeFragment = false;
    final FragmentoOne _fragmentoOne = new FragmentoOne();
    final FragmentoTwo _fragmentoTWO = new FragmentoTwo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intercambiar_fragmento);
    }

    public void actionIntercambiar(View view)
    {
        FragmentTransaction _fragmentTran = getSupportFragmentManager().beginTransaction();
        if (_flagChangeFragment) _fragmentTran.replace(R.id._layoutContenedorI,_fragmentoOne);
        else _fragmentTran.replace(R.id._layoutContenedorI,_fragmentoTWO);
        _fragmentTran.addToBackStack(null);
        _fragmentTran.commit();
        _flagChangeFragment = !_flagChangeFragment;
    }
}