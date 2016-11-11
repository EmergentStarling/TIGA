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
    public List<AnalysedViewTabInfo> GetDistinctLoc() {
        SQLiteDatabase db = dataBase.getWritableDatabase();
        //select key from table_name where imagename is img_name
        List<AnalysedViewTabInfo> value = new ArrayList<AnalysedViewTabInfo>();
        String[] column = {DataBase.State};
        Cursor cursor1 = db.query(true,DataBase.Table_Name, column, null, null, null, null,DataBase.Location+" ASC", null);

        while (cursor1.moveToNext()) {
            Log.wtf("cursor 1 ", "1");

            int key_index1 = cursor1.getColumnIndex(DataBase.State);
            AnalysedViewTabInfo info=new AnalysedViewTabInfo();
            info.PlaceName=cursor1.getString(key_index1);
            value.add(info);
        }
        cursor1.close();
        return value;
    }
    public List<AnalysedViewTabInfo> GetCoordinates() {
        SQLiteDatabase db = dataBase.getWritableDatabase();
        //select key from table_name where imagename is img_name
        List<AnalysedViewTabInfo> value = new ArrayList<AnalysedViewTabInfo>();
        String[] column = {DataBase.GeoCoordinates,DataBase.State};
        Cursor cursor1 = db.query(DataBase.Table_Name, column, null, null, null, null, null);

        while (cursor1.moveToNext()) {
            Log.wtf("cursor 1 ", "1");
            int key_index = cursor1.getColumnIndex(DataBase.GeoCoordinates);
            int key_index1 = cursor1.getColumnIndex(DataBase.State);
            AnalysedViewTabInfo info=new AnalysedViewTabInfo();
            info.PlaceName=cursor1.getString(key_index1);
            info.imgCoordinates=cursor1.getString(key_index);
            Log.wtf("cursor 1 ", "placename: "+info.PlaceName+" coordinates: "+info.imgCoordinates);
            value.add(info);
        }
        cursor1.close();
        return value;
    }
    public List<String> GetKeys(String img_name)
    {
        SQLiteDatabase db= dataBase.getWritableDatabase();
        //select key from table_name where imagename is img_name
        List<String> value=new ArrayList<String>();
        String[] column ={DataBase.ImageKey};

        Cursor cursor=db.query(DataBase.Table_Name,column,DataBase.ImageName+" = '"+img_name+"'",null,null,null,null);
        Cursor cursor1=db.query(DataBase.Table_Name,column,null,null,null,null,null);
        Log.wtf("cursor ", String.valueOf(cursor.getCount()));
        String string=null;
        while(cursor.moveToNext())
        {
            Log.wtf("cursor ","1");
            int key_index=cursor.getColumnIndex(DataBase.ImageKey);
            value.add(cursor.getString(key_index));
            Log.wtf("value 0 ", value.get(0));
        }
        while(cursor1.moveToNext())
        {

            Log.wtf("cursor 1 ","1");
            int key_index=cursor1.getColumnIndex(DataBase.ImageKey);
            if (string==null)
                string =cursor1.getString(key_index);
            else
            string +=","+cursor1.getString(key_index);

            Log.wtf("database adapter string",string);
        }
        //value.add(string);// just to prevent null exception for now
        Log.wtf("database adapter string",string);
        if (string!=null)
        {
            String[] str=string.split(",");
            String finalString="NULL";
            for(String subString :str)
            {
                Log.wtf("database adapter","here 1");
                Log.wtf("database adapter",subString.toString());

                int ind;
                for(int i=0; i<finalString.length(); i++)
                {
                    ind=finalString.indexOf(subString,i);
                    if(ind>=0)
                    {
                        Log.wtf("database adapter if ind", String.valueOf(ind));
                        i=finalString.length();
                    }
                   else if (ind==-1)
                    {
                        Log.wtf("database adapter else ind", String.valueOf(ind));
                        if (value.size()==1)
                        {
                            Log.wtf("database adapter else ind", "1");
                            finalString = finalString+","+subString;
                            i=finalString.length();
                            value.add(1,subString);
                        }
                        else if (value.size()>=2)
                        {
                            Log.wtf("database adapter else ind", "2 " );
                            finalString = finalString+subString;
                            i=finalString.length();
                            value.set(1, value.get(1) + "," + subString);

                        }
                    }
                }
            }
        }
   //  Log.wtf("database check value", value.get(value.size()-1));
        cursor.close();
        cursor1.close();
        return value;
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
        cursor.close();
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
        cursor.close();
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
        cursor.close();
        return value;
    }
    public int Locationupdatedata(String img_name, String imgnewname,String Location, String Coordinates,String Img_URI, String state)
    {
        SQLiteDatabase db= dataBase.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(DataBase.ImageName,imgnewname);
        contentValues.put(DataBase.Location,Location);
        contentValues.put(DataBase.GeoCoordinates,Coordinates);
        contentValues.put(DataBase.ImageURI,Img_URI);
        contentValues.put(DataBase.State,state);
        String[] whereArgs={img_name};
        int count =db.update(DataBase.Table_Name,contentValues,DataBase.ImageName+" =?",whereArgs);
        return count;
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
        else if (type.equals("location"))
            contentValues.put(DataBase.Location,value);

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
            cursor.close();
        }
        else if (Type.equals("text"))
        {String[] column ={DataBase.ImageText};

            Cursor cursor=db.query(DataBase.Table_Name,column,DataBase.ImageName+" = '"+img_name+"'",null,null,null,null);
            while(cursor.moveToNext())
            {
                int index=cursor.getColumnIndex(DataBase.ImageText);
                value=cursor.getString(index);
                Log.wtf("value",value);
            }
            cursor.close();
        }
        Log.wtf("value",value);

        return value;
    }
    static class DataBase extends SQLiteOpenHelper {
        private static final String ID="_ID";
        private static final String Database_Name="SigmaWay";
        private static final String Table_Name="ImageData";
        private static final int Database_Version=8;
        private static final String ImageName="IMG_NAME";
        private static final String ImageText="IMG_TEXT";
        private static final String ImageKey="IMG_KEY";
        private static final String ImageURI="IMG_URI";
        private static final String AnalysedData="ANALYSED_DATA";
        private static final String ImageTransText="IMG_TRANS_TEXT";
        private static final String ArabicToEnglishText="ARABIC_ENGLISH_TEXT";
        private static final String GeoCoordinates="Geo_Coordinate";
        private static final String Location="Location";
        private static final String State="State";
        String Tag=this.getClass().getName();
        private static final String Alter_table_Query="ALTER TABLE "+Table_Name+" ADD "+GeoCoordinates+" VARCHAR(255); ";
        private static final String Alter_table_Query1="ALTER TABLE "+Table_Name+" ADD "+Location+" VARCHAR(255); ";
        private static final String Alter_table_Query2="ALTER TABLE "+Table_Name+" ADD "+State+" VARCHAR(255); ";
        String Create_Table_Query ="CREATE TABLE "+ Table_Name+"("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+ImageName
                +" VARCHAR(255),"+ImageURI+" VARCHAR(255),"+ImageText+" VARCHAR(255),"+ImageKey+" VARCHAR(255),"
                +ImageTransText+" VARCHAR(255),"+ArabicToEnglishText+" VARCHAR(255),"+AnalysedData+" VARCHAR(255),"
                +GeoCoordinates+" VARCHAR(255),"+Location+" VARCHAR(255),"+State+" VARCHAR(255));";

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
            if (oldVersion==6){
                db.execSQL(Alter_table_Query);
                db.execSQL(Alter_table_Query1);
            }

            db.execSQL(Alter_table_Query2);
        }
    }
}
