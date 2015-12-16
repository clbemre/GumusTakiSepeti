package com.clb.gumustakisepeti.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clb.gumustakisepeti.R;
import com.clb.gumustakisepeti.database.PostClass;
import com.clb.gumustakisepeti.util.ConstantString;
import com.clb.gumustakisepeti.util.ImageLoader;
import com.clb.gumustakisepeti.util.Utilities;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emre on 18.09.2015.
 */
public class Activity_Siparis_Detay extends AppCompatActivity {

    private Toolbar toolbar;
    Bundle extras;

    ImageView sdImgSiparis;
    TextView tvSDUrunAdi;
    TextView tvSDKargoDurumu;
    TextView tvSDFiyat;
    TextView tvSDAdres;
    TextView tvSDAdSoyad;
    TextView tvSDTelNo;
    TextView tvSDSiparisKodu;
    TextView tvSDMesaj;
    TextView tvSDAdet;

    int siparisID = -1;
    String bndSiparisAdi;
    float btnSiparisFiyat;
    String bndSkAdres;
    String bndImageUrl;
    String bndSkAd;
    String bndSkSoyad;
    String bndSkTelNo;
    int bndSiparisDurumu;
    String bndSiparisKodu;
    String bndSiparisMesaj;
    int bndSiparisAdet;
    float bndSiparisFiyat;
    String dialogURL = "";


    String sonuc;
    PostClass post = new PostClass();
    JSONObject cevap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siparis_detay);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        extras = getIntent().getExtras();

        initialComponents(); //
        registerEvents();
        if (extras != null) {

            getSupportActionBar().setTitle("Sipariş Detay");
            siparisID = extras.getInt(ConstantString.S_ID);
            bndSiparisAdi = extras.getString(ConstantString.SIPARIS_ADI);
            btnSiparisFiyat = extras.getFloat(ConstantString.SIPARIS_FIYAT);
            bndSkAdres = extras.getString(ConstantString.SK_ADRES);
            bndImageUrl = extras.getString(ConstantString.SIPARIS_IMAGE_URL);
            bndSkAd = extras.getString(ConstantString.SK_AD);
            bndSkSoyad = extras.getString(ConstantString.SK_SOYAD);
            bndSkTelNo = extras.getString(ConstantString.SK_TEL_NO);
            bndSiparisDurumu = extras.getInt(ConstantString.SIPARIS_DURUMU);
            bndSiparisKodu = extras.getString(ConstantString.SIPARIS_KODU);
            bndSiparisMesaj = extras.getString(ConstantString.SIPARIS_MESAJ);
            bndSiparisAdet = extras.getInt(ConstantString.SIPARIS_ADET);
            bndSiparisFiyat = extras.getFloat(ConstantString.SIPARIS_FIYAT);

            tvSDUrunAdi.setText(bndSiparisAdi);
            int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < Build.VERSION_CODES.LOLLIPOP) {
                tvSDFiyat.setText("Fiyat : " + String.valueOf((int) bndSiparisFiyat) + " TL");
            } else {
                tvSDFiyat.setText("Fiyat : " + String.valueOf((int) bndSiparisFiyat) + " ₺");
            }

            tvSDAdres.setText(bndSkAdres);
            tvSDAdSoyad.setText(bndSkAd + " " + bndSkSoyad);
            tvSDTelNo.setText(bndSkTelNo);
            if (bndSiparisDurumu == 0) {
                tvSDKargoDurumu.setText("Kargo Durumu : Onay Bekliyor");
            } else if (bndSiparisDurumu == 1) {
                tvSDKargoDurumu.setBackgroundColor(Color.parseColor("#2ddb2d"));
                tvSDKargoDurumu.setText("Kargo Durumu : Kargoya Verildi");
            } else if (bndSiparisDurumu == 2) {
                tvSDKargoDurumu.setBackgroundColor(Color.parseColor("#2ddb2d"));
                tvSDKargoDurumu.setText("Kargo Durumu : Teslim Edildi");
            }
            tvSDSiparisKodu.setText(bndSiparisKodu);
            tvSDMesaj.setText(bndSiparisMesaj);
            tvSDAdet.setText(bndSiparisAdet + "");
            ImageLoader.loader.displayImage(bndImageUrl, sdImgSiparis, ImageLoader.options);
            dialogURL = bndImageUrl;
        }

    }

    private void registerEvents() {

    }

    private void fillTextView(int id, String text) {
        TextView tv = (TextView) findViewById(id);
        tv.setText(text);
    }

    private void initialComponents() {
        sdImgSiparis = (ImageView) findViewById(R.id.sdImgSiparis);
//        sdImgSiparis.setOnTouchListener(this);//
        tvSDUrunAdi = (TextView) findViewById(R.id.tvSDUrunAdi);
        tvSDKargoDurumu = (TextView) findViewById(R.id.tvSDKargoDurumu);
        tvSDFiyat = (TextView) findViewById(R.id.tvSDFiyat);
        tvSDAdres = (TextView) findViewById(R.id.tvSDAdres);
        tvSDAdSoyad = (TextView) findViewById(R.id.tvSDAdSoyad);
        tvSDTelNo = (TextView) findViewById(R.id.tvSDTelNo);
        tvSDSiparisKodu = (TextView) findViewById(R.id.tvSDSiparisKodu);
        tvSDMesaj = (TextView) findViewById(R.id.tvSDMesaj);
        tvSDAdet = (TextView) findViewById(R.id.tvSDAdet);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (bndSiparisDurumu == 0) {
            getMenuInflater().inflate(R.menu.menu_ob_siparis, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (bndSiparisDurumu == 0) {
            int id = item.getItemId();

            if (id == R.id.action_siparis_iptal) {
                Utilities.warningAlertDialog(Activity_Siparis_Detay.this, "Sipariş İptali", "Onaylıyor musunuz ? ", "Evet", "Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (extras != null) {
                            if (siparisID != -1) {
                                new siparisİptalAsync().execute();
                            } else {
                                Utilities.showToastMessage(Activity_Siparis_Detay.this, "Beklenmeyen Hata");
                            }
                        }
                    }
                }, null);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    class siparisİptalAsync extends AsyncTask<Void, Void, Void> {
        private String sonucmesaji;
        ProgressDialog pDialog;

        protected void onPreExecute() {
            //  progress dialog
            pDialog = new ProgressDialog(Activity_Siparis_Detay.this);
            pDialog.setMessage("Siliniyor");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected Void doInBackground(Void... unused) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(ConstantString.S_ID, String.valueOf(siparisID)));
            params.add(new BasicNameValuePair(ConstantString.SIPARIS_KODU, bndSiparisKodu));

            String json = post.httpPost(ConstantString.URL_POST_SIPARIS_IPTAL_ET, ConstantString.POST_METHOD, params, 20000);

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
                        Utilities.warningAlertDialogPoz(Activity_Siparis_Detay.this, "Silinemedi", sonucmesaji, getString(R.string.tamam), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                    } else if (Integer.parseInt(sonuc) == 1) {
                        Utilities.utilStartActivity(Activity_Siparis_Detay.this, ActivityOnayBekleyenSiparis.class);
                        finish();
                    }
                }


            });
        }
    }

}
