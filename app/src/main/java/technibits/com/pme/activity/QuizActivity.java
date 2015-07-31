package technibits.com.pme.activity;


import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;

import technibits.com.pme.R;
import technibits.com.pme.alarmactivity.RemindMe;

public class QuizActivity extends AppCompatActivity {

    String knArea;
    String prGroup;
    String prName;
    String prMode;
    String mode;
    String type;
    String choosedvalue;
    int difficulty;
    private Toolbar mToolbar;
    public ExamFragment examfragment;
    public StudyModeFragment studyfragment;
    public Fragment frag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_activity);
//        mToolbar = (Toolbar) findViewById(R.id.toolbar);

//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        mToolbar.setTitle("testfromquizactivity");
        Bundle bundle = getIntent().getExtras();

//        knArea = bundle.getString("knArea");
//        prGroup = bundle.getString("prGroup");
//        prName = bundle.getString("prName");
//        prMode = bundle.getString("prMode");

        mode = bundle.getString("mode");
        type = bundle.getString("type");
        choosedvalue = bundle.getString("choosedvalue");
        difficulty = bundle.getInt("difficulty");

        String mailid= RemindMe.returnMail();
//        String url = "http://www.jmbok.techtestbox.com/and/all.php?knowledgearea=" + knArea + "&group=" + prGroup + "&processname=" + prName + "&difficulty=" + prMode + "&mailid="+mailid;
        String url = "http://jmbok.techtestbox.com/and/get_questions.php?type="+type+"&value="+choosedvalue+"&difficulty="+difficulty+"&mailid="+mailid+"";
        url = url.replace(" ", "");
        System.out.println("   " + url);

//		Bundle args = new Bundle();
//		args.putInt("schedule_status", 0);
//		fragment.setArguments(args);
        if (mode.equals("exam")) {
            examfragment = new ExamFragment(url);
            FragmentManager fragmentManager = getFragmentManager();
            android.app.FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.container, examfragment)
                    .addToBackStack(null).commit();
            frag=examfragment;
        } else {
             studyfragment = new StudyModeFragment(url);
            FragmentManager fragmentManager = getFragmentManager();
            android.app.FragmentTransaction ft = fragmentManager
                    .beginTransaction();
            ft.replace(R.id.container, studyfragment)
                    .addToBackStack(null).commit();
            frag=studyfragment;
        }
    }

    // 2.0 and above
    @Override
    public void onBackPressed() {

        quit();
    }

    public void quit() {
        new AlertDialog.Builder(this)
                .setTitle("Message")
                .setMessage("Are you sure you want to quit this test?")

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


}
