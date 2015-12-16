package com.clb.gumustakisepeti.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clb.gumustakisepeti.R;

/**
 * Created by Admin on 04-06-2015.
 */
public class FragmentIletisim extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_iletisim, container, false);

        return v;
    }


}
