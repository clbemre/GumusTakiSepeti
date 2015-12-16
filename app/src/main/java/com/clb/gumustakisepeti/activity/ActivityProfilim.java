package com.clb.gumustakisepeti.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import java.util.HashMap;
import java.util.List;

/**
 * Created by Emre on 15.10.2015.
 */
public class ActivityProfilim extends AppCompatActivity {

    private Toolbar toolbar;

    TextView prAd;
    TextView prSoyad;
    String kullanici_adi = "";
    TextView prEmail;
    TextView prSifre;
    TextView prTelNo;
    TextView prAdres;
    Button prBtnDuzenle;
    Button prBtnSifreGoster;


    String bndKulAdi;
    String bndSifre;

    ////YENILE////
    String sonuc;
    PostClass post = new PostClass();
    JSONObject cevap = null;
    JSONArray kullaniciArray = null;
    JSONObject kullaniciVeri = null;
    String gelenUserName = null;
    String gelenSifre = null;
    String gelenEmail = null;
    String gelenAdres = null;
    String gelenTelno = null;
    String gelenAd = null;
    String gelenSoyad = null;
    HashMap<String, String> stringStringHashMap = null;
    ////YENILE////

    String helperKulAdi = null;
    String helperSifre = null;
    String helperEmail = null;
    String helperAdres = null;
    String helperTelno = null;
    String helperAd = null;
    String helperSoyad = null;

