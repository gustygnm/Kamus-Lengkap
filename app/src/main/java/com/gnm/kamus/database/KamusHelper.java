package com.gnm.kamus.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.gnm.kamus.model.Kamus;

import java.util.ArrayList;

public class KamusHelper {

    private static String ENGLISH = DatabaseHelper.TABLE_ENGLISH;
    private static String INDONESIA = DatabaseHelper.TABLE_INDONESIA;

    private Context context;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    public KamusHelper(Context context) {
        this.context = context;
    }

    public KamusHelper open() throws SQLException {
        databaseHelper = new DatabaseHelper(context);
        database = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        databaseHelper.close();
    }

    public Cursor searchQueryByName(String query, boolean isEnglish) {
        String DATABASE_TABLE = isEnglish ? ENGLISH : INDONESIA;
        return database.rawQuery("SELECT * FROM " + DATABASE_TABLE +
                " WHERE " + DatabaseHelper.FIELD_WORD + " LIKE '%" + query.trim() + "%'", null);
    }

    public ArrayList<Kamus> getDataByName(String search, boolean isEnglish) {
        Kamus kamus;

        ArrayList<Kamus> arrayList = new ArrayList<>();
        Cursor cursor = searchQueryByName(search, isEnglish);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                kamus = new Kamus();
                kamus.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_ID)));
                kamus.setKata(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_WORD)));
                kamus.setTerjemahan(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_TRANSLATE)));
                arrayList.add(kamus);

                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public Cursor queryAllData(boolean isEnglish) {
        String DATABASE_TABLE = isEnglish ? ENGLISH : INDONESIA;
        return database.rawQuery("SELECT * FROM " + DATABASE_TABLE + " ORDER BY " + DatabaseHelper.FIELD_ID + " ASC", null);
    }

    public ArrayList<Kamus> getAllData(boolean isEnglish) {
        Kamus kamus;

        ArrayList<Kamus> arrayList = new ArrayList<>();
        Cursor cursor = queryAllData(isEnglish);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                kamus = new Kamus();
                kamus.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_ID)));
                kamus.setKata(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_WORD)));
                kamus.setTerjemahan(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_TRANSLATE)));
                arrayList.add(kamus);

                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public void beginTransaction() {
        database.beginTransaction();
    }

    public void setTransactionSuccess() {
        database.setTransactionSuccessful();
    }

    public void endTransaction() {
        database.endTransaction();
    }

    public long insert(Kamus kamus, boolean isEnglish) {
        String DATABASE_TABLE = isEnglish ? ENGLISH : INDONESIA;
        ContentValues initialValues = new ContentValues();
        initialValues.put(DatabaseHelper.FIELD_WORD, kamus.getKata());
        initialValues.put(DatabaseHelper.FIELD_TRANSLATE, kamus.getTerjemahan());
        return database.insert(DATABASE_TABLE, null, initialValues);
    }

    public void insertTransaction(ArrayList<Kamus> kamus, boolean isEnglish) {
        beginTransaction();

        String DATABASE_TABLE = isEnglish ? ENGLISH : INDONESIA;
        String sql = "INSERT INTO " + DATABASE_TABLE + " (" +
                DatabaseHelper.FIELD_WORD + ", " +
                DatabaseHelper.FIELD_TRANSLATE + ") VALUES (?, ?)";

        SQLiteStatement stmt = database.compileStatement(sql);
        for (int i = 0; i < kamus.size(); i++) {
            stmt.bindString(1, kamus.get(i).getKata());
            stmt.bindString(2, kamus.get(i).getTerjemahan());
            stmt.execute();
            stmt.clearBindings();
        }

        setTransactionSuccess();
        endTransaction();
    }

    public void update(Kamus kamus, boolean isEnglish) {
        String DATABASE_TABLE = isEnglish ? ENGLISH : INDONESIA;
        ContentValues args = new ContentValues();
        args.put(DatabaseHelper.FIELD_WORD, kamus.getKata());
        args.put(DatabaseHelper.FIELD_TRANSLATE, kamus.getTerjemahan());
        database.update(DATABASE_TABLE, args, DatabaseHelper.FIELD_ID + "= '" + kamus.getId() + "'", null);
    }

    public void delete(int id, boolean isEnglish) {
        String DATABASE_TABLE = isEnglish ? ENGLISH : INDONESIA;
        database.delete(DATABASE_TABLE, DatabaseHelper.FIELD_ID + " = '" + id + "'", null);
    }
}
