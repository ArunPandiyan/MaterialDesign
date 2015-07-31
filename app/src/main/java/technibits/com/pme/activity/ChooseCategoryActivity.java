package technibits.com.pme.activity;

/**
 * Created by technibitsuser on 7/23/2015.
 */


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import technibits.com.pme.R;
import technibits.com.pme.alarmactivity.RemindMe;
import technibits.com.pme.data.MasterData;
import technibits.com.pme.data.NetworkUtil;

/**
 * Created by technibitsuser on 7/22/2015.
 */
public class ChooseCategoryActivity extends AppCompatActivity {

    private Activity activity;
    private DBConnection db;
    private Spinner knowledge_area;
    private Spinner process_name;
    private Spinner process_group;
    private Spinner difficulty_level;
    private ImageView knowledge_area_tick_img;
    private ImageView process_name_tick_img;
    private ImageView process_group_tick_img;
    private TextView selectedprocess, selectedprocessgroup, selectedknowledgearea;
    private RelativeLayout  processlayout,procesgrplayout,knlayout;
    private ImageView knownledge_area_img;
    private ImageView process_group_img;
    private ImageView process_img;
    private View rootView;
    private FloatingActionButton fab;
    int active = 0, setdifficulty = 0;
    private String Category = null;
    private String mode = null;
    private boolean setUI = false;

    private Toolbar mToolbar;
    int sdk = android.os.Build.VERSION.SDK_INT;


