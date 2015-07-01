package technibits.com.pme.alarmactivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import technibits.com.pme.R;
import technibits.com.pme.alarmmodel.Alarm;
import technibits.com.pme.alarmmodel.AlarmMsg;

/**
 * @author appsrox.com
 */
public class AlarmReceiver extends BroadcastReceiver {

//	private static final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        long alarmMsgId = intent.getLongExtra(AlarmMsg.COL_ID, -1);
        long alarmId = intent.getLongExtra(AlarmMsg.COL_ALARMID, -1);

        AlarmMsg alarmMsg = new AlarmMsg(alarmMsgId);
        alarmMsg.setStatus(AlarmMsg.EXPIRED);
        alarmMsg.persist(RemindMe.db);

        Alarm alarm = new Alarm(alarmId);
        alarm.load(RemindMe.db);

        Notification n = new Notification(R.mipmap.app_logo, alarm.getName(), System.currentTimeMillis());
        PendingIntent pi = PendingIntent.getActivity(context, 0, new Intent(), 0);

        n.setLatestEventInfo(context, "Remind Me", alarm.getName(), pi);
        if (RemindMe.isVibrate()) {
            n.defaults |= Notification.DEFAULT_VIBRATE;
        }
        if (alarm.getSound()) {
            n.sound = Uri.parse(RemindMe.getRingtone());
//			n.defaults |= Notification.DEFAULT_SOUND;
        }
        n.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify((int) alarmMsgId, n);
    }

}
