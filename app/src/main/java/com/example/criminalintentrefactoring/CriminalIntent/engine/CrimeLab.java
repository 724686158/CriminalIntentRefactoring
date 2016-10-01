package com.example.criminalintentrefactoring.CriminalIntent.engine;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.example.criminalintentrefactoring.CriminalIntent.db.CrimeBaseHelper;
import com.example.criminalintentrefactoring.CriminalIntent.db.CrimeCursorWrapper;
import com.example.criminalintentrefactoring.CriminalIntent.db.CrimeDbSchema;
import com.example.criminalintentrefactoring.CriminalIntent.db.CrimeDbSchema.CrimeTable;
import com.example.criminalintentrefactoring.CriminalIntent.entity.Crime;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by 离子态狍子 on 2016/9/29.
 */

public class CrimeLab {
    private static CrimeLab ourInstance;
    //private List<Crime> mCrimeList;
    private Context mContext;
    private SQLiteDatabase mDatabase;


    private CrimeLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();

        //mCrimeList = new ArrayList<Crime>();
    }



    public static CrimeLab get(Context context)
    {
        if (ourInstance == null)
        {
            ourInstance = new CrimeLab(context);
        }
        else {
            ;
        }
        return ourInstance;
    }


    private static ContentValues getContentValues(Crime crime)
    {
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE, crime.getTitle().toString());
        values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
        values.put(CrimeTable.Cols.SUSPECT, crime.getSuspect());
        return values;
    }
    public void addCrime(Crime crime)
    {
        ContentValues values = getContentValues(crime);
        mDatabase.insert(CrimeTable.NAME, null, values);
    }

    public void updateCrime(Crime crime){
        String uuidString = crime.getId().toString();
        ContentValues contentValues = getContentValues(crime);
        mDatabase.update(CrimeTable.NAME, contentValues, CrimeTable.Cols.UUID + " = ?", new String[]{uuidString});

    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArge){
        Cursor cursor = mDatabase.query(
                CrimeTable.NAME,
                null, //Columns - null selects all colums
                whereClause,
                whereArge,
                null,
                null,
                null
        );
        return new CrimeCursorWrapper(cursor);

    }
    public Crime getCrime(UUID id){
        CrimeCursorWrapper cursor = queryCrimes(
                CrimeTable.Cols.UUID + " = ?", new String[] {id.toString()}
        );
        try{
            if (cursor.getCount() == 0)
            {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }
    }

    public List<Crime> getCrimeList() {
        List<Crime> crimes = new ArrayList<>();
        CrimeCursorWrapper cursorWrapper = queryCrimes(null, null);

        try{
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast())
            {
                crimes.add(cursorWrapper.getCrime());
                cursorWrapper.moveToNext();
            }
        } finally {
            cursorWrapper.close();
        }
        return crimes;
    }

    public File getPhotoFile(Crime crime)
    {
        File externalFilesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (externalFilesDir == null)
        {
            return null;
        }
        return new File(externalFilesDir, crime.getPhotoFilename());
    }
}
