package technibits.com.pme.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;


public class MasterDownload {

    public String queryRESTurl(String url) {

        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        HttpResponse response;

        try {
            response = httpclient.execute(httpget);
            Log.i("", "Status:[" + response.getStatusLine().toString() + "]");
            HttpEntity entity = response.getEntity();

            if (entity != null) {

                InputStream instream = entity.getContent();

                String result = convertStreamToString(instream);
                Log.i("", "Result of converstion: [" + result + "]");

                instream.close();
                return result;
            }
        } catch (ClientProtocolException e) {
            Log.e("REST", "There was a protocol based error", e);
        } catch (IOException e) {
            Log.e("REST", "There was an IO Stream related error", e);
        }

        return null;
    }

    static String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public String post(String url, List<NameValuePair> params) throws ClientProtocolException, IOException {
        InputStream is = null;
        StringBuilder sb = null;
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        String json = sb.toString();
        System.out.println("" + sb.toString());

        return json;
    }

}
