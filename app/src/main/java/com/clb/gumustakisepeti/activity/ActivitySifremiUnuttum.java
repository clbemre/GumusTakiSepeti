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
import android.widget.TextView;

import com.clb.gumustakisepeti.R;
import com.clb.gumustakisepeti.database.PostClass;
import com.clb.gumustakisepeti.util.ConstantString;
import com.clb.gumustakisepeti.util.Utilities;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emre on 15.10.2015.
 */
public class ActivitySifremiUnuttum extends AppCompatActivity {

    private Toolbar toolbar;

    TextView prEmail;
    Button prBtnSifreOgren;

    String sonuc;
    PostClass post = new PostClass();
    JSONObject cevap = null;
//    JSONArray kullaniciArray = null;
//    JSONObject kullaniciVeri = null;
//    String gelenName = null;
    String stringEmail = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sifremi_unuttum);
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
        registerEvents();


    }


    private void initializeComponents() {
        prEmail = (TextView) findViewById(R.id.prTxtEmailSifreOgren);
        prBtnSifreOgren = (Button) findViewById(R.id.prBtnSifreOgren);
    }

    private void registerEvents() {
        prBtnSifreOgren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringEmail = prEmail.getText().toString();
                if (stringEmail.equals("") || stringEmail != null) {
                    new SifreOgrenAsync().execute();
                }
            }
        });
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

    class SifreOgrenAsync extends AsyncTask<Void, Void, Void> {
        private String sonucmesaji;
        ProgressDialog pDialog;

        protected void onPreExecute() {
            //  progress dialog
            pDialog = new ProgressDialog(ActivitySifremiUnuttum.this);
            pDialog.setMessage(getString(R.string.sifre_gonderiliyor));
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected Void doInBackground(Void... unused) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(ConstantString.VJ_EMAIL, stringEmail));
            String json = post.httpPost(ConstantString.URL_POST_SIFREMI_UNUTTUM, ConstantString.POST_METHOD, params, 20000);

            Log.d("Gelen Json", "" + json);//Gelen veriyi logluyoruz.Log Catten kontrol edebiliriz
            try {
                cevap = new JSONObject(json);
                sonucmesaji = cevap.getString("message");
                if (cevap.getString("success") != null) {
                    sonuc = cevap.getString("success");
//                    if (Integer.parseInt(sonuc) == 1) { //Eğer giriş başarılı ise
//                        JSONObject sifirJson = cevap.getJSONObject("0");
//                        kullaniciArray = sifirJson.getJSONArray("sifre_hatirlatma");
//                        kullaniciVeri = kullaniciArray.getJSONObject(0);
//                        gelenName = kullaniciVeri.getString("name").toString();
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
                        Utilities.warningAlertDialogPoz(ActivitySifremiUnuttum.this, getString(R.string.sifre_gonderilemedi), sonucmesaji, getString(R.string.tamam), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                prEmail.setText("");
                            }
                        });
                    } else if (Integer.parseInt(sonuc) == 1) {
                        Utilities.warningAlertDialogPoz(ActivitySifremiUnuttum.this, getString(R.string.bilgilendirme), getString(R.string.mail_gonderildi), getString(R.string.tamam), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(ActivitySifremiUnuttum.this, ActivityLogin.class);
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
