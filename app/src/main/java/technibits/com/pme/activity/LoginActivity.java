package technibits.com.pme.activity;

import java.io.InputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import technibits.com.pme.R;
import technibits.com.pme.data.NetworkUtil;
//import com.facebook.Session;
//import com.facebook.SessionState;
//import com.facebook.UiLifecycleHelper;
//import com.facebook.model.GraphUser;
//import com.facebook.login.widget.LoginButton;
//import com.facebook.login.widget.LoginButton.UserInfoChangedCallback;
//import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
//=====import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
//import com.google.android.gms.common.api.GooglePlayServicesClient.ConnectionCallbacks;
//import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

public class LoginActivity extends AppCompatActivity implements OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 0;
    // Logcat tag
    private static final String TAG = "MainActivity";

    // Profile pic image size in pixels
    private static final int PROFILE_PIC_SIZE = 400;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    /**
     * A flag indicating that a PendingIntent is in progress and prevents us
     * from starting further intents.
     */
    private boolean mIntentInProgress;

    private boolean mSignInClicked;
    private Toolbar mToolbar;
    private ConnectionResult mConnectionResult;

    private SignInButton btnSignIn;

    Button btnSignUP, login;

    // private ImageView imgProfilePic;
    private TextView txtName, txtEmail;
    // private LinearLayout llProfileLayout;
    EditText userName, password;
    DBConnection db;
    //	private LoginButton loginBtn;
    private TextView username;

    String personName = null,
            lastname = null,
            location = null,
            personPhotoUrl = null,
            personGooglePlusProfile = null,
            email = null;
    String logout_string=null;

    //	private UiLifecycleHelper uiHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		uiHelper = new UiLifecycleHelper(this, statusCallback);
//		uiHelper.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
// logout_string=getIntent().getExtras().getString("logout_string");

        int rowCount = 0;
        try {
            db = new technibits.com.pme.activity.DBConnection(this);

            db.createDataBase();
            db.open();
            rowCount = db.getProfilesCount("user");
            db.close();
            db = null;
        } catch (Exception e) {
//			db = new DBConnection(MainActivity);
            e.printStackTrace();
        }

        if (rowCount > 0) {
//			Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);

            finish();
        }

		
		/*loginBtn = (LoginButton) findViewById(R.id.fb_login_button);
		loginBtn.setReadPermissions(Arrays.asList("email"));
		loginBtn.setUserInfoChangedCallback(new UserInfoChangedCallback() {
			@Override
			public void onUserInfoFetched(GraphUser user) {
				if (user != null) {
					System.out.println("You are currently logged in as " + user.getName());
					System.out.println("Name  " + user.getName() +"Email  " + user.getProperty("email").toString()); ;
				} else {
					System.out.println("You are not logged in.");
				}
			}
		});*/

        userName = (EditText) findViewById(R.id.editUserName);
        password = (EditText) findViewById(R.id.editPassword);

//        userName.setText("asgurumoorthy@gmail.com");
//        password.setText("qwert");

        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);

        btnSignUP = (Button) findViewById(R.id.signUP);
        login = (Button) findViewById(R.id.login);

        // imgProfilePic = (ImageView) findViewById(R.id.imgProfilePic);
        txtName = (TextView) findViewById(R.id.txtName);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        // llProfileLayout = (LinearLayout) findViewById(R.id.llProfile);

        // Button click listeners
        btnSignIn.setOnClickListener(this);
        btnSignUP.setOnClickListener(this);
        login.setOnClickListener(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
        signOutFromGplus();
        revokeGplusAccess();
        if (logout_string=="true" ){
            signOutFromGplus();
        }
    }

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
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
            } catch (SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,0).show();
//            showNetworkstatus(getApplicationContext());
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
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnected(Bundle arg0) {
        mSignInClicked = false;
        Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();

        // Get user's information
        getProfileInformation();

        // Update the UI after signin

    }

    /**
     * Updating the UI, showing/hiding buttons and profile layout
     * */
    // private void updateUI(boolean isSignedIn) {
    // if (isSignedIn) {
    // btnSignIn.setVisibility(View.GONE);
    // btnSignOut.setVisibility(View.VISIBLE);
    // btnRevokeAccess.setVisibility(View.VISIBLE);
    // llProfileLayout.setVisibility(View.VISIBLE);
    // } else {
    // btnSignIn.setVisibility(View.VISIBLE);
    // btnSignOut.setVisibility(View.GONE);
    // btnRevokeAccess.setVisibility(View.GONE);
    // llProfileLayout.setVisibility(View.GONE);
    // }
    // }

    /**
     * Fetching user's information name, email, profile pic from google+
     * user:arun
     */
    private void getProfileInformation(String oldmethod_removethisparameter) {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String location = currentPerson.getCurrentLocation();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);


                Log.e(TAG, "Name: " + personName + ", plusProfile: "
                        + personGooglePlusProfile + ", email: " + email
                        + ", Image: " + personPhotoUrl + "Location" + location);

                personPhotoUrl = personPhotoUrl.substring(0, personPhotoUrl.length() - 2) + PROFILE_PIC_SIZE;
                String url = "http://jmbok.techtestbox.com/profile/v1/login";
                ArrayList params = new ArrayList();
                params.add(email);
                params.add(new BasicNameValuePair("password", password.getText().toString().trim()));
                params.add(new BasicNameValuePair("confirmpassword", password.getText().toString().trim()));
