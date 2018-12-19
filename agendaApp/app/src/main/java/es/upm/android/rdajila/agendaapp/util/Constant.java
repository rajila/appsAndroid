package es.upm.android.rdajila.agendaapp.util;

import android.content.res.Resources;

/**
 * Clase que gestiona la configuración de todas los parametros que se usan en la aplicación
 */
public class Constant
{
    // Variables para conocer el tipo de acción que se esta realizando
    public static final int _REQUEST_ADD_CONTACT = 1; // Crea un nuevo contacto
    public static final int _REQUEST_EDIT_CONTACT = 2; // Edita un contacto
    public static final int _REQUEST_SHOW_CONTACT = 3; // Carga el detalle del contacto

    public static final String _KEY_ID_CONTACT = "_IDCONTACT"; // Nombre de la variable en donde se guarda el ID del contacto

    public static final int _PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1; // Verificar los permisos de escritura en la memo externa

    public static final String _NAME_FILE = "contacts.data"; // Nombre del archivo en donde se exporta e importa los contactos

    public static final String _STRING_EMPTY = ""; // Cadena vacía

    // Referencia en values: integer: max_character
    public static final int _MAX_CHARACTER = 50; // Valor maximo de ingreso de caracteres
}