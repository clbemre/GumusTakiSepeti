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
import com.clb.gumustakisepeti.pojo.Siparis;
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
public class ActivitySepetSiparisVer extends AppCompatActivity {

    private Toolbar toolbar;
    PostClass post = new PostClass();
    JSONObject cevap = null;


    EditText edtSkAd;
    EditText edtSkSoyAd;
    EditText edtSkTelNo;
    EditText edtSkEmail;
    EditText edtSkTeslimatAdresi;
    TextView tvSiparisFiyati;
    Button btnSiparisVazgec;
    Button btnSiparisiOnayla;


    HashMap<String, String> kulDetay = null;
    String helperKulAdi = null;
    String helperSifre = null;
    String helperEmail = null;
    String helperAdres = null;
    String helperTelno = null;
    String helperAd = null;
    String helperSoyad = null;


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
    String stSonFiyat;
    String stSiparisKodu = "";
    String stSiparisTarih;

    float toplamFiyat = 0f;

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
        setContentView(R.layout.activity_sepet_siparis_ver);
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
        edtSkAd = (EditText) findViewById(R.id.edtSkAd);
        edtSkSoyAd = (EditText) findViewById(R.id.edtSkSoyAd);
        edtSkTelNo = (EditText) findViewById(R.id.edtSkTelNo);
        edtSkEmail = (EditText) findViewById(R.id.edtSkEmail);
        edtSkTeslimatAdresi = (EditText) findViewById(R.id.edtSkTeslimatAdresi);
        tvSiparisFiyati = (TextView) findViewById(R.id.tvSiparisFiyati);
        btnSiparisVazgec = (Button) findViewById(R.id.btnSiparisVazgec);
        btnSiparisiOnayla = (Button) findViewById(R.id.btnSiparisiOnayla);


        Bundle extras = getIntent().getExtras();


        if (extras != null) {
            toplamFiyat = extras.getFloat("ToplamFiyat");
            getSupportActionBar().setTitle("Alışveriş Tamamla " + (int) toplamFiyat + " TL");
            tvSiparisFiyati.setText((int) toplamFiyat + "");

            if (Fonksiyonlar.giriskontrol(getApplicationContext())) {
                Database database = new Database(ActivitySepetSiparisVer.this);
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
        } else {
            getSupportActionBar().setTitle("Ürün Seçiminde Hata Oluştu");
        }

    }

