package es.upm.android.calculadora;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author Ronald Daniel Ajila
 */
public class MainActivity extends AppCompatActivity {

    private TextView _txtResultado; // Componente en donde se muestra el resultado de la calculadora

    private String _operacionActual = "0"; // Indica la operación basica actual que debe realizarse
    private Double _operandoOne = 0.0; // Guarda operando 1
    private Double _operandoTwo = null; // Guarda el operando 2
    private boolean _banderaEjecucion = false; // Indica si ya se ejecuto la operacion basica

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init(savedInstanceState);
    }

    /**
     * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
     */
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        // Guardamos el estado de la Actividad (sus componentes)
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("_operacionActual",_operacionActual);
        savedInstanceState.putDouble("_operandoOne",_operandoOne);
        savedInstanceState.putString("_operandoTwo",(_operandoTwo == null)?"null":_operandoTwo+"");
        savedInstanceState.putBoolean("_banderaEjecucion",_banderaEjecucion);
        savedInstanceState.putString("_txtResultado", _txtResultado.getText().toString());
    }

    /**
     * Función que iniciliza los componentes y actualiza las variables de estado
     * @param savedInstanceState
     */
    public void init( Bundle savedInstanceState )
    {
        _txtResultado = (TextView)findViewById(R.id.txtResultado);

        // Se recuepera las variables de estado
        if( savedInstanceState != null )
        {
            _operacionActual = savedInstanceState.getString("_operacionActual");
            _operandoOne = savedInstanceState.getDouble("_operandoOne");
            _operandoTwo = (savedInstanceState.getString("_operandoTwo").compareTo("null")==0?null:Double.parseDouble(savedInstanceState.getString("_operandoTwo")));
            _banderaEjecucion = savedInstanceState.getBoolean("_banderaEjecucion");
            _txtResultado.setText(savedInstanceState.getString("_txtResultado"));
        }
    }

    /**
     * Funcion que captura el evento de los digitos
     * @param view
     */
    public void procesarBoton(View view)
    {
        Log.d("Digito", ((Button) view).getText().toString());
        _txtResultado.setText((_txtResultado.getText().toString().compareTo("0") == 0 || _banderaEjecucion)? ((Button) view).getText().toString(): _txtResultado.getText().toString()+""+((Button) view).getText().toString());
        if( _operacionActual.compareTo("0") != 0 ) _operandoTwo = Double.parseDouble(_txtResultado.getText().toString());
        else _operandoTwo = null;
        _banderaEjecucion = false;
    }

    /**
     * Funcion que captura el eveneto punto
     * @param view
     */
    public void actionPunto(View view)
    {
        // Verificamos si el numero entero es un decimal
        if( _txtResultado.getText().toString().compareTo("0") == 0 ) {
            if(!_txtResultado.getText().toString().contains(".")) _txtResultado.setText("0.");
        }else{
            if(!_txtResultado.getText().toString().contains(".")) _txtResultado.setText(_txtResultado.getText().toString()+".");
        }
    }

    /**
     * Función que captura el evento del boton C, en donde resetea los parametros al inicio
     * @param view
     */
    public void actionResetC(View view)
    {
        _txtResultado.setText("0");
        _operacionActual = "0";
        _operandoTwo = null;
    }

    /**
     * Cambia el valor del operando actual a CERO
     * @param view
     */
    public void actionResetCE(View view)
    {
        _txtResultado.setText("0");
        _operandoTwo = null;
    }

    /**
     * Funcion que captura el evento de los operadores basicos +, -, *. /
     * @param view
     */
    public void actionOperacionBasica(View view)
    {
        String _operador = ((Button) view).getText().toString();
        if( _operacionActual.compareTo("0") != 0 && _operandoTwo != null ) ejecutarOperacionBasica(_operacionActual);
        _operacionActual = _operador;
        _operandoOne = Double.parseDouble(_txtResultado.getText().toString());
        _banderaEjecucion = true;
        _operandoTwo = null;
    }

    /**
     * Función que realiza la operación de los digitos
     * @param view
     */
    public void actionIgual(View view)
    {
        if( _operacionActual.compareTo("0") != 0 && _operandoTwo != null )
        {
            ejecutarOperacionBasica(_operacionActual);
            _operacionActual = "0";
            _banderaEjecucion = true;
        }
    }

    /**
     * Cambia el signo del nuúmero
     * @param view
     */
    public void cambiarSigno(View view)
    {
        if( _txtResultado.getText().toString().compareTo("0") != 0 )
        {
            if( _txtResultado.getText().toString().contains(".") ) _txtResultado.setText((Double.parseDouble(_txtResultado.getText().toString())*-1)+"");
            else _txtResultado.setText((Integer.parseInt(_txtResultado.getText().toString())*-1)+"");
        }
    }

    /**
     * Funcion que eleva al cuadrado el digito actual
     * @param view
     */
    public void actionCuadrado(View view)
    {
        operacionEspecial( Math.pow(Double.parseDouble(_txtResultado.getText().toString()),2) );
        actualizarVar();
    }

    /**
     * Función que calcula la Raiz cuadrada del numero
     * @param view
     */
    public void actionRaiz(View view)
    {
        operacionEspecial( Math.sqrt(Double.parseDouble(_txtResultado.getText().toString())) );
        actualizarVar();
    }

    /**
     * Función que calcula el Seno
     * @param view
     */
    public void actionSin(View view)
    {
        operacionEspecial( Math.sin(Double.parseDouble(_txtResultado.getText().toString())) );
        actualizarVar();
    }

    /**
     * Funcion que calcula el Coseno de un numero
     * @param view
     */
    public void actionCos(View view)
    {
        operacionEspecial( Math.cos(Double.parseDouble(_txtResultado.getText().toString())) );
        actualizarVar();
    }

    /**
     * Funcion que calcula la Tangente de un numero
     * @param view
     */
    public void actionTan(View view)
    {
        operacionEspecial( Math.tan(Double.parseDouble(_txtResultado.getText().toString())) );
        actualizarVar();
    }

    /**
     * Muestra el numero PI en pantalla
     * @param view
     */
    public void actionPi(View view)
    {
        _txtResultado.setText(Math.PI+"");
        actualizarVar();
    }

    /**
     * Función que calcula la inversa de un numero
     * @param view
     */
    public void actionInversa(View view)
    {
        operacionEspecial( (1/Double.parseDouble(_txtResultado.getText().toString())) );
        actualizarVar();
    }

    private void operacionEspecial(Double _tmp)
    {
        if( _tmp == (int)(_tmp*1) ) _txtResultado.setText((int)(_tmp*1)+"");
        else _txtResultado.setText(_tmp+"");
    }

    /**
     * Función que elimina los numeros de derecha a izquierda
     * @param view
     */
    public void actionDel(View view)
    {
        char _buffer[] =  _txtResultado.getText().toString().toCharArray();
        String _valTmp = "";
        for(int i=0; i<_buffer.length-1;i++) _valTmp += _buffer[i];
        _txtResultado.setText((_buffer.length == 1 || _buffer.length == 0)? "0" : _valTmp);
    }

    /**
     * Funcion que cierra la aplicacion
     * @param view
     */
    public void actionOff(View view)
    {
        finish();
    }

    /**
     * Funcion que calcula el porcentaje para tres operaciones basica SUMA, RESTA, MULTIPLICACION
     * @param view
     */
    public void actionPorcentaje(View view)
    {
        Double _resultado = 0.0;
        if( _operacionActual.compareTo("0") != 0 && _operandoTwo != null )
        {
            switch ( _operacionActual )
            {
                case "+":
                    _resultado = _operandoOne + (_operandoOne * _operandoTwo)/100;
                    _txtResultado.setText( (_resultado == (int)(_resultado*1))?(int)(_resultado*1)+"":_resultado+"" );
                    break;
                case "-":
                    _resultado = _operandoOne - (_operandoOne * _operandoTwo)/100;
                    _txtResultado.setText( (_resultado == (int)(_resultado*1))?(int)(_resultado*1)+"":_resultado+"" );
                    break;
                case "*":
                    _resultado = (_operandoOne * _operandoTwo)/100;
                    _txtResultado.setText( (_resultado == (int)(_resultado*1))?(int)(_resultado*1)+"":_resultado+"" );
                    break;
                default:
                    break;
            }
            _operacionActual = "0";
            _banderaEjecucion = true;
        }
    }

    /**
     * Función que realiza el calculo de todas las operaciones basicas: SUMA, RESTA, MULTIPLICACION, DIVISION
     * @param operacion
     */
    private void ejecutarOperacionBasica(String operacion)
    {
        Double _resultado = 0.0;
        switch ( operacion )
        {
            case "+":
                Log.d("Operacion: ", "Suma");
                _resultado = _operandoOne + _operandoTwo;
                _txtResultado.setText( (_resultado == (int)(_resultado*1))?(int)(_resultado*1)+"":_resultado+"" );
                break;
            case "-":
                Log.d("Operacion: ", "Resta");
                _resultado = _operandoOne - _operandoTwo;
                _txtResultado.setText( (_resultado == (int)(_resultado*1))?(int)(_resultado*1)+"":_resultado+"" );
                break;
            case "*":
                Log.d("Operacion: ", "Multiplicacion");
                _resultado = _operandoOne * _operandoTwo;
                _txtResultado.setText( (_resultado == (int)(_resultado*1))?(int)(_resultado*1)+"":_resultado+"" );
                break;
            case "/":
                Log.d("Operacion: ", "Division");
                _resultado = _operandoOne / _operandoTwo;
                _txtResultado.setText( (_resultado == (int)(_resultado*1))?(int)(_resultado*1)+"":_resultado+"" );
                break;
            default:
                break;
        }
    }

    /**
     * Funcion que actualiza los paramatros de estado: OPERACIONACTUAL y BANDERA DE EJECUCION
     */
    private void actualizarVar()
    {
        if( _operacionActual.compareTo("0") != 0 ) _operandoTwo = Double.parseDouble(_txtResultado.getText().toString());
        else _operandoTwo = null;
        _banderaEjecucion = true;
    }
}