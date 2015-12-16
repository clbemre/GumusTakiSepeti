package com.clb.gumustakisepeti.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Vibrator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.clb.gumustakisepeti.R;
import com.clb.gumustakisepeti.database.Database;
import com.clb.gumustakisepeti.pojo.Siparis;
import com.clb.gumustakisepeti.util.ImageLoader;
import com.clb.gumustakisepeti.util.Utilities;

import java.util.List;

/**
 * Created by Emre on 17.09.2015.
 */
public class SepetRecyclerViewAdapter extends RecyclerView
        .Adapter<SepetRecyclerViewAdapter.DataSepetHolder> {

//    ImageLoader imageLoader = ImageLoader.getInstance();
//    DisplayImageOptions options = new DisplayImageOptions.Builder()
//            .cacheOnDisc(true).cacheInMemory(true).resetViewBeforeLoading(true)
//            .showImageForEmptyUri(R.drawable.stub)
//            .showImageOnFail(R.drawable.stub).showImageOnLoading(R.drawable.stub).build();

    private static String LOG_TAG = "GTSSepetRecyclerViewAdapter";
    private List<Siparis> mDataset;
    private static MyClickListener myClickListener;
    Activity context;


    public class DataSepetHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        ImageView imgSepetUrun;
        TextView tvSptSiparisKodu;
        TextView tvSptUrunAdi;
        TextView tvSptUrunFiyat;
        TextView tvSptUrunAdet;
        TextView tvAdetEksilt;
        TextView tvAdetArttır;
        TextView tvSptUrunMesaj;
        TextView tvSptSepettenCikar;
        Button sptBtnKaydet;


        public DataSepetHolder(View itemView) {
            super(itemView);
            imgSepetUrun = (ImageView) itemView.findViewById(R.id.imgSepetUrun);
            tvSptSiparisKodu = (TextView) itemView.findViewById(R.id.tvSptSiparisKodu);
            tvSptUrunAdi = (TextView) itemView.findViewById(R.id.tvSptUrunAdi);
            tvSptUrunFiyat = (TextView) itemView.findViewById(R.id.tvSptUrunFiyat);
            tvSptUrunAdet = (TextView) itemView.findViewById(R.id.tvSptUrunAdet);
            tvAdetEksilt = (TextView) itemView.findViewById(R.id.tvAdetEksilt);
            tvAdetArttır = (TextView) itemView.findViewById(R.id.tvAdetArttır);
            tvSptUrunMesaj = (TextView) itemView.findViewById(R.id.tvSptUrunMesaj);
            tvSptSepettenCikar = (TextView) itemView.findViewById(R.id.tvSptSepettenCikar);
            sptBtnKaydet = (Button) itemView.findViewById(R.id.sptBtnKaydet);
            sptBtnKaydet.setOnClickListener(this);
            tvAdetArttır.setOnClickListener(this);
            tvAdetEksilt.setOnClickListener(this);
            tvSptSepettenCikar.setOnClickListener(this);
            Log.i(LOG_TAG, "Adding Sepet Listener");
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Database database = new Database(context.getApplicationContext());
            int id = v.getId();
            if (id == R.id.tvAdetArttır) {
                vibrate(100);
                int adapterPosition = getAdapterPosition();
                double geciciAdet = Double.valueOf(tvSptUrunAdet.getText().toString());
                double geciciFiyat = Double.valueOf(mDataset.get(adapterPosition).getSiparisUrunFiyat());
                geciciAdet++;
                double genelFiyat = geciciAdet * geciciFiyat;
                tvSptUrunFiyat.setText((int) genelFiyat + "");
                tvSptUrunAdet.setText((int) geciciAdet + " ");
                Siparis siparisGuncelle = new Siparis();
                siparisGuncelle.setsID(mDataset.get(adapterPosition).getsID());
                siparisGuncelle.setSiparisAdet((int) geciciAdet);
                siparisGuncelle.setSiparisFiyat((int) genelFiyat);
                database.sepetAdetFiyatGuncelle(siparisGuncelle);
            }
            if (id == R.id.tvAdetEksilt) {
                double geciciAdet2 = Double.valueOf(tvSptUrunAdet.getText().toString());
                if ((int) geciciAdet2 <= 1) {

                } else {
                    vibrate(100);
                    int adapterPosition = getAdapterPosition();
                    double geciciFiyat2 = Double.valueOf(mDataset.get(adapterPosition).getSiparisUrunFiyat());
                    geciciAdet2--;
                    double genelFiyat2 = geciciAdet2 * geciciFiyat2;
                    tvSptUrunFiyat.setText((int) genelFiyat2 + "");
                    tvSptUrunAdet.setText((int) geciciAdet2 + " ");
                    Siparis siparisGuncelle = new Siparis();
                    siparisGuncelle.setsID(mDataset.get(adapterPosition).getsID());
                    siparisGuncelle.setSiparisAdet((int) geciciAdet2);
                    siparisGuncelle.setSiparisFiyat((int) genelFiyat2);
                    database.sepetAdetFiyatGuncelle(siparisGuncelle);
                }
            }
            if (id == R.id.tvSptSepettenCikar) {
                Siparis siparis = new Siparis();
                siparis.setsID(mDataset.get(getAdapterPosition()).getsID());
                boolean deleteSpt = database.sptUrunSil(siparis);
                if (deleteSpt) {
                    deleteItem(getAdapterPosition());
                } else {
                    Utilities.showToastMessage(context.getApplicationContext(), "Beklenmeyen Hata!");
                }

            }
            if (id == R.id.sptBtnKaydet) {
                String stMesaj = tvSptUrunMesaj.getText().toString();
                Siparis siparisGuncelle = new Siparis();
                siparisGuncelle.setsID(mDataset.get(getAdapterPosition()).getsID());
                siparisGuncelle.setSiparisMesaj(stMesaj);
                database.sepetMesajGuncelle(siparisGuncelle);
                Utilities.showToastMessage(context.getApplicationContext(), "Kaydedildi");
            }
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public SepetRecyclerViewAdapter(List<Siparis> myDataset, Activity a) {
        context = a;
        mDataset = myDataset;
    }


    @Override
    public DataSepetHolder onCreateViewHolder(ViewGroup parent,
                                              int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_row_sepetim, parent, false);

        DataSepetHolder dataSepetHolder = new DataSepetHolder(view);
        return dataSepetHolder;
    }

    @Override
    public void onBindViewHolder(DataSepetHolder holder, int position) {
        ImageLoader.loader.displayImage(mDataset.get(position).getSiparisImageUrl(), holder.imgSepetUrun, ImageLoader.options);
        holder.tvSptSiparisKodu.setText(mDataset.get(position).getSiparisKodu());
        holder.tvSptUrunAdi.setText(mDataset.get(position).getSiparisAdi());
        holder.tvSptUrunFiyat.setText((int) mDataset.get(position).getSiparisFiyat() + "");
        holder.tvSptUrunAdet.setText(mDataset.get(position).getSiparisAdet() + "");
        holder.tvSptUrunMesaj.setText(mDataset.get(position).getSiparisMesaj());
        if (mDataset.get(position).getUrunTuru().equals("0")) {
            holder.tvSptUrunMesaj.setHint("Parmak Numarası/Numaralarını Giriniz.");
        } else if (mDataset.get(position).getUrunTuru().equals("1")) {
            holder.tvSptUrunMesaj.setHint("Özel Mesajınız");
        }
    }

    public void vibrate(int duration) {
        Vibrator vibs = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibs.vibrate(duration);
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
