package com.clb.gumustakisepeti.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.clb.gumustakisepeti.R;
import com.clb.gumustakisepeti.database.Database;
import com.clb.gumustakisepeti.database.PostClass;
import com.clb.gumustakisepeti.util.ConstantString;
import com.clb.gumustakisepeti.util.Fonksiyonlar;
import com.clb.gumustakisepeti.util.Utilities;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by Emre on 21.10.2015.
 */
public class ActivitySiparisVer extends AppCompatActivity {

    private Toolbar toolbar;
    PostClass post = new PostClass();
    JSONObject cevap = null;


    EditText edtSiparisAdi;
    EditText edtSiparisAdet;
    EditText edtSkAd;
    EditText edtSkSoyAd;
    EditText edtSkTelNo;
    EditText edtSkEmail;
    EditText edtSkTeslimatAdresi;
    EditText edtSkMesaj;
    TextView tvSiparisFiyati;
    TextView tvAdetArttır;
    TextView tvAdetEksilt;
    Button btnSiparisVazgec;
    Button btnSiparisiOnayla;


    HashMap<String, String> kulDetay = null;
    HashMap<String, String> stringStringHashMap = null;
    String helperKulAdi = null;
    String helperSifre = null;
    String helperEmail = null;
    String helperAdres = null;
    String helperTelno = null;
    String helperAd = null;
    String helperSoyad = null;

    String gelenUserName = null;
    String gelenSifre = null;
    String gelenEmail = null;
    String gelenAdres = null;
    String gelenTelno = null;
    String gelenAd = null;
    String gelenSoyad = null;

    String bundleObjectId;
    String bundleAdi;
    String bundleResimYolu;
    String bundleStokNo;
    String bundleFiyat;
    String bundleUrunTuru;


    String stSkKulAdi;
    String stSiparisAdi;
    String stSiparisAdet;
    String stSiparisImageUrl;
    String stKisiAdi;
    String stKisiSoyadi;
    String stKisiTelNo;
    String stKisiEmail;
    String stKisiTeslimatAdresi;
    String stUrunObjectId;
    int stSiparisDurumu = 0;
    String stKisiMesaji;
    String stSonFiyat;
    String stSiparisKodu = "";
    String stSiparisTarih;