//                ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
//                params.add(new BasicNameValuePair("email", email));
//                params.add(new BasicNameValuePair("email", email));
//                params.add(new BasicNameValuePair("password", password.getText().toString().trim()));
//                params.add(new BasicNameValuePair("confirmpassword", password.getText().toString().trim()));
//                Bundle extra = new Bundle();
//                extra.putAll("objects", params);
                Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
                intent.putExtra("name", personName);
                intent.putExtra("email", email);
                intent.putExtra("image_url", personPhotoUrl);
                startActivity(intent);
                finish();
//                AsyncTaskCall ask = new AsyncTaskCall(this, "signin", params);
//                ask.execute(url);

                // by default the profile url gives 50x50 px image only
                // we can replace the value with whatever dimension we want by
                // replacing sz=X
//                personPhotoUrl = personPhotoUrl.substring(0,
//                        personPhotoUrl.length() - 2)
//                        + PROFILE_PIC_SIZE;

                // new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);

            } else {
                Toast.makeText(getApplicationContext(),
                        "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Fetching user's information name, email, profile pic
     */
    private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                personName = currentPerson.getDisplayName();
                lastname = currentPerson.getNickname();
                location = currentPerson.getCurrentLocation();
                personPhotoUrl = currentPerson.getImage().getUrl();
                personGooglePlusProfile = currentPerson.getUrl();
                email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                Log.e(TAG, "Name: " + personName + ", plusProfile: "
                        + personGooglePlusProfile + ", email: " + email
                        + ", Image: " + personPhotoUrl + "Location" + location);


//---------------------------------------G+ login with server----------------------------------------------------------
                int rowCount = 0;
                try {
                    db = new technibits.com.pme.activity.DBConnection(this);

                    db.createDataBase();
                    db.open();
                    rowCount = db.getProfilesCount("user");
                    db.close();
                    db = null;
                } catch (Exception e) {
//			db = new DBConnection(MainActivity);
                    e.printStackTrace();
                }

                if (rowCount == 0) {
//			Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    SessionIdentifierGenerator nextSession=new SessionIdentifierGenerator();
                    String pwd=nextSession.nextSessionId();
                    String url = "http://jmbok.techtestbox.com/profile/v1/register";
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("name", personName));
                    params.add(new BasicNameValuePair("lastname", "fromGplus"));
                    params.add(new BasicNameValuePair("email", email));
                    params.add(new BasicNameValuePair("password", pwd));
                    params.add(new BasicNameValuePair("confirmpassword", pwd));
                    params.add(new BasicNameValuePair("mobile", "Not Entered"));
                    params.add(new BasicNameValuePair("country", "Not Selected"));
                    params.add(new BasicNameValuePair("code", "0"));
                    boolean status = NetworkUtil.isOnline();
                    if(status) {
                        AsyncTaskCall ask = new AsyncTaskCall(this, "Gplussignup", params);
                        ask.execute(url);
                    }else{
                        NetworkUtil.showNetworkstatus(this);
                    }
                } else {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }


//---------------------------------------G+ login with server ends-----------------------------------------------------


//                String url = "http://jmbok.techtestbox.com/profile/v1/login";
//                List<NameValuePair> params = new ArrayList<NameValuePair>();
//                params.add(new BasicNameValuePair("email", email));
//                params.add(new BasicNameValuePair("password", password.getText().toString().trim()));
//                params.add(new BasicNameValuePair("confirmpassword", password.getText().toString().trim()));
//                AsyncTaskCall ask = new AsyncTaskCall(this,"signin",params);
//                ask.execute(url);

                // by default the profile url gives 50x50 px image only
                // we can replace the value with whatever dimension we want by
                // replacing sz=X
                personPhotoUrl = personPhotoUrl.substring(0,
                        personPhotoUrl.length() - 2)
                        + PROFILE_PIC_SIZE;

                // new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);

            } else {
                Toast.makeText(getApplicationContext(),
                        "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final class SessionIdentifierGenerator {
        private SecureRandom random = new SecureRandom();

        public String nextSessionId() {
            return new BigInteger(130, random).toString(32);
        }
    }
    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Button on click listener
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in:
                // Signin button clicked
                signInWithGplus();
                break;

            case R.id.signUP:
                // Signin button clicked
                Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
			    intent.putExtra("mode", "create");
                startActivity(intent);
                break;

            case R.id.login:

                String url = "http://jmbok.techtestbox.com/profile/v1/login";
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("email", userName.getText().toString().trim()));
                params.add(new BasicNameValuePair("password", password.getText().toString().trim()));
                params.add(new BasicNameValuePair("confirmpassword", password.getText().toString().trim()));
                boolean status = NetworkUtil.isOnline();
                if(status) {
                    AsyncTaskCall ask = new AsyncTaskCall(this, "signin", params);
                    ask.execute(url);
                }else{
                    NetworkUtil.showNetworkstatus(this);

                }


                break;


        }
    }

    /**
     * Sign-in into google
     */
    private void signInWithGplus() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }
    }

    /**
     * Sign-out from google
     */
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

    public void login(String strJson) throws JSONException {
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

                String name = json.getString("name");
                String lastname = json.getString("lastname");
                String password = json.getString("apiKey");
                String email = json.getString("email");
                String mobile = json.getString("mobile");
                String country = json.getString("country");
                String image_url = "http://jmbok.techtestbox.com/and/images/Dummy_profile_pic.png";

                db = new DBConnection(this);
                db.open();
                ContentValues inst = new ContentValues();

                inst.put("name", name);
                inst.put("password", password);
                inst.put("lastname", lastname);
                inst.put("email", email);
                inst.put("country", country);
                inst.put("mobile", mobile);
                inst.put("imageurl", image_url);

                db.insert(inst, "user");

                Toast.makeText(getApplicationContext(), mes,
                        Toast.LENGTH_LONG).show();

//				Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);  For testing purpose (older activity)
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);

                finish();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Background Async task to load user profile picture from url
     */
    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
	
	
	/*private Session.StatusCallback statusCallback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			if (state.isOpened()) {
				Log.d("MainActivity", "Facebook session opened.");
			} else if (state.isClosed()) {
				Log.d("MainActivity", "Facebook session closed.");
			}
		}
	};

	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle savedState) {
		super.onSaveInstanceState(savedState);
		uiHelper.onSaveInstanceState(savedState);
	}*/

    public void finishGplus(String strJson) throws JSONException {
        try {
            JSONObject json = new JSONObject(strJson);
            String error = json.getString("error");
            String mes = json.getString("message");
            String ispremium=json.getString("ispremium");


//            $user = $db->getUserByEmail($email);


//            if ($user != NULL) {
//                //  $response["error"] = false;
//                $response['id'] = $user['id'];
//                $response['name'] = $user['name'];
//                $response['lastname'] = $user['lastname'];
//                $response['email'] = $user['email'];
//                $response['mobile'] = $user['mobile'];
//                $response['country'] = $user['country'];
//                $response['code'] = $user['code'];
//
//
//                $response['apiKey'] = $user['api_key'];
//                $response['createdAt'] = $user['created_at'];
//                $response['message'] = "You are Successfully logged";
//
//                $response["error"] = false;
            System.out.println("  " + error + " " + mes);
            if (error.equals("true")) {
                Toast.makeText(getApplicationContext(), mes,Toast.LENGTH_LONG).show();
//				finish();
            } else {
                CreateUserinPhone(personName, lastname, email, personPhotoUrl,ispremium);
                Toast.makeText(getApplicationContext(), mes,Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
//                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//		finish();
    }

    void CreateUserinPhone(String personName, String lastname, String email, String personPhotoUrl,String ispremium) {
        db = new DBConnection(this);
        db.open();
        ContentValues inst = new ContentValues();

        inst.put("name", personName);
        inst.put("password", "fromGplus");
        inst.put("lastname", lastname);
        inst.put("email", email);
        inst.put("country", "fromGplus");
        inst.put("mobile", "fromGplus");
        inst.put("imageurl", personPhotoUrl);
        inst.put("ispremium", "Free");


        db.insert(inst, "user");
    }
    public void showNetworkstatus(Context context) {
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
