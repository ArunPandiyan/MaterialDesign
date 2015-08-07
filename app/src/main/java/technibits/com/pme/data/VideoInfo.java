package technibits.com.pme.data;

import android.widget.TextView;

/**
 * Created by technibitsuser on 8/7/2015.
 */
public class VideoInfo {
    private String  title;
    private String  about_video;
    private String  thumbnail;
    private String  length;
    private String context;
    private String video_url;

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbout_video() {
        return about_video;
    }

    public void setAbout_video(String about_video) {
        this.about_video = about_video;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
