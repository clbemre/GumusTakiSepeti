package com.clb.gumustakisepeti.pojo;

/**
 * Created by Emre on 14.10.2015.
 */
public class User {

    private int userId;
    private String kullaniciAdi;
    private String sifre;
    private String eMail;
    private String telNo;
    private String adres;
    private String ad;
    private String soyad;
    private String time;

    public User() {
    }

    public User(int userId,String kullaniciAdi, String eMail, String sifre,String time) {
        this.userId = userId;
        this.kullaniciAdi = kullaniciAdi;
        this.sifre = sifre;
        this.eMail = eMail;
        this.time = time;
    }

    public User(String kullaniciAdi, String sifre, String eMail) {
        this.kullaniciAdi = kullaniciAdi;
        this.sifre = sifre;
        this.eMail = eMail;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getKullaniciAdi() {
        return kullaniciAdi;
    }

    public void setKullaniciAdi(String kullaniciAdi) {
        this.kullaniciAdi = kullaniciAdi;
    }

    public String getSifre() {
        return sifre;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getSoyad() {
        return soyad;
    }

    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", kullaniciAdi='" + kullaniciAdi + '\'' +
                ", Sifre='" + sifre + '\'' +
                ", eMail='" + eMail + '\'' +
                ", telNo='" + telNo + '\'' +
                ", adres='" + adres + '\'' +
                ", ad='" + ad + '\'' +
                ", soyad='" + soyad + '\'' +
                '}';
    }
}
