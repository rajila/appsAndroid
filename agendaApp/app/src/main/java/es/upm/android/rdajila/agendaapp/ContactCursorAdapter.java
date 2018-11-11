package es.upm.android.rdajila.agendaapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import es.upm.android.rdajila.agendaapp.contract.ContactContract;

public class ContactCursorAdapter extends CursorAdapter
{
    public ContactCursorAdapter(Context context, Cursor c)
    {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.list_item_contact, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        TextView _name = (TextView) view.findViewById(R.id._nameList);
        _name.setText(cursor.getString(cursor.getColumnIndex(ContactContract._NAME)));
    }
}