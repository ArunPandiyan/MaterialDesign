package technibits.com.pme.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import technibits.com.pme.R;
import technibits.com.pme.activity.WebviewActivity;
import technibits.com.pme.data.VideoInfo;


/**
 * Created by technibitsuser on 8/7/2015.
 */
public class VideoListAdapter  extends RecyclerView.Adapter<VideoListAdapter.VideoViewHolder> {
    private List<VideoInfo> videoList;
    private static final int TYPE_ITEM = 1;
    public VideoInfo vi;

    public VideoListAdapter(List<VideoInfo> result) {
        videoList=result;
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }
    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.video_card_layout, viewGroup, false);


        return new VideoViewHolder(itemView, i, viewGroup.getContext());
    }

    @Override
    public void onBindViewHolder(VideoViewHolder videoViewHolder, int position) {
         vi=videoList.get(position);

        videoViewHolder.title.setText(vi.getTitle());
        videoViewHolder.about_video.setText(vi.getAbout_video());
        Glide.with(videoViewHolder.context).load(vi.getThumbnail()).into(videoViewHolder.thumbnail);
        videoViewHolder.length.setText(vi.getLength());
        videoViewHolder.video_url.setText(vi.getVideo_url());




    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        protected TextView title;
        protected TextView about_video;
        protected ImageView thumbnail;
        protected TextView length;
        protected TextView video_url;
        protected Context context;

        int Holderid;

        public VideoViewHolder(View v, int ViewType, Context c) {
            super(v);
            context = c;
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
            title = (TextView) v.findViewById(R.id.title);
            about_video = (TextView) v.findViewById(R.id.about_video);
            length = (TextView) v.findViewById(R.id.length);
            video_url = (TextView) v.findViewById(R.id.video_url);
            thumbnail = (ImageView) v.findViewById(R.id.thumbnail);
//            ImageView overflow = (ImageView) v.findViewById(R.id.overflow);
//            overflow.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
////                    Toast.makeText(context, "The overflow Clicked is: " + Schedule_name.getText(), Toast.LENGTH_SHORT).show();
//                    showPopupMenu(view, context);
//                }
//            });
//            if (ViewType == TYPE_ITEM) {
//                //                textView = (TextView) itemView.findViewById(R.id.rowText); // Creating TextView object with the id of textView from item_row.xml
//                //                imageView = (ImageView) itemView.findViewById(R.id.rowIcon);// Creating ImageView object with the id of ImageView from item_row.xmHolderid = 1;// setting holder id as 1 as the object being populated are of type item row
//            }
        }




        @Override
        public void onClick(View view) {

            Toast.makeText(context, "The Item Clicked is: " + video_url.getText().toString(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, WebviewActivity.class);
            Bundle bundleObject = new Bundle();
//            bundleObject.putSerializable("data", data);
            intent.putExtra("type", video_url.getText().toString());
            context.startActivity(intent);
        }

    }

}
