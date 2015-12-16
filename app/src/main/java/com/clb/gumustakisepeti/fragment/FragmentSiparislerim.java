package com.clb.gumustakisepeti.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.clb.gumustakisepeti.R;
import com.clb.gumustakisepeti.activity.ActivityEskiSiparis;
import com.clb.gumustakisepeti.activity.ActivityOnayBekleyenSiparis;
import com.clb.gumustakisepeti.activity.ActivityOnaylananSiparis;
import com.clb.gumustakisepeti.util.Utilities;

/**
 * Created by Admin on 04-06-2015.
 */
public class FragmentSiparislerim extends Fragment {

    Button btnOnayBekleyenler;
    Button btnOnaylananlar;
    Button btnEskiSiparisler;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_siparislerim, container, false);
        btnOnayBekleyenler = (Button) v.findViewById(R.id.btnOnayBekleyenSiparis);
        btnOnaylananlar = (Button) v.findViewById(R.id.btnOnaylananSiparis);
        btnEskiSiparisler = (Button) v.findViewById(R.id.btnEskiSiparis);

        btnOnayBekleyenler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.utilStartActivity(getActivity(), ActivityOnayBekleyenSiparis.class);
            }
        });
        btnOnaylananlar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.utilStartActivity(getActivity(), ActivityOnaylananSiparis.class);
            }
        });

        btnEskiSiparisler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.utilStartActivity(getActivity(), ActivityEskiSiparis.class);
            }
        });


        return v;
    }


}
