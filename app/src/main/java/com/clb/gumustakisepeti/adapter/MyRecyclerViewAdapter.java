package com.clb.gumustakisepeti.adapter;

import android.app.Activity;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.clb.gumustakisepeti.R;
import com.clb.gumustakisepeti.pojo.Product;
import com.clb.gumustakisepeti.util.ImageLoader;

import java.util.List;

/**
 * Created by Emre on 17.09.2015.
 */
public class MyRecyclerViewAdapter extends RecyclerView
        .Adapter<MyRecyclerViewAdapter.DataAlyansHolder> {

//    ImageLoader imageLoader = ImageLoader.getInstance();
//    DisplayImageOptions options = new DisplayImageOptions.Builder()
//            .cacheOnDisc(true).cacheInMemory(true).resetViewBeforeLoading(true)
//            .showImageForEmptyUri(R.drawable.stub)
//            .showImageOnFail(R.drawable.stub).showImageOnLoading(R.drawable.stub).build();

    private static String LOG_TAG = "GTSRecyclerViewAdapter";
    private List<Product> mDataset;
    private static MyClickListener myClickListener;

    public static class DataAlyansHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        ImageView imgAlyans;
        TextView txtAlyansAdi;
        TextView txtAlyansAciklama;
        TextView txtAlyansIndirim;
        TextView txtAlyansFiyati;
        TextView txtAlyansEskiFiyati;


        public DataAlyansHolder(View itemView) {
            super(itemView);
            imgAlyans = (ImageView) itemView.findViewById(R.id.imgAlyans);
            txtAlyansAdi = (TextView) itemView.findViewById(R.id.tvAlyansAdi);
            txtAlyansAciklama = (TextView) itemView.findViewById(R.id.tvAlyansAciklama);
            txtAlyansIndirim = (TextView) itemView.findViewById(R.id.tvAlyansIndirim);
            txtAlyansFiyati = (TextView) itemView.findViewById(R.id.tvAlyansFiyati);
            txtAlyansEskiFiyati = (TextView) itemView.findViewById(R.id.tvAlyansEskiFiyat);
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public MyRecyclerViewAdapter(List<Product> myDataset, Activity a) {
        mDataset = myDataset;
    }


    @Override
    public DataAlyansHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_row_allproduct, parent, false);

        DataAlyansHolder dataAlyansHolder = new DataAlyansHolder(view);
        return dataAlyansHolder;
    }

    @Override
    public void onBindViewHolder(DataAlyansHolder holder, int position) {
        ImageLoader.loader.displayImage(mDataset.get(position).getResimURL(), holder.imgAlyans, ImageLoader.options);
        holder.txtAlyansAdi.setText(mDataset.get(position).getAdi());
        holder.txtAlyansAciklama.setText(mDataset.get(position).getAciklama());
        int isIndirim = mDataset.get(position).getIndirim();
        if (isIndirim == 0) {
            holder.txtAlyansIndirim.setVisibility(View.GONE);
        } else {
            holder.txtAlyansIndirim.setText(" %" + mDataset.get(position).getIndirim() + " indirim");
        }

        holder.txtAlyansFiyati.setText(mDataset.get(position).getFiyati() + " TL");
        int isEskiFiyat = mDataset.get(position).getIndirim();
        if (isEskiFiyat == 0) {
            holder.txtAlyansEskiFiyati.setVisibility(View.GONE);
        } else {
            holder.txtAlyansEskiFiyati.setText(mDataset.get(position).getEskiFiyati() + " TL");
            holder.txtAlyansEskiFiyati.setPaintFlags(holder.txtAlyansEskiFiyati.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

    }

    public void addItem(Product alyansBean, int index) {
        mDataset.add(index, alyansBean);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}
