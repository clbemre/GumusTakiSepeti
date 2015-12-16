package com.clb.gumustakisepeti.database;

import android.util.Log;

import com.clb.gumustakisepeti.util.ConstantString;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class PostClass {
    static InputStream veri;
    static String veri_string;


    public PostClass() {
        // TODO Auto-generated constructor stub
    }

    public void postData(String url, JSONObject obj) {
        // Create a new HttpClient and Post Header

        HttpParams myParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(myParams, 10000);
        HttpConnectionParams.setSoTimeout(myParams, 10000);
        HttpClient httpclient = new DefaultHttpClient(myParams);
        String json = obj.toString();

        try {

            HttpPost httppost = new HttpPost(url.toString());
            httppost.setHeader("Content-type", "application/json");

            StringEntity se = new StringEntity(obj.toString());
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httppost.setEntity(se);

            HttpResponse response = httpclient.execute(httppost);
            String temp = EntityUtils.toString(response.getEntity());
            Log.i("tag", temp);


        } catch (ClientProtocolException e) {

        } catch (IOException e) {
        }
    }

    public String httpPost(String url, String method, List<NameValuePair> params, int time) {

        //url: post yap�lacak adres
        //method: post mu get mi
        //params:post edilecek veriler de�i�kenler
        //time: sunucudan cevap gelmezse ka� sn sonra uygulama donmadan postun iptal edilece�i
        try {

            if (method == "POST") {
                HttpParams httpParameters = new BasicHttpParams();
                int timeout1 = time;
                int timeout2 = time;
                HttpConnectionParams.setConnectionTimeout(httpParameters, timeout1);
                HttpConnectionParams.setSoTimeout(httpParameters, timeout2);
                DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                veri = httpEntity.getContent();

            } else if (method == "GET") {

                DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "?" + paramString;
                HttpGet httpGet = new HttpGet(url);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                veri = httpEntity.getContent();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    veri, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            veri.close();
            veri_string = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Hata " + e.toString());
        }

        return veri_string; // Ald���m�z cevab�n string halini geri d�n�yoruz
    }


    public static void sendJson(final JSONObject json) {
        HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
        HttpResponse response;
        String URL = ConstantString.URL_POST_SEPET_SIPARIS_VER;

        try {
            HttpPost post = new HttpPost(URL);

            // Create a NameValuePair out of the JSONObject + a name
            List<NameValuePair> nVP = new ArrayList<NameValuePair>(2);
            nVP.add(new BasicNameValuePair("Siparis", json.toString()));

            // Hand the NVP to the POST
            post.setEntity(new UrlEncodedFormEntity(nVP));
            Log.i("main", "TestPOST - nVP = " + nVP.toString());

            // Collect the response
            response = client.execute(post);

            /*Checking response */
            if (response != null) {
                InputStream in = response.getEntity().getContent(); //Get the data in the entity
            }
        } catch (Exception e) {
            e.printStackTrace();
            //createDialog("Error", "Cannot Establish Connection");
        }
    }


//    public String GetHTTPData(String urlString){
//        try{
//            URL url = new URL(urlString);
//            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//
//            // Check the connection status
//            if(urlConnection.getResponseCode() == 200)
//            {
//                // if response code = 200 ok
//                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
//
//                // Read the BufferedInputStream
//                BufferedReader r = new BufferedReader(new InputStreamReader(in));
//                StringBuilder sb = new StringBuilder();
//                String line;
//                while ((line = r.readLine()) != null) {
//                    sb.append(line);
//                }
//                veri_string = sb.toString();
//                // End reading...............
//
//                // Disconnect the HttpURLConnection
//                urlConnection.disconnect();
//            }
//            else
//            {
//                // Do something
//            }
//        }catch (MalformedURLException e){
//            e.printStackTrace();
//        }catch(IOException e){
//            e.printStackTrace();
//        }finally {
//
//        }
//        // Return the data from specified url
//        return veri_string;
//    }

}
