package technibits.com.pme.adapter;

/**
 * Created by technibitsuser on 6/16/2015.
 */

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

import technibits.com.pme.R;
import technibits.com.pme.alarmactivity.AlarmService;
import technibits.com.pme.alarmactivity.RemindMe;
import technibits.com.pme.alarmmodel.Alarm;
import technibits.com.pme.alarmmodel.AlarmMsg;
import technibits.com.pme.data.ScheduleInfo;


public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.ContactViewHolder> {


    private List<ScheduleInfo> contactList;
    private static final int TYPE_HEADER = 0; // Declaring Variable to Understand which View is being worked on
    // IF the viaew under inflation and population is header or Item
    private static final int TYPE_ITEM = 1;


    public ScheduleListAdapter(List<ScheduleInfo> contactList) {
        this.contactList = contactList;
    }


    @Override
    public int getItemCount() {
        return contactList.size();
    }


    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        ScheduleInfo si = contactList.get(i);
        contactViewHolder.Schedule_name.setText(si.getName());
        contactViewHolder.time.setText(si.getTime());
        contactViewHolder.date.setText(si.getDate());
        contactViewHolder.month.setText(si.getMonth());
        contactViewHolder.year.setText(si.getYear());
        int dd = si.getId();
        String str = null;
        str = str.valueOf(dd);
        contactViewHolder.alarmid.setText(str);

        boolean state = si.isNotification_state();
        if (state) {
            contactViewHolder.notification.setImageResource(R.drawable.bell_ring);
        } else {
            contactViewHolder.notification.setImageResource(R.drawable.bell_off);
        }


    }


    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_layout, viewGroup, false);


        return new ContactViewHolder(itemView, i, viewGroup.getContext());
    }


    public static class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        protected TextView Schedule_name;
        protected TextView time;
        protected ImageView notification;
        protected TextView date;
        protected TextView alarmid;
        protected TextView month;
        protected TextView year;
        protected Context context;
        int Holderid;

        public ContactViewHolder(View v, int ViewType, Context c) {
            super(v);
            context = c;
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
            Schedule_name = (TextView) v.findViewById(R.id.Schedule_name);
            time = (TextView) v.findViewById(R.id.time_tv);
            date = (TextView) v.findViewById(R.id.date_tv);
            month = (TextView) v.findViewById(R.id.month_tv);
            year = (TextView) v.findViewById(R.id.year_tv);
            alarmid = (TextView) v.findViewById(R.id.alarmid);
            notification = (ImageView) v.findViewById(R.id.notification);
            ImageView overflow = (ImageView) v.findViewById(R.id.overflow);
            overflow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(context, "The overflow Clicked is: " + Schedule_name.getText(), Toast.LENGTH_SHORT).show();
                    showPopupMenu(view, context);
                }
            });
            if (ViewType == TYPE_ITEM) {
  //                textView = (TextView) itemView.findViewById(R.id.rowText); // Creating TextView object with the id of textView from item_row.xml
  //                imageView = (ImageView) itemView.findViewById(R.id.rowIcon);// Creating ImageView object with the id of ImageView from item_row.xmHolderid = 1;// setting holder id as 1 as the object being populated are of type item row
            }
        }

        private void showPopupMenu(View view, Context c) {
            final AlarmMsg alarmMsg = new AlarmMsg();
            final Alarm alarm = new Alarm();
            final SQLiteDatabase db;
            db = RemindMe.db;

            PopupMenu popupMenu = new PopupMenu(c, view);
            popupMenu.getMenuInflater().inflate(R.menu.context_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    boolean refresh = false;
                    Toast.makeText(context, item.toString(), Toast.LENGTH_LONG).show();
                    String id_txt = alarmid.getText().toString();
                    long id = Long.parseLong(id_txt);
                    switch (item.getItemId()) {
//                        case R.id.menu_edit:
//                            alarmMsg.setId(id);
//                            alarmMsg.load(db);
//                            alarm.reset();
//                            alarm.setId(alarmMsg.getAlarmId());
//                            alarm.load(db);
//
////                            context.showDialog(R.id.menu_edit);
//                            break;

                        case R.id.menu_delete:
                            RemindMe.dbHelper.cancelNotification(db, id, false);
                            refresh = true;

                            Intent cancelThis = new Intent(context, AlarmService.class);
                            cancelThis.putExtra(AlarmMsg.COL_ID, id);
                            cancelThis.setAction(AlarmService.CANCEL);
                            context.startService(cancelThis);
                            break;

                        case R.id.menu_delete_repeating:
                            alarmMsg.setId(id);
                            alarmMsg.load(db);
                            RemindMe.dbHelper.cancelNotification(db, alarmMsg.getAlarmId(), true);
                            refresh = true;

                            Intent cancelRepeating = new Intent(context, AlarmService.class);
                            cancelRepeating.putExtra(AlarmMsg.COL_ALARMID, String.valueOf(alarmMsg.getAlarmId()));
                            cancelRepeating.setAction(AlarmService.CANCEL);
                            context.startService(cancelRepeating);
                            break;
                    }
                    return true;
                }
            });

            popupMenu.show();

        }



        @Override
        public void onClick(View view) {

            Toast.makeText(context, "The Item Clicked is: " + Schedule_name.getText(), Toast.LENGTH_SHORT).show();
        }

    }
}