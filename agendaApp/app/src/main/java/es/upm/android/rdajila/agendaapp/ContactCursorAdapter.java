package es.upm.android.rdajila.agendaapp;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

import es.upm.android.rdajila.agendaapp.contract.ContactContract;

/**
 * Clase Adaptador que gestiona la carga de contanctos en el listado
 */
public class ContactCursorAdapter extends CursorAdapter
{
    public ContactCursorAdapter(Context context, Cursor c)
    {
        super(context, c, false);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.list_item_contact, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        //Random random = new Random();
        TextView _name = (TextView) view.findViewById(R.id._nameList);
        _name.setText(cursor.getString(cursor.getColumnIndex(ContactContract._NAME)));
        ImageView _avatar = (ImageView) view.findViewById(R.id.iv_avatar);
        //int color = Color.argb(255, random.nextInt(250), random.nextInt(250), random.nextInt(250));
        _avatar.setColorFilter(cursor.getInt(cursor.getColumnIndex(ContactContract._COLOR)));
    }
}