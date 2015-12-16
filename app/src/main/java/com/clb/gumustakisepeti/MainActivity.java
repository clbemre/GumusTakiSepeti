package com.clb.gumustakisepeti;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clb.gumustakisepeti.activity.ActivityLogin;
import com.clb.gumustakisepeti.activity.ActivityProfilim;
import com.clb.gumustakisepeti.database.Database;
import com.clb.gumustakisepeti.fragment.FragmentAlyans;
import com.clb.gumustakisepeti.fragment.FragmentFirsatlar;
import com.clb.gumustakisepeti.fragment.FragmentHayalet;
import com.clb.gumustakisepeti.fragment.FragmentIletisim;
import com.clb.gumustakisepeti.fragment.FragmentSepetim;
import com.clb.gumustakisepeti.fragment.FragmentSiparislerim;
import com.clb.gumustakisepeti.util.ConstantString;
import com.clb.gumustakisepeti.util.Fonksiyonlar;
import com.clb.gumustakisepeti.util.Utilities;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    //Defining Variables
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    HashMap<String, String> userMap = null;
    String kullanici_adi;
    String sifre;
    String mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Utilities.isNetworkAvailable(this)) {

        } else {
            Utilities.showAlertDialog(this, getString(R.string.opps),
                    getString(R.string.check_network_connection),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
        }

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        checkFirstRun();

        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        //
//        View header = navigationView.inflateHeaderView(R.layout.header);
//        RelativeLayout drawerHeader = (RelativeLayout) header.findViewById(R.id.headerLayout);
//        drawerHeader.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "HEADER", Toast.LENGTH_SHORT).show();
//            }
//        });
        //
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {


                    //Replacing the main content with FragmentAlyans Which is our Inbox View;
                    case R.id.profilim:
                        if (Fonksiyonlar.giriskontrol(getApplicationContext())) {
                            try {
                                openIntentPutString();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Utilities.showAlertDialog(getApplicationContext(), "Bir Hata var :S", "Loading Hatası ", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                            }
                        } else {
                            Utilities.utilStartActivity(MainActivity.this, ActivityLogin.class);
                        }
                        return true;

                    // For rest of the options we just show a toast on click

                    case R.id.alyans:
                        FragmentAlyans fragmentAlyans = new FragmentAlyans();
                        android.support.v4.app.FragmentTransaction fragmentTransactionAlyans = getSupportFragmentManager().beginTransaction();
                        fragmentTransactionAlyans.replace(R.id.frame, fragmentAlyans);
                        fragmentTransactionAlyans.commit();
                        return true;
                    case R.id.hayaletKolye:
                        FragmentHayalet fragmentHayalet = new FragmentHayalet();
                        android.support.v4.app.FragmentTransaction fragmentTransactionHayalet = getSupportFragmentManager().beginTransaction();
                        fragmentTransactionHayalet.replace(R.id.frame, fragmentHayalet);
                        fragmentTransactionHayalet.commit();
                        return true;
                    case R.id.erkekYuzugu:
                        Toast.makeText(getApplicationContext(), "Yakında", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.siparislerim:
                        if (Fonksiyonlar.giriskontrol(getApplicationContext())) {
                            FragmentSiparislerim fragmentSiparislerim = new FragmentSiparislerim();
                            android.support.v4.app.FragmentTransaction fragmentTransactionSiparislerim = getSupportFragmentManager().beginTransaction();
                            fragmentTransactionSiparislerim.replace(R.id.frame, fragmentSiparislerim);
                            fragmentTransactionSiparislerim.commit();
                        } else {
                            Utilities.showToastMessage(MainActivity.this, "Sadece Üyeler Siparişlerini Görebilir!");
                        }
                        return true;
                    case R.id.sepetim:
                        FragmentSepetim fragmentSepetim = new FragmentSepetim();
                        android.support.v4.app.FragmentTransaction fragmentTransactionSepetim = getSupportFragmentManager().beginTransaction();
                        fragmentTransactionSepetim.replace(R.id.frame, fragmentSepetim);
                        fragmentTransactionSepetim.commit();
                        return true;
                    case R.id.firsatlar:
                        FragmentFirsatlar fragmentFirsatlar = new FragmentFirsatlar();
                        android.support.v4.app.FragmentTransaction fragmentTransactionFirsatlar = getSupportFragmentManager().beginTransaction();
                        fragmentTransactionFirsatlar.replace(R.id.frame, fragmentFirsatlar);
                        fragmentTransactionFirsatlar.commit();
                        return true;
                    case R.id.iletisim:
                        FragmentIletisim fragmentIletisim = new FragmentIletisim();
                        android.support.v4.app.FragmentTransaction fragmentTransactionIletisim = getSupportFragmentManager().beginTransaction();
                        fragmentTransactionIletisim.replace(R.id.frame, fragmentIletisim);
                        fragmentTransactionIletisim.commit();
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(), "Birşeyler Yanlış", Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(final View drawerView) {
                RelativeLayout relativeLayout = (RelativeLayout) drawerView.findViewById(R.id.headerLayout);
                final TextView txtKullaniciAdi = (TextView) relativeLayout.findViewById(R.id.username);
                final TextView email = (TextView) relativeLayout.findViewById(R.id.email);
                final TextView cikisYap = (TextView) relativeLayout.findViewById(R.id.cikisYap);
                if (Fonksiyonlar.giriskontrol(getApplicationContext())) {//önceden giriş yapmış ise
                    Database dbGiris = new Database(getApplicationContext());
                    userMap = dbGiris.kullaniciDetay();
                    kullanici_adi = userMap.get(ConstantString.VJ_KULLANICI_ADI);
                    mail = userMap.get(ConstantString.VJ_MAIL);
                    sifre = userMap.get(ConstantString.VJ_SIFRE);
                    txtKullaniciAdi.setText(kullanici_adi);
                    email.setText(mail);
//                    txtKullaniciAdi.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            openIntentPutString();
//                        }
//                    });
//                    email.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            openIntentPutString();
//                        }
//                    });
                    cikisYap.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Utilities.warningAlertDialog(MainActivity.this, "Çıkış Yapılıyor", "Onaylıyor musunuz ? ", "Evet", "Hayır", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Database db = new Database(getApplicationContext());
                                    db.resetTables(); //Databasi sıfırlıyoruz.Verileri siliyoruz.Ve Login sayfasına gidiyoruz.
                                    txtKullaniciAdi.setText(getString(R.string.giris_yap));
                                    email.setText(getString(R.string.giris_yapilmadi));
                                    cikisYap.setVisibility(View.INVISIBLE);
                                    drawerLayout.closeDrawers();
                                    Utilities.showToastMessage(MainActivity.this, getString(R.string.cikis_yapildi));
                                }
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    drawerLayout.closeDrawers();
                                    Utilities.showToastMessage(MainActivity.this, "Çıkış Yapılmadı");
                                }
                            });
                        }
                    });

                } else {
                    txtKullaniciAdi.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Utilities.utilStartActivity(MainActivity.this, ActivityLogin.class);
                        }
                    });
                    email.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Utilities.utilStartActivity(MainActivity.this, ActivityLogin.class);
                        }
                    });
                    cikisYap.setVisibility(View.INVISIBLE);
                }
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up

        actionBarDrawerToggle.syncState();

    }

    // Toolbarda bulunan navigationDrawer acmaya yarayan icon referansı
    final Target viewTarget = new Target() {
        @Override
        public Point getPoint() {
            View navIcon = null;
            for (int i = 0; i < toolbar.getChildCount(); i++) {
                View child = toolbar.getChildAt(i);
                if (ImageButton.class.isInstance(child)) {
                    navIcon = child;
                    break;
                }
            }

            if (navIcon != null)
                return new ViewTarget(navIcon).getPoint();
            else
                return new ViewTarget(toolbar).getPoint();
        }
    };

    private void checkFirstRun() {

        final String PREFS_NAME = "firstRunIs";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;


        // Get current version code
        int currentVersionCode = 0;
        try {
            currentVersionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            // handle exception
            e.printStackTrace();
            return;
        }

        // Get saved version code
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        // Check for first run or upgrade
        if (currentVersionCode == savedVersionCode) {

            // This is just a normal run
            return;

        } else if (savedVersionCode == DOESNT_EXIST) {

            // TODO This is a new install (or the user cleared the shared preferences)
            ShowcaseView.Builder shb = new ShowcaseView.Builder(MainActivity.this);
            shb.setTarget(viewTarget)
                    .setContentTitle("Menüyü Açın")
                    .hideOnTouchOutside()
                    .build().show();


        } else if (currentVersionCode > savedVersionCode) {

            // TODO This is an upgrade

        }

        // Update the shared preferences with the current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).commit();

    }

    public void openIntentPutString() {
        Intent intent = new Intent(MainActivity.this, ActivityProfilim.class);
        Bundle bundle = new Bundle();
        bundle.putString(ConstantString.VJ_KULLANICI_ADI, kullanici_adi);
        bundle.putString(ConstantString.VJ_SIFRE, sifre);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
