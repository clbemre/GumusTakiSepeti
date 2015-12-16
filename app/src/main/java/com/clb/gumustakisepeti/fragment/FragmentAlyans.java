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
public class FragmentAlyans extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "GTSCardViewActivity";
    ProgressDialog mProgressDialog;

    private List<Product> alyansAllList = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_alyans, container, false);
        if (Utilities.isNetworkAvailable(getActivity())) {
            new RemoteDataTask().execute(ConstantString.ALYANS_DOWNLOAD_URL);
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

        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view_alyans);
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
            alyansAllList = new ArrayList<Product>();
            JSONObject jsonObject = null;
            JSONArray jsonArray = null;
            JSONObject alyansObject = null;
            int length = 0;

            try {
                jsonObject = new JSONObject(Utilities.downloadData(params[0]));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                jsonArray = jsonObject.getJSONArray("Alyanslar");
                length = jsonArray.length();

                for (int i = 0; i < length; i++) {
                    Product mapAlyans = new Product();
                    alyansObject = jsonArray.getJSONObject(i);
                    int vtAlyansObjectID = alyansObject.getInt(ConstantString.ALYANS_OBJECT_ID);
                    String vtAlyansAdi = (String) alyansObject.getString(ConstantString.ALYANS_ADI);
                    int vtAlyansFiyati = (int) alyansObject.getInt(ConstantString.ALYANS_FIYAT);
                    Float tmp1 = new Float(vtAlyansFiyati);
                    float v1 = tmp1.floatValue();
                    int vtAlyansEskiFiyati = (int) alyansObject.getInt(ConstantString.ALYANS_ESKI_FIYAT);
                    Float tmp2 = new Float(vtAlyansEskiFiyati);
                    float v2 = tmp2.floatValue();
                    int vtAlyansIndirim = (int) alyansObject.getInt(ConstantString.ALYANS_INDIRIM);
                    String vtAlyansAciklama = (String) alyansObject.getString(ConstantString.ALYANS_ACIKLAMA);
                    String vtAlyansResimYolu = (String) alyansObject.getString(ConstantString.ALYANS_RESIM_YOLU);
                    String vtAlyansStokNo = (String) alyansObject.getString(ConstantString.ALYANS_STOK_NO);
                    String vtAlyansOdemeSekli = (String) alyansObject.getString(ConstantString.ALYANS_ODEME_TURU);
                    String vtAlyansKargoSekli = (String) alyansObject.getString(ConstantString.ALYANS_KARGO_SEKLI);
                    String vtAlyansKargoSuresi = (String) alyansObject.getString(ConstantString.ALYANS_KARGO_SURESI);
                    String vtAlyansStokDurumu = (String) alyansObject.getString(ConstantString.ALYANS_STOK_DURUMU);
                    mapAlyans.setObjectID(vtAlyansObjectID);
                    mapAlyans.setAdi(vtAlyansAdi);
                    mapAlyans.setFiyati(v1);
                    mapAlyans.setEskiFiyati(v2);
                    mapAlyans.setIndirim(vtAlyansIndirim);
                    mapAlyans.setAciklama(vtAlyansAciklama);
                    mapAlyans.setResimURL(vtAlyansResimYolu);
                    mapAlyans.setStokNo(vtAlyansStokNo);
                    mapAlyans.setOdemeSekli(vtAlyansOdemeSekli);
                    mapAlyans.setKargoSekli(vtAlyansKargoSekli);
                    mapAlyans.setKargoSuresi(vtAlyansKargoSuresi);
                    mapAlyans.setStokDurumu(vtAlyansStokDurumu);
                    alyansAllList.add(mapAlyans);
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
                    bundle.putInt(ConstantString.BUNDLE_OBJECT_ID, alyansAllList.get(position).getObjectID());
                    bundle.putString(ConstantString.BUNDLE_AD, alyansAllList.get(position).getAdi());
                    bundle.putFloat(ConstantString.BUNDLE_FIYAT, alyansAllList.get(position).getFiyati());
                    bundle.putFloat(ConstantString.BUNDLE_ESKI_FIYAT, alyansAllList.get(position).getEskiFiyati());
                    bundle.putInt(ConstantString.BUNDLE_INDIRIM, alyansAllList.get(position).getIndirim());
                    bundle.putString(ConstantString.BUNDLE_STOK_NO, alyansAllList.get(position).getStokNo());
                    bundle.putSerializable(ConstantString.BUNDLE_RESIM_YOLU, alyansAllList.get(position).getResimURL());
                    bundle.putString(ConstantString.BUNDLE_ACIKLAMA, alyansAllList.get(position).getAciklama());
                    bundle.putString(ConstantString.BUNDLE_ODEME_TURU, alyansAllList.get(position).getOdemeSekli());
                    bundle.putString(ConstantString.BUNDLE_KARGO_SEKLI, alyansAllList.get(position).getKargoSekli());
                    bundle.putString(ConstantString.BUNDLE_KARGO_SURESI, alyansAllList.get(position).getKargoSuresi());
                    bundle.putString(ConstantString.BUNDLE_STOK_DURUMU, alyansAllList.get(position).getStokDurumu());
                    bundle.putString(ConstantString.BUNDLE_URUN_TURU, "0");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }

        //Gereksiz Kod Uzaması ** TODO
        public List<Product> getDataSet() {
            List results = new ArrayList<Product>();
            for (Product ab : alyansAllList) {
                Product alyansBean = new Product();
                int objectID = ab.getObjectID();
                String alyansAdi = ab.getAdi();
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

                alyansBean.setObjectID(objectID);
                alyansBean.setAdi(alyansAdi);
                alyansBean.setAciklama(aciklama);
                alyansBean.setResimURL(resimYolu);
                alyansBean.setFiyati(fiyat);
                alyansBean.setEskiFiyati(eskiFiyat);
                alyansBean.setIndirim(indirim);
                alyansBean.setStokNo(stokNo);
                alyansBean.setOdemeSekli(odemeTuru);
                alyansBean.setKargoSekli(kargoSekli);
                alyansBean.setKargoSuresi(kargoSuresi);
                alyansBean.setStokDurumu(stokDurumu);

                results.add(alyansBean);
            }
            return results;
        }
    }
}
