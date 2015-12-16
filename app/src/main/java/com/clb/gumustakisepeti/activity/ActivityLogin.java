package com.clb.gumustakisepeti.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.clb.gumustakisepeti.MainActivity;
import com.clb.gumustakisepeti.R;
import com.clb.gumustakisepeti.database.Database;
import com.clb.gumustakisepeti.database.PostClass;
import com.clb.gumustakisepeti.util.ConstantString;
import com.clb.gumustakisepeti.util.Utilities;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author Emre Celebi (emrecelebiiii@gmail.com)
 * @version 1.0
 * @since October 15, 2015 1:32:42 PM
 */
public class ActivityLogin extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView lblHeader;
    private TextView lblSifremiUnuttum;
    private EditText txtKullaniciAdi;
    private EditText txtSifre;
    private Button btnLogin;
    private Button btnYeniHesapOlustur;


    String kullanici_adi, sifre, sonuc, tarih;
    String hata_mesaji = "";
    Boolean hata = false;

    PostClass post = new PostClass();
    JSONObject cevap = null;
    JSONArray kullaniciArray = null;
    JSONObject kullaniciVeri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //TOOLBAR////
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
        initializeComponents();
        registerEvents();
    }

    private void initializeComponents() {
//        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
//        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP){
//// Do something for lollipop and above versions
//        } else{
//// do something for phones running an SDK before lollipop
//        }
        lblHeader = (TextView) findViewById(R.id.lblHeader);
        txtKullaniciAdi = (EditText) findViewById(R.id.txtKullaniciAdi);
        txtSifre = (EditText) findViewById(R.id.txtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnYeniHesapOlustur = (Button) findViewById(R.id.btnYeniHesapOlustur);
        lblSifremiUnuttum = (TextView) findViewById(R.id.txtSifremiUnuttum);
    }

    private void registerEvents() {

        btnLogin.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            kullanici_adi = txtKullaniciAdi.getText().toString();
                                            sifre = txtSifre.getText().toString();
                                            //Tarih bilgisini almak için
                                            Calendar c = Calendar.getInstance();
                                            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
                                            tarih = format.format(c.getTime());

                                            if (kullanici_adi.matches("")) {
                                                hata_mesaji += getString(R.string.uyari_kul_adi_bos_olamaz);
                                                hata = true;
                                            }
                                            int sifre_karakter = sifre.length();
                                            int kullanici_adi_karakter = kullanici_adi.length();
                                            if (sifre_karakter < 6) {
                                                hata_mesaji += getString(R.string.uyari_sifre_karakter_say_min);
                                                hata = true;
                                            } else if (sifre_karakter > 15) {//şifre karakter sayısı kontrolü
                                                hata_mesaji += getString(R.string.uyari_sifre_karakter_say_max);
                                                hata = true;
                                            } else if (kullanici_adi_karakter < 4) {
                                                hata_mesaji += getString(R.string.uyari_kul_adi_karakter_sayi_min);
                                                hata = true;
                                            } else if (kullanici_adi_karakter > 15) {
                                                hata_mesaji += getString(R.string.uyari_kul_adi_karakter_sayi_max);
                                                hata = true;
                                            } else if (Utilities.containsTurkishCharacters(kullanici_adi)) {
                                                hata_mesaji += getString(R.string.uyari_turkce_karakter_kul_adi);
                                                hata = true;
                                            } else if (Utilities.containsTurkishCharacters(sifre)) {
                                                hata_mesaji += getString(R.string.uyari_turkce_karakter_sifre);
                                                hata = true;
                                            }

                                            if (hata) {//hata varsa AlertDialog ile kullanıcıyı uyarıyoruz.
                                                Utilities.warningAlertDialogPoz(ActivityLogin.this, getString(R.string.hata), hata_mesaji, getString(R.string.tamam), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        txtSifre.setText("");
                                                        hata_mesaji = "";
                                                        hata = false;
                                                    }
                                                });
                                            } else {//Hata yoksa Asynctask classı çağırıyoruz.İşlemlere orda devam ediyoruz
                                                new GirisKontrol().execute();
                                            }


                                        }
                                    }

        );
        btnYeniHesapOlustur.setOnClickListener(new View.OnClickListener()

                                               {
                                                   @Override
                                                   public void onClick(View v) {
                                                       Utilities.utilStartActivity(ActivityLogin.this, ActivitySignUp.class);
                                                   }
                                               }

        );
        lblSifremiUnuttum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.utilStartActivity(ActivityLogin.this, ActivitySifremiUnuttum.class);
            }
        });
    }

    class GirisKontrol extends AsyncTask<Void, Void, Void> {
        private String sonucmesaji;
        ProgressDialog pDialog;

        protected void onPreExecute() {
            pDialog = new ProgressDialog(ActivityLogin.this);
            pDialog.setMessage(getString(R.string.giris_yapiliyor));
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected Void doInBackground(Void... unused) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(ConstantString.VJ_KULLANICI_ADI, kullanici_adi));
            params.add(new BasicNameValuePair(ConstantString.VJ_SIFRE, sifre));
            String json = post.httpPost(ConstantString.URL_POST_LOGIN, ConstantString.POST_METHOD, params, 20000);

            Log.d("Gelen Json", "" + json);//Gelen veriyi logluyoruz.Log Catten kontrol edebiliriz
            try {

                cevap = new JSONObject(json);
                sonucmesaji = cevap.getString(ConstantString.JSON_SONUC_MESAJI);
                if (cevap.getString(ConstantString.JSON_SONUC) != null) {
                    sonuc = cevap.getString(ConstantString.JSON_SONUC);
                    if (Integer.parseInt(sonuc) == 1) { //Eğer giriş başarılı ise
                        //////////KULLANICI//////////
                        JSONObject sifirJson = cevap.getJSONObject(ConstantString.JSON_ARRAY_NUMBER);
                        kullaniciArray = sifirJson.getJSONArray(ConstantString.JSON_ARRAY_KULLANICI);
                        String gelenKullaniciAdi = null;
                        String gelenSifre = null;
                        String gelenEmail = null;
                        String gelenAdres = null;
                        String gelenTelno = null;
                        String gelenAd = null;
                        String gelenSoyad = null;
                        kullaniciVeri = kullaniciArray.getJSONObject(0);
                        gelenKullaniciAdi = kullaniciVeri.getString(ConstantString.VJ_KULLANICI_ADI).toString();
                        gelenSifre = kullaniciVeri.getString(ConstantString.VJ_SIFRE).toString();
                        gelenEmail = kullaniciVeri.getString(ConstantString.VJ_EMAIL).toString();
                        gelenAdres = kullaniciVeri.getString(ConstantString.VJ_ADRES).toString();
                        gelenTelno = kullaniciVeri.getString(ConstantString.VJ_TEL_NO).toString();
                        gelenAd = kullaniciVeri.getString(ConstantString.VJ_AD).toString();
                        gelenSoyad = kullaniciVeri.getString(ConstantString.VJ_SOYAD).toString();
                        //////////KULLANICI//////////
                        Database db = new Database(getApplicationContext());
                        db.resetTables();
                        db.kullaniciEkleDetayli(gelenKullaniciAdi, gelenEmail, gelenSifre, gelenTelno,gelenAdres, gelenAd, gelenSoyad, tarih);    //kullanıcıyı ekliyoruz.Ve Main activitye yönlendiriyoruz.

                        //Sonuc başarılı ise main activitye yönlendirdk.
                        Utilities.clearAndStartActivity(ActivityLogin.this, MainActivity.class);
                        finish();

                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        // Sonuç başarılı ise bu kod çalışmıcak çünkü Main activitye yönlenmiş durumda
        protected void onPostExecute(Void unused) {
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    if (Integer.parseInt(sonuc) == 0) {// Sonuç başarılı değil ise
                        Utilities.warningAlertDialogPoz(ActivityLogin.this, getString(R.string.hata), sonucmesaji, getString(R.string.tamam), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                txtSifre.setText("");
                            }
                        });
                    }
                }


            });
        }
    }
}
