package com.clb.gumustakisepeti.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.clb.gumustakisepeti.MainActivity;
import com.clb.gumustakisepeti.R;
import com.clb.gumustakisepeti.database.Database;
import com.clb.gumustakisepeti.database.PostClass;
import com.clb.gumustakisepeti.util.ConstantString;
import com.clb.gumustakisepeti.util.Fonksiyonlar;
import com.clb.gumustakisepeti.util.Utilities;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Emre on 14.10.2015.
 */
public class ActivitySignUp extends AppCompatActivity {

    private Toolbar toolbar;
    EditText edtKullaniciAdi;
    EditText edtEmail;
    EditText edtSifre;
    Button btnKayitOl;

    String kullanici_adi;
    String sifre;
    String email;
    String hata_mesaji;
    boolean hata = false;

    PostClass post = new PostClass();
    JSONObject cevap = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
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
        edtKullaniciAdi = (EditText) findViewById(R.id.edKullaniciAdi);
        edtEmail = (EditText) findViewById(R.id.edEmail);
        edtSifre = (EditText) findViewById(R.id.edSifre);
        btnKayitOl = (Button) findViewById(R.id.btUyeOl);
    }

    private void registerEvents() {
        btnKayitOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kullanici_adi = edtKullaniciAdi.getText().toString();
                email = edtEmail.getText().toString();
                sifre = edtSifre.getText().toString();
                int kullanici_adi_karakter = kullanici_adi.length();
                int sifre_karakter = sifre.length();
                if (kullanici_adi.matches("") || email.matches("") || sifre.matches("")) {// boş veri var mı kontrolü
                    hata = true;
                    hata_mesaji = getString(R.string.uyari_gerekli_alanlar_bos_olamaz);
                } else if (sifre_karakter < 6) {//şifre karakter sayısı kontrolü
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
                } else if (!Fonksiyonlar.isEmailValid(email)) {//Mail format kontrol
                    hata_mesaji += getString(R.string.uyari_yanlis_email_format);
                    hata = true;
                } else if (Utilities.containsTurkishCharacters(kullanici_adi)) {
                    hata_mesaji += getString(R.string.uyari_turkce_karakter_kul_adi);
                    hata = true;
                } else if (Utilities.containsTurkishCharacters(sifre)) {
                    hata_mesaji += getString(R.string.uyari_turkce_karakter_sifre);
                    hata = true;
                }

                if (hata) {//hata varsa hatayı alertDialog ile gösteriyoruz
                    Utilities.warningAlertDialogPoz(ActivitySignUp.this, getString(R.string.hata), hata_mesaji, getString(R.string.tamam), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            edtSifre.setText("");
                            hata_mesaji = "";
                            hata = false;
                        }
                    });
                } else {//hata yoksa
                    new KayitOl().execute();//KayıtOl asynctask classı cagırıyoruz
                }

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    class KayitOl extends AsyncTask<Void, Void, Void> {
        private String sonucmesaji, sonuc, tarih;
        ProgressDialog pDialog;

        protected void onPreExecute() {
            //  progress dialog
            pDialog = new ProgressDialog(ActivitySignUp.this);
            pDialog.setMessage(getString(R.string.kayit_islemi_gerceklestiriliyor));
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected Void doInBackground(Void... unused) {

//            sifre = Fonksiyonlar.sha1(sifre);//şifreyi sha1 ile şifreledik

            //Parametreleri ekliyoruz
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(ConstantString.VJ_KULLANICI_ADI, kullanici_adi));
            params.add(new BasicNameValuePair(ConstantString.VJ_SIFRE, sifre));
            params.add(new BasicNameValuePair(ConstantString.VJ_EMAIL, email));

            Calendar c = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
            tarih = format.format(c.getTime());

            String json = post.httpPost(ConstantString.URL_POST_REGISTER, ConstantString.POST_METHOD, params, 20000);

            Log.d("Gelen Json", "" + json);//Gelen veriyi logluyoruz.Log Catten kontrol edebiliriz
            try {

                cevap = new JSONObject(json);
                sonucmesaji = cevap.getString(ConstantString.JSON_SONUC_MESAJI);

                if (cevap.getString(ConstantString.JSON_SONUC) != null) {
                    sonuc = cevap.getString(ConstantString.JSON_SONUC);
                    if (Integer.parseInt(sonuc) == 1) { //Eğer kayıt başarılı ise
//                        Sqlite database baglanıp gerekli verileri kaydediyoruz.
                        Database db = new Database(getApplicationContext());
                        db.resetTables();
                        //kullanıcıyı ekliyoruz.Ve Main activitye yönlendiriyoruz.

                        //Sonuc başarılı ise main activitye yönlendirdk.
                        db.kullaniciEkleDetayli(kullanici_adi, email, sifre, "", "", "", "", tarih);    //kullanıcıyı ekliyoruz.Ve Main activitye yönlendiriyoruz.

                        //Sonuc başarılı ise main activitye yönlendirdk.
                        Utilities.clearAndStartActivity(ActivitySignUp.this, MainActivity.class);
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
            // closing progress dialog
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    if (Integer.parseInt(sonuc) == 0) {// Sonuç başarılı değil ise
                        Utilities.warningAlertDialogPoz(ActivitySignUp.this, getString(R.string.hata), sonucmesaji, getString(R.string.tamam), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                edtSifre.setText("");
                            }
                        });
                    }
                }
            });
        }
    }
}