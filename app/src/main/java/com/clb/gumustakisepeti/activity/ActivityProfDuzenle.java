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
public class ActivityProfDuzenle extends AppCompatActivity {

    private Toolbar toolbar;

    EditText prAd;
    EditText prSoyad;
    String prKulAdi = "";
    TextView prEmail;
    EditText prTelNo;
    EditText prAdres;
    Button prBtnKaydet;


    String sonuc;
    PostClass post = new PostClass();
    JSONObject cevap = null;

    String bndAd;
    String bndSoyad;
    String bndEmail;
    String bndTelNo;
    String bndAdres;
    String bndKulAdi;
    String bndSifre;

    String gelenUserName = null;
    String gelenSifre = null;
    String gelenEmail = null;
    String gelenAdres = null;
    String gelenTelno = null;
    String gelenAd = null;
    String gelenSoyad = null;


    String stringAD = "";
    String stringSoyad = "";
    String stringAdres = "";
    String stringTelNo = "";

    String stringEmail = "";
    String stringKulAdi = "";
    String stringSifre = "";

    HashMap<String, String> stringStringHashMap = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_duzenle);
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
        prAd = (EditText) findViewById(R.id.prTxtAd);
        prSoyad = (EditText) findViewById(R.id.prTxtSoyad);
        prEmail = (TextView) findViewById(R.id.prTxtEmail);
        prTelNo = (EditText) findViewById(R.id.prTxtTelNo);
        prAdres = (EditText) findViewById(R.id.prTxtAdres);
        prBtnKaydet = (Button) findViewById(R.id.prBtnKaydet);

        Bundle extras = getIntent().getExtras();
        bndAd = extras.getString(ConstantString.VJ_AD);
        bndSoyad = extras.getString(ConstantString.VJ_SOYAD);
        bndEmail = extras.getString(ConstantString.VJ_EMAIL);
        bndAdres = extras.getString(ConstantString.VJ_ADRES);
        bndKulAdi = extras.getString(ConstantString.VJ_KULLANICI_ADI);
        bndSifre = extras.getString(ConstantString.VJ_SIFRE);
        bndTelNo = extras.getString(ConstantString.VJ_TEL_NO);

        stringKulAdi = bndKulAdi;
        stringSifre = bndSifre;
        stringEmail = bndEmail;
        if (!(bndKulAdi.equals("")) || bndKulAdi != null) {
            registerEvents();
            getSupportActionBar().setTitle(bndKulAdi);

            prBtnKaydet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAllTextToString();
                    if (stringAD.equals("")) {
                        Utilities.showToastMessage(getApplicationContext(), "BOŞ AD");
                    } else {

                        new ProfilimAsync().execute();
                    }

                }
            });
        } else {
            getSupportActionBar().setTitle(getString(R.string.veri_yok));
        }
    }

    private void registerEvents() {
        prAd.setText(bndAd);
        prSoyad.setText(bndSoyad);
        prAdres.setText(bndAdres);
        prEmail.setText(bndEmail);
        prKulAdi = bndKulAdi;
        prTelNo.setText(bndTelNo);
    }

    public void getAllTextToString() {
        stringAD = prAd.getText().toString().trim();
        stringSoyad = prSoyad.getText().toString().trim();
        stringAdres = prAdres.getText().toString().trim();
        stringTelNo = prTelNo.getText().toString().trim();
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

    class ProfilimAsync extends AsyncTask<Void, Void, Void> {
        private String sonucmesaji;
        ProgressDialog pDialog;

        protected void onPreExecute() {
            //  progress dialog
            pDialog = new ProgressDialog(ActivityProfDuzenle.this);
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
            params.add(new BasicNameValuePair(ConstantString.VJ_AD, stringAD));
            params.add(new BasicNameValuePair(ConstantString.VJ_SOYAD, stringSoyad));
            params.add(new BasicNameValuePair(ConstantString.VJ_EMAIL, stringEmail));
            params.add(new BasicNameValuePair(ConstantString.VJ_TEL_NO, stringTelNo));
            params.add(new BasicNameValuePair(ConstantString.VJ_ADRES, stringAdres));

            String json = post.httpPost(ConstantString.URL_POST_PROF_DUZENLE, ConstantString.POST_METHOD, params, 20000);

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
                        Utilities.warningAlertDialogPoz(ActivityProfDuzenle.this, getString(R.string.guncelleme_yapilmadi), sonucmesaji, getString(R.string.tamam), new DialogInterface.OnClickListener() {
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
                        dbUpdate.kullaniciGuncelleTwo(id, stringTelNo, stringAdres, stringAD, stringSoyad, tarih);
                        //Tarih bilgisini almak için
                        Utilities.warningAlertDialogPoz(ActivityProfDuzenle.this, getString(R.string.guncelleme), getString(R.string.sonuc_basarili), getString(R.string.tamam), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(ActivityProfDuzenle.this, ActivityProfilim.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
//                                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                                Bundle bundle = new Bundle();
                                bundle.putString(ConstantString.VJ_KULLANICI_ADI, stringKulAdi);
                                bundle.putString(ConstantString.VJ_SIFRE, stringSifre);
                                intent.putExtras(bundle);
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