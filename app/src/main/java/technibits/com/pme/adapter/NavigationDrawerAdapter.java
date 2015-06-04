package technibits.com.pme.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import technibits.com.pme.R;
import technibits.com.pme.model.NavDrawerItem;


/**
 * Created by Technibits-13 on 15-May-2015.
 */
public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {
    List<NavDrawerItem> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    //    String[] mTestArray;
    TypedArray imgs;


    public NavigationDrawerAdapter(Context context, List<NavDrawerItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        mTestArray= context.getResources().getStringArray(R.array.nav_drawer_icons);
        imgs = context.getResources().obtainTypedArray(R.array.nav_drawer_icons);
        View view = inflater.inflate(R.layout.nav_drawer_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NavDrawerItem current = data.get(position);
        holder.title.setText(current.getTitle());
//        holder.img_icon.setImageResource(R.drawable.ic_home);
        holder.img_icon.setImageResource(imgs.getResourceId(position, -1));
//        System.out.println(mTestArray[position]);
//        holder.img_icon.setImageBitmap(mTestArray[position]);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView img_icon;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            img_icon = (ImageView) itemView.findViewById(R.id.icon);
        }
    }
}