    HashMap<String, String> kullaniciDetay = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilim);
        //TOOLBAR////
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
//                Utilities.clearAndStartActivity(ActivityProfilim.this, MainActivity.class);
            }
        });
        ///TOOLBAR////
        try {
            initializeComponents();
        } catch (Exception e) {
            e.printStackTrace();
            Utilities.showAlertDialog(getApplicationContext(), "Bir Hata var :S", "Loading Hatası ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
        }
    }


    private void initializeComponents() {
        prAd = (TextView) findViewById(R.id.prTxtAd);
        prSoyad = (TextView) findViewById(R.id.prTxtSoyad);
        prEmail = (TextView) findViewById(R.id.prTxtEmail);
        prSifre = (TextView) findViewById(R.id.prTxtSifre);
        prTelNo = (TextView) findViewById(R.id.prTxtTelNo);
        prAdres = (TextView) findViewById(R.id.prTxtAdres);
        prAdres.setMovementMethod(new ScrollingMovementMethod());
        prBtnDuzenle = (Button) findViewById(R.id.prBtnDuzenle);
        prBtnSifreGoster = (Button) findViewById(R.id.prBtnSifreGoster);

        Bundle extras = getIntent().getExtras();
        bndKulAdi = extras.getString(ConstantString.VJ_KULLANICI_ADI);
        bndSifre = extras.getString(ConstantString.VJ_SIFRE);

        if (!(bndKulAdi.equals("")) || bndKulAdi != null) {
            getSupportActionBar().setTitle(bndKulAdi);
            Database database = new Database(ActivityProfilim.this);
            kullaniciDetay = database.kullaniciDetay();
            helperAd = kullaniciDetay.get("ad");
            helperSoyad = kullaniciDetay.get("soyad");
            helperEmail = kullaniciDetay.get("mail");
            helperSifre = kullaniciDetay.get("sifre");
            helperTelno = kullaniciDetay.get("tel_no");
            helperAdres = kullaniciDetay.get("adres");
            registerEvents();
        } else {
            getSupportActionBar().setTitle(getString(R.string.veri_yok));
        }
    }

    private void registerEvents() {
        ////////NULLABLE////////
        nullableStringCheckAndAdded(helperAd, prAd, ConstantString.TXT_AD);
        nullableStringCheckAndAdded(helperSoyad, prSoyad, ConstantString.TXT_SOYAD);
        nullableStringCheckAndAdded(helperEmail, prEmail, ConstantString.TXT_EMAIL);
        nullableStringCheckAndAdded(helperAdres, prAdres, ConstantString.TXT_ADRES);
        nullableStringCheckAndAdded(helperTelno, prTelNo, ConstantString.TXT_TEL_NO);
        nullableStringCheckAndAdded(helperSifre, prSifre, ConstantString.TXT_SIFRE);
        ////////NULLABLE////////
        prAd.setText(helperAd);
        prSoyad.setText(helperSoyad);
        prAdres.setText(helperAdres);
        prEmail.setText(helperEmail);
        prSifre.setText("*******");
        kullanici_adi = bndKulAdi;
        prTelNo.setText(helperTelno);

        prBtnDuzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityProfilim.this, ActivityProfDuzenle.class);
                Bundle bundle = new Bundle();
                bundle.putString(ConstantString.VJ_AD, prAd.getText().toString());
                bundle.putString(ConstantString.VJ_SOYAD, prSoyad.getText().toString());
                bundle.putString(ConstantString.VJ_EMAIL, prEmail.getText().toString());
                bundle.putString(ConstantString.VJ_SIFRE, helperSifre);
                bundle.putString(ConstantString.VJ_ADRES, prAdres.getText().toString());
                bundle.putString(ConstantString.VJ_TEL_NO, prTelNo.getText().toString());
                bundle.putString(ConstantString.VJ_KULLANICI_ADI, kullanici_adi);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        prBtnSifreGoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prSifre.getText().toString().equals("*******")) {
                    prSifre.setText(helperSifre.toString());
                    prBtnSifreGoster.setText("GİZLE");
                } else if (prSifre.getText().toString().equals(helperSifre)) {
                    prSifre.setText("*******");
                    prBtnSifreGoster.setText("GÖSTER");
                }
            }
        });

    }

    public void nullableStringCheckAndAdded(String check, TextView textView, String text) {
        if (check.equals("")) {
            textView.setText(text);
            textView.setTextColor(Color.RED);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profilim, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            new ProfilimAsync().execute();
            return true;
        }

        if (id == R.id.action_sifre_degistir) {
            Intent intent = new Intent(ActivityProfilim.this, ActivitySifreDegistir.class);
            Bundle bundle = new Bundle();
            bundle.putString(ConstantString.VJ_KULLANICI_ADI, bndKulAdi);
            bundle.putString(ConstantString.VJ_EMAIL, helperEmail);
            bundle.putString(ConstantString.VJ_SIFRE, helperSifre);
            intent.putExtras(bundle);
            startActivity(intent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Utilities.clearAndStartActivity(ActivityProfilim.this, MainActivity.class);
    }

    class ProfilimAsync extends AsyncTask<Void, Void, Void> {
        private String sonucmesaji;
        ProgressDialog pDialog;

        protected void onPreExecute() {
            pDialog = new ProgressDialog(ActivityProfilim.this);
            pDialog.setMessage(getString(R.string.bilgileriniz_getiriliyor));
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected Void doInBackground(Void... unused) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(ConstantString.VJ_KULLANICI_ADI, bndKulAdi));
            params.add(new BasicNameValuePair(ConstantString.VJ_SIFRE, bndSifre));

//            params.add(new BasicNameValuePair("ad", ad));
//            params.add(new BasicNameValuePair("soyad", soyad));
//            params.add(new BasicNameValuePair("email", email));
//            params.add(new BasicNameValuePair("tel_no", tel_no));
//            params.add(new BasicNameValuePair("adres", adres));

            String json = post.httpPost(ConstantString.URL_POST_PROFILIM, ConstantString.POST_METHOD, params, 20000);

            Log.d("Gelen json: ", "" + json);//Gelen veriyi logluyoruz.Log Catten kontrol edebiliriz
            try {
                cevap = new JSONObject(json);
                sonucmesaji = cevap.getString(ConstantString.JSON_SONUC_MESAJI);
                if (cevap.getString(ConstantString.JSON_SONUC) != null) {
                    sonuc = cevap.getString(ConstantString.JSON_SONUC);

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
                    if (Integer.parseInt(sonuc) == 1) { //Eğer giriş başarılı ise
                        //////////KULLANICI//////////
                        try {
                            JSONObject sifirJson = cevap.getJSONObject(ConstantString.JSON_ARRAY_NUMBER);
                            kullaniciArray = sifirJson.getJSONArray(ConstantString.JSON_ARRAY_KULLANICI);
                            kullaniciVeri = kullaniciArray.getJSONObject(0);
                            gelenUserName = kullaniciVeri.getString(ConstantString.VJ_KULLANICI_ADI).toString();
                            gelenSifre = kullaniciVeri.getString(ConstantString.VJ_SIFRE).toString();
                            gelenEmail = kullaniciVeri.getString(ConstantString.VJ_EMAIL).toString();
                            gelenAdres = kullaniciVeri.getString(ConstantString.VJ_ADRES).toString();
                            gelenTelno = kullaniciVeri.getString(ConstantString.VJ_TEL_NO).toString();
                            gelenAd = kullaniciVeri.getString(ConstantString.VJ_AD).toString();
                            gelenSoyad = kullaniciVeri.getString(ConstantString.VJ_SOYAD).toString();

                            Calendar c = Calendar.getInstance();
                            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
                            String tarih = format.format(c.getTime());
                            Database dbUpdate = new Database(getApplicationContext());
                            stringStringHashMap = dbUpdate.kullaniciDetay();
                            String id = stringStringHashMap.get("_id");
                            dbUpdate.kullaniciGuncelle(id, gelenUserName, gelenEmail, gelenSifre, gelenTelno, gelenAdres, gelenAd, gelenSoyad, tarih);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //////////KULLANICI//////////
                        ////////NULLABLE////////
                        nullableStringCheckAndAdded(gelenAd, prAd, ConstantString.TXT_AD);
                        nullableStringCheckAndAdded(gelenSoyad, prSoyad, ConstantString.TXT_SOYAD);
                        nullableStringCheckAndAdded(gelenEmail, prEmail, ConstantString.TXT_EMAIL);
                        nullableStringCheckAndAdded(gelenAdres, prAdres, ConstantString.TXT_ADRES);
                        nullableStringCheckAndAdded(gelenTelno, prTelNo, ConstantString.TXT_TEL_NO);
                        nullableStringCheckAndAdded(gelenSifre, prSifre, ConstantString.TXT_SIFRE);
                        ////////NULLABLE////////

                        prAd.setText(gelenAd);
                        prSoyad.setText(gelenSoyad);
                        prAdres.setText(gelenAdres);
                        prEmail.setText(gelenEmail);
                        prSifre.setText("*******");
                        prTelNo.setText(gelenTelno);
                    } else if (Integer.parseInt(sonuc) == 0) {// Sonuç başarılı değil ise
                        Utilities.warningAlertDialogPoz(ActivityProfilim.this, getString(R.string.hata), "Lütfen Tekrar Giriş Yapın.", getString(R.string.tamam), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Database db = new Database(getApplicationContext());
                                db.resetTables();
                                Utilities.clearAndStartActivity(ActivityProfilim.this, MainActivity.class);
                            }
                        });
                    }
                }
            });
        }
    }


}
