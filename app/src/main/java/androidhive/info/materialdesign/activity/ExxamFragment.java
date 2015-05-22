package androidhive.info.materialdesign.activity;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import androidhive.info.materialdesign.R;


public class ExxamFragment extends Fragment {
    private View rootView;
    private TextView time;
    private Button button_time;
    private Button button_date;


    public ExxamFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_exams, container, false);
        time=(TextView)rootView.findViewById(R.id.time_view);

        button_time=(Button)rootView.findViewById(R.id.btn_time);
        button_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(view);
            }
        });
        Button open=(Button)rootView.findViewById(R.id.open);
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(getActivity(), MainActivity_as.class);
                intent.putExtra("mode", "study");
                startActivity(intent);
            }
        });

        button_date=(Button)rootView.findViewById(R.id.btn_date);
        button_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    private void showDatePickerDialog(View view) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

//        DatePickerDialog
        DatePickerDialog time=new DatePickerDialog(getActivity(),ondate, year,month,day);
        time.show();

        time.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_NEGATIVE) {
                    TextView time = (TextView)rootView.findViewById(R.id.time_view);
                    time.setText("No time");
                }
            }
        });
// Create a new instance of DatePickerDialog and return it
//        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    private void showTimePickerDialog(View view) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.AM_PM);
        int minute = c.get(Calendar.MINUTE);
        boolean is24hour=false;

        TimePickerDialog time=new TimePickerDialog(getActivity(),ontime, hour,minute,is24hour);
        time.show();

        time.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_NEGATIVE) {
                    TextView time = (TextView)rootView.findViewById(R.id.time_view);
                    time.setText("No time");
                }
            }
        });
    }


    TimePickerDialog.OnTimeSetListener ontime=new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
//        TextView time = (TextView)rootView.findViewById(R.id.time_view);
//        System.out.println("time is "+hourOfDay +": "+minute);
            //time.setText(hourOfDay +" : "+minute);
            updateTime(hourOfDay,minute);
        }
    };

    DatePickerDialog.OnDateSetListener ondate= new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

        }
    };
    private void updateTime(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        time.setText(aTime);
    }
}
