package com.clb.gumustakisepeti.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.clb.gumustakisepeti.pojo.Siparis;
import com.clb.gumustakisepeti.util.ConstantString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "login_database";//database ad�

    private static final String TABLE_NAME = "login";
    private static String VT_KULLANICI_ID = "_id";
    private static String VT_KULLANICI_ADI = "kullanici_adi";
    private static String VT_KULLANICI_MAIL = "mail";
    private static String VT_KULLANICI_SIFRE = "sifre";
    private static String VT_KAYIT_TARIHI = "tarih";
    private static String VT_KULLANICI_TEL_NO = "tel_no";
    private static String VT_KULLANICI_ADRES = "adres";
    private static String VT_AD = "ad";
    private static String VT_KULLANICI_SOYAD = "soyad";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {  // Databesi oluşturuyoruz.Bu methodu biz çağırmıyoruz. Databese de obje oluşturduğumuzda otamatik çağırılıyor.
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + VT_KULLANICI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + VT_KULLANICI_ADI + " TEXT,"
                + VT_KULLANICI_MAIL + " TEXT,"
                + VT_KULLANICI_SIFRE + " TEXT,"
                + VT_KULLANICI_TEL_NO + " TEXT,"
                + VT_KULLANICI_ADRES + " TEXT,"
                + VT_AD + " TEXT,"
                + VT_KULLANICI_SOYAD + " TEXT,"
                + VT_KAYIT_TARIHI + " TEXT" + ")";

        String CREATE_SEPETIM = "CREATE TABLE " + ConstantString.TABLE_SEPETIM_NAME + "(" +
                ConstantString.SS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ConstantString.S_KUL_ADI + " TEXT," +
                ConstantString.S_AD + " TEXT," +
                ConstantString.S_SOYAD + " TEXT," +
                ConstantString.S_TEL_NO + " TEXT," +
                ConstantString.S_EMAIL + " TEXT," +
                ConstantString.S_ADRES + " TEXT," +
                ConstantString.SS_OBJECT_ID + " TEXT," +
                ConstantString.SS_ADI + " TEXT," +
                ConstantString.SS_ADET + " TEXT," +
                ConstantString.SS_IMAGE_URL + " TEXT," +
                ConstantString.SS_DURUMU + " INTEGER," +
                ConstantString.SS_MESAJ + " TEXT," +
                ConstantString.SS_FIYAT + " TEXT," +
                ConstantString.SS_URUN_FIYAT + " TEXT," +
                ConstantString.SS_KODU + " TEXT," +
                ConstantString.SS_STOK_KODU + " TEXT," +
                ConstantString.SS_URUN_TURU + " TEXT," +
                ConstantString.SS_TARIH + " TEXT" + " )";
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_SEPETIM);
    }

    public boolean sepeteEkleAll(Siparis siparis) {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ConstantString.S_KUL_ADI, siparis.getSkKulAdi());
        values.put(ConstantString.S_AD, siparis.getSkAd());
        values.put(ConstantString.S_SOYAD, siparis.getSkSoyad());
        values.put(ConstantString.S_TEL_NO, siparis.getSkTelNo());
        values.put(ConstantString.S_EMAIL, siparis.getSkEmail());
        values.put(ConstantString.S_ADRES, siparis.getSkAdres());
        values.put(ConstantString.SS_OBJECT_ID, siparis.getSiparisObjectId());
        values.put(ConstantString.SS_ADI, siparis.getSiparisAdi());
        values.put(ConstantString.SS_ADET, String.valueOf(siparis.getSiparisAdet()));
        values.put(ConstantString.SS_IMAGE_URL, siparis.getSiparisImageUrl());
        values.put(ConstantString.SS_DURUMU, siparis.getSiparisDurumu());
        values.put(ConstantString.SS_MESAJ, siparis.getSiparisMesaj());
        values.put(ConstantString.SS_FIYAT, String.valueOf(siparis.getSiparisFiyat()));
        values.put(ConstantString.SS_URUN_FIYAT, String.valueOf(siparis.getSiparisUrunFiyat()));
        values.put(ConstantString.SS_KODU, siparis.getSiparisKodu());
        values.put(ConstantString.SS_STOK_KODU, siparis.getSiparisStokKodu());
        values.put(ConstantString.SS_URUN_TURU, siparis.getUrunTuru());
        values.put(ConstantString.SS_TARIH, siparis.getSiparisTarih());

        result = db.insert(ConstantString.TABLE_SEPETIM_NAME, null, values) > 0;
        db.close();
        return result;
    }


    public boolean sepeteEkle(Siparis siparis) {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ConstantString.S_KUL_ADI, siparis.getSkKulAdi());
        values.put(ConstantString.S_AD, siparis.getSkAd());
        values.put(ConstantString.S_SOYAD, siparis.getSkSoyad());
        values.put(ConstantString.S_TEL_NO, siparis.getSkTelNo());
        values.put(ConstantString.S_EMAIL, siparis.getSkEmail());
        values.put(ConstantString.S_ADRES, siparis.getSkAdres());
        values.put(ConstantString.SS_OBJECT_ID, siparis.getSiparisObjectId());
        values.put(ConstantString.SS_ADI, siparis.getSiparisAdi());
        values.put(ConstantString.SS_ADET, String.valueOf(siparis.getSiparisAdet()));
        values.put(ConstantString.SS_IMAGE_URL, siparis.getSiparisImageUrl());
        values.put(ConstantString.SS_DURUMU, siparis.getSiparisDurumu());
        values.put(ConstantString.SS_MESAJ, siparis.getSiparisMesaj());
        values.put(ConstantString.SS_FIYAT, String.valueOf(siparis.getSiparisFiyat()));
        values.put(ConstantString.SS_URUN_FIYAT, String.valueOf(siparis.getSiparisUrunFiyat()));
        values.put(ConstantString.SS_KODU, siparis.getSiparisKodu());
        values.put(ConstantString.SS_STOK_KODU, siparis.getSiparisStokKodu());
        values.put(ConstantString.SS_URUN_TURU, siparis.getUrunTuru());
        values.put(ConstantString.SS_TARIH, siparis.getSiparisTarih());

        result = db.insert(ConstantString.TABLE_SEPETIM_NAME, null, values) > 0;
        db.close();
        return result;
    }

    public boolean sepetAdetFiyatGuncelle(Siparis siparis) {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ConstantString.SS_ADET, String.valueOf(siparis.getSiparisAdet()));
        values.put(ConstantString.SS_FIYAT, String.valueOf(siparis.getSiparisFiyat()));
        result = db.update(ConstantString.TABLE_SEPETIM_NAME, values, ConstantString.SS_ID + " = " + siparis.getsID(), null) > 0;
        db.close();
        return result;
    }

    public boolean sepetMesajGuncelle(Siparis siparis) {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ConstantString.SS_MESAJ, String.valueOf(siparis.getSiparisMesaj()));
        result = db.update(ConstantString.TABLE_SEPETIM_NAME, values, ConstantString.SS_ID + " = " + siparis.getsID(), null) > 0;
        db.close();
        return result;
    }

    public void kullaniciEkle(String kullanici_adi, String mail, String sifre, String tarih) {
        //kullan�c�Ekle methodu ise ad� �st�nde Databese veri eklemek i�in
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(VT_KULLANICI_ADI, kullanici_adi);
        values.put(VT_KULLANICI_MAIL, mail);
        values.put(VT_KULLANICI_SIFRE, sifre);
        values.put(VT_KAYIT_TARIHI, tarih);

        db.insert(TABLE_NAME, null, values);
        db.close(); //Database Ba�lant�s�n� kapatt�k*/
    }

    public void kullaniciEkleDetayli(String kullanici_adi, String mail, String sifre, String tel_no, String adres, String ad, String soyad, String tarih) {
        //kullan�c�Ekle methodu ise ad� �st�nde Databese veri eklemek i�in
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(VT_KULLANICI_ADI, kullanici_adi);
        values.put(VT_KULLANICI_MAIL, mail);
        values.put(VT_KULLANICI_SIFRE, sifre);
        values.put(VT_KULLANICI_TEL_NO, tel_no);
        values.put(VT_KULLANICI_ADRES, adres);
        values.put(VT_AD, ad);
        values.put(VT_KULLANICI_SOYAD, soyad);
        values.put(VT_KAYIT_TARIHI, tarih);

        db.insert(TABLE_NAME, null, values);
        db.close(); //Database Ba�lant�s�n� kapatt�k*/
    }

    public void kullaniciGuncelle(String id, String kullanici_adi, String mail, String sifre, String tel_no, String adres, String ad, String soyad, String tarih) {
        //kullan�c�Ekle methodu ise ad� �st�nde Databese veri eklemek i�in
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(VT_KULLANICI_ADI, kullanici_adi);
        values.put(VT_KULLANICI_MAIL, mail);
        values.put(VT_KULLANICI_SIFRE, sifre);
        values.put(VT_KULLANICI_TEL_NO, tel_no);
        values.put(VT_KULLANICI_ADRES, adres);
        values.put(VT_AD, ad);
        values.put(VT_KULLANICI_SOYAD, soyad);
        values.put(VT_KAYIT_TARIHI, tarih);
        db.update(TABLE_NAME, values, VT_KULLANICI_ID + " = " + id, null);
        db.close(); //Database Ba�lant�s�n� kapatt�k*/
    }

    public void kullaniciGuncelleTwo(String id, String tel_no, String adres, String ad, String soyad, String tarih) {
        //kullan�c�Ekle methodu ise ad� �st�nde Databese veri eklemek i�in
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(VT_KULLANICI_TEL_NO, tel_no);
        values.put(VT_KULLANICI_ADRES, adres);
        values.put(VT_AD, ad);
        values.put(VT_KULLANICI_SOYAD, soyad);
        values.put(VT_KAYIT_TARIHI, tarih);
        db.update(TABLE_NAME, values, VT_KULLANICI_ID + " = " + id, null);
        db.close(); //Database Ba�lant�s�n� kapatt�k*/
    }

    public void kullaniciSifreGuncelle(String id, String sifre, String tarih) {
        //kullan�c�Ekle methodu ise ad� �st�nde Databese veri eklemek i�in
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(VT_KULLANICI_SIFRE, sifre);
        values.put(VT_KAYIT_TARIHI, tarih);
        db.update(TABLE_NAME, values, VT_KULLANICI_ID + " = " + id, null);
        db.close(); //Database Ba�lant�s�n� kapatt�k*/
    }


    public HashMap<String, String> kullaniciDetay() {
        //Bu methodda sadece tek row değerleri alınır.
        HashMap<String, String> kisi = new HashMap<String, String>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            kisi.put(VT_KULLANICI_ID, cursor.getString(0));
            kisi.put(VT_KULLANICI_ADI, cursor.getString(1));
            kisi.put(VT_KULLANICI_MAIL, cursor.getString(2));
            kisi.put(VT_KULLANICI_SIFRE, cursor.getString(3));
            kisi.put(VT_KULLANICI_TEL_NO, cursor.getString(4));
            kisi.put(VT_KULLANICI_ADRES, cursor.getString(5));
            kisi.put(VT_AD, cursor.getString(6));
            kisi.put(VT_KULLANICI_SOYAD, cursor.getString(7));
            kisi.put(VT_KAYIT_TARIHI, cursor.getString(8));
        }
        cursor.close();
        db.close();
        // return kitap
        return kisi;
    }


    public int getRowCount() { //tabloda kac satır kayıtlı olduğunu geri döner
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
        // return row count
        return rowCount;
    }

    public int getRowCountSepetim() { //tabloda kac satır kayıtlı olduğunu geri döner
        String countQuerySepet = "SELECT  * FROM " + ConstantString.TABLE_SEPETIM_NAME;
        SQLiteDatabase dbSepet = this.getReadableDatabase();
        Cursor cursorSepet = dbSepet.rawQuery(countQuerySepet, null);
        int rowCountSepet = cursorSepet.getCount();
        dbSepet.close();
        cursorSepet.close();
        // return row count
        return rowCountSepet;
    }

    public List<Siparis> getAllSiparisSepet() {
        List<Siparis> siparisList = new ArrayList<Siparis>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + ConstantString.TABLE_SEPETIM_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Siparis siparis = new Siparis();
                siparis.setsID(cursor.getInt(0));
                siparis.setSkKulAdi(cursor.getString(1));
                siparis.setSkAd(cursor.getString(2));
                siparis.setSkSoyad(cursor.getString(3));
                siparis.setSkTelNo(cursor.getString(4));
                siparis.setSkEmail(cursor.getString(5));
                siparis.setSkAdres(cursor.getString(6));
                siparis.setSiparisObjectId(cursor.getString(7));
                siparis.setSiparisAdi(cursor.getString(8));
                siparis.setSiparisAdet(cursor.getInt(9));
                siparis.setSiparisImageUrl(cursor.getString(10));
                siparis.setSiparisDurumu(cursor.getInt(11));
                siparis.setSiparisMesaj(cursor.getString(12));
                siparis.setSiparisFiyat(Float.valueOf(cursor.getString(13)));
                siparis.setSiparisUrunFiyat(Float.valueOf(cursor.getString(14)));
                siparis.setSiparisKodu(cursor.getString(15));
                siparis.setSiparisStokKodu(cursor.getString(16));
                siparis.setUrunTuru(cursor.getString(17));
                siparis.setSiparisTarih(cursor.getString(18));
                // Adding appointment to list
                siparisList.add(siparis);
            } while (cursor.moveToNext());
        }
        // return appointment list
        return siparisList;
    }


    public boolean sptUrunSil(Siparis siparis) {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        result = db.delete(ConstantString.TABLE_SEPETIM_NAME, ConstantString.SS_ID + " = " + siparis.getsID(), null) > 0;
        db.close(); //Database Ba�lant�s�n� kapatt�k*/
        return result;
    }


    public void resetTables() {
        // Tüm verileri siler. tabloyu resetler.
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    public void resetSepetim() {
        SQLiteDatabase dbSepet = this.getWritableDatabase();
        dbSepet.delete(ConstantString.TABLE_SEPETIM_NAME, null, null);
        dbSepet.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ConstantString.TABLE_SEPETIM_NAME);

        onCreate(db);

    }

}
