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

import com.clb.gumustakisepeti.R;
import com.clb.gumustakisepeti.activity.Activity_Detay;
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
public class FragmentFirsatlar extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "GTSCardViewActivity";
    ProgressDialog mProgressDialog;

    private List<Product> firsatlarAllList = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_firsatlar, container, false);
        if (Utilities.isNetworkAvailable(getActivity())) {
            new RemoteDataTask().execute(ConstantString.FIRSATLAR_DOWNLOAD_URL);
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

        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view_firsatlar);
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
            firsatlarAllList = new ArrayList<Product>();
            JSONObject jsonObject = null;
            JSONArray jsonArray = null;
            JSONObject firsatlarObject = null;
            int length = 0;

            try {
                jsonObject = new JSONObject(Utilities.downloadData(params[0]));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                jsonArray = jsonObject.getJSONArray(ConstantString.JSON_ARRAY_NAME_FIRSATLAR);
                length = jsonArray.length();

                for (int i = 0; i < length; i++) {
                    Product mapFirsatlar = new Product();
                    firsatlarObject = jsonArray.getJSONObject(i);
                    int vtFirsatObjectID = firsatlarObject.getInt(ConstantString.FIRSATLAR_OBJECT_ID);
                    String vtFirsatAdi = (String) firsatlarObject.getString(ConstantString.FIRSATLAR_ADI);
                    int vtFirsatFiyati = (int) firsatlarObject.getInt(ConstantString.FIRSATLAR_FIYAT);
                    Float tmp1 = new Float(vtFirsatFiyati);
                    float v1 = tmp1.floatValue();
                    int vtFirsatEskiFiyati = (int) firsatlarObject.getInt(ConstantString.FIRSATLAR_ESKI_FIYAT);
                    Float tmp2 = new Float(vtFirsatEskiFiyati);
                    float v2 = tmp2.floatValue();
                    int vtFirsatIndirim = (int) firsatlarObject.getInt(ConstantString.FIRSATLAR_INDIRIM);
                    String vtFirsatAciklama = (String) firsatlarObject.getString(ConstantString.FIRSATLAR_ACIKLAMA);
                    String vtFirsatResimYolu = (String) firsatlarObject.getString(ConstantString.FIRSATLAR_RESIM_YOLU);
                    String vtFirsatStokNo = (String) firsatlarObject.getString(ConstantString.FIRSATLAR_STOK_NO);
                    String vtFirsatOdemeSekli = (String) firsatlarObject.getString(ConstantString.FIRSATLAR_ODEME_TURU);
                    String vtFirsatKargoSekli = (String) firsatlarObject.getString(ConstantString.FIRSATLAR_KARGO_SEKLI);
                    String vtFirsatKargoSuresi = (String) firsatlarObject.getString(ConstantString.FIRSATLAR_KARGO_SURESI);
                    String vtFirsatStokDurumu = (String) firsatlarObject.getString(ConstantString.FIRSATLAR_STOK_DURUMU);
                    mapFirsatlar.setObjectID(vtFirsatObjectID);
                    mapFirsatlar.setAdi(vtFirsatAdi);
                    mapFirsatlar.setFiyati(v1);
                    mapFirsatlar.setEskiFiyati(v2);
                    mapFirsatlar.setIndirim(vtFirsatIndirim);
                    mapFirsatlar.setAciklama(vtFirsatAciklama);
                    mapFirsatlar.setResimURL(vtFirsatResimYolu);
                    mapFirsatlar.setStokNo(vtFirsatStokNo);
                    mapFirsatlar.setOdemeSekli(vtFirsatOdemeSekli);
                    mapFirsatlar.setKargoSekli(vtFirsatKargoSekli);
                    mapFirsatlar.setKargoSuresi(vtFirsatKargoSuresi);
                    mapFirsatlar.setStokDurumu(vtFirsatStokDurumu);
                    firsatlarAllList.add(mapFirsatlar);
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
                    bundle.putInt(ConstantString.BUNDLE_OBJECT_ID, firsatlarAllList.get(position).getObjectID());
                    bundle.putString(ConstantString.BUNDLE_AD, firsatlarAllList.get(position).getAdi());
                    bundle.putFloat(ConstantString.BUNDLE_FIYAT, firsatlarAllList.get(position).getFiyati());
                    bundle.putFloat(ConstantString.BUNDLE_ESKI_FIYAT, firsatlarAllList.get(position).getEskiFiyati());
                    bundle.putInt(ConstantString.BUNDLE_INDIRIM, firsatlarAllList.get(position).getIndirim());
                    bundle.putString(ConstantString.BUNDLE_STOK_NO, firsatlarAllList.get(position).getStokNo());
                    bundle.putSerializable(ConstantString.BUNDLE_RESIM_YOLU, firsatlarAllList.get(position).getResimURL());
                    bundle.putString(ConstantString.BUNDLE_ACIKLAMA, firsatlarAllList.get(position).getAciklama());
                    bundle.putString(ConstantString.BUNDLE_ODEME_TURU, firsatlarAllList.get(position).getOdemeSekli());
                    bundle.putString(ConstantString.BUNDLE_KARGO_SEKLI, firsatlarAllList.get(position).getKargoSekli());
                    bundle.putString(ConstantString.BUNDLE_KARGO_SURESI, firsatlarAllList.get(position).getKargoSuresi());
                    bundle.putString(ConstantString.BUNDLE_STOK_DURUMU, firsatlarAllList.get(position).getStokDurumu());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }

        //Gereksiz Kod Uzaması ** TODO
        public List<Product> getDataSet() {
            List results = new ArrayList<Product>();
            for (Product ab : firsatlarAllList) {
                Product firsatlarBean = new Product();
                int objectID = ab.getObjectID();
                String firsatlarAdi = ab.getAdi();
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

                firsatlarBean.setObjectID(objectID);
                firsatlarBean.setAdi(firsatlarAdi);
                firsatlarBean.setAciklama(aciklama);
                firsatlarBean.setResimURL(resimYolu);
                firsatlarBean.setFiyati(fiyat);
                firsatlarBean.setEskiFiyati(eskiFiyat);
                firsatlarBean.setIndirim(indirim);
                firsatlarBean.setStokNo(stokNo);
                firsatlarBean.setOdemeSekli(odemeTuru);
                firsatlarBean.setKargoSekli(kargoSekli);
                firsatlarBean.setKargoSuresi(kargoSuresi);
                firsatlarBean.setStokDurumu(stokDurumu);

                results.add(firsatlarBean);
            }
            return results;
        }
    }
}