    String[] difficultylevel = { "Select All", "Beginner", "Intermediate", "Expert"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
      setContentView(R.layout.choosecategory_nocard_layout);








        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Choose Filters");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();

        mode = bundle.getString("mode");
//        GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
//        Tracker tracker = analytics.newTracker("UA-XXXX-Y"); // Send hits to tracker id UA-XXXX-Y
//
//// All subsequent hits will be send with screen name = "main screen"
//        tracker.setScreenName("Schedulers");
//
//        tracker.send(new HitBuilders.EventBuilder()
//                .setCategory("UX")
//                .setAction("click")
//                .setLabel("submit")
//                .build());
//
//// Builder parameters can overwrite the screen name set on the tracker.
//        tracker.send(new HitBuilders.EventBuilder()
//                .setCategory("UX")
//                .setAction("click")
//                .setLabel("help popup")
////                .setScreenName("help popup dialog")
//                .build());

        setupUI();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spin, difficultylevel);
        difficulty_level.setAdapter(adapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent scheIntent = new Intent(getApplicationContext(), QuizActivity.class);
                int pos = difficulty_level.getSelectedItemPosition();
//                pos = pos - 1;

                if (active != 0 && setdifficulty != 0 ) {
                    if (active == 1) {
                        Category = "processgroup";
                        scheIntent.putExtra("type", Category);
                        scheIntent.putExtra("choosedvalue", process_group.getSelectedItem().toString());
                        scheIntent.putExtra("difficulty", pos);
                        scheIntent.putExtra("mode", mode);


                    } else if (active == 2) {
                        Category = "knowledgearea";
                        scheIntent.putExtra("type", Category);
                        scheIntent.putExtra("choosedvalue", knowledge_area.getSelectedItem().toString());
                        scheIntent.putExtra("difficulty", pos);
                        scheIntent.putExtra("mode", mode);

                    } else if (active == 3) {
                        Category = "processname";
                        scheIntent.putExtra("type", Category);
                        scheIntent.putExtra("choosedvalue", process_name.getSelectedItem().toString());
                        scheIntent.putExtra("difficulty", pos);
                        scheIntent.putExtra("mode", mode);
                    }
                    startActivity(scheIntent);

                } else {
                    if (active == 0) {
                        Snackbar.make(view, "Choose any Category first", Snackbar.LENGTH_SHORT).show();
                    }
                }

            }
        });

        int count = 0;
        count = db.countRecords("allprocess");
        if (count > 0) {
            setDataforspinners();
        } else {
            boolean status = NetworkUtil.isOnline();
            if (status) {
                String urls = "http://www.jmbok.techtestbox.com/and/all-in-one.php";
                AsyncTaskCall ask = new AsyncTaskCall(this, urls, "selection_choose");
                ask.execute(urls);
            } else {
                NetworkUtil.showNetworkstatus(this);
                finish();
            }
            setDataforspinners();
        }
    }

    private void setDataforspinners() {
        ArrayList<String> list = db.getProcessgroup("");
        if (list != null) {
            list = new ArrayList<String>(new LinkedHashSet<String>(list));
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.spin, list);
            process_group.setAdapter(adapter1);
            list = null;

            list = db.getProcessName("", "");
            list = new ArrayList<String>(new LinkedHashSet<String>(list));
            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, R.layout.spin, list);
            process_name.setAdapter(adapter2);
            list = null;

            list = db.getSpinnerknowledgearea();
            list = new ArrayList<String>(new LinkedHashSet<String>(list));
            ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, R.layout.spin, list);
            knowledge_area.setAdapter(adapter3);
            list = null;
        }


        setSpinnersClickListeners();
        setLayoutClickListeners();
    }


    public void setupUI() {
        db = RemindMe.dbConnection;
        db.open();
        processlayout = (RelativeLayout) findViewById(R.id.process_layout);
        procesgrplayout = (RelativeLayout) findViewById(R.id.proces_grp_layout);
        knlayout = (RelativeLayout) findViewById(R.id.kn_layout);

        difficulty_level = (Spinner) findViewById(R.id.difficulty_level);
        knowledge_area = (Spinner) findViewById(R.id.spinner_knowledge_area);
        process_name = (Spinner) findViewById(R.id.spinner_process);
        process_group = (Spinner) findViewById(R.id.spinner_process_group);
        fab = (FloatingActionButton) findViewById(R.id.fab);
//        difficulty_level.setVisibility(View.INVISIBLE);
        knowledge_area.setVisibility(View.INVISIBLE);
        process_name.setVisibility(View.INVISIBLE);
        process_group.setVisibility(View.INVISIBLE);

        difficulty_level.setSelection(0);
        knowledge_area.setSelection(0);
        process_name.setSelection(0);
        process_group.setSelection(0);
        //Tick Image of each group
        knowledge_area_tick_img = (ImageView) findViewById(R.id.tick_knowledge_area);
        process_name_tick_img = (ImageView) findViewById(R.id.tick_process);
        process_group_tick_img = (ImageView) findViewById(R.id.tick_process_group);
        knownledge_area_img = (ImageView) findViewById(R.id.knownledge_area_img);
        process_group_img = (ImageView) findViewById(R.id.process_group_img);
        process_img = (ImageView) findViewById(R.id.process_img);
        selectedprocess = (TextView) findViewById(R.id.selected_process);
        selectedprocessgroup = (TextView) findViewById(R.id.selected_process_group);
        selectedknowledgearea = (TextView) findViewById(R.id.selected_knowledge_area);

        selectedprocess.setVisibility(View.INVISIBLE);
        selectedprocessgroup.setVisibility(View.INVISIBLE);
        selectedknowledgearea.setVisibility(View.INVISIBLE);
        knowledge_area_tick_img.setVisibility(View.INVISIBLE);
        process_name_tick_img.setVisibility(View.INVISIBLE);
        process_group_tick_img.setVisibility(View.INVISIBLE);
    }

    public void setLayoutClickListeners() {
        knlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processlayout.setBackground( getResources().getDrawable(R.drawable.border_not_selected));
                procesgrplayout.setBackground( getResources().getDrawable(R.drawable.border_not_selected));
                knlayout.setBackground(getResources().getDrawable(R.drawable.border_selected));
                knowledge_area.performClick();
            }
        });
        procesgrplayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processlayout.setBackground( getResources().getDrawable(R.drawable.border_not_selected));
                procesgrplayout.setBackground( getResources().getDrawable(R.drawable.border_selected));
                knlayout.setBackground(getResources().getDrawable(R.drawable.border_not_selected));
                process_group.performClick();
            }
        });
        processlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processlayout.setBackground( getResources().getDrawable(R.drawable.border_selected));
                procesgrplayout.setBackground( getResources().getDrawable(R.drawable.border_not_selected));
                knlayout.setBackground(getResources().getDrawable(R.drawable.border_not_selected));
                process_name.performClick();
            }
        });
    }


    private void setSpinnersClickListeners() {
        process_group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (setUI) {
                    knowledge_area_tick_img.setVisibility(View.GONE);
                    process_name_tick_img.setVisibility(View.GONE);
                    process_group_tick_img.setVisibility(View.VISIBLE);

                    selectedprocessgroup.setVisibility(View.VISIBLE);
                    selectedprocessgroup.setText(process_group.getSelectedItem().toString());
                    selectedknowledgearea.setVisibility(View.GONE);
                    selectedprocess.setVisibility(View.GONE);


                    active = 1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });

        knowledge_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (setUI) {
                    knowledge_area_tick_img.setVisibility(View.VISIBLE);

                    process_name_tick_img.setVisibility(View.GONE);
                    process_group_tick_img.setVisibility(View.GONE);

                    selectedknowledgearea.setVisibility(View.VISIBLE);
                    selectedknowledgearea.setText(knowledge_area.getSelectedItem().toString());
                    selectedprocessgroup.setVisibility(View.GONE);
                    selectedprocess.setVisibility(View.GONE);
                    active = 2;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });
        process_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (setUI) {
                    knowledge_area_tick_img.setVisibility(View.GONE);

                    process_name_tick_img.setVisibility(View.VISIBLE);
                    process_group_tick_img.setVisibility(View.GONE);

                    selectedprocess.setVisibility(View.VISIBLE);
                    selectedprocess.setText(process_name.getSelectedItem().toString());
                    selectedprocessgroup.setVisibility(View.INVISIBLE);
                    selectedknowledgearea.setVisibility(View.INVISIBLE);
                    active = 3;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });

        difficulty_level.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//                if (difficulty_level.getSelectedItem().toString().equals("Choose Difficulty level")) {
//                    setUI = false;
//                    setdifficulty = 0;
//                }
//
//                if (setUI) {
                    setdifficulty = 1;
//                }
                setUI = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });
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
            dataSource.setKnowledgeArea(c.getString("knowledgearea"));
            dataSource.setProcessGroup(c.getString("processgroup"));
            dataSource.setProcessName(c.getString("processname"));
            list.add(dataSource);
        }
        MainActivity_as.dbInsert(list);
        setDataforspinners();

    }

}
