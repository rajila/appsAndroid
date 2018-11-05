package es.upm.android.rdajila.agendaapp.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import es.upm.android.rdajila.agendaapp.contract.ContactContract;

public class ScheduleDbHelper extends SQLiteOpenHelper
{
    private static final int _VERSION = 1;
    private static final String _NAME = "ScheduleDb.db";

    public ScheduleDbHelper(Context context)
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
                + ContactContract._DIRECTION + " VARCHAR(100) NOT NULL,"
                + ContactContract._MOBILE + " VARCHAR(100) NOT NULL,"
                + ContactContract._PHONE + " VARCHAR(100),"
                + ContactContract._EMAIL + " VARCHAR(100) NOT NULL)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + ContactContract._TABLE_NAME);
        onCreate(db);
    }
}