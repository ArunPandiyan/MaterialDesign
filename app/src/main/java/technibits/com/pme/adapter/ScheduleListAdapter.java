package technibits.com.pme.adapter;

/**
 * Created by technibitsuser on 6/16/2015.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

import technibits.com.pme.R;
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
        contactViewHolder.textView7.setText(si.getDate());
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
        protected TextView textView7;
        protected ImageView notification;
        protected Context context;
        int Holderid;

        public ContactViewHolder(View v, int ViewType, Context c) {
            super(v);
            context = c;
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
            Schedule_name = (TextView) v.findViewById(R.id.Schedule_name);
            textView7 = (TextView) v.findViewById(R.id.textView7);
            notification = (ImageView) v.findViewById(R.id.notification);
            if (ViewType == TYPE_ITEM) {
                //                textView = (TextView) itemView.findViewById(R.id.rowText); // Creating TextView object with the id of textView from item_row.xml
                //                imageView = (ImageView) itemView.findViewById(R.id.rowIcon);// Creating ImageView object with the id of ImageView from item_row.xmHolderid = 1;// setting holder id as 1 as the object being populated are of type item row
            }
        }

        @Override
        public void onClick(View view) {

            Toast.makeText(context, "The Item Clicked is: " + Schedule_name.getText(), Toast.LENGTH_SHORT).show();
        }
    }
}