    /**
     * PROFIL
     */
    String sonucProfil;
    PostClass postProfil = new PostClass();
    JSONObject cevapProfil = null;
    JSONArray kullaniciArrayProfil = null;
    JSONObject kullaniciVeriProfil = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siparis_ver);
        ///TOOLBAR////
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ///TOOLBAR////

        initialComponent();
        registerEvents();
    }

    private void initialComponent() {
        edtSiparisAdi = (EditText) findViewById(R.id.edtSiparisAdi);
        edtSiparisAdet = (EditText) findViewById(R.id.edtSiparisAdet);
        edtSkAd = (EditText) findViewById(R.id.edtSkAd);
        edtSkSoyAd = (EditText) findViewById(R.id.edtSkSoyAd);
        edtSkTelNo = (EditText) findViewById(R.id.edtSkTelNo);
        edtSkEmail = (EditText) findViewById(R.id.edtSkEmail);
        edtSkTeslimatAdresi = (EditText) findViewById(R.id.edtSkTeslimatAdresi);
        edtSkMesaj = (EditText) findViewById(R.id.edtSkMesaj);
        tvAdetArttır = (TextView) findViewById(R.id.tvAdetArttır);
        tvAdetEksilt = (TextView) findViewById(R.id.tvAdetEksilt);
        tvSiparisFiyati = (TextView) findViewById(R.id.tvSiparisFiyati);
        btnSiparisVazgec = (Button) findViewById(R.id.btnSiparisVazgec);
        btnSiparisiOnayla = (Button) findViewById(R.id.btnSiparisiOnayla);


        Bundle extras = getIntent().getExtras();
        bundleObjectId = extras.getString(ConstantString.BUNDLE_OBJECT_ID);
        bundleAdi = extras.getString(ConstantString.BUNDLE_AD);
        bundleResimYolu = extras.getString(ConstantString.BUNDLE_RESIM_YOLU);
        bundleStokNo = extras.getString(ConstantString.BUNDLE_STOK_NO);
        bundleFiyat = extras.getString(ConstantString.BUNDLE_FIYAT);
        bundleUrunTuru = extras.getString(ConstantString.BUNDLE_URUN_TURU);

        if (extras != null) {
            getSupportActionBar().setTitle(bundleAdi + " / " + bundleFiyat + " TL");
            if (Fonksiyonlar.giriskontrol(getApplicationContext())) {
                Database database = new Database(ActivitySiparisVer.this);
                kulDetay = database.kullaniciDetay();
                kulDetay = database.kullaniciDetay();
                helperKulAdi = kulDetay.get(ConstantString.SQ_KULLANICI_ADI);
                helperAd = kulDetay.get(ConstantString.SQ_AD);
                helperSoyad = kulDetay.get(ConstantString.SQ_SOYAD);
                helperEmail = kulDetay.get(ConstantString.SQ_KULLANICI_MAIL);
                helperTelno = kulDetay.get(ConstantString.SQ_KULLANICI_TEL_NO);
                helperAdres = kulDetay.get(ConstantString.SQ_KULLANICI_ADRES);
                edtSkAd.setText(helperAd);
                edtSkSoyAd.setText(helperSoyad);
                edtSkTelNo.setText(helperTelno);
                edtSkEmail.setText(helperEmail);
                edtSkTeslimatAdresi.setText(helperAdres);
            } else {
                edtSkEmail.setEnabled(true);
            }
            edtSiparisAdi.setText(bundleAdi);
            edtSiparisAdet.setText("1");
            if (bundleUrunTuru.equals("0")) {// 0-ALYANS 1-KOLYE
                edtSkMesaj.setHint("Lütfen Alyans Parmak Numarınızı Buraya Yazın.");
            } else if (bundleUrunTuru.equals("1")) {
                edtSkMesaj.setHint("Özel Mesajınız");
            }
            double geciciAdet = Double.valueOf(edtSiparisAdet.getText().toString());
            double geciciFiyat = Double.valueOf(bundleFiyat);
            double genelFiyat = geciciAdet * geciciFiyat;
            tvSiparisFiyati.setText(genelFiyat + "");

            tvAdetArttır.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrate(100);
                    double geciciAdet = Double.valueOf(edtSiparisAdet.getText().toString());
                    double geciciFiyat = Double.valueOf(bundleFiyat);
                    geciciAdet++;
                    double genelFiyat = geciciAdet * geciciFiyat;
                    tvSiparisFiyati.setText(genelFiyat + "");
                    edtSiparisAdet.setText((int) geciciAdet + " ");
                    Utilities.showToastMessage(getApplicationContext(), (int) geciciAdet + " Adet : " + (int) genelFiyat + " TL");
                }
            });

            tvAdetEksilt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrate(100);
                    double geciciAdet = Double.valueOf(edtSiparisAdet.getText().toString());
                    if ((int) geciciAdet <= 1) {
                        Utilities.showToastMessage(getApplicationContext(), "En az 1 Adet");
                    } else {
                        double geciciFiyat = Double.valueOf(bundleFiyat);
                        geciciAdet--;
                        double genelFiyat = geciciAdet * geciciFiyat;
                        tvSiparisFiyati.setText(genelFiyat + "");
                        edtSiparisAdet.setText((int) geciciAdet + " ");
                        Utilities.showToastMessage(getApplicationContext(), (int) geciciAdet + " Adet : " + (int) genelFiyat + " TL");
                    }
                }
            });
        } else {
            getSupportActionBar().setTitle("Ürün Seçiminde Hata Oluştu");
        }

    }

    private void registerEvents() {
        btnSiparisiOnayla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stSiparisAdi = edtSiparisAdi.getText().toString().trim();
                stSiparisAdet = edtSiparisAdet.getText().toString().trim();
                stKisiAdi = edtSkAd.getText().toString().trim();
                stKisiSoyadi = edtSkSoyAd.getText().toString().trim();
                stKisiTelNo = edtSkTelNo.getText().toString().trim();
                stKisiEmail = edtSkEmail.getText().toString().trim();
                stKisiTeslimatAdresi = edtSkTeslimatAdresi.getText().toString().trim();
                stKisiMesaji = edtSkMesaj.getText().toString().trim();
                stSonFiyat = tvSiparisFiyati.getText().toString().trim();

                if (Fonksiyonlar.giriskontrol(getApplicationContext())) {
                    stSkKulAdi = helperKulAdi;
                } else {
                    stSkKulAdi = "Misafir Kullanıcı";
                }
                stSiparisImageUrl = bundleResimYolu;
                stUrunObjectId = bundleObjectId;

                String hata = "";
                if (stKisiEmail.equals("")) {
                    stKisiEmail = "Misafir Kullanıcı";
                }

                if (stKisiAdi.matches("")) {
                    hata += "Lütfen İsim Giriniz\n";
                } else {
                    if (stKisiAdi.length() < 3) {
                        hata += "İsim En Az 3 Karakter Olabilir\n";
                    }
                }
                if (stKisiSoyadi.matches("")) {
                    hata += "Lütfen Soyisim Giriniz\n";
                } else {
                    if (stKisiSoyadi.length() < 2) {
                        hata += "Soyisim En Az 2 Karakter Olabilir\n";
                    }
                }
                if (stKisiTelNo.matches("")) {
                    hata += "Lütfen Telefon Numarası Giriniz\n";
                } else {
                    if (stKisiTelNo.length() < 10 || stKisiTelNo.length() > 11) {
                        hata += "Lütfen Telefon Numarasını Kontrol Ediniz\n";
                    }
                }
                if (stKisiTeslimatAdresi.matches("")) {
                    hata += "Lütfen Adres Giriniz\n";
                }

                if (bundleUrunTuru.equals("0")) {
                    if (stKisiMesaji.matches("")) {
                        hata += "Lütfen Parmak Numarası  Giriniz\n";
                        edtSkMesaj.setFocusableInTouchMode(true);
                        edtSkMesaj.requestFocus();
                    }
                }


                if (!(hata.equals(""))) {
                    Utilities.showToastMessageLengthLong(ActivitySiparisVer.this, hata);
                } else {
                    ////////SIPARIS KODU OLUSTURMA KODU////////
                    String basKisim = randomCharCheckTurkishCharacter();
                    String sonKisim = randomCharCheckTurkishCharacter();
                    Random rnd = new Random();
                    int randomSayi;
                    String sayiS;
                    do {
                        randomSayi = rnd.nextInt();
                        sayiS = String.valueOf(randomSayi);
                    } while (randomSayi <= 0 || sayiS.length() != 6);

                    stSiparisKodu = basKisim + randomSayi + sonKisim;
                    ////////SIPARIS KODU OLUSTURMA KODU////////
                    if (bundleAdi.equals("")) {
                        Utilities.showToastMessage(ActivitySiparisVer.this, "Beklenmeyen Hata Tekrar Deneyin");
                    } else {
                        Utilities.showAlertDialog(ActivitySiparisVer.this, "Opps :) ", "Beta Sürümü-Sipariş Verilemiyor.", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        // new SiparisVerAsync().execute();
                    }

                }


            }
        });
        btnSiparisVazgec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static String randomCharCheckTurkishCharacter() {
        String son = null;
        do {
            son = randomChar();
        } while (Utilities.containsTurkishCharacters(son));
        return son;
    }

    public static String randomChar() {
        Random r = new Random();
        char c = (char) (r.nextInt(26) + 'a');
        char c2 = (char) (r.nextInt(26) + 'a');
        return (c + "" + c2).toUpperCase();
    }


    public void vibrate(int duration) {
        Vibrator vibs = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibs.vibrate(duration);
    }

    class SiparisVerAsync extends AsyncTask<Void, Void, Void> {
        private String sonucmesaji, sonuc, tarih;
        ProgressDialog pDialog;

        protected void onPreExecute() {
            //  progress dialog
            pDialog = new ProgressDialog(ActivitySiparisVer.this);
            pDialog.setMessage("Sipariş Oluşturuluyor");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected Void doInBackground(Void... unused) {

            Calendar c = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("dd/MMMM/yyyy HH:mm:ss");
            tarih = format.format(c.getTime());
            stSiparisTarih = tarih;

            if (!(tarih.equals(""))) {

                //Parametreleri ekliyoruz
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair(ConstantString.SK_KUL_ADI, stSkKulAdi));
                params.add(new BasicNameValuePair(ConstantString.SK_AD, stKisiAdi));
                params.add(new BasicNameValuePair(ConstantString.SK_SOYAD, stKisiSoyadi));
                params.add(new BasicNameValuePair(ConstantString.SK_TEL_NO, stKisiTelNo));
                params.add(new BasicNameValuePair(ConstantString.SK_EMAIL, stKisiEmail));
                params.add(new BasicNameValuePair(ConstantString.SK_ADRES, stKisiTeslimatAdresi));
                params.add(new BasicNameValuePair(ConstantString.SIPARIS_OBJECT_ID, stUrunObjectId));
                params.add(new BasicNameValuePair(ConstantString.SIPARIS_ADI, stSiparisAdi));
                params.add(new BasicNameValuePair(ConstantString.SIPARIS_ADET, stSiparisAdet));
                params.add(new BasicNameValuePair(ConstantString.SIPARIS_IMAGE_URL, stSiparisImageUrl));
                params.add(new BasicNameValuePair(ConstantString.SIPARIS_DURUMU, String.valueOf(stSiparisDurumu)));
                params.add(new BasicNameValuePair(ConstantString.SIPARIS_FIYAT, stSonFiyat));
                params.add(new BasicNameValuePair(ConstantString.SIPARIS_MESAJ, stKisiMesaji));
                params.add(new BasicNameValuePair(ConstantString.SIPARIS_KODU, stSiparisKodu));
                params.add(new BasicNameValuePair(ConstantString.SIPARIS_TARIH, stSiparisTarih));


                String json = post.httpPost(ConstantString.URL_POST_SIPARIS_VER, ConstantString.POST_METHOD, params, 20000);

                Log.d("Gelen Json", "" + json);//Gelen veriyi logluyoruz.Log Catten kontrol edebiliriz
                try {

                    cevap = new JSONObject(json);
                    sonucmesaji = cevap.getString(ConstantString.JSON_SONUC_MESAJI);

                    if (cevap.getString(ConstantString.JSON_SONUC) != null) {
                        sonuc = cevap.getString(ConstantString.JSON_SONUC);
//                    if (Integer.parseInt(sonuc) == 1) { //Eğer kayıt başarılı ise

//                        Sqlite database baglanıp gerekli verileri kaydediyoruz.
//                        Database db = new Database(getApplicationContext());
//                        db.resetTables();
                        //kullanıcıyı ekliyoruz.Ve Main activitye yönlendiriyoruz.

                        //Sonuc başarılı ise main activitye yönlendirdk.
//                        db.kullaniciEkle(kullanici_adi, email, sifre, tarih);    //kullanıcıyı ekliyoruz.Ve Main activitye yönlendiriyoruz.

                        //Sonuc başarılı ise main activitye yönlendirdk.
//                        Utilities.clearAndStartActivity(ActivitySignUp.this, MainActivity.class);
//                        finish();

//                    }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        // Sonuç başarılı ise bu kod çalışmıcak çünkü Main activitye yönlenmiş durumda
        protected void onPostExecute(Void unused) {
            // closing progress dialog
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    if (Integer.parseInt(sonuc) == 0) {// Sonuç başarılı değil ise
                        Utilities.warningAlertDialogPoz(ActivitySiparisVer.this, getString(R.string.hata), sonucmesaji, getString(R.string.tamam), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                    } else if (Integer.parseInt(sonuc) == 1) {
                        Utilities.warningAlertDialogPoz(ActivitySiparisVer.this, "SORUN YOK", sonucmesaji + "\nSiparis Kodu:" + stSiparisKodu, getString(R.string.tamam), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                    }
                }
            });
        }
    }

}
