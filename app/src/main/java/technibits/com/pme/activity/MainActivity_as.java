package technibits.com.pme.activity;


import java.util.ArrayList;
import java.util.LinkedHashSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import technibits.com.pme.data.MasterData;
import technibits.com.pme.R;
import technibits.com.pme.data.NetworkUtil;


import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class MainActivity_as extends AppCompatActivity {
    DBConnection db;
    OnItemSelectedListener myListener;
    public MainActivity_as MainActivity_as;


    String knArea;
    String prGroup;
    String prName;
    String prMode;

    public String knowledgearea = "knowledgearea";
    public String processgroup = "processgroup";
    public String processname = "processname";
    String mode;
    Spinner spinner1, spinner2, spinner3, spinner4;
    TextView text1, text2, text3, text4;
    CheckBox checkBox;
    Button next;
    String[] difficultylevel = {"Select All", "Beginner", "Inter mediator", "Expert"};

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_as);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Choose Knowledge Area");
        setSupportActionBar(mToolbar);
//		getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        MainActivity_as = this;

        Bundle bundle = getIntent().getExtras();

        mode = bundle.getString("mode");

        knArea = "SelectAll";
        prGroup = "SelectAll";
        prName = "SelectAll";
        prMode = "SelectAll";

        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner3 = (Spinner) findViewById(R.id.spinner3);
        spinner4 = (Spinner) findViewById(R.id.spinner4);

        text1 = (TextView) findViewById(R.id.textView1);
        text2 = (TextView) findViewById(R.id.textView2);
        text3 = (TextView) findViewById(R.id.textView3);
        text4 = (TextView) findViewById(R.id.textView4);

        spinner2.setVisibility(View.GONE);
        spinner3.setVisibility(View.GONE);
        spinner4.setVisibility(View.GONE);
        text4.setVisibility(View.GONE);
        text2.setVisibility(View.GONE);
        text3.setVisibility(View.GONE);

        checkBox = (CheckBox) findViewById(R.id.checkBox1);
        checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    spinner2.setVisibility(View.VISIBLE);
                    spinner3.setVisibility(View.VISIBLE);
                    spinner4.setVisibility(View.VISIBLE);
                    text4.setVisibility(View.VISIBLE);
                    text2.setVisibility(View.VISIBLE);
                    text3.setVisibility(View.VISIBLE);
                } else {
                    spinner2.setVisibility(View.GONE);
                    spinner3.setVisibility(View.GONE);
                    spinner4.setVisibility(View.GONE);
                    text4.setVisibility(View.GONE);
                    text2.setVisibility(View.GONE);
                    text3.setVisibility(View.GONE);
                }
            }
        });


        next = (Button) findViewById(R.id.button1);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity_as,
                R.layout.spin, difficultylevel);
        spinner4.setAdapter(adapter);

        spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                knArea = arg0.getItemAtPosition(arg2).toString();
//	            	System.out.println(prGroup);
                ArrayList<String> list = db.getProcessgroup(knArea);
                list = new ArrayList<String>(new LinkedHashSet<String>(list));
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity_as, R.layout.spin, list);
                spinner2.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });

        spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                prGroup = arg0.getItemAtPosition(arg2).toString();
                System.out.println(prGroup + prGroup);

                ArrayList<String> list = db.getProcessName(knArea, prGroup);
                list = new ArrayList<String>(new LinkedHashSet<String>(list));
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity_as, R.layout.spin, list);
                spinner3.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });

        spinner3.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                prName = arg0.getItemAtPosition(arg2).toString();

//	        	 prName = arg0.getItemAtPosition(arg2).toString();

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });


        spinner4.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                prMode = arg0.getItemAtPosition(arg2).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent scheIntent = new Intent(MainActivity_as.this, QuizActivity.class);
                scheIntent.putExtra("knArea", knArea);
                if (checkBox.isChecked()) {
                    scheIntent.putExtra("prGroup", prGroup);
                    scheIntent.putExtra("prName", prName);
                    scheIntent.putExtra("prMode", prMode);
                } else {
                    scheIntent.putExtra("prGroup", "SelectAll");
                    scheIntent.putExtra("prName", "SelectAll");
                    scheIntent.putExtra("prMode", "SelectAll");
                }
                scheIntent.putExtra("mode", mode);
                startActivity(scheIntent);
            }
        });


        int rowCount = 0;
        try {
            db = new technibits.com.pme.activity.DBConnection(MainActivity_as);
            db.createDataBase();
            rowCount = db.getProfilesCount("allprocess");
            db.close();
            db = null;
        } catch (Exception e) {
//			db = new DBConnection(MainActivity_as);
            e.printStackTrace();
        }

        if (rowCount > 0) {
            spinnerAdapter();
        } else {
            String urls = "http://www.jmbok.techtestbox.com/and/all-in-one.php";
            boolean status = NetworkUtil.isOnline();
            if(status) {
                AsyncTaskCall ask = new AsyncTaskCall(this, urls, "selection");
                ask.execute(urls);
            }else{
                NetworkUtil.showNetworkstatus(this);
                finish();
            }

        }

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    public void jsonParsing(JSONObject json) throws JSONException {
        System.out.println("Json data in side Mactivity_as is: " + json);

        JSONArray contacts = json.getJSONArray("allprocess");
//		 MasterData dataSource = new MasterData();
        ArrayList<MasterData> list = new ArrayList<MasterData>();
        // looping through All Contacts
        for (int i = 0; i < contacts.length(); i++) {
            MasterData dataSource = new MasterData();
            JSONObject c = contacts.getJSONObject(i);
            dataSource.setKnowledgeArea(c.getString(knowledgearea));
            dataSource.setProcessGroup(c.getString(processgroup));
            dataSource.setProcessName(c.getString(processname));
            list.add(dataSource);
        }
        dbInsert(list);

    }

    /*
    This is invoked for the first time installation of the application
     */
    public void dbInsert(ArrayList<MasterData> list) {
        db = new DBConnection(MainActivity_as);
//		System.out.println("count "+ db.getProfilesCount("allprocess"));
//		ContentValues inst = new ContentValues();
        db.open();
        for (int i = 0; i < list.size(); i++) {
            ContentValues inst = new ContentValues();
            MasterData dataSource = list.get(i);
            inst.put("id", i);
            inst.put(knowledgearea, dataSource.getKnowledgeArea());
            inst.put("processgroup", dataSource.getProcessGroup());
            inst.put(processname, dataSource.getProcessName());
            db.insert(inst, "allprocess");

        }
        spinnerAdapter();

    }

    public void spinnerAdapter() {
        db = new DBConnection(MainActivity_as);
        db.open();
        ArrayList<String> list = db.getSpinnerknowledgearea();
        list = new ArrayList<String>(new LinkedHashSet<String>(list));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spin, list);
        spinner1.setAdapter(adapter);
//        db.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
