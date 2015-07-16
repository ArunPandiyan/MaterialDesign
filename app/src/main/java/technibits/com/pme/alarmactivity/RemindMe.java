package technibits.com.pme.alarmactivity;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.provider.Settings;


import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import technibits.com.pme.R;
import technibits.com.pme.alarmmodel.DbHelper;

/**
 * @author appsrox.com
 */
public class RemindMe extends Application  {

//	private static final String TAG = "RemindMe";

    public static DbHelper dbHelper;
    public static SQLiteDatabase db;
    public static SharedPreferences sp;

    public static final String TIME_OPTION = "time_option";
    public static final String DATE_RANGE = "date_range";
    public static final String DATE_FORMAT = "date_format";
    public static final String TIME_FORMAT = "time_format";
    public static final String VIBRATE_PREF = "vibrate_pref";
    public static final String RINGTONE_PREF = "ringtone_pref";

    public static final String DEFAULT_DATE_FORMAT = "yyyy-M-d";
    public static GoogleAnalytics analytics;
    public static Tracker tracker;

    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        sp = PreferenceManager.getDefaultSharedPreferences(this);

        dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();
        analytics = GoogleAnalytics.getInstance(this);
        analytics.setLocalDispatchPeriod(1800);

        tracker = analytics.newTracker("UA-65003772-1"); // Replace with actual tracker/property Id
        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);
    }

    public static boolean showRemainingTime() {
        return "1".equals(sp.getString(TIME_OPTION, "0"));
    }

    public static int getDateRange() {
        return Integer.parseInt(sp.getString(DATE_RANGE, "0"));
    }

    public static String getDateFormat() {
        return sp.getString(DATE_FORMAT, DEFAULT_DATE_FORMAT);
    }

    public static boolean is24Hours() {
        return sp.getBoolean(TIME_FORMAT, true);
    }

    public static boolean isVibrate() {
        return sp.getBoolean(VIBRATE_PREF, true);
    }

    public static String getRingtone() {
        return sp.getString(RINGTONE_PREF, Settings.System.DEFAULT_ALARM_ALERT_URI.toString());
    }
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
                return  true;
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
