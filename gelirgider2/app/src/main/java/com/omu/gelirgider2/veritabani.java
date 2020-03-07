package com.omu.gelirgider2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class veritabani extends SQLiteOpenHelper {

    private static final String VERI_TABANI_AD ="hesaplar";
    private static final int SURUM =1;

    public veritabani(Context c){
        super(c,VERI_TABANI_AD,null,SURUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table hesap (tur Text, katagori Text, tarih Text, tutar Int)");
        db.execSQL("create table hesapgider (tur Text, katagori Text, tarih Text, tutar Int)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists hesap");
        db.execSQL("drop table if exists hesapgider");
        onCreate(db);
    }
}
