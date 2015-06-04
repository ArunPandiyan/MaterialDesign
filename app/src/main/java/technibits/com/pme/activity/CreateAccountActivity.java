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
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import technibits.com.pme.R;


/**
 * Create an Account. Username is the primary method of login. Email is used for forgotten password recovery.
 *
 * @author Trey Robinson
 */
public class CreateAccountActivity extends Activity implements OnClickListener {

    protected static final String EXTRA_EMAIL = "com.keyconsultant.parse.logintutorial.fragment.extra.EMAIL";
    protected static final String EXTRA_USERNAME = "com.keyconsultant.parse.logintutorial.fragment.extra.USERNAME";
    protected static final String EXTRA_PASSWORD = "com.keyconsultant.parse.logintutorial.fragment.extra.PASSWORD";
    protected static final String EXTRA_CONFIRM = "com.keyconsultant.parse.logintutorial.fragment.extra.CONFIRMPASSWORD";

    private EditText mUserNameEditText;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private EditText mConfirmPasswordEditText;
    private EditText mMobileEditText;
    private Spinner mCountrySpinner;
    private Button mCreateAccountButton;
    ArrayList<String> countries;
    private String mEmail;
    private String mUsername;
    private String mPassword;
    private String mConfirmPassword, mMobileNumber, mCountry;

    /**
     * Factory method for creating fragment instances.
     *
     * @return
     */


    public static CreateAccountActivity newInstance() {
        return new CreateAccountActivity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_create_account);
        mUserNameEditText = (EditText) findViewById(R.id.etUsername);
        mEmailEditText = (EditText) findViewById(R.id.etEmail);
        mPasswordEditText = (EditText) findViewById(R.id.etPassword);
        mConfirmPasswordEditText = (EditText) findViewById(R.id.etPasswordConfirm);
        mMobileEditText = (EditText) findViewById(R.id.etmobile);
//		mCountryEditText = (EditText)findViewById(R.id.etCountry);

        mCreateAccountButton = (Button) findViewById(R.id.btnCreateAccount);
        mCreateAccountButton.setOnClickListener(this);


        Locale[] locale = Locale.getAvailableLocales();
        countries = new ArrayList<String>();
        String country;
        for (Locale loc : locale) {
            country = loc.getDisplayCountry();
            if (country.length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }
        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);

        mCountrySpinner = (Spinner) findViewById(R.id.spinnercountry);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, countries);
        mCountrySpinner.setAdapter(adapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCreateAccount:
                createAccount();
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

        // Check for a valid confirm password.
        if (TextUtils.isEmpty(mUsername)) {
            mUserNameEditText.setError(Html.fromHtml("<font color='red'>Incorrect input!</font>"));
//        	focusView = mUserNameEditText;
            cancel = true;
        }
        if (TextUtils.isEmpty(mConfirmPassword)) {
            mConfirmPasswordEditText.setError(Html.fromHtml("<font color='red'>Incorrect input!</font>"));
//        	focusView = mConfirmPasswordEditText;
            cancel = true;
        } else if (mPassword != null && !mConfirmPassword.equals(mPassword)) {
            System.out.println("" + mPassword + "  " + mConfirmPassword);
            mPasswordEditText.setError(Html.fromHtml("<font color='red'>Incorrect input!</font>"));
//        	focusView = mPasswordEditText;
            cancel = true;
        }
        // Check for a valid password.
        if (TextUtils.isEmpty(mPassword)) {
            mPasswordEditText.setError(Html.fromHtml("<font color='red'>Incorrect input!</font>"));
//            focusView = mPasswordEditText;
            cancel = true;
        } else if (mPassword.length() < 4) {
            mPasswordEditText.setError(Html.fromHtml("<font color='red'>Incorrect input!</font>"));
//            focusView = mPasswordEditText;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(mEmail)) {
            mEmailEditText.setError(Html.fromHtml("<font color='red'>Incorrect input!</font>"));
//            focusView = mEmailEditText;
            cancel = true;
        } else if (!mEmail.contains("@")) {
            mEmailEditText.setError(Html.fromHtml("<font color='red'>Incorrect input!</font>"));
//            focusView = mEmailEditText;
            cancel = true;
        }

        if (!cancel) {
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

}
