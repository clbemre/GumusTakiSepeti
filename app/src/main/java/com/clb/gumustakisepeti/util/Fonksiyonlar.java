package com.clb.gumustakisepeti.util;

import android.content.Context;

import com.clb.gumustakisepeti.database.Database;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Fonksiyonlar {
	
	public static boolean isEmailValid(String email) { //mail format� kontrol eder
	    boolean isValid = false;

	    String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
	    CharSequence inputStr = email;

	    Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(inputStr);
	    if (matcher.matches()) {
	        isValid = true;
	    }
	    return isValid;
	}
	
	public static String sha1(String data) //Sha1 �ifreleme yapar
    {
        try
        {
            byte[] b = data.getBytes();
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.reset();
            md.update(b);
            byte messageDigest[] = md.digest();
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < messageDigest.length; i++)
            {
                result.append(Integer.toString((messageDigest[i] & 0xff) + 0x100, 16).substring(1));
            }

            return result.toString();

        } catch (NoSuchAlgorithmException e)
        {

          //  Log.e("ARTags", "SHA1 is not a supported algorithm");
        }
        return null;
    }
	
	public static boolean giriskontrol(Context context){
		Database db = new Database(context);
		int count = db.getRowCount();// databasedeki table logindeki row say�s�
		if(count > 0){//0 dan fazla ise giri� yapm�s �nceden demek
			//kullan�c� giri� yapm�s
			return true;
		}
		return false;
	}

}
