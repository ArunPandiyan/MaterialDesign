package technibits.com.pme.data;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by technibitsuser on 7/9/2015.
 */
public class NetworkUtil extends Activity {
    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
    public boolean isNetworkConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (isOnline())
        {
            try
            {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://m.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(3000); //choose your own timeframe
                urlc.setReadTimeout(4000); //choose your own timeframe
                urlc.connect();
                int networkcode2 = urlc.getResponseCode();
//                return (urlc.getResponseCode() == 200);
                return true;
            } catch (IOException e)
            {
                return (false);  //connectivity exists, but no internet.
            }
        } else
        {
            return false;  //no connectivity
        }
    }
}