    private void registerEvents() {
        btnSiparisiOnayla.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     stKisiAdi = edtSkAd.getText().toString().trim();
                                                     stKisiSoyadi = edtSkSoyAd.getText().toString().trim();
                                                     stKisiTelNo = edtSkTelNo.getText().toString().trim();
                                                     stKisiEmail = edtSkEmail.getText().toString().trim();
                                                     stKisiTeslimatAdresi = edtSkTeslimatAdresi.getText().toString().trim();
                                                     stSonFiyat = tvSiparisFiyati.getText().toString().trim();

                                                     if (Fonksiyonlar.giriskontrol(getApplicationContext())) {
                                                         stSkKulAdi = helperKulAdi;
                                                     } else {
                                                         stSkKulAdi = "Misafir Kullanıcı";
                                                     }

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


                                                     if (!(hata.equals("")))

                                                     {
                                                         Utilities.showToastMessageLengthLong(ActivitySepetSiparisVer.this, hata);
                                                     } else

                                                     {
                                                         ////////SIPARIS KODU OLUSTURMA KODU////////
                                                         String basKisim = randomCharCheckTurkishCharacter();
                                                         String sonKisim = randomCharCheckTurkishCharacter();
                                                         Random rnd = new Random();
                                                         int randomSayi;
                                                         String sayiS;
                                                         do {
                                                             randomSayi = rnd.nextInt();
                                                             sayiS = String.valueOf(randomSayi);
                                                         }
                                                         while (randomSayi <= 0 || sayiS.length() != 6);

                                                         stSiparisKodu = basKisim + randomSayi + sonKisim;
                                                         ////////SIPARIS KODU OLUSTURMA KODU////////
                                                         if (toplamFiyat == 0f) {
                                                             Utilities.showToastMessage(ActivitySepetSiparisVer.this, "Beklenmeyen Hata Tekrar Deneyin");
                                                         } else {
                                                             Utilities.showAlertDialog(ActivitySepetSiparisVer.this, "Opps :) ", "Beta Sürümü-Sipariş Verilemiyor.", new DialogInterface.OnClickListener() {
                                                                 @Override
                                                                 public void onClick(DialogInterface dialog, int which) {
                                                                     finish();
                                                                 }
                                                             });
                                                            // new SiparisVerAsync().execute();
                                                         }

                                                     }


                                                 }
                                             }

        );
        btnSiparisVazgec.setOnClickListener(new View.OnClickListener()

                                            {
                                                @Override
                                                public void onClick(View v) {
                                                    finish();
                                                }
                                            }

        );
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
            pDialog = new ProgressDialog(ActivitySepetSiparisVer.this);
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
                Database database1 = new Database(getApplicationContext());
                List<Siparis> allSiparisSepet = database1.getAllSiparisSepet();
                List<Siparis> allSiparisSepetSonHali = new ArrayList<Siparis>();
                JSONObject sepetSiparisleriJSONOBJECT = null;
                for (Siparis siparis : allSiparisSepet) {
                    Siparis spSon = new Siparis();
                    String skKulAdi;
                    String skAd;
                    String skSoyad;
                    String skTelNo;
                    String skEmail;
                    String skAdres;
                    String siparisObjectId;
                    String siparisAdi;
                    int siparisAdet;
                    String siparisImageUrl;
                    int siparisDurumu;
                    float siparisFiyat;
                    String siparisMesaj;
                    String siparisKodu;

                    skKulAdi = siparis.getSkKulAdi();
                    siparisObjectId = siparis.getSiparisObjectId();
                    siparisAdi = siparis.getSiparisAdi();
                    siparisAdet = siparis.getSiparisAdet();
                    siparisImageUrl = siparis.getSiparisImageUrl();
                    siparisDurumu = siparis.getSiparisDurumu();
                    siparisFiyat = siparis.getSiparisFiyat();
                    siparisMesaj = siparis.getSiparisMesaj();
                    siparisKodu = siparis.getSiparisKodu();

                    spSon.setSkKulAdi(skKulAdi);
                    spSon.setSkAd(stKisiAdi);
                    spSon.setSkSoyad(stKisiSoyadi);
                    spSon.setSkTelNo(stKisiTelNo);
                    spSon.setSkEmail(stKisiEmail);
                    spSon.setSkAdres(stKisiTeslimatAdresi);
                    spSon.setSiparisObjectId(siparisObjectId);
                    spSon.setSiparisAdi(siparisAdi);
                    spSon.setSiparisAdet(siparisAdet);
                    spSon.setSiparisImageUrl(siparisImageUrl);
                    spSon.setSiparisDurumu(siparisDurumu);
                    spSon.setSiparisFiyat(siparisFiyat);
                    spSon.setSiparisMesaj(siparisMesaj);
                    spSon.setSiparisMesaj(siparisMesaj);
                    spSon.setSiparisKodu(siparisKodu);
                    spSon.setSiparisTarih(tarih);
                    allSiparisSepetSonHali.add(spSon);
                }
//                if (allSiparisSepetSonHali != null) {
                try {
                    sepetSiparisleriJSONOBJECT = new JSONObject();
                    JSONArray jsArray = new JSONArray();
                    jsArray.put(allSiparisSepetSonHali);
                    sepetSiparisleriJSONOBJECT.put("Siparis", jsArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String string = sepetSiparisleriJSONOBJECT.toString();
                string.replace("&quot;", "'");
//                }
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("Siparis", string));
                String json = post.httpPost(ConstantString.URL_POST_SEPET_SIPARIS_VER, ConstantString.POST_METHOD, params, 20000);
//                post.sendJson(sepetSiparisleriJSONOBJECT);
//                Log.d("JSONJSON", sepetSiparisleriJSONOBJECT.toString());
//                 post.postData(ConstantString.URL_POST_SEPET_SIPARIS_VER, sepetSiparisleriJSONOBJECT);
                Log.d("GİDEN JSON ", string + "");
                Log.d("GELEN JSON ", json + "");
//                Log.d("JSON JSON ", sepetSiparisleriJSONOBJECT.toString());
            }
            return null;
        }

        // Sonuç başarılı ise bu kod çalışmıcak çünkü Main activitye yönlenmiş durumda

        protected void onPostExecute(Void unused) {
            // closing progress dialog
            pDialog.dismiss();

        }
    }

}
