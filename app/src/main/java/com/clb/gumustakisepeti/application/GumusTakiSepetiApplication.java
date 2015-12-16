package com.clb.gumustakisepeti.application;

import android.app.Application;

import com.clb.gumustakisepeti.util.Utilities;
import com.nostra13.universalimageloader.cache.disc.impl.ext.LruDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.io.IOException;

public class GumusTakiSepetiApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());
        FileNameGenerator generator = new FileNameGenerator() {
            @Override
            public String generate(String s) {
//                String hash = Utilities.getMD5Hash(s.getBytes());
                String hash = Utilities.getHashRandom(s.toString());
                return hash;
            }
        };
        long cacheSize = 1024 * 1024 * 50;
        int maxFileCount = 100;
        LruDiskCache cacheSettings = null;
        try {
            cacheSettings = new LruDiskCache(cacheDir, cacheDir, generator, cacheSize, maxFileCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImageLoaderConfiguration config;
        if (cacheSettings == null) {
            config = new ImageLoaderConfiguration.Builder(this)
                    .build();
        } else {
            config = new ImageLoaderConfiguration.Builder(this)
                    .diskCache(cacheSettings)
                    .build();
        }

        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config);

    }


}
