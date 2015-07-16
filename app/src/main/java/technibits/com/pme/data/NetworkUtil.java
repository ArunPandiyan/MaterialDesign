package technibits.com.pme.data;
//
//import android.app.Activity;
//import android.app.Application;
//import android.content.Context;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//
//import java.io.IOException;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
///**
// * Created by technibitsuser on 7/9/2015.
// */
//public class NetworkUtil extends Application {
//    protected boolean isOnline() {
//        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo netInfo = cm.getActiveNetworkInfo();
//        if (netInfo != null && netInfo.isConnected()) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//    public  boolean isNetworkConnected(){
//        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo netInfo = cm.getActiveNetworkInfo();
//        if (isOnline())
//        {
//            try
//            {
//                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://m.google.com").openConnection());
//                urlc.setRequestProperty("User-Agent", "Test");
//                urlc.setRequestProperty("Connection", "close");
//                urlc.setConnectTimeout(3000); //choose your own timeframe
//                urlc.setReadTimeout(4000); //choose your own timeframe
//                urlc.connect();
//                int networkcode2 = urlc.getResponseCode();
////                return (urlc.getResponseCode() == 200);
//                return true;
//            } catch (IOException e)
//            {
//                return (false);  //connectivity exists, but no internet.
//            }
//        } else
//        {
//            return false;  //no connectivity
//        }
//    }
//}
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtil {

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;


    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static boolean getConnectivityStatusString(Context context) {
        int conn = NetworkUtil.getConnectivityStatus(context);
        boolean status = false;
        if (conn == NetworkUtil.TYPE_WIFI) {
//            status = "Wifi enabled";
//            status = "Wifi enabled";
            status=checkIP();
        } else if (conn == NetworkUtil.TYPE_MOBILE) {
            status=checkIP();
//            status = "Mobile data enabled";
        } else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
            status=checkIP();
//            status = "Not connected to Internet";
        }
        return status;
    }
    public static boolean checkIP(){
        try {
//            InetAddress ipAddr = InetAddress.getByName("http://m.google.com"); //You can replace it with your name
//
//            if (ipAddr.equals("")) {
//                return false;
//            } else {
//                return true;
//            }

            HttpURLConnection urlc = (HttpURLConnection) (new URL("http://m.google.com").openConnection());
            urlc.setRequestProperty("User-Agent", "Test");
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(500); //choose your own timeframe
            urlc.setReadTimeout(500); //choose your own timeframe
            urlc.connect();
            int networkcode2 = urlc.getResponseCode();
            return (urlc.getResponseCode() == 200);

        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isConnectingToInternet(Context context){
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        try
                        {
                            HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                            urlc.setRequestProperty("User-Agent", "Test");
                            urlc.setRequestProperty("Connection", "close");
                            urlc.setConnectTimeout(500); //choose your own timeframe
                            urlc.setReadTimeout(500); //choose your own timeframe
                            urlc.connect();
                            int networkcode2 = urlc.getResponseCode();
                            return (urlc.getResponseCode() == 200);
                        } catch (IOException e)
                        {
                            return (false);  //connectivity exists, but no internet.
                        }
                    }

        }
        return false;
    }

    public static boolean isOnline() {

        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }


    public static void showNetworkstatus(Context context) {
        new AlertDialog.Builder(context)
                .setTitle("Warning")
                .setMessage("There is no internet connection.")

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete

                    }
                })

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}
