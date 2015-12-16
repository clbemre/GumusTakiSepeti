package com.clb.gumustakisepeti.pojo;

/**
 * Created by Emre on 17.09.2015.
 */
public class Product {

    private int objectID;
    private String adi;
    private float fiyati;
    private float eskiFiyati;
    private int indirim;
    private String stokNo;
    private String resimURL;
    private String aciklama;
    private String odemeSekli;
    private String kargoSekli;
    private String kargoSuresi;
    private String stokDurumu;

    public Product() {
    }

    public int getObjectID() {
        return objectID;
    }

    public void setObjectID(int objectID) {
        this.objectID = objectID;
    }

    public String getAdi() {
        return adi;
    }

    public void setAdi(String adi) {
        this.adi = adi;
    }

    public float getFiyati() {
        return fiyati;
    }

    public void setFiyati(float fiyati) {
        this.fiyati = fiyati;
    }

    public float getEskiFiyati() {
        return eskiFiyati;
    }

    public void setEskiFiyati(float eskiFiyati) {
        this.eskiFiyati = eskiFiyati;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public String getResimURL() {
        return resimURL;
    }

    public void setResimURL(String resimURL) {
        this.resimURL = resimURL;
    }

    public int getIndirim() {
        return indirim;
    }

    public void setIndirim(int indirim) {
        this.indirim = indirim;
    }

    public String getStokNo() {
        return stokNo;
    }

    public void setStokNo(String stokNo) {
        this.stokNo = stokNo;
    }

    public String getOdemeSekli() {
        return odemeSekli;
    }

    public void setOdemeSekli(String odemeSekli) {
        this.odemeSekli = odemeSekli;
    }

    public String getKargoSekli() {
        return kargoSekli;
    }

    public void setKargoSekli(String kargoSekli) {
        this.kargoSekli = kargoSekli;
    }

    public String getKargoSuresi() {
        return kargoSuresi;
    }

    public void setKargoSuresi(String kargoSuresi) {
        this.kargoSuresi = kargoSuresi;
    }

    public String getStokDurumu() {
        return stokDurumu;
    }

    public void setStokDurumu(String stokDurumu) {
        this.stokDurumu = stokDurumu;
    }


    @Override
    public String toString() {
        return "Product{" +
                "objectID=" + objectID +
                ", adi='" + adi + '\'' +
                ", fiyati=" + fiyati +
                ", eskiFiyati=" + eskiFiyati +
                ", indirim=" + indirim +
                ", stokNo='" + stokNo + '\'' +
                ", resimURL='" + resimURL + '\'' +
                ", aciklama='" + aciklama + '\'' +
                ", odemeSekli='" + odemeSekli + '\'' +
                ", kargoSekli='" + kargoSekli + '\'' +
                ", kargoSuresi='" + kargoSuresi + '\'' +
                ", stokDurumu='" + stokDurumu + '\'' +
                '}';
    }
}
