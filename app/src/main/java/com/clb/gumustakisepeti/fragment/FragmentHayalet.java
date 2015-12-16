package com.clb.gumustakisepeti.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clb.gumustakisepeti.activity.Activity_Detay;
import com.clb.gumustakisepeti.R;
import com.clb.gumustakisepeti.adapter.MyRecyclerViewAdapter;
import com.clb.gumustakisepeti.pojo.Product;
import com.clb.gumustakisepeti.util.ConstantString;
import com.clb.gumustakisepeti.util.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 04-06-2015.
 */
public class FragmentHayalet extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "GTSCardViewActivity";
    ProgressDialog mProgressDialog;

    private List<Product> hayaletAllList = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_hayalet, container, false);
        if (Utilities.isNetworkAvailable(getActivity())) {
            new RemoteDataTask().execute(ConstantString.HAYALET_DOWNLOAD_URL);
        } else {
            Utilities.showAlertDialog(getActivity(), getString(R.string.opps),
                    getString(R.string.check_network_connection),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().finish();
                        }
                    });
        }

        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view_hayalet);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        return v;
    }


    // RemoteDataTask AsyncTask
    private class RemoteDataTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setTitle(getString(R.string.app_name));
            mProgressDialog.setMessage(getString(R.string.yukleniyor));
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            hayaletAllList = new ArrayList<Product>();
            JSONObject jsonObject = null;
            JSONArray jsonArray = null;
            JSONObject hayaletObject = null;
            int length = 0;

            try {
                jsonObject = new JSONObject(Utilities.downloadData(params[0]));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                jsonArray = jsonObject.getJSONArray(ConstantString.JSON_ARRAY_NAME_HAYALET);
                length = jsonArray.length();

                for (int i = 0; i < length; i++) {
                    Product mapHayalet = new Product();
                    hayaletObject = jsonArray.getJSONObject(i);
                    int vtHayaletObjectID = hayaletObject.getInt(ConstantString.HAYALET_OBJECT_ID);
                    String vtHayaletAdi = (String) hayaletObject.getString(ConstantString.HAYALET_ADI);
                    int vtHayaletFiyati = (int) hayaletObject.getInt(ConstantString.HAYALET_FIYAT);
                    Float tmp1 = new Float(vtHayaletFiyati);
                    float v1 = tmp1.floatValue();
                    int vtHayaletEskiFiyati = (int) hayaletObject.getInt(ConstantString.HAYALET_ESKI_FIYAT);
                    Float tmp2 = new Float(vtHayaletEskiFiyati);
                    float v2 = tmp2.floatValue();
                    int vtHayaletIndirim = (int) hayaletObject.getInt(ConstantString.HAYALET_INDIRIM);
                    String vtHayaletAciklama = (String) hayaletObject.getString(ConstantString.HAYALET_ACIKLAMA);
                    String vtHayaletResimYolu = (String) hayaletObject.getString(ConstantString.HAYALET_RESIM_YOLU);
                    String vtHayaletStokNo = (String) hayaletObject.getString(ConstantString.HAYALET_STOK_NO);
                    String vtHayaletOdemeSekli = (String) hayaletObject.getString(ConstantString.HAYALET_ODEME_TURU);
                    String vtHayaletKargoSekli = (String) hayaletObject.getString(ConstantString.HAYALET_KARGO_SEKLI);
                    String vtHayaletKargoSuresi = (String) hayaletObject.getString(ConstantString.HAYALET_KARGO_SURESI);
                    String vtHayaletStokDurumu = (String) hayaletObject.getString(ConstantString.HAYALET_STOK_DURUMU);
                    mapHayalet.setObjectID(vtHayaletObjectID);
                    mapHayalet.setAdi(vtHayaletAdi);
                    mapHayalet.setFiyati(v1);
                    mapHayalet.setEskiFiyati(v2);
                    mapHayalet.setIndirim(vtHayaletIndirim);
                    mapHayalet.setAciklama(vtHayaletAciklama);
                    mapHayalet.setResimURL(vtHayaletResimYolu);
                    mapHayalet.setStokNo(vtHayaletStokNo);
                    mapHayalet.setOdemeSekli(vtHayaletOdemeSekli);
                    mapHayalet.setKargoSekli(vtHayaletKargoSekli);
                    mapHayalet.setKargoSuresi(vtHayaletKargoSuresi);
                    mapHayalet.setStokDurumu(vtHayaletStokDurumu);
                    hayaletAllList.add(mapHayalet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mAdapter = new MyRecyclerViewAdapter(getDataSet(), getActivity());
            mRecyclerView.setAdapter(mAdapter);
            mProgressDialog.dismiss();
            ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter
                    .MyClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    Intent intent = new Intent(getActivity(), Activity_Detay.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt(ConstantString.BUNDLE_OBJECT_ID, hayaletAllList.get(position).getObjectID());
                    bundle.putString(ConstantString.BUNDLE_AD, hayaletAllList.get(position).getAdi());
                    bundle.putFloat(ConstantString.BUNDLE_FIYAT, hayaletAllList.get(position).getFiyati());
                    bundle.putFloat(ConstantString.BUNDLE_ESKI_FIYAT, hayaletAllList.get(position).getEskiFiyati());
                    bundle.putInt(ConstantString.BUNDLE_INDIRIM, hayaletAllList.get(position).getIndirim());
                    bundle.putString(ConstantString.BUNDLE_STOK_NO, hayaletAllList.get(position).getStokNo());
                    bundle.putSerializable(ConstantString.BUNDLE_RESIM_YOLU, hayaletAllList.get(position).getResimURL());
                    bundle.putString(ConstantString.BUNDLE_ACIKLAMA, hayaletAllList.get(position).getAciklama());
                    bundle.putString(ConstantString.BUNDLE_ODEME_TURU, hayaletAllList.get(position).getOdemeSekli());
                    bundle.putString(ConstantString.BUNDLE_KARGO_SEKLI, hayaletAllList.get(position).getKargoSekli());
                    bundle.putString(ConstantString.BUNDLE_KARGO_SURESI, hayaletAllList.get(position).getKargoSuresi());
                    bundle.putString(ConstantString.BUNDLE_STOK_DURUMU, hayaletAllList.get(position).getStokDurumu());
                    bundle.putString(ConstantString.BUNDLE_URUN_TURU, "1");
                    bundle.putString(ConstantString.BUNDLE_MODEL_NO, "1");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }

        //Gereksiz Kod Uzaması ** TODO
        public List<Product> getDataSet() {
            List results = new ArrayList<Product>();
            for (Product ab : hayaletAllList) {
                Product hayaletBean = new Product();
                int objectID = ab.getObjectID();
                String hayaletAdi = ab.getAdi();
                String aciklama = ab.getAciklama();
                String resimYolu = ab.getResimURL();
                float fiyat = ab.getFiyati();
                float eskiFiyat = ab.getEskiFiyati();
                int indirim = ab.getIndirim();
                String stokNo = ab.getStokNo();
                String odemeTuru = ab.getOdemeSekli();
                String kargoSekli = ab.getKargoSekli();
                String kargoSuresi = ab.getKargoSuresi();
                String stokDurumu = ab.getStokDurumu();

                hayaletBean.setObjectID(objectID);
                hayaletBean.setAdi(hayaletAdi);
                hayaletBean.setAciklama(aciklama);
                hayaletBean.setResimURL(resimYolu);
                hayaletBean.setFiyati(fiyat);
                hayaletBean.setEskiFiyati(eskiFiyat);
                hayaletBean.setIndirim(indirim);
                hayaletBean.setStokNo(stokNo);
                hayaletBean.setOdemeSekli(odemeTuru);
                hayaletBean.setKargoSekli(kargoSekli);
                hayaletBean.setKargoSuresi(kargoSuresi);
                hayaletBean.setStokDurumu(stokDurumu);

                results.add(hayaletBean);
            }
            return results;
        }
    }
}
