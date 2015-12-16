package com.clb.gumustakisepeti.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public final class Utilities {

    public static boolean containsTurkishCharacters(final String inString) {
        return inString.matches(".*[ıİşŞüÜğĞçÇöÖ].*");
    }

    public static boolean startsWithANumber(final String inString) {
        return inString.matches("[//d].*");
    }


    public static String getHashRandom(String s) {
        String basKisim = randomCharCheckTurkishCharacter();
        String sonKisim = randomCharCheckTurkishCharacter();
        Random rnd = new Random();
        int randomSayi;
        String sayiS;
        do {
            randomSayi = rnd.nextInt();
            sayiS = String.valueOf(randomSayi);
        } while (randomSayi <= 0 || sayiS.length() != 6);
        s = basKisim + randomSayi + sonKisim;
        return s;
    }

    public static String randomCharCheckTurkishCharacter() {
        String son = null;
        do {
            son = randomChar();
        } while (Utilities.containsTurkishCharacters(son));
        return son;
    }

    public static String randomChar() {
        Random r = new Random();
        char c = (char) (r.nextInt(26) + 'a');
        char c2 = (char) (r.nextInt(26) + 'a');
        return (c + "" + c2).toLowerCase();
    }

//    public static String getMD5Hash(byte[] bytes) {
//        MessageDigest m = null;
//        try {
//            m = MessageDigest.getInstance("MD5");
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        if (m != null) {
//            byte[] digest = m.digest(bytes);
//            StringBuilder hexString = new StringBuilder();
//            for (byte aDigest : digest) {
//                String hex = Integer.toHexString(0xFF & aDigest);
//                if (hex.length() == 1)
//                    hexString.append('0');
//
//                hexString.append(hex);
//            }
//            return hexString.toString();
//        } else {
//            return "";
//        }
//
//    }


    public static void warningAlertDialog(Context context, String title,
                                          String message, String positiveButton, String negativeButton,
                                          DialogInterface.OnClickListener pozBListener,
                                          DialogInterface.OnClickListener negBListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveButton, pozBListener);
        builder.setNegativeButton(negativeButton, negBListener);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static void warningAlertDialogPoz(Context context, String title,
                                             String message, String positiveButton,
                                             DialogInterface.OnClickListener pozBListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(positiveButton, pozBListener);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public static void showAlertDialog(Context context, String title,
                                       String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(android.R.string.ok, listener);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static void showToastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showToastMessageLengthLong(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void clearAndStartActivity(Context packageContext,
                                             Class<?> cls) {
        Intent intent = new Intent(packageContext, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        packageContext.startActivity(intent);
    }

    public static void utilStartActivity(Context packageContext,
                                         Class<?> cls) {
        Intent intent = new Intent(packageContext, cls);
        packageContext.startActivity(intent);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isAvailable();
    }

    public static String downloadData(String address) {
        StringBuilder stringBuilder = new StringBuilder();
        URL url;
        try {
            url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                int c = 0;
                while ((c = inputStream.read()) != -1) {
                    stringBuilder.appendCodePoint(c);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static String buildPostParameters(Object content) {
        String output = null;
        if ((content instanceof String) ||
                (content instanceof JSONObject) ||
                (content instanceof JSONArray)) {
            output = content.toString();
        } else if (content instanceof Map) {
            Uri.Builder builder = new Uri.Builder();
            HashMap hashMap = (HashMap) content;
            if (hashMap != null) {
                Iterator entries = hashMap.entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry entry = (Map.Entry) entries.next();
                    builder.appendQueryParameter(entry.getKey().toString(), entry.getValue().toString());
                    entries.remove(); // avoids a ConcurrentModificationException
                }
                output = builder.build().getEncodedQuery();
            }
        }

        return output;
    }

    public static URLConnection makeRequest(String method, String apiAddress, String accessToken, String mimeType, String requestBody) throws IOException {
        URL url = new URL(apiAddress);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(!method.equals("GET"));
        urlConnection.setRequestMethod(method);

//        urlConnection.setRequestProperty("Authorization", "Bearer " + accessToken);

        urlConnection.setRequestProperty("Content-Type", mimeType);
        OutputStream outputStream = new BufferedOutputStream(urlConnection.getOutputStream());
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "utf-8"));
        writer.write(requestBody);
        writer.flush();
        writer.close();
        outputStream.close();

        urlConnection.connect();

        return urlConnection;
    }

    private Utilities() {
    }
}
