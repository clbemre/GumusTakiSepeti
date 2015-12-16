package com.clb.gumustakisepeti.pojo;

/**
 * Created by Emre on 17.09.2015.
 */
public class Siparis {

    private int sID;
    private String skKulAdi;
    private String skAd;
    private String skSoyad;
    private String skTelNo;
    private String skEmail;
    private String skAdres;
    private String siparisObjectId;
    private String siparisAdi;
    private int siparisAdet;
    private String siparisImageUrl;
    private int siparisDurumu;
    private String siparisMesaj;
    private String siparisStokKodu;
    private String siparisTarih;
    private String siparisKodu;
    private float siparisFiyat;
    private float siparisUrunFiyat;
    private String urunTuru;

    public Siparis() {
    }

    public int getsID() {
        return sID;
    }

    public void setsID(int sID) {
        this.sID = sID;
    }

    public String getSkKulAdi() {
        return skKulAdi;
    }

    public void setSkKulAdi(String skKulAdi) {
        this.skKulAdi = skKulAdi;
    }

    public String getSkAd() {
        return skAd;
    }

    public void setSkAd(String skAd) {
        this.skAd = skAd;
    }

    public String getSkSoyad() {
        return skSoyad;
    }

    public void setSkSoyad(String skSoyad) {
        this.skSoyad = skSoyad;
    }

    public String getSkTelNo() {
        return skTelNo;
    }

    public void setSkTelNo(String skTelNo) {
        this.skTelNo = skTelNo;
    }

    public String getSkEmail() {
        return skEmail;
    }

    public void setSkEmail(String skEmail) {
        this.skEmail = skEmail;
    }

    public String getSkAdres() {
        return skAdres;
    }

    public void setSkAdres(String skAdres) {
        this.skAdres = skAdres;
    }

    public int getSiparisDurumu() {
        return siparisDurumu;
    }

    public void setSiparisDurumu(int siparisDurumu) {
        this.siparisDurumu = siparisDurumu;
    }

    public String getSiparisObjectId() {
        return siparisObjectId;
    }

    public void setSiparisObjectId(String siparisObjectId) {
        this.siparisObjectId = siparisObjectId;
    }

    public String getSiparisAdi() {
        return siparisAdi;
    }

    public void setSiparisAdi(String siparisAdi) {
        this.siparisAdi = siparisAdi;
    }

    public int getSiparisAdet() {
        return siparisAdet;
    }

    public void setSiparisAdet(int siparisAdet) {
        this.siparisAdet = siparisAdet;
    }

    public String getSiparisImageUrl() {
        return siparisImageUrl;
    }

    public void setSiparisImageUrl(String siparisImageUrl) {
        this.siparisImageUrl = siparisImageUrl;
    }


    public String getSiparisMesaj() {
        return siparisMesaj;
    }

    public void setSiparisMesaj(String siparisMesaj) {
        this.siparisMesaj = siparisMesaj;
    }

    public String getSiparisStokKodu() {
        return siparisStokKodu;
    }

    public void setSiparisStokKodu(String siparisStokKodu) {
        this.siparisStokKodu = siparisStokKodu;
    }

    public String getSiparisKodu() {
        return siparisKodu;
    }

    public void setSiparisKodu(String siparisKodu) {
        this.siparisKodu = siparisKodu;
    }

    public String getSiparisTarih() {
        return siparisTarih;
    }

    public void setSiparisTarih(String siparisTarih) {
        this.siparisTarih = siparisTarih;
    }

    public float getSiparisFiyat() {
        return siparisFiyat;
    }

    public void setSiparisFiyat(float siparisFiyat) {
        this.siparisFiyat = siparisFiyat;
    }

    public float getSiparisUrunFiyat() {
        return siparisUrunFiyat;
    }

    public void setSiparisUrunFiyat(float siparisUrunFiyat) {
        this.siparisUrunFiyat = siparisUrunFiyat;
    }

    public String getUrunTuru() {
        return urunTuru;
    }

    public void setUrunTuru(String urunTuru) {
        this.urunTuru = urunTuru;
    }

    @Override
    public String toString() {
        return "Siparis{" +
                "sID=" + sID +
                ", skKulAdi='" + skKulAdi + '\'' +
                ", skAd='" + skAd + '\'' +
                ", skSoyad='" + skSoyad + '\'' +
                ", skTelNo='" + skTelNo + '\'' +
                ", skEmail='" + skEmail + '\'' +
                ", skAdres='" + skAdres + '\'' +
                ", siparisObjectId='" + siparisObjectId + '\'' +
                ", siparisAdi='" + siparisAdi + '\'' +
                ", siparisAdet=" + siparisAdet +
                ", siparisImageUrl='" + siparisImageUrl + '\'' +
                ", siparisDurumu=" + siparisDurumu +
                ", siparisMesaj='" + siparisMesaj + '\'' +
                ", siparisStokKodu='" + siparisStokKodu + '\'' +
                ", siparisTarih='" + siparisTarih + '\'' +
                ", siparisKodu='" + siparisKodu + '\'' +
                ", siparisFiyat=" + siparisFiyat +
                ", siparisUrunFiyat=" + siparisUrunFiyat +
                ", urunTuru='" + urunTuru + '\'' +
                '}';
    }
}
