package com.clb.gumustakisepeti.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.clb.gumustakisepeti.R;
import com.clb.gumustakisepeti.database.Database;
import com.clb.gumustakisepeti.listener.ImageZoomListener;
import com.clb.gumustakisepeti.pojo.Siparis;
import com.clb.gumustakisepeti.util.ConstantString;
import com.clb.gumustakisepeti.util.ImageLoader;
import com.clb.gumustakisepeti.util.Utilities;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by Emre on 18.09.2015.
 */
public class Activity_Detay extends AppCompatActivity {

    private Toolbar toolbar;
    Bundle extras;

    ImageView detayImgUrun;
    TextView txtDetayIndirim;
    TextView txtDetayEskiFiyat;
    TextView txtDetayFiyat;
    TextView txtDetayUrunAciklama;
    TextView txtDetayOdemeTuru;
    TextView txtDetayKargoDurumu;
    TextView txtDetayKargoSuresi;
    TextView txtDetayStokNo;
    TextView txtDetayStokDurumu;

    TextView txtResmiGenislet;

    Button btnSepeteEkle;
    Button btnSiparisVer;

    String dialogURL = "";

    HashMap<String, String> kulDetay = null;
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
        setContentView(R.layout.activity_detay);
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
            final String siparisDetayObjectId = String.valueOf(extras.getInt(ConstantString.BUNDLE_OBJECT_ID));
            final String siparisDetayAdi = extras.getString(ConstantString.BUNDLE_AD);
            final float siparisDetayFiyat = extras.getFloat(ConstantString.BUNDLE_FIYAT);
            float siparisDetayEskiFiyat = extras.getFloat(ConstantString.BUNDLE_ESKI_FIYAT);
            int siparisDetayIndirim = extras.getInt(ConstantString.BUNDLE_INDIRIM);
            final String siparisDetayResimURL = extras.getString(ConstantString.BUNDLE_RESIM_YOLU);
            final String siparisDetayStokNo = extras.getString(ConstantString.BUNDLE_STOK_NO);
            final String siparisDetayAciklama = extras.getString(ConstantString.BUNDLE_ACIKLAMA);
            String siparisDetayOdemeTuru = extras.getString(ConstantString.BUNDLE_ODEME_TURU);
            String siparisDetayKargoSekli = extras.getString(ConstantString.BUNDLE_KARGO_SEKLI);
            String siparisDetayKargoSuresi = extras.getString(ConstantString.BUNDLE_KARGO_SURESI);
            final String siparisDetayStokDurumu = extras.getString(ConstantString.BUNDLE_STOK_DURUMU);
            final String urunTuru = extras.getString(ConstantString.BUNDLE_URUN_TURU);

            getSupportActionBar().setTitle(siparisDetayAdi);

//            Utilities.showToastMessage(getApplicationContext(), "Ürün : " + siparisDetayAdi);


            if (siparisDetayIndirim == 0) {
                txtDetayIndirim.setVisibility(View.GONE);
            } else {
                txtDetayIndirim.setText("%" + siparisDetayIndirim + " İndirim");
            }
            if (siparisDetayEskiFiyat == 0) {
                txtDetayEskiFiyat.setVisibility(View.GONE);
            } else {
                txtDetayEskiFiyat.setText(siparisDetayEskiFiyat + " TL");
            }
            txtDetayFiyat.setText(siparisDetayFiyat + " TL");
            txtDetayUrunAciklama.setText(siparisDetayAciklama);
            txtDetayOdemeTuru.setText(siparisDetayOdemeTuru);
            txtDetayKargoDurumu.setText(siparisDetayKargoSekli);
            txtDetayKargoSuresi.setText(siparisDetayKargoSuresi);
            txtDetayStokNo.setText(siparisDetayStokNo.toUpperCase());
            txtDetayStokDurumu.setText(siparisDetayStokDurumu);
            ImageLoader.loader.displayImage(siparisDetayResimURL, detayImgUrun, ImageLoader.options);
            dialogURL = siparisDetayResimURL;

