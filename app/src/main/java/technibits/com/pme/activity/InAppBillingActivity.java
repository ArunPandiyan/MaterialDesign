package technibits.com.pme.activity;

/**
 * Created by technibitsuser on 7/13/2015.
 */

import technibits.com.pme.R;
import technibits.com.pme.util.IabHelper;
import technibits.com.pme.util.IabResult;
import technibits.com.pme.util.Inventory;
import technibits.com.pme.util.Purchase;

import android.app.Activity;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class InAppBillingActivity extends Activity {

    private static final String TAG = "InappBillingActivity";
    static final String ITEM_SKU = "android.test.purchased";
    IabHelper mHelper;

    private Button clickButton;
    private Button buyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_about);

        buyButton = (Button) findViewById(R.id.buyButton);

        String base64EncodedPublicKey =
    "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsbuXRUkqRiqVHNKkuzDEhCZ1woplT/KvjbGmVmeaKO36YyIwZL00tmYARan9rfwYTY9ncQ9Jw5fJagMPC6Dc+VR5gqfna9zSuaTBK5J5badZGOFVacxemEqnVRH3e0iLHOxbqA7D4hBa5+58Z0vvoPP+UZl9Nr6YI9s/RCfgiMxbk4dMKfsn7bXF4vwscZzU9eZKWjQ6U8VrOFRBl7OXgiOIOkkX6NrfAPVc7YRINKCUsRWbg8oSw77pS5+gLTDfrUH817OHAjkTTWrko5a9i8ooVGqjmSM7ZXYFsmam9t7sethihJ0sRUaHy39bngHftzT386EudXDCbVNJO9BGOQIDAQAB";

        mHelper = new IabHelper(this, base64EncodedPublicKey);


        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    Log.d(TAG, "In-app Billing setup failed:" + result);
                } else {
                    Log.d(TAG, "In-app Billing is set up OK");
                }
            }
        });
    }

    public void onbuyButtonClicked(View view){
        mHelper.launchPurchaseFlow(this, ITEM_SKU, 10001,
                mPurchaseFinishedListener, "mypurchasetoken");
    }
    public void buyClick(View view) {
        mHelper.launchPurchaseFlow(this, ITEM_SKU, 10001,
                mPurchaseFinishedListener, "mypurchasetoken");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data)
    {
        if (!mHelper.handleActivityResult(requestCode,
                resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase)
        {
            if (result.isFailure()) {
                // Handle error
                return;
            }
            else if (purchase.getSku().equals(ITEM_SKU)) {
                consumeItem();
                buyButton.setEnabled(false);
            }

        }
    };
    public void consumeItem() {
        mHelper.queryInventoryAsync(mReceivedInventoryListener);
    }

    IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {

            if (result.isFailure()) {
                // Handle failure
            } else {
                mHelper.consumeAsync(inventory.getPurchase(ITEM_SKU),
                        mConsumeFinishedListener);
            }
        }
    };
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
            new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase,
                                              IabResult result) {

                    if (result.isSuccess()) {
                        clickButton.setEnabled(true);
                    } else {
                        // handle error
                        Toast.makeText(getApplicationContext(),"Error in onconsumefinished listener",Toast.LENGTH_SHORT).show();
                    }
                }
            };

}