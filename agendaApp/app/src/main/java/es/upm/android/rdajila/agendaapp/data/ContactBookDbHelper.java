package es.upm.android.rdajila.agendaapp.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import es.upm.android.rdajila.agendaapp.contract.ContactContract;
import es.upm.android.rdajila.agendaapp.entity.Contact;

public class ContactBookDbHelper extends SQLiteOpenHelper
{
    private static final int _VERSION = 1;
    private static final String _NAME = "ContactBookDb.db";

    public ContactBookDbHelper(Context context)
    {
        super(context, _NAME, null, _VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(
                "CREATE TABLE " + ContactContract._TABLE_NAME + " ("
                + ContactContract._ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
                + ContactContract._NAME + " VARCHAR(100) NOT NULL,"
                + ContactContract._ADRESS + " VARCHAR(100) NOT NULL,"
                + ContactContract._MOBILE + " VARCHAR(100) NOT NULL,"
                + ContactContract._PHONE + " VARCHAR(100),"
                + ContactContract._EMAIL + " VARCHAR(100) NOT NULL,"
                + ContactContract._COLOR + " INTEGER)"
        );

        // Insertar valores de inicio
        //initData(db);
    }

    private void initData(SQLiteDatabase db)
    {
        insertContactInit(db, new Contact("Ronald","Madrid","0989322323","0989322323","rdajila@gmail.com"));
        insertContactInit(db, new Contact("Ronan","Leganes","0989322323","0989322323","ronan@gmail.com"));
        insertContactInit(db, new Contact("Mafer","Guayaquil","0989322323","0989322323","mafer@gmail.com"));
        insertContactInit(db, new Contact("Mariela","Guayaquil","0989322323","0989322323","mariela@gmail.com"));
        insertContactInit(db, new Contact("Daniel","Guayaquil","0989322323","0989322323","daniel@gmail.com"));
        insertContactInit(db, new Contact("Cindy","Guayaquil","0989322323","0989322323","cindy@gmail.com"));
    }

    private long insertContactInit(SQLiteDatabase db, Contact contacto)
    {
        long _id = db.insert(ContactContract._TABLE_NAME,null, contacto.createContentValue());
        db.close();
        return _id;
    }

    public long insertContact(Contact contacto)
    {
        SQLiteDatabase _db = getReadableDatabase();
        long _id = _db.insert(ContactContract._TABLE_NAME,null, contacto.createContentValue());
        _db.close();
        return _id;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + ContactContract._TABLE_NAME);
        onCreate(db);
    }

    public Cursor getAllContacts()
    {
        return getReadableDatabase()
                .query(
                        ContactContract._TABLE_NAME,
                        ContactContract._GET_ALL_DATA,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
    }

    public Cursor getContactById(String idContact)
    {
        Cursor _cursorDB = getReadableDatabase().query(
                ContactContract._TABLE_NAME,
                ContactContract._GET_ALL_DATA,
                ContactContract._ID + " LIKE ?",
                new String[]{ idContact },
                null,
                null,
                null);
        return _cursorDB;
    }

    public int updateContact(Contact data, String idContact)
    {
        return getWritableDatabase().update(
                ContactContract._TABLE_NAME,
                data.createContentValue(),
                ContactContract._ID + " LIKE ?",
                new String[]{idContact}
        );
    }

    public int deleteContact(String idContact)
    {
        return getWritableDatabase().delete(
                ContactContract._TABLE_NAME,
                ContactContract._ID + " LIKE ?",
                new String[]{idContact});
    }
}