            btnSiparisVer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent siparisVerIntent = new Intent(Activity_Detay.this, ActivitySiparisVer.class);
                    Bundle bundleSiparis = new Bundle();
                    bundleSiparis.putString(ConstantString.BUNDLE_OBJECT_ID, siparisDetayObjectId);
                    bundleSiparis.putString(ConstantString.BUNDLE_AD, siparisDetayAdi);
                    bundleSiparis.putString(ConstantString.BUNDLE_RESIM_YOLU, siparisDetayResimURL);
                    bundleSiparis.putString(ConstantString.BUNDLE_STOK_NO, siparisDetayStokNo);
                    bundleSiparis.putString(ConstantString.BUNDLE_FIYAT, String.valueOf(siparisDetayFiyat));
                    bundleSiparis.putString(ConstantString.BUNDLE_URUN_TURU, urunTuru);
                    siparisVerIntent.putExtras(bundleSiparis);
                    startActivity(siparisVerIntent);

                }
            });

            btnSepeteEkle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Database database = new Database(Activity_Detay.this);
                    List<Siparis> allSiparisSepet = database.getAllSiparisSepet();
                    boolean checkSptUrun = false;
                    float urunFiyat = 0f;
                    int urunAdet = 0;
                    int IDNew = 0;
                    for (Siparis siparis : allSiparisSepet) {
                        if (siparis.getSiparisObjectId().equals(siparisDetayObjectId)) {
                            urunFiyat = siparis.getSiparisUrunFiyat();
                            urunAdet = siparis.getSiparisAdet();
                            IDNew = siparis.getsID();
                            checkSptUrun = true;
                        }
                    }
                    if (checkSptUrun) {
                        urunAdet++;
                        float urunSonFiyat = urunFiyat * urunAdet;
                        Siparis siparisGuncelle = new Siparis();
                        siparisGuncelle.setsID(IDNew);
                        siparisGuncelle.setSiparisFiyat(urunSonFiyat);
                        siparisGuncelle.setSiparisAdet(urunAdet);

                        boolean siparisArttır = database.sepetAdetFiyatGuncelle(siparisGuncelle);
                        if (siparisArttır) {
                            Utilities.showToastMessage(Activity_Detay.this, "Bir Ürün Daha Eklendi");
                        }

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

                        String stSiparisKodu = basKisim + randomSayi + sonKisim;
                        ////////SIPARIS KODU OLUSTURMA KODU////////
                        Siparis sepetSiparis = new Siparis();
                        if (database.getRowCount() > 0) {
                            kulDetay = database.kullaniciDetay();
                            helperKulAdi = kulDetay.get(ConstantString.SQ_KULLANICI_ADI);
                            helperAd = kulDetay.get(ConstantString.SQ_AD);
                            helperSoyad = kulDetay.get(ConstantString.SQ_SOYAD);
                            helperEmail = kulDetay.get(ConstantString.SQ_KULLANICI_MAIL);
                            helperTelno = kulDetay.get(ConstantString.SQ_KULLANICI_TEL_NO);
                            helperAdres = kulDetay.get(ConstantString.SQ_KULLANICI_ADRES);
                            sepetSiparis.setSkKulAdi(helperKulAdi);
                            sepetSiparis.setSkAd(helperAd);
                            sepetSiparis.setSkSoyad(helperSoyad);
                            sepetSiparis.setSkEmail(helperEmail);
                            sepetSiparis.setSkTelNo(helperTelno);
                            sepetSiparis.setSkAdres(helperAdres);
                        } else {
                            sepetSiparis.setSkKulAdi("");
                            sepetSiparis.setSkAd("");
                            sepetSiparis.setSkSoyad("");
                            sepetSiparis.setSkEmail("");
                            sepetSiparis.setSkTelNo("");
                            sepetSiparis.setSkAdres("");
                        }
                        sepetSiparis.setSiparisObjectId(siparisDetayObjectId);
                        sepetSiparis.setSiparisAdi(siparisDetayAdi);
                        sepetSiparis.setSiparisAdet(1);
                        sepetSiparis.setSiparisImageUrl(siparisDetayResimURL);
                        sepetSiparis.setSiparisDurumu(0);
                        sepetSiparis.setSiparisMesaj("");
                        sepetSiparis.setSiparisFiyat(siparisDetayFiyat);
                        sepetSiparis.setSiparisUrunFiyat(siparisDetayFiyat);
                        sepetSiparis.setSiparisStokKodu(siparisDetayStokNo);
                        sepetSiparis.setSiparisKodu(stSiparisKodu);
                        sepetSiparis.setUrunTuru(urunTuru.toString());
                        sepetSiparis.setSiparisTarih("");
                        boolean insertSpt = database.sepeteEkleAll(sepetSiparis);
                        if (insertSpt) {
                            Utilities.showToastMessage(getApplicationContext(), "Sepete Eklendi");
                        }
                    }
                }
            });
        }

    }

    private void registerEvents() {
        detayImgUrun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageDialog();
            }
        });
        txtResmiGenislet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageDialog();
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

    private void openImageDialog() {
        final Dialog dialog = new Dialog(Activity_Detay.this);
        dialog.setTitle("Yakınlaştır(+)/Uzaklaştır(-)");
        dialog.setContentView(R.layout.dialog_custom_image);
        ImageView dialogImageView = (ImageView) dialog.findViewById(R.id.dialogImageView);
        dialogImageView.setOnTouchListener(new ImageZoomListener());
        ImageLoader.loader.displayImage(dialogURL, dialogImageView, ImageLoader.options);
        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonKapat);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void initialComponents() {
        detayImgUrun = (ImageView) findViewById(R.id.detayImgUrun);
//        detayImgUrun.setOnTouchListener(this);//
        txtDetayIndirim = (TextView) findViewById(R.id.tvDetayIndirim);
        txtDetayEskiFiyat = (TextView) findViewById(R.id.tvDetayEskiFiyat);
        //
        txtDetayEskiFiyat.setPaintFlags(txtDetayEskiFiyat.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        txtDetayEskiFiyat.setPaintFlags(txtDetayEskiFiyat.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        //
        txtDetayFiyat = (TextView) findViewById(R.id.tvDetayFiyat);
        txtDetayUrunAciklama = (TextView) findViewById(R.id.tvDetayUrunAciklama);
        txtDetayOdemeTuru = (TextView) findViewById(R.id.tvDetayOdemeTuru);
        txtDetayKargoDurumu = (TextView) findViewById(R.id.tvDetayKargoDurumu);
        txtDetayKargoSuresi = (TextView) findViewById(R.id.tvDetayKargoSuresi);
        txtDetayStokNo = (TextView) findViewById(R.id.tvDetayStokNo);
        txtDetayStokDurumu = (TextView) findViewById(R.id.tvDetayStokDurumu);
        txtResmiGenislet = (TextView) findViewById(R.id.tvResmiGenislet);
        btnSepeteEkle = (Button) findViewById(R.id.btnSepeteEkle);
        btnSiparisVer = (Button) findViewById(R.id.btnSiparisVer);
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


}
