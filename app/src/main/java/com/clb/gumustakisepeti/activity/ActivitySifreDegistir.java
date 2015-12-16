package com.clb.gumustakisepeti.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;

import com.clb.gumustakisepeti.MainActivity;
import com.clb.gumustakisepeti.R;
import com.clb.gumustakisepeti.database.Database;
import com.clb.gumustakisepeti.database.PostClass;
import com.clb.gumustakisepeti.util.ConstantString;
import com.clb.gumustakisepeti.util.Utilities;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Emre on 15.10.2015.
 */
public class ActivitySifreDegistir extends AppCompatActivity {

    private Toolbar toolbar;

    String prKulAdi = "";
    TextView prKullaniciAdi;
    TextView prEskiSifre;
    EditText prYeniSifre;
    EditText prYeniSifreKontrol;
    TextView prEmail;
    Button prBtnKaydet;


    String sonuc;
    PostClass post = new PostClass();
    JSONObject cevap = null;

    String tarih;


    String bndEmail;
    String bndKulAdi;
    String bndSifre;


    String stringEmail = "";
    String stringKulAdi = "";
    String stringSifre = "";
    String stringOnaySifre = "";

    HashMap<String, String> stringStringHashMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sifre_degistir);
        //TOOLBAR////
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
//                Utilities.clearAndStartActivity(ActivityProfDuzenle.this, ActivityProfilim.class);
            }
        });
        ///TOOLBAR////
        initializeComponents();


    }


    private void initializeComponents() {
        prEmail = (TextView) findViewById(R.id.prTxtEmail);
        prKullaniciAdi = (TextView) findViewById(R.id.prTxtKulAdi);
        prEskiSifre = (TextView) findViewById(R.id.prTxtEskiSifre);
        prYeniSifre = (EditText) findViewById(R.id.prTxtYeniSifre);
        prYeniSifreKontrol = (EditText) findViewById(R.id.prTxtYeniSifreTekrar);
        prBtnKaydet = (Button) findViewById(R.id.prBtnKaydet);

        Bundle extras = getIntent().getExtras();
        bndEmail = extras.getString(ConstantString.VJ_EMAIL);
        bndKulAdi = extras.getString(ConstantString.VJ_KULLANICI_ADI);
        bndSifre = extras.getString(ConstantString.VJ_SIFRE);

        stringKulAdi = bndKulAdi;
        stringSifre = bndSifre;
        stringEmail = bndEmail;
        if (!(bndKulAdi.equals("")) || bndKulAdi != null) {
            registerEvents();
            getSupportActionBar().setTitle(bndKulAdi);
            prBtnKaydet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (prYeniSifre.getText().toString().equals(prYeniSifreKontrol.getText().toString())) {
                        stringOnaySifre = prYeniSifre.getText().toString();
                        if (stringOnaySifre.length() < 6) {//şifre karakter sayısı kontrolü
                            sifreDurumlarıKontrol(getString(R.string.uyari_sifre_karakter_say_min));
                        } else if (stringOnaySifre.length() > 15) {//şifre karakter sayısı kontrolü
                            sifreDurumlarıKontrol(getString(R.string.uyari_sifre_karakter_say_max));
                        } else {
                            if (Utilities.containsTurkishCharacters(stringOnaySifre)) {
                                sifreDurumlarıKontrol(getString(R.string.uyari_turkce_karakter_sifre));
                            } else {
                                new SifreDegistirAsync().execute();
                            }
                        }
                    } else {
                        sifreDurumlarıKontrol(getString(R.string.sifreler_eslesmiyor));
                    }
                }
            });
        } else {
            getSupportActionBar().setTitle(getString(R.string.veri_yok));
        }
    }

    public void sifreDurumlarıKontrol(String mesaj) {
        Utilities.showAlertDialog(ActivitySifreDegistir.this, getString(R.string.hata), mesaj, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                prYeniSifre.setText("");
                prYeniSifreKontrol.setText("");
            }
        });
    }

    private void registerEvents() {
        prEmail.setText(bndEmail);
        prKulAdi = bndKulAdi;
        prKullaniciAdi.setText(prKulAdi);
        prEskiSifre.setText(bndSifre);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    class SifreDegistirAsync extends AsyncTask<Void, Void, Void> {
        private String sonucmesaji;
        ProgressDialog pDialog;

        protected void onPreExecute() {
            //  progress dialog
            pDialog = new ProgressDialog(ActivitySifreDegistir.this);
            pDialog.setMessage(getString(R.string.guncelleme_yapiliyor));
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected Void doInBackground(Void... unused) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(ConstantString.VJ_KULLANICI_ADI, stringKulAdi));
            params.add(new BasicNameValuePair(ConstantString.VJ_SIFRE, stringSifre));
            params.add(new BasicNameValuePair(ConstantString.VJ_EMAIL, stringEmail));
            params.add(new BasicNameValuePair("yenisifre", stringOnaySifre));

            String json = post.httpPost(ConstantString.URL_POST_SIFRE_DEGISTIR, ConstantString.POST_METHOD, params, 20000);

            Log.d("Gelen Json", "" + json);//Gelen veriyi logluyoruz.Log Catten kontrol edebiliriz
            try {

                cevap = new JSONObject(json);
                sonucmesaji = cevap.getString(ConstantString.JSON_SONUC_MESAJI);
                if (cevap.getString(ConstantString.JSON_SONUC) != null) {
                    sonuc = cevap.getString(ConstantString.JSON_SONUC);
//                    if (Integer.parseInt(sonuc) == 1) { //Eğer giriş başarılı ise
//
//                    }
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
                        Utilities.warningAlertDialogPoz(ActivitySifreDegistir.this, getString(R.string.guncelleme_yapilmadi), sonucmesaji, getString(R.string.tamam), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                    } else if (Integer.parseInt(sonuc) == 1) {
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
                        String tarih = format.format(c.getTime());
                        Database dbUpdate = new Database(getApplicationContext());
                        stringStringHashMap = dbUpdate.kullaniciDetay();
                        String id = stringStringHashMap.get("_id");
                        dbUpdate.kullaniciSifreGuncelle(id,stringOnaySifre, tarih);
                        //
                        Utilities.warningAlertDialogPoz(ActivitySifreDegistir.this, getString(R.string.guncelleme), "Sonuç : Şifre Değiştirildi\n", getString(R.string.tamam), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(ActivitySifreDegistir.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                }


            });
        }
    }
}
