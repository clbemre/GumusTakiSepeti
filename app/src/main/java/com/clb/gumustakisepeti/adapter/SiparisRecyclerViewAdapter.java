package com.clb.gumustakisepeti.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.clb.gumustakisepeti.R;
import com.clb.gumustakisepeti.pojo.Siparis;
import com.clb.gumustakisepeti.util.ImageLoader;

import java.util.List;

/**
 * Created by Emre on 17.09.2015.
 */
public class SiparisRecyclerViewAdapter extends RecyclerView
        .Adapter<SiparisRecyclerViewAdapter.DataSiparisHolder> {

//    ImageLoader imageLoader = ImageLoader.getInstance();
//    DisplayImageOptions options = new DisplayImageOptions.Builder()
//            .cacheOnDisc(true).cacheInMemory(true).resetViewBeforeLoading(true)
//            .showImageForEmptyUri(R.drawable.stub)
//            .showImageOnFail(R.drawable.stub).showImageOnLoading(R.drawable.stub).build();

    private static String LOG_TAG = "GTSSiparisRecyclerViewAdapter";
    private List<Siparis> mDataset;
    private static MyClickListener myClickListener;

    public static class DataSiparisHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        ImageView imgSiparis;
        TextView txtSiparisTarih;
        TextView txtSiparisStokKodu;

        public DataSiparisHolder(View itemView) {
            super(itemView);
            imgSiparis = (ImageView) itemView.findViewById(R.id.imgSiparis);
            txtSiparisTarih = (TextView) itemView.findViewById(R.id.tvSiparisTarih);
            txtSiparisStokKodu = (TextView) itemView.findViewById(R.id.tvSiparisStokKodu);
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

    public SiparisRecyclerViewAdapter(List<Siparis> myDataset, Activity a) {
        mDataset = myDataset;
    }


    @Override
    public DataSiparisHolder onCreateViewHolder(ViewGroup parent,
                                                int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_row_siparislerim, parent, false);

        DataSiparisHolder dataSiparisHolder = new DataSiparisHolder(view);
        return dataSiparisHolder;
    }

    @Override
    public void onBindViewHolder(DataSiparisHolder holder, int position) {
        ImageLoader.loader.displayImage(mDataset.get(position).getSiparisImageUrl(), holder.imgSiparis, ImageLoader.options);
        holder.txtSiparisTarih.setText(mDataset.get(position).getSiparisTarih());
        holder.txtSiparisStokKodu.setText(mDataset.get(position).getSiparisStokKodu());

    }

    public void addItem(Siparis siparisBean, int index) {
        mDataset.add(index, siparisBean);
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
