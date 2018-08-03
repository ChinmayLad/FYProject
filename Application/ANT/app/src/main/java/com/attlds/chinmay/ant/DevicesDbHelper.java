package com.attlds.chinmay.ant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by chinmay on 17/3/16.
 */
public class DevicesDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "Device.db";
    private Context mContext;
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Devices.Attld.TABLE_NAME + " (" +
                    Devices.Attld.COLUMN_NAME_UU_ID + " TEXT PRIMARY KEY," +
                    Devices.Attld.COLUMN_NAME_DEV_NAME + TEXT_TYPE + COMMA_SEP +
                    Devices.Attld.COLUMN_NAME_USER + " INTEGER"+
            " )";

    private static final String SQL_CREATE_ENTRIES2 =
            "CREATE TABLE " + Devices.LostDevices.TABLE_NAME + " (" +
                    Devices.LostDevices.COLUMN_NAME_UU_ID + " TEXT PRIMARY KEY" +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Devices.Attld.TABLE_NAME;

    private static final String SQL_DELETE_ENTRIES2 =
            "DROP TABLE IF EXISTS " + Devices.LostDevices.TABLE_NAME;

    public DevicesDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = mContext.getFilesDir().getAbsolutePath().replace("files", "databases")
                    + File.separator + DATABASE_NAME;
            File file = new File(myPath);
            if (file.exists() && !file.isDirectory())
            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READONLY); // This is where the error apparently occurs
        } catch (SQLiteException e) {
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_DELETE_ENTRIES2);
        onCreate(db);
    }

    public long insertDevices( String uu_id,String dev_name,int user_id){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Devices.Attld.COLUMN_NAME_UU_ID, uu_id);
        values.put(Devices.Attld.COLUMN_NAME_DEV_NAME, dev_name);
        values.put(Devices.Attld.COLUMN_NAME_USER, user_id);
        if(!checkData(uu_id,1)) {
            return db.insert(Devices.Attld.TABLE_NAME, null, values);
        }
        return 0;
    }

    public boolean checkData(String data,int table){
        SQLiteDatabase db = getReadableDatabase();
        String TABLE_NAME;
        String COLUMN_NAME;
        if(table ==1){
            TABLE_NAME = Devices.Attld.TABLE_NAME;
            COLUMN_NAME = Devices.Attld.COLUMN_NAME_UU_ID;
        }
        else{
            TABLE_NAME = Devices.LostDevices.TABLE_NAME;
            COLUMN_NAME = Devices.LostDevices.COLUMN_NAME_UU_ID;
        }

        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+COLUMN_NAME+" = \""+data+"\"",null);

        return cursor.getCount()>0;
    }
    public long insertTagDevices(String uu_id){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Devices.LostDevices.COLUMN_NAME_UU_ID,uu_id);
        if(!checkData(uu_id, 2)) {
            return db.insert(Devices.LostDevices.TABLE_NAME, null, values);
        }
        return 0;
    }

    public long removeTagDevices(String uuid){
        SQLiteDatabase db = getWritableDatabase();
        if(checkData(uuid,2)) {
            return db.delete(Devices.LostDevices.TABLE_NAME, Devices.LostDevices.COLUMN_NAME_UU_ID + " = \"" + uuid + "\"", null);
        }
        return 0;
    }
    public boolean checkIfDeviceTagged(String uuid){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor curse = db.rawQuery("SELECT * FROM " + Devices.LostDevices.TABLE_NAME + " WHERE " + Devices.LostDevices.COLUMN_NAME_UU_ID + " = \"" + uuid+"\"", null);
        if(curse.getCount()==0){
            return false;
        }
        curse.close();
        return true;
    }

    public String getUUIDByName(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from " + Devices.Attld.TABLE_NAME + " where " + Devices.Attld.COLUMN_NAME_DEV_NAME + "=" + name, null);
        String uuid = res.getString(res.getColumnIndex(Devices.Attld.COLUMN_NAME_UU_ID));
        res.close();
        return uuid;
    }
    public Cursor getData(String uu_id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+Devices.Attld.TABLE_NAME+" where "+Devices.Attld.COLUMN_NAME_UU_ID +"="+uu_id, null );
        res.close();
        return res;
    }

    public int updateDevices( String uu_id,String dev_name,int user_id){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Devices.Attld.COLUMN_NAME_DEV_NAME, dev_name);
        values.put(Devices.Attld.COLUMN_NAME_USER, user_id);

        return db.update(Devices.Attld.TABLE_NAME,values,Devices.Attld.COLUMN_NAME_UU_ID + " = "+ uu_id, null);

    }

    public int deleteDevices(String uu_id){
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(Devices.Attld.TABLE_NAME, Devices.Attld.COLUMN_NAME_UU_ID + " = \"" + uu_id+"\"", null);
    }

    public ArrayList<ArrayList<String>> getAllDevices()
    {
        ArrayList<String> array_list = new ArrayList<>();
        ArrayList<String> UUID = new ArrayList<>();
        ArrayList<ArrayList<String>> as = new ArrayList<>();
        if(checkDataBase()) {
            //hp = new HashMap();
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res = db.rawQuery("SELECT * FROM " + Devices.Attld.TABLE_NAME, null);
            res.moveToFirst();

            while (!res.isAfterLast()) {
                array_list.add(res.getString(res.getColumnIndex(Devices.Attld.COLUMN_NAME_DEV_NAME)));
                UUID.add(res.getString(res.getColumnIndex(Devices.Attld.COLUMN_NAME_UU_ID)));

                res.moveToNext();
            }
            as.add(array_list);
            as.add(UUID);
            res.close();
        }
        return as;
    }

}
