package com.niksoft.android.cheatdatabase.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Nikunj Kacha on 17/04/16.
 */
public class GVCCDB {

    public static final String DATABASE_NAME = "gtavicecity";
    public static final String DATA_TABLE = "gvc_cheat";
    public static final int DATABASE_VERSION = 1;

    public final static String DB_PATH = "/data/data/com.niksoft.android.cheatdatabase/databases/";

    public static final String KEY_ROWID = "_id";
    public static final String KEY_CHEAT_EFFECT = "key_effect";
    public static final String KEY_CHEAT_CODE = "key_cheat";

    private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + DATA_TABLE + " ("
            + KEY_ROWID + " INTEGER PRIMARY KEY, "
            + KEY_CHEAT_EFFECT + " text NOT NULL, "
            + KEY_CHEAT_CODE + " text NOT NULL);";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    public final Context mContext;

    public GVCCDB(Context ctx) {
        this.mContext = ctx;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        private boolean doesDatabaseExist() {
            File dBFile = new File(DB_PATH + DATABASE_NAME);
            return dBFile.exists();
        }

        private void copyDatabase(Context ctx) throws IOException {
            InputStream inputStream = ctx.getAssets().open(DATABASE_NAME);
            String outputFilePath = DB_PATH + DATABASE_NAME;
            OutputStream outputStream = new FileOutputStream(outputFilePath);

            //Utils.writeFromInputToOutput(inputStream, outputStream);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            outputStream.flush();
            inputStream.close();

        }

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }

    public GVCCDB open() throws SQLException {
        try {
            mDbHelper = new DatabaseHelper(mContext);
            if (!mDbHelper.doesDatabaseExist()) {
                mDb = mDbHelper.getWritableDatabase();
                mDb.close();
                mDbHelper.copyDatabase(mContext);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public Cursor getAllEffects() {
        return mDb.query(DATA_TABLE, new String[]{KEY_ROWID, KEY_CHEAT_EFFECT}, null, null, null, null, null);
    }

    public Cursor getMatchingEffects(String matchPattern) {
        String query = "SELECT * FROM " + DATA_TABLE + " WHERE " + KEY_CHEAT_EFFECT + " LIKE ?";

        Cursor cursor = mDb.rawQuery(query, new String[]{"%" + matchPattern + "%"});
        if (cursor != null) {
            cursor.moveToFirst();
            return cursor;
        }
        return null;
    }

}
