package com.clb.gumustakisepeti.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.clb.gumustakisepeti.R;
import com.clb.gumustakisepeti.adapter.SiparisRecyclerViewAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Admin on 04-06-2015.
 */
public class ActivityEskiSiparis extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "GTSSEskiSiparisCardViewActivity";
    ProgressDialog mProgressDialog;

    String sonuc;
    PostClass post = new PostClass();
    HashMap<String, String> kullaniciDetay = null;
    private List<Siparis> siparisAllList = null;
    String helperKulAdi = null;
    String helperSifre = null;
    String helperEmail = null;
    String helperAdres = null;
    String helperTelno = null;
    String helperAd = null;
    String helperSoyad = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eski_siparis);
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
        if (Utilities.isNetworkAvailable(ActivityEskiSiparis.this)) {
            if (Fonksiyonlar.giriskontrol(ActivityEskiSiparis.this)) {
                Database database = new Database(ActivityEskiSiparis.this);
                kullaniciDetay = database.kullaniciDetay();
                helperKulAdi = kullaniciDetay.get(ConstantString.SQ_KULLANICI_ADI);
                helperAd = kullaniciDetay.get(ConstantString.SQ_AD);
                helperSoyad = kullaniciDetay.get(ConstantString.SQ_SOYAD);
                helperEmail = kullaniciDetay.get(ConstantString.SQ_KULLANICI_MAIL);
                helperSifre = kullaniciDetay.get(ConstantString.SQ_KULLANICI_SIFRE);
                helperTelno = kullaniciDetay.get(ConstantString.SQ_KULLANICI_TEL_NO);
                helperAdres = kullaniciDetay.get(ConstantString.SQ_KULLANICI_ADRES);
            }
            if (helperKulAdi != null || helperEmail != null) {
                getSupportActionBar().setTitle("Eski Siparişlerim");
                new RemoteDataTask().execute();
            } else {
                getSupportActionBar().setTitle("Veri Yok");
            }
        } else {
            Utilities.showAlertDialog(ActivityEskiSiparis.this, getString(R.string.opps),
                    getString(R.string.check_network_connection),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityEskiSiparis.this.finish();
                        }
                    });
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_ob_siparis);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(ActivityEskiSiparis.this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        initializeComponents();
        registerEvents();
    }

    private void initializeComponents() {

    }

    private void registerEvents() {

    }


    // RemoteDataTask AsyncTask
    class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        private String sonucmesaji;
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(ActivityEskiSiparis.this);
            mProgressDialog.setTitle(getString(R.string.app_name));
            mProgressDialog.setMessage(getString(R.string.yukleniyor));
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            List<NameValuePair> paramss = new ArrayList<NameValuePair>();
            paramss.add(new BasicNameValuePair(ConstantString.SK_KUL_ADI, helperKulAdi));
            paramss.add(new BasicNameValuePair(ConstantString.SK_EMAIL, helperEmail));

            String json = post.httpPost(ConstantString.SIPARISLERIM_ESKI_DOWNLOAD_URL, ConstantString.POST_METHOD, paramss, 20000);

            Log.d("Gelen Json", "" + json);//Gelen veriyi logluyoruz.Log Catten kontrol edebiliriz

            siparisAllList = new ArrayList<Siparis>();
            JSONObject jsonObject = null;
            JSONArray jsonArray = null;
            JSONObject siparisObject = null;
            int length = 0;

            try {
                jsonObject = new JSONObject(json);
                sonucmesaji = jsonObject.getString(ConstantString.JSON_SONUC_MESAJI);
                if (jsonObject.getString(ConstantString.JSON_SONUC) != null) {
                    sonuc = jsonObject.getString(ConstantString.JSON_SONUC);
                    if (Integer.parseInt(sonuc) == 1) {
                        JSONObject sifirJson = jsonObject.getJSONObject(ConstantString.JSON_ARRAY_NUMBER);
                        jsonArray = sifirJson.getJSONArray("siparislerim");
                        length = jsonArray.length();

                        for (int i = 0; i < length; i++) {
                            Siparis mapSiparis = new Siparis();
                            siparisObject = jsonArray.getJSONObject(i);

                            int vtSiparisDurumu = Integer.valueOf(siparisObject.getString(ConstantString.SIPARIS_DURUMU));
                            if (vtSiparisDurumu == 2) {
                                int vtSiparisID = siparisObject.getInt(ConstantString.S_ID);
                                String vtSkKulAdi = (String) siparisObject.getString(ConstantString.SK_KUL_ADI);
                                String vtSkAd = (String) siparisObject.getString(ConstantString.SK_AD);
                                String vtSkSoyad = (String) siparisObject.getString(ConstantString.SK_SOYAD);
                                String vtSkTelNo = (String) siparisObject.getString(ConstantString.SK_TEL_NO);
                                String vtSkEmail = (String) siparisObject.getString(ConstantString.SK_EMAIL);
                                String vtSkAdres = (String) siparisObject.getString(ConstantString.SK_ADRES);
                                String vtSiparisObjectID = (String) siparisObject.getString(ConstantString.SIPARIS_OBJECT_ID);
                                String vtSiparisAdi = (String) siparisObject.getString(ConstantString.SIPARIS_ADI);
                                int vtSiparisAdet = siparisObject.getInt(ConstantString.SIPARIS_ADET);
                                String vtSiparisImageUrl = (String) siparisObject.getString(ConstantString.SIPARIS_IMAGE_URL);
                                String vtSiparisMesaj = (String) siparisObject.getString(ConstantString.SIPARIS_MESAJ);
                                String vtSiparisStokKodu = (String) siparisObject.getString(ConstantString.SIPARIS_KODU);
                                String vtSiparisTarih = (String) siparisObject.getString(ConstantString.SIPARIS_TARIH);
                                int vtSiparisDurum = siparisObject.getInt(ConstantString.SIPARIS_DURUMU);
                                int vtSiparisFiyat = siparisObject.getInt(ConstantString.SIPARIS_FIYAT);
                                Float tmp1SP = new Float(vtSiparisFiyat);
                                float v1Sp = tmp1SP.floatValue();

                                mapSiparis.setSiparisFiyat(v1Sp);

                                mapSiparis.setsID(vtSiparisID);

                                mapSiparis.setSiparisImageUrl(vtSiparisImageUrl);
                                mapSiparis.setSiparisStokKodu(vtSiparisStokKodu);
                                mapSiparis.setSiparisTarih(vtSiparisTarih);

                                mapSiparis.setSiparisObjectId(vtSiparisObjectID);
                                mapSiparis.setSkKulAdi(vtSkKulAdi);
                                mapSiparis.setSkAd(vtSkAd);
                                mapSiparis.setSkSoyad(vtSkSoyad);
                                mapSiparis.setSkTelNo(vtSkTelNo);
                                mapSiparis.setSkEmail(vtSkEmail);
                                mapSiparis.setSkAdres(vtSkAdres);
                                mapSiparis.setSiparisAdi(vtSiparisAdi);
                                mapSiparis.setSiparisAdet(vtSiparisAdet);
                                mapSiparis.setSiparisMesaj(vtSiparisMesaj);
                                mapSiparis.setSiparisFiyat(vtSiparisFiyat);
                                mapSiparis.setSiparisDurumu(vtSiparisDurumu);
                                siparisAllList.add(mapSiparis);
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (Integer.parseInt(sonuc) == 0) {
                Utilities.showAlertDialog(ActivityEskiSiparis.this, "Bilgilendirme", "Siparişiniz Bulunmamaktadır.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
            }
            mAdapter = new SiparisRecyclerViewAdapter(getDataSet(), ActivityEskiSiparis.this);
            mRecyclerView.setAdapter(mAdapter);
            mProgressDialog.dismiss();
            ((SiparisRecyclerViewAdapter) mAdapter).setOnItemClickListener(new SiparisRecyclerViewAdapter
                    .MyClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    Intent intent = new Intent(ActivityEskiSiparis.this, Activity_Siparis_Detay.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt(ConstantString.S_ID, siparisAllList.get(position).getsID());
                    bundle.putString(ConstantString.SIPARIS_ADI, siparisAllList.get(position).getSiparisAdi());
                    bundle.putFloat(ConstantString.SIPARIS_FIYAT, siparisAllList.get(position).getSiparisFiyat());
                    bundle.putString(ConstantString.SK_ADRES, siparisAllList.get(position).getSkAdres());
                    bundle.putSerializable(ConstantString.SIPARIS_IMAGE_URL, siparisAllList.get(position).getSiparisImageUrl());
                    bundle.putString(ConstantString.SK_AD, siparisAllList.get(position).getSkAd());
                    bundle.putString(ConstantString.SK_SOYAD, siparisAllList.get(position).getSkSoyad());
                    bundle.putString(ConstantString.SK_TEL_NO, siparisAllList.get(position).getSkTelNo());
                    bundle.putInt(ConstantString.SIPARIS_DURUMU, siparisAllList.get(position).getSiparisDurumu());
                    bundle.putString(ConstantString.SIPARIS_KODU, siparisAllList.get(position).getSiparisStokKodu());
                    bundle.putString(ConstantString.SIPARIS_MESAJ, siparisAllList.get(position).getSiparisMesaj());
                    bundle.putInt(ConstantString.SIPARIS_ADET, siparisAllList.get(position).getSiparisAdet());
                    bundle.putFloat(ConstantString.SIPARIS_FIYAT, siparisAllList.get(position).getSiparisFiyat());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }

        //Gereksiz Kod Uzaması ** TODO
        public List<Siparis> getDataSet() {
            List results = new ArrayList<Siparis>();
            for (Siparis ab : siparisAllList) {
                Siparis siparisBean = new Siparis();
                int id = ab.getsID();
                String spKod = ab.getSiparisStokKodu();
                String spTarih = ab.getSiparisTarih();
                String spImageURL = ab.getSiparisImageUrl();

                String spObjectId = ab.getSiparisObjectId();
                String spKulAdi = ab.getSkKulAdi();
                String spAd = ab.getSkAd();
                String spSoyad = ab.getSkSoyad();
                String spTelNo = ab.getSkTelNo();
                String spEmail = ab.getSkEmail();
                String spAdres = ab.getSkAdres();
                String spSiparisAdi = ab.getSiparisAdi();
                int spSiparisAdet = ab.getSiparisAdet();
                String spSiparisMesaj = ab.getSiparisMesaj();
                float spSiparisFiyat = ab.getSiparisFiyat();

                siparisBean.setsID(id);
                siparisBean.setSiparisImageUrl(spImageURL);
                siparisBean.setSiparisStokKodu(spKod);
                siparisBean.setSiparisTarih(spTarih);

                siparisBean.setSiparisObjectId(spObjectId);
                siparisBean.setSkKulAdi(spKulAdi);
                siparisBean.setSkAd(spAd);
                siparisBean.setSkSoyad(spSoyad);
                siparisBean.setSkTelNo(spTelNo);
                siparisBean.setSkEmail(spEmail);
                siparisBean.setSkAdres(spAdres);
                siparisBean.setSiparisAdi(spSiparisAdi);
                siparisBean.setSiparisAdet(spSiparisAdet);
                siparisBean.setSiparisMesaj(spSiparisMesaj);
                siparisBean.setSiparisFiyat(spSiparisFiyat);

                results.add(siparisBean);
            }
            return results;
        }
    }
}
