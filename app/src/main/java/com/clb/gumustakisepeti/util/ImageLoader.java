package com.clb.gumustakisepeti.util;

import com.clb.gumustakisepeti.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * Created by Emre on 19.09.2015.
 */
public class ImageLoader {


    public static DisplayImageOptions options = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .resetViewBeforeLoading(true)
            .showImageForEmptyUri(R.drawable.stub)
            .showImageOnFail(R.drawable.stub).showImageOnLoading(R.drawable.stub)
            .build();
    public static com.nostra13.universalimageloader.core.ImageLoader loader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();

}
