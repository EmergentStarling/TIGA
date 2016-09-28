package com.example.sigmaway.homeimage.CustomClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Family on 12-09-2016.
 */
public class DataBaseAdapter{
    DataBase dataBase;
    public DataBaseAdapter(Context context){
        dataBase=new DataBase(context);
    }
    public String GetAnalysedText(String img_name)
    {
        SQLiteDatabase db= dataBase.getWritableDatabase();
        //select key from table_name where imagename is img_name
        String value=null;
        String[] column ={DataBase.AnalysedData};
        Cursor cursor=db.query(DataBase.Table_Name,column,DataBase.ImageName+" = '"+img_name+"'",null,null,null,null);
        Log.wtf("cursor ", String.valueOf(cursor.getCount()));
        while(cursor.moveToNext())
        {
            Log.wtf("cursor ","1");
            int text_index=cursor.getColumnIndex(DataBase.AnalysedData);
            value=cursor.getString(text_index);
        }
        return value;
    }
    public String GetTranslatedText(String img_name,String lang)
    {
        String language = null;
        SQLiteDatabase db= dataBase.getWritableDatabase();
        //select key from table_name where imagename is img_name
        String value=null;
        if (lang.equals("en")) {
            language = DataBase.ArabicToEnglishText;
        }
        else if (lang.equals("fa"))
        {
            language =DataBase.ImageTransText;
        }

        String[] column ={language};
        Cursor cursor=db.query(DataBase.Table_Name,column,DataBase.ImageName+" = '"+img_name+"'",null,null,null,null);
        Log.wtf("cursor ", String.valueOf(cursor.getCount()));
        while(cursor.moveToNext())
        {
            Log.wtf("cursor ","1");
            int text_index=cursor.getColumnIndex(language);
            value=cursor.getString(text_index);
        }
        return value;
    }
    public List<String> getdata(String img_name){
        SQLiteDatabase db= dataBase.getWritableDatabase();
        //select key from table_name where imagename is img_name
        List<String> value=new ArrayList<String>();
        String[] column ={DataBase.ImageKey, DataBase.ImageText};
        Cursor cursor=db.query(DataBase.Table_Name,column,DataBase.ImageName+" = '"+img_name+"'",null,null,null,null);
        Log.wtf("cursor ", String.valueOf(cursor.getCount()));
        while(cursor.moveToNext())
        {
            Log.wtf("cursor ","1");
          int text_index=cursor.getColumnIndex(DataBase.ImageText);
            int key_index=cursor.getColumnIndex(DataBase.ImageKey);
            value.add(cursor.getString(text_index));
            value.add(cursor.getString(key_index));
            Log.wtf("value 0 ", value.get(0));
            Log.wtf("value 1", value.get(1));
        }
        return value;
    }
    public long updatedata(String img_name,String type, String value)
    {
        SQLiteDatabase db= dataBase.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(DataBase.ImageName,img_name);
        if(type.equals("key"))
        {
            contentValues.put(DataBase.ImageKey,value);
        }

        else if(type.equals("text"))
            contentValues.put(DataBase.ImageText,value);
        else if (type.equals("transtext"))
            contentValues.put(DataBase.ImageTransText,value);
        else if (type.equals("engtransdata"))
            contentValues.put(DataBase.ArabicToEnglishText  ,value);
        else if (type.equals("AnalysedData"))
            contentValues.put(DataBase.AnalysedData ,value);
            String[] whereArgs={img_name};
        long count =db.update(DataBase.Table_Name,contentValues,DataBase.ImageName+" =?",whereArgs);
        return count;
    }
    public long insertdata(String img_name,String img_uri,String img_text,String img_key,String img_fa_tran_text,String img_eng_tran_text,String img_analysed ){
        SQLiteDatabase db= dataBase.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(DataBase.ImageName,img_name);
        contentValues.put(DataBase.ImageURI,img_uri);
        contentValues.put(DataBase.ImageText,img_text);
        contentValues.put(DataBase.ImageKey,img_key);
        contentValues.put(DataBase.ImageTransText,img_fa_tran_text);
        contentValues.put(DataBase.AnalysedData,img_analysed);
        contentValues.put(DataBase.ArabicToEnglishText,img_eng_tran_text);
        long id=db.insert(DataBase.Table_Name,null,contentValues);
        Log.wtf("INSERT DATA", String.valueOf(id));
        return id;
    }
    public String getvalue(String Type,String img_name){
        SQLiteDatabase db= dataBase.getWritableDatabase();
        //select key from table_name where imagename is img_name
        String value=null;

        if (Type.equals("key"))
        {String[] column ={DataBase.ImageKey};

            Cursor cursor=db.query(DataBase.Table_Name,column,DataBase.ImageName+" = '"+img_name+"'",null,null,null,null);
            while(cursor.moveToNext())
            {
                int index=cursor.getColumnIndex(DataBase.ImageKey);
                value=cursor.getString(index);
            }
        }
        else if (Type.equals("text"))
        {String[] column ={DataBase.ImageText};

            Cursor cursor=db.query(DataBase.Table_Name,column,DataBase.ImageName+" = '"+img_name+"'",null,null,null,null);
            while(cursor.moveToNext())
            {
                int index=cursor.getColumnIndex(DataBase.ImageText);
                value=cursor.getString(index);
            }
        }

        return value;
    }
    static class DataBase extends SQLiteOpenHelper {
        private static final String ID="_ID";
        private static final String Database_Name="SigmaWay";
        private static final String Table_Name="ImageData";
        private static final int Database_Version=4;
        private static final String ImageName="IMG_NAME";
        private static final String ImageText="IMG_TEXT";
        private static final String ImageKey="IMG_KEY";
        private static final String ImageURI="IMG_URI";
        private static final String AnalysedData="ANALYSED_DATA";
        private static final String ImageTransText="IMG_TRANS_TEXT";
        private static final String ArabicToEnglishText="ARABIC_ENGLISH_TEXT";
        String Tag=this.toString();
        private static final String Alter_table_Query="ALTER TABLE "+Table_Name+" ADD "+AnalysedData+" VARCHAR(255);";
        String Create_Table_Query ="CREATE TABLE "+ Table_Name+"("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+ImageName
                +" VARCHAR(255),"+ImageURI+" VARCHAR(255),"+ImageText+" VARCHAR(255),"+ImageKey+" VARCHAR(255),"
                +ImageTransText+" VARCHAR(255),"+ArabicToEnglishText+" VARCHAR(255),"+AnalysedData+" VARCHAR(255));";

        public DataBase(Context context) {
            super(context, Database_Name, null, Database_Version);
            Log.wtf(Tag," constructor called");
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(Create_Table_Query);
            Log.wtf(Tag," oncreate called");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.wtf(Tag,"onupgrade called");
            db.execSQL(Alter_table_Query);

        }
    }
}
