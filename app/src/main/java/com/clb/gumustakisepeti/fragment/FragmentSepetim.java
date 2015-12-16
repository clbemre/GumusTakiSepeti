package com.clb.gumustakisepeti.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.clb.gumustakisepeti.R;
import com.clb.gumustakisepeti.activity.ActivitySepetSiparisVer;
import com.clb.gumustakisepeti.adapter.SepetRecyclerViewAdapter;
import com.clb.gumustakisepeti.database.Database;
import com.clb.gumustakisepeti.pojo.Siparis;
import com.clb.gumustakisepeti.util.Utilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emre on 27-10-2015.
 */
public class FragmentSepetim extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "GTSSepetimCardViewActivity";
    ProgressDialog mProgressDialog;

    float toplamFiyat = 0f;

    private List<Siparis> sepetAllList = null;
    private List<Siparis> sepetListSiparisTamamla = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sepetim, container, false);
        setHasOptionsMenu(true);
        Database dbSepet = new Database(getActivity());
        sepetAllList = dbSepet.getAllSiparisSepet();

        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view_sepetim);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SepetRecyclerViewAdapter(getDataSet(), getActivity());
        mRecyclerView.setAdapter(mAdapter);

        ((SepetRecyclerViewAdapter) mAdapter).setOnItemClickListener(new SepetRecyclerViewAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {

            }
        });
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_sepetim, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.alisveristamamla) {
            toplamFiyat = 0f;
            Database dbSepet = new Database(getActivity());
            int rowCountSepetim = dbSepet.getRowCountSepetim();
            if (rowCountSepetim > 0) {
                sepetListSiparisTamamla = dbSepet.getAllSiparisSepet();
                for (Siparis siparisFiyat : sepetListSiparisTamamla) {
                    float abSiparisFiyat = siparisFiyat.getSiparisFiyat();
                    toplamFiyat += abSiparisFiyat;
                }
                Intent intent = new Intent(getActivity(), ActivitySepetSiparisVer.class);
                Bundle bundle = new Bundle();
                bundle.putFloat("ToplamFiyat", toplamFiyat);
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                Utilities.showToastMessage(getActivity(), "Sepet Boş");
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Gereksiz Kod Uzaması ** TODO
    public List<Siparis> getDataSet() {
        List results = new ArrayList<Siparis>();
        for (Siparis ab : sepetAllList) {
            Siparis sepetBean = new Siparis();
            int abSID = ab.getsID();
            String abKulAdi = ab.getSkKulAdi();
            String abAd = ab.getSkAd();
            String abSoyad = ab.getSkSoyad();
            String abTelNo = ab.getSkTelNo();
            String abEmail = ab.getSkEmail();
            String abAdres = ab.getSkAdres();
            String abSiparisObjectId = ab.getSiparisObjectId();
            String abSiparisAdi = ab.getSiparisAdi();
            int abSiparisAdet = ab.getSiparisAdet();
            String abSiparisImageUrl = ab.getSiparisImageUrl();
            int abSiparisDurumu = ab.getSiparisDurumu();
            String abSiparisMesaj = ab.getSiparisMesaj();
            float abSiparisFiyat = ab.getSiparisFiyat();
            float abSiparisUrunFiyat = ab.getSiparisUrunFiyat();
            String abSiparisKodu = ab.getSiparisKodu();
            String abSiparisStokKodu = ab.getSiparisStokKodu();
            String abSiparisTarih = ab.getSiparisTarih();
            String abSiparisUrunTuru = ab.getUrunTuru();

            sepetBean.setsID(abSID);
            sepetBean.setSkKulAdi(abKulAdi);
            sepetBean.setSkAd(abAd);
            sepetBean.setSkSoyad(abSoyad);
            sepetBean.setSkTelNo(abTelNo);
            sepetBean.setSkEmail(abEmail);
            sepetBean.setSkAdres(abAdres);
            sepetBean.setSiparisObjectId(abSiparisObjectId);
            sepetBean.setSiparisAdi(abSiparisAdi);
            sepetBean.setSiparisAdet(abSiparisAdet);
            sepetBean.setSiparisImageUrl(abSiparisImageUrl);
            sepetBean.setSiparisDurumu(abSiparisDurumu);
            sepetBean.setSiparisMesaj(abSiparisMesaj);
            sepetBean.setSiparisFiyat(abSiparisFiyat);
            sepetBean.setSiparisUrunFiyat(abSiparisUrunFiyat);
            sepetBean.setSiparisKodu(abSiparisKodu);
            sepetBean.setSiparisStokKodu(abSiparisStokKodu);
            sepetBean.setSiparisTarih(abSiparisTarih);
            sepetBean.setUrunTuru(abSiparisUrunTuru);
            results.add(sepetBean);
        }
        return results;
    }
}
