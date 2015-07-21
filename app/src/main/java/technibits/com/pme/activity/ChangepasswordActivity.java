package technibits.com.pme.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import technibits.com.pme.R;
import technibits.com.pme.data.NetworkUtil;
import technibits.com.pme.model.CircleTransform;

/**
 * Created by technibitsuser on 7/7/2015.
 */
public class ChangepasswordActivity extends AppCompatActivity {

    private EditText oldPassword;
    private EditText newPassword;
    private EditText newconfirmpassword;
    private EditText mConfirmPasswordEditText;
    private Button btn_changepassword;
    private Toolbar mToolbar;
    private DBConnection db;
    private String email = null;
    private String txt_oldPassword=null;
    private String txt_newPassword=null;
    private String txt_newconfirmpassword=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepassword_layout);
        InitUI();
        btn_changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatepassword();
            }
        });
        returnOldpassfromDB();
    }

    private void updatepassword() {
        boolean cancel = false;

      txt_oldPassword = oldPassword.getText().toString();
      txt_newPassword = newPassword.getText().toString().trim();
      txt_newconfirmpassword = newconfirmpassword.getText().toString();

        if (TextUtils.isEmpty(txt_oldPassword)) {
            oldPassword.setError("Enter Your Password !");

            oldPassword.requestFocus();
            cancel = true;
        } else if (TextUtils.isEmpty(txt_newPassword)) {
            newPassword.setError("Can't be Empty !");
            newPassword.requestFocus();
            cancel = true;
        }else if (TextUtils.isEmpty(txt_newconfirmpassword)) {
            newconfirmpassword.setError("Can't be Empty !");
            newconfirmpassword.requestFocus();

            cancel = true;
        }else if (!(txt_newPassword.equals(txt_newconfirmpassword))) {
            newconfirmpassword.setError("Passwords do not Match");
            oldPassword.setText("");
            newPassword.setText("");
            newPassword.requestFocus();
            cancel = true;
        }

        else if (!cancel) {
            String url = "http://jmbok.techtestbox.com/and/password-update.php";
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("oldpassword", txt_oldPassword));
            params.add(new BasicNameValuePair("password", txt_newPassword));
            boolean status = NetworkUtil.isOnline();
            if(status) {
                AsyncTaskCall ask = new AsyncTaskCall(this, "changepass", params);
                ask.execute(url);
            }else{
                NetworkUtil.showNetworkstatus(this);
            }

            // There was an error; don't attempt login and focus the first
            // form field with an error.
//            focusView.requestFocus();
        }
    }

    private void returnOldpassfromDB() {


        Cursor cursor=db.QueryEngine("SELECT * FROM USER");
        try {
            if (cursor.moveToFirst()) {
//                mUserNameEditText.setText(cursor.getString(0));
//                mEmailEditText.setText(cursor.getString(2));
//                mEmailEditText.setKeyListener(null);
//                mPassword=cursor.getString(3);
//                mPasswordEditText.setVisibility(View.GONE);
//                mConfirmPasswordEditText.setVisibility(View.GONE);
                String coum=cursor.getString(4);
//                p.setSelection(((ArrayAdapter<String>)p.getAdapter()).getPosition(cursor.getString(4)));
                String mobile     = cursor.getString(5);
                mobile=mobile.substring(3,mobile.length());
//                mMobileEditText.setText(mobile);
                String image_url  = cursor.getString(6);
//                Glide.with(this).load(image_url).override(70, 70).transform(new CircleTransform(this)).into(mUserImage);


            }

        } finally {

            cursor.close();
        }
    }

    private void InitUI() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Change Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        oldPassword = (EditText) findViewById(R.id.oldPassword);
        newPassword = (EditText) findViewById(R.id.newPassword);
        btn_changepassword= (Button) findViewById(R.id.btn_changepassword);
        newconfirmpassword = (EditText) findViewById(R.id.newconfirmpassword);
        db=new DBConnection(getApplicationContext());
        db.open();
        email=db.getuserEmail();

    }

    public void changedPassword(String strJson) throws JSONException {
        try {
            JSONObject json = new JSONObject(strJson);
            String error = json.getString("success");
            String mes = json.getString("message");
            System.out.println("  " + error + " " + mes);
            if (error.equals("0")) {
                Toast.makeText(getApplicationContext(), mes,
                        Toast.LENGTH_LONG).show();
//				finish();
            } else {
                Toast.makeText(getApplicationContext(), mes,
                        Toast.LENGTH_LONG).show();
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//		finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.changepass_menu, menu);
            return true;

    }
}
