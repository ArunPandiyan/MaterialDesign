package technibits.com.pme.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;

import technibits.com.pme.R;
import technibits.com.pme.model.CircleTransform;


/**
 * Create an Account. Username is the primary method of login. Email is used for forgotten password recovery.
 *
 * @author Trey Robinson
 */
public class CreateAccountActivity extends Activity implements OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    protected static final String EXTRA_EMAIL = "com.keyconsultant.parse.logintutorial.fragment.extra.EMAIL";
    protected static final String EXTRA_USERNAME = "com.keyconsultant.parse.logintutorial.fragment.extra.USERNAME";
    protected static final String EXTRA_PASSWORD = "com.keyconsultant.parse.logintutorial.fragment.extra.PASSWORD";
    protected static final String EXTRA_CONFIRM = "com.keyconsultant.parse.logintutorial.fragment.extra.CONFIRMPASSWORD";
    private GoogleApiClient mGoogleApiClient;
    private EditText mUserNameEditText;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private EditText mConfirmPasswordEditText;
    private EditText mMobileEditText;
    private EditText etcountrycode;
    private ImageView mUserImage, flag;
    private Spinner mCountrySpinner;
    private Button mCreateAccountButton;
    private Button signoutall;
    ArrayList<String> countries;
    private String mEmail;
    private String mUsername;
    private String mPassword;
    private String mConfirmPassword, mMobileNumber, mCountry;
    private boolean mIntentInProgress;
    private ConnectionResult mConnectionResult;
    private boolean mSignInClicked;
    private static final int RC_SIGN_IN = 0;

    /**
     * Factory method for creating fragment instances.
     *
     * @return
     */


    public static CreateAccountActivity newInstance() {
        return new CreateAccountActivity();
    }

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_create_account);
        InitUI();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
        Intent intent = getIntent();
        if (intent != null) {
            String personName = intent.getStringExtra("name");
            String email = intent.getStringExtra("email");
            String personPhotoUrl = intent.getStringExtra("image_url");

            mUserNameEditText.setText(personName);
            mEmailEditText.setText(email);

            mUserNameEditText.setKeyListener(null);
            mEmailEditText.setKeyListener(null);
            Glide.with(this).load(personPhotoUrl).override(70, 70).transform(new CircleTransform(this)).into(mUserImage);
        }


    }

    void InitUI() {

        mUserNameEditText = (EditText) findViewById(R.id.etUsername);
        mEmailEditText = (EditText) findViewById(R.id.etEmail);
        mPasswordEditText = (EditText) findViewById(R.id.etPassword);
        mConfirmPasswordEditText = (EditText) findViewById(R.id.etPasswordConfirm);
        mMobileEditText = (EditText) findViewById(R.id.etmobile);
        etcountrycode = (EditText) findViewById(R.id.etcountrycode);
        mUserImage = (ImageView) findViewById(R.id.user_img);
        flag = (ImageView) findViewById(R.id.flag);

        mCreateAccountButton = (Button) findViewById(R.id.btnCreateAccount);
        signoutall = (Button) findViewById(R.id.signoutall);
        mCreateAccountButton.setOnClickListener(this);

        String rs = "res/drawable/afghanistan.png";
        Glide.with(this).load(rs).override(70, 70).into(flag);

        signoutall.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                revokeGplusAccess();
                signOutFromGplus();
                Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
//        Locale[] locale = Locale.getAvailableLocales();
//        countries = new ArrayList<String>();
//        String country;
//        for (Locale loc : locale) {
//            country = loc.getDisplayCountry();
//            if (country.length() > 0 && !countries.contains(country)) {
//                countries.add(country);
//            }
//        }
//        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);
//
//        mCountrySpinner = (Spinner) findViewById(R.id.spinnercountry);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, countries);
//        mCountrySpinner.setAdapter(adapter);
        Resources r = getResources();
        TypedArray countrieCodes = r.obtainTypedArray(R.array.countries);

        ArrayList<String> country = new ArrayList<String>();
        ArrayList<String> code = new ArrayList<String>();
        ArrayList<String> country_img = new ArrayList<String>();

        int cpt = countrieCodes.length();
        for (int i = 0; i < cpt; ++i) {
            int id = countrieCodes.getResourceId(i, 0);
            code.add(r.getStringArray(id)[0]);
            country.add(r.getStringArray(id)[1]);
            country_img.add(r.getStringArray(id)[3]);
        }

        countrieCodes.recycle();

        final ArrayList<String> fCode = code;
        final ArrayList<String> fCountry = country;
        final ArrayList<String> fCountry_img = country_img;


        final Spinner p = (Spinner) findViewById(R.id.spinnercountry);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, country);
        p.setAdapter(dataAdapter);
        p.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String selectedCountry = (String) p.getSelectedItem();
                int selectedPosition = fCountry.indexOf(selectedCountry);
                String correspondingCode = fCode.get(selectedPosition);
                String corresponding_image = fCountry_img.get(selectedPosition);
                // Here is your corresponding country code
                String dd = corresponding_image.substring(13, corresponding_image.length() - 4);
                etcountrycode.setText(correspondingCode);
                int res = getResources().getIdentifier(dd, "drawable", getApplicationContext().getPackageName());
