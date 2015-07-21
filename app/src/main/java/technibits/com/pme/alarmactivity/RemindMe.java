package technibits.com.pme.alarmactivity;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;


import com.android.vending.billing.IInAppBillingService;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import technibits.com.pme.R;
import technibits.com.pme.activity.BuyProFragment;
import technibits.com.pme.activity.DBConnection;
import technibits.com.pme.alarmmodel.DbHelper;
import technibits.com.pme.util.IabHelper;
import technibits.com.pme.util.IabResult;
import technibits.com.pme.util.Inventory;
import technibits.com.pme.util.Purchase;

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
    public static final String ITEM_SKU = "test_product";


//    public static final String DEFAULT_DATE_FORMAT = "yyyy-M-d";
    public static final String DEFAULT_DATE_FORMAT = "dd-MM-yyyy";
    public static GoogleAnalytics analytics;
    public static Tracker tracker;
    public static boolean mIsPremium;
    public static DBConnection dbConnection;
    IInAppBillingService mServiceApp;
    ServiceConnection mServiceConnApp;
    public static IabHelper mHelperApp;
    String TAG="RemindMe";


    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        dbConnection=new DBConnection(this);

        dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();
//        analytics = GoogleAnalytics.getInstance(this);
//        analytics.setLocalDispatchPeriod(1800);
//
//        tracker = analytics.newTracker("UA-65003772-1"); // Replace with actual tracker/property Id
//        tracker.enableExceptionReporting(true);
//        tracker.enableAdvertisingIdCollection(true);
//        tracker.enableAutoActivityTracking(true);

        mServiceConnApp = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
                mServiceApp = null;
            }

            @Override
            public void onServiceConnected(ComponentName name,
                                           IBinder service) {
                mServiceApp = IInAppBillingService.Stub.asInterface(service);
            }
        };
        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConnApp, Context.BIND_AUTO_CREATE);
//        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsbuXRUkqRiqVHNKkuzDEhCZ1woplT/KvjbGmVmeaKO36YyIwZL00tmYARan9rfwYTY9ncQ9Jw5fJagMPC6Dc+VR5gqfna9zSuaTBK5J5badZGOFVacxemEqnVRH3e0iLHOxbqA7D4hBa5+58Z0vvoPP+UZl9Nr6YI9s/RCfgiMxbk4dMKfsn7bXF4vwscZzU9eZKWjQ6U8VrOFRBl7OXgiOIOkkX6NrfAPVc7YRINKCUsRWbg8oSw77pS5+gLTDfrUH817OHAjkTTWrko5a9i8ooVGqjmSM7ZXYFsmam9t7sethihJ0sRUaHy39bngHftzT386EudXDCbVNJO9BGOQIDAQAB";
          String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqxBxdTkPR3csWLSc0mbhk1s2hH9el7ApJwoIW2ws6EeCuEDz0zQjP/735NwuUk7cxCSqqfBz3HnacfUgN1i6PBfBrozmTlqChNpyBQ7BBY5Jk2end9INPFO5VdzCNwyMQOqunHM1b9d78ZEIslab7ED6QG1cqKEnilS9cZjbkUqFflY41U1hLVqlTM0W38TXoBxuuE4rQ9EN7jCvBNan23Mkk2ofHYjyCZOEyLthsDEneQm0v9Ags6twf0IjN1MUdb9a9fkAA9KlC6YTg73qNQIs2WBGez3v0iArU1JnQAu79Uu5xyKVVXpzYgyDmgp3rXv6kAGMGI2+pZebQ0PZJwIDAQAB";
//        Account[] accounts = AccountManager.get(getActivity()).getAccounts();
//        mail=accounts[0].name;
        mHelperApp = new IabHelper(getApplicationContext(), base64EncodedPublicKey);


        mHelperApp.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    Log.d(TAG, "In-app Billing setup failed: " +
                            result);
                } else {
                    Log.d(TAG, "In-app Billing is set up OK");
                    mHelperApp.queryInventoryAsync(mReceivedInventoryListener);
                }
            }
        });
    }


    IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            if (result.isFailure()) {
                Toast.makeText(getApplicationContext(), "Inventory Not Loaded", Toast.LENGTH_SHORT).show();
            } else {
                Purchase premiumPurchase = inventory.getPurchase(ITEM_SKU);
                mIsPremium = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));
                Log.d("RemindME", "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));
                Toast.makeText(getApplicationContext(), "Inventory loaded !", Toast.LENGTH_SHORT).show();

//                mHelper.consumeAsync(inventory.getPurchase(ITEM_SKU),
//                        mConsumeFinishedListener);
            }
        }
    };

    public static boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();
        dbConnection.open();
        String mail=dbConnection.getuserEmail();
        dbConnection.close();
        if (mail.equalsIgnoreCase(payload)) {
            return true;
        } else
            return false;
    }
    public static String returnMail(){
        dbConnection.open();
        String mail=dbConnection.getuserEmail();
        dbConnection.close();
        return mail;
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
//    protected boolean isOnline() {
//        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo netInfo = cm.getActiveNetworkInfo();
//        if (netInfo != null && netInfo.isConnected()) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//    public boolean isNetworkConnected(){
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
//                return  true;
//            } catch (IOException e)
//            {
//                return (false);  //connectivity exists, but no internet.
//            }
//        } else
//        {
//            return false;  //no connectivity
//        }
//    }


}
