package technibits.com.pme.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import technibits.com.pme.R;
import technibits.com.pme.adapter.PerformanceFragmentAdapter;
import technibits.com.pme.alarmactivity.RemindMe;
import technibits.com.pme.data.NetworkUtil;
import technibits.com.pme.parser.QuizJSONParser;
import technibits.com.pme.util.IabHelper;
import technibits.com.pme.util.IabResult;
import technibits.com.pme.util.Inventory;
import technibits.com.pme.util.Purchase;

/**
 * Created by technibitsuser on 7/16/2015.
 */


public class BuyProFragment extends Fragment {
    private static final String TAG = "Inapp billing";
    //    static final String ITEM_SKU = "android.test.purchased";
//    public static final String ITEM_SKU = "fuel";
    IabHelper mHelper;
    String mail = null;
    DBConnection db;
    View rootView;
    String ITEM_SKU = RemindMe.ITEM_SKU;

    private Button clickButton;
    private Button buyButton;
    private Button dup_purchase;
    private Button details_button;
    IInAppBillingService mService;
    ServiceConnection mServiceConn;
    BuyProFragment buyProFragment;

    boolean mIsPremium = RemindMe.mIsPremium;

    public BuyProFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DBConnection(getActivity());
        db.open();
        mail = db.getuserEmail();
        String status=null;
        status=db.getisPremium();
//        db.close();

//        if (!mIsPremium) {
        if(status.equals("free")){
//            mServiceConn=RemindMe.
            mServiceConn = new ServiceConnection() {
                @Override
                public void onServiceDisconnected(ComponentName name) {
                    mService = null;
                }

                @Override
                public void onServiceConnected(ComponentName name,
                                               IBinder service) {
                    mService = IInAppBillingService.Stub.asInterface(service);
                }
            };
            Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
            serviceIntent.setPackage("com.android.vending");
            getActivity().bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);

            String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqxBxdTkPR3csWLSc0mbhk1s2hH9el7ApJwoIW2ws6EeCuEDz0zQjP/735NwuUk7cxCSqqfBz3HnacfUgN1i6PBfBrozmTlqChNpyBQ7BBY5Jk2end9INPFO5VdzCNwyMQOqunHM1b9d78ZEIslab7ED6QG1cqKEnilS9cZjbkUqFflY41U1hLVqlTM0W38TXoBxuuE4rQ9EN7jCvBNan23Mkk2ofHYjyCZOEyLthsDEneQm0v9Ags6twf0IjN1MUdb9a9fkAA9KlC6YTg73qNQIs2WBGez3v0iArU1JnQAu79Uu5xyKVVXpzYgyDmgp3rXv6kAGMGI2+pZebQ0PZJwIDAQAB";
//        Account[] accounts = AccountManager.get(getActivity()).getAccounts();
//        mail=accounts[0].name;
            mHelper = new IabHelper(getActivity(), base64EncodedPublicKey);


            mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
                public void onIabSetupFinished(IabResult result) {
                    if (!result.isSuccess()) {
                        Log.d(TAG, "In-app Billing setup failed: " +
                                result);
                    } else {
                        Log.d(TAG, "In-app Billing is set up OK");
                        mHelper.queryInventoryAsync(mReceivedInventoryListener);
                    }
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String rescodde = "" + resultCode;
        Log.d("inapp billing", rescodde);
        Toast.makeText(getActivity(), "rescode is: " + rescodde, Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), "Intent is: " + data, Toast.LENGTH_SHORT).show();
        String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
//        Account[] accounts = AccountManager.get(getApplicationContext()).getAccounts();
//        String mail=accounts[0].name;
//        for (Account account : accounts) {
//            if (emailPattern.matcher(account.name).matches()) {
//                String possibleEmail = account.name;
//
//            }
//        }
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {

            super.onActivityResult(requestCode, resultCode, data);

        }
    }

    /*
    Check here what are the products that is already bought
     */
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            if (result.isFailure()) {
                // Handle error
                alert("Item already owned by default playstore account");
                return;
            } else if (purchase.getSku().equals(ITEM_SKU)) {
                //TODO:send details to server
//                sendToserver(purchase);

                consumeItem();
                buyButton.setEnabled(false);
            }

        }
    };

    public void sendToserver(Purchase purchase){
        String url = "http://jmbok.techtestbox.com/and/paid_details.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("orderId", purchase.getOrderId()));
        params.add(new BasicNameValuePair("packageName", purchase.getPackageName()));
        params.add(new BasicNameValuePair("productId", purchase.getSku()));
        params.add(new BasicNameValuePair("purchaseTime", String.valueOf(purchase.getPurchaseTime())));
        params.add(new BasicNameValuePair("purchaseState",String.valueOf(purchase.getPurchaseState())));
        params.add(new BasicNameValuePair("developerPayload", purchase.getDeveloperPayload()));
        params.add(new BasicNameValuePair("purchaseToken",purchase.getToken() ));
        boolean status = NetworkUtil.isOnline();
        if(status) {
            AsyncTaskCall ask = new AsyncTaskCall(getActivity(),buyProFragment,url, "buypro", params);
            ask.execute(url);
        }else{
            NetworkUtil.showNetworkstatus(getActivity());

        }
    }

    public void sendToserverdup(){
        String url = "http://jmbok.techtestbox.com/and/paid_details.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("orderId", "123"));
        params.add(new BasicNameValuePair("packageName", "mypackage.first.order"));
        params.add(new BasicNameValuePair("productId", "product_434"));
        params.add(new BasicNameValuePair("purchaseTime", "1437377936"));
        params.add(new BasicNameValuePair("purchaseState","0"));
        params.add(new BasicNameValuePair("developerPayload", "useremail@gmail.com"));
        params.add(new BasicNameValuePair("purchaseToken","1233fs563d432sdf13ds5f4s3d51fsd5f4dsf653s1s4365d4f" ));
        boolean status = NetworkUtil.isOnline();
        if(status) {
            buyProFragment=this;
            AsyncTaskCall ask = new AsyncTaskCall(getActivity(),buyProFragment,url, "buypro", params);
            ask.execute(url);
        }else{
            NetworkUtil.showNetworkstatus(getActivity());

        }
    }

    public void consumeItem() {
        mHelper.queryInventoryAsync(mReceivedInventoryListener);
    }

    IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            if (mHelper == null) return;
            if (result.isFailure()) {

                Toast.makeText(getActivity(), "Inventory Not Loaded", Toast.LENGTH_SHORT).show();
            } else {
                Purchase premiumPurchase = inventory.getPurchase(ITEM_SKU);
                mIsPremium = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));
//                mIsPremium = (premiumPurchase != null && RemindMe.verifyDeveloperPayload(premiumPurchase));
                Log.d(TAG, "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));
                Toast.makeText(getActivity(), "Inventory loaded !", Toast.LENGTH_SHORT).show();
//                mHelper.consumeAsync(inventory.getPurchase(ITEM_SKU),
//                        mConsumeFinishedListener);
            }
        }
    };

    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =  new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase,
                                              IabResult result) {

                    if (result.isSuccess()) {
                        clickButton.setEnabled(true);
                    }

                }
            };
    public void afterBuy(String strJson) throws JSONException {
        try {
            JSONObject json = new JSONObject(strJson);
            String success = json.getString("success");
            String mes = json.getString("message");
//			JSONObject json = new JSONObject(strJson);
//			String error = json.getString("error");
//			String mes = json.getString("message");
            if(success.equals("1")){
                Toast.makeText(getActivity(), "sent to server", Toast.LENGTH_SHORT).show();
                updateinPhoneDB();
            }else
                Toast.makeText(getActivity(), "server returned error", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.i("Erroring", "Buy Fragment");
            e.printStackTrace();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }

    private void updateinPhoneDB() {
        String sql="UPDATE user SET ispremium='premium' WHERE email='"+mail+"'";
        db.open();
        db.executeUpdate(sql);
        db.close();

    }

    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();
        if (mail.equalsIgnoreCase(payload)) {
            return true;
        } else
            return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_buypro, container, false);

        buyButton = (Button) rootView.findViewById(R.id.buy_pro);
        dup_purchase = (Button) rootView.findViewById(R.id.dup_purchase);
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mIsPremium){
                    alert("You've Already Bought the Premium Package ! Yay!!");
                }else {
                mHelper.launchPurchaseFlow(getActivity(), ITEM_SKU, 10001, mPurchaseFinishedListener, mail);
                }
            }
        });
        dup_purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToserverdup();
            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(getActivity());
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }

    void setWaitScreen(boolean set) {
        rootView.findViewById(R.id.user_status_layout).setVisibility(set ? View.GONE : View.VISIBLE);
//        rootView.findViewById(R.id.screen_wait).setVisibility(set ? View.VISIBLE : View.GONE);
    }
}