//                int red=getResources().getid
                flag.setImageResource(res);

                System.out.println(correspondingCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });


    }

    private void signOutFromGplus() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();

        }
    }

    private void revokeGplusAccess() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Log.e("CreateAccountActivity", "User access revoked!");
                            mGoogleApiClient.connect();

                        }

                    });
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCreateAccount:
                if (mPasswordEditText.getText().toString().equals(mConfirmPasswordEditText.getText().toString())) {
                    createAccount();
                } else
                    Toast.makeText(this, "Password is not same !", Toast.LENGTH_SHORT);
                break;


            default:
                break;
        }
    }

    /**
     * Some front end validation is done that is not monitored by the service.
     * If the form is complete then the information is passed to the service.
     */
    private void createAccount() {
//		clearErrors();

        boolean cancel = false;
        View focusView = null;

        // Store values at the time of the login attempt.
        mEmail = mEmailEditText.getText().toString();
        mUsername = mUserNameEditText.getText().toString().trim();
        mPassword = mPasswordEditText.getText().toString();
        mConfirmPassword = mConfirmPasswordEditText.getText().toString();
        mMobileNumber = mMobileEditText.getText().toString();
        int pos = mCountrySpinner.getSelectedItemPosition();
        mCountry = countries.get(pos);

        // Check for a valid confirm username.
        if (TextUtils.isEmpty(mUsername)) {
            mUserNameEditText.setError(Html.fromHtml("<font color='red'>Incorrect input!</font>"));
            mUserNameEditText.requestFocus();
            cancel = true;
        } else if (TextUtils.isEmpty(mMobileNumber)) {
            mMobileEditText.setError(Html.fromHtml("<font color='red'>Incorrect input!</font>"));
            mMobileEditText.requestFocus();
            cancel = true;
        } else if (TextUtils.isEmpty(mConfirmPassword)) {
            mConfirmPasswordEditText.setError(Html.fromHtml("<font color='red'>Incorrect input!</font>"));
            mConfirmPasswordEditText.requestFocus();
            cancel = true;
        } else if (mPassword != null && !mConfirmPassword.equals(mPassword)) {
            System.out.println("" + mPassword + "  " + mConfirmPassword);
            mPasswordEditText.setError(Html.fromHtml("<font color='red'>Incorrect input!</font>"));
            mPasswordEditText.requestFocus();
            cancel = true;
        }
        // Check for a valid password.
        else if (TextUtils.isEmpty(mPassword)) {
            mPasswordEditText.setError(Html.fromHtml("<font color='red'>Incorrect input!</font>"));
//            focusView = mPasswordEditText;
            cancel = true;
        } else if (mPassword.length() < 4) {
            mPasswordEditText.setError(Html.fromHtml("<font color='red'>Incorrect input!</font>"));
//            focusView = mPasswordEditText;
            cancel = true;
        }

        // Check for a valid email address.
        else if (TextUtils.isEmpty(mEmail)) {
            mEmailEditText.setError(Html.fromHtml("<font color='red'>Incorrect input!</font>"));
//            focusView = mEmailEditText;
            cancel = true;
        } else if (!mEmail.contains("@")) {
            mEmailEditText.setError(Html.fromHtml("<font color='red'>Incorrect input!</font>"));
//            focusView = mEmailEditText;
            cancel = true;
        } else if (!cancel) {
            String url = "http://jmbok.avantgoutrestaurant.com/profile/v1/register";
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", mUsername));
            params.add(new BasicNameValuePair("lastname", "mUsername"));
            params.add(new BasicNameValuePair("email", mEmail));
            params.add(new BasicNameValuePair("password", mPassword));
            params.add(new BasicNameValuePair("confirmpassword", mConfirmPassword));
            params.add(new BasicNameValuePair("mobile", mMobileNumber));
            params.add(new BasicNameValuePair("country", mCountry));
            params.add(new BasicNameValuePair("code", "0"));
            AsyncTaskCall ask = new AsyncTaskCall(this, "signup", params);
            ask.execute(url);
            // There was an error; don't attempt login and focus the first
            // form field with an error.
//            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
//            UserManager.getInstance().signUp(mUsername.toLowerCase(Locale.getDefault()), mEmail, mPassword);

        }

    }

    /**
     * Remove error messages from all fields.
     */
    private void clearErrors() {
        mEmailEditText.setError(null);
        mUserNameEditText.setError(null);
        mPasswordEditText.setError(null);
        mConfirmPasswordEditText.setError(null);
    }


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

    public void finish(String strJson) throws JSONException {
        try {
            JSONObject json = new JSONObject(strJson);
            String error = json.getString("error");
            String mes = json.getString("message");
            System.out.println("  " + error + " " + mes);
            if (error.equals("true")) {
                Toast.makeText(getApplicationContext(), mes,
                        Toast.LENGTH_LONG).show();
//				finish();
            } else {
                Toast.makeText(getApplicationContext(), mes,
                        Toast.LENGTH_LONG).show();
                finish();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
//		finish();
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //Back buttons was pressed, do whatever logic you want
            revokeGplusAccess();
            signOutFromGplus();
            Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();

        }

        return false;
    }
}
