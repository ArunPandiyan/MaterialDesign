package technibits.com.pme.activity;

/**
 * Created by Technibits-13 on 15-May-2015.
 */

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.app.Fragment;
//import android.app.FragmentActivity;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;

import java.util.regex.Pattern;

import technibits.com.pme.R;
import technibits.com.pme.alarmactivity.SettingsActivity;
import technibits.com.pme.data.NetworkUtil;
import technibits.com.pme.util.IabHelper;
import technibits.com.pme.util.IabResult;
import technibits.com.pme.util.Inventory;
import technibits.com.pme.util.Purchase;


public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static String TAG = MainActivity.class.getSimpleName();
    //    private static final String TAG = "Inapp billing";

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private TypedArray navMenuIcons;
    public DBConnection db;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 0;

    /**
     * A flag indicating that a PendingIntent is in progress and prevents us
     * from starting further intents.
     */
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private ConnectionResult mConnectionResult;

    //    static final String ITEM_SKU = "android.test.purchased";
    static final String ITEM_SKU = "fuel";
    IabHelper mHelper;
    String mail=null;

    private Button clickButton;
    private Button buyButton;
    private Button details_button;
    IInAppBillingService mService;
    ServiceConnection mServiceConn;


    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
//        mServiceConn = new ServiceConnection() {
//            @Override
//            public void onServiceDisconnected(ComponentName name) {
//                mService = null;
//            }
//
//            @Override
//            public void onServiceConnected(ComponentName name,
//                                           IBinder service) {
//                mService = IInAppBillingService.Stub.asInterface(service);
//            }
//        };
//        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
//        serviceIntent.setPackage("com.android.vending");
//        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);
//        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsbuXRUkqRiqVHNKkuzDEhCZ1woplT/KvjbGmVmeaKO36YyIwZL00tmYARan9rfwYTY9ncQ9Jw5fJagMPC6Dc+VR5gqfna9zSuaTBK5J5badZGOFVacxemEqnVRH3e0iLHOxbqA7D4hBa5+58Z0vvoPP+UZl9Nr6YI9s/RCfgiMxbk4dMKfsn7bXF4vwscZzU9eZKWjQ6U8VrOFRBl7OXgiOIOkkX6NrfAPVc7YRINKCUsRWbg8oSw77pS5+gLTDfrUH817OHAjkTTWrko5a9i8ooVGqjmSM7ZXYFsmam9t7sethihJ0sRUaHy39bngHftzT386EudXDCbVNJO9BGOQIDAQAB";
//        Account[] accounts = AccountManager.get(getApplicationContext()).getAccounts();
//        mail=accounts[0].name;
//        mHelper = new IabHelper(this, base64EncodedPublicKey);
//
//
//        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
//            public void onIabSetupFinished(IabResult result)
//            {
//                if (!result.isSuccess()) {
//                    Log.d(TAG, "In-app Billing setup failed: " +
//                            result);
//                } else {
//                    Log.d(TAG, "In-app Billing is set up OK");
//                    mHelper.queryInventoryAsync(mReceivedInventoryListener);
//                }
//            }
//        });
    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode,Intent data)
//    {
//        String rescodde=""+ resultCode;
//        Log.d("inapp billing",rescodde );
//        Toast.makeText(getApplicationContext(), "rescode is: "+rescodde, Toast.LENGTH_SHORT).show();
//        Toast.makeText(getApplicationContext(), "Intent is: "+data, Toast.LENGTH_SHORT).show();
//        String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
//        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
////        Account[] accounts = AccountManager.get(getApplicationContext()).getAccounts();
////        String mail=accounts[0].name;
////        for (Account account : accounts) {
////            if (emailPattern.matcher(account.name).matches()) {
////                String possibleEmail = account.name;
////
////            }
////        }
//        if (!mHelper.handleActivityResult(requestCode,resultCode, data)) {
//
//            super.onActivityResult(requestCode, resultCode, data);
//
//        }
//    }

    /*
    Check here what are the products that is already bought
     */


    @Override
    public void onDestroy() {
        super.onDestroy();
       
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db=new DBConnection(getApplicationContext());
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        int d = getIntent().getIntExtra("fragmentNumber", 0);//getExtras("fragmentNumber");
        if (getIntent().getIntExtra("fragmentNumber", 0) == 3) {
            displayView(3);//set the desired fragment as current fragment to fragment pager
        }
        // display the first navigation drawer view on app launch
        displayView(0);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        if (id == R.id.action_logout) {
            db.open();
            db.dropTable();
            signOutFromGplus();
            revokeGplusAccess();
            Intent intent=new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        if (id == R.id.action_profile) {
            Intent intent=new Intent(this,CreateAccountActivity.class);
            intent.putExtra("mode", "update");
            startActivity(intent);
            return true;
        }

//        if (id == R.id.action_search) {
//            Toast.makeText(getApplicationContext(), "Search action is selected!", Toast.LENGTH_SHORT).show();
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new ModeActivity();
                title = getString(R.string.title_take_exam);
                break;
            case 1:
                fragment = new PerformanceFragment();
                title = getString(R.string.title_performance_history);
                break;
            case 2:
                fragment = new ReviewFragment();
                title = getString(R.string.title_review);
                break;
            case 3:
                fragment = new ScheduleFragment();
                title = getString(R.string.title_schedules);
                break;
            case 4:
                fragment = new VideoFragment();
                title = getString(R.string.title_videos);
                break;
            case 5:
                fragment = new ExxamFragment();
                title = getString(R.string.title_about);
                break;

            case 6:
                fragment = new BuyProFragment();
                title = getString(R.string.title_pro);
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();// getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }

    }

    @Override
    public void onConnected(Bundle bundle) {
        mSignInClicked = false;
//        Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
//            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,0).show();
            NetworkUtil.showNetworkstatus(this);

            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }

    }


    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Method to resolve any signin errors
     */
    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }
    public void signOutFromGplus() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();

        }
    }

    /**
     * Revoking access from google
     */
    private void revokeGplusAccess() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Log.e(TAG, "User access revoked!");
                            mGoogleApiClient.connect();

                        }

                    });
        }
    }

}
