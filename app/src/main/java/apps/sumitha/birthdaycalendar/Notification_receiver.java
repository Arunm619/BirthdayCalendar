package apps.sumitha.birthdaycalendar;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.DataFormatException;

import static android.content.Context.MODE_PRIVATE;
import static apps.sumitha.birthdaycalendar.getUserdetails.MY_PREFS_NAME;

/**
 * Created by root on 22/11/17.
 */

public class Notification_receiver extends BroadcastReceiver {
    SharedPreferences prefs;

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent repeatingintent = new Intent(context, Home.class);
        repeatingintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                100, repeatingintent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        int index = prefs.getInt("index", 0);
        if (index == 0) {
            return;
        }

        ArrayList<String> birthdayguys = new ArrayList<>();
        for (int i = 1; i < index; i++) {
            Gson gson = new Gson();
            String user_json = prefs.getString(Integer.toString(i), "");
            Person person = gson.fromJson(user_json, Person.class);
            Date date = person.getDateindate();
            if (issameday(date))
                birthdayguys.add(person.getname());

        }


        for (int i = 0; i < birthdayguys.size(); i++) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.cake)
                    .setContentTitle(birthdayguys.get(i) + "'s Birthday Today!")
                    .setContentText("Don't forget to wish good luck!")
                    .setSound(soundUri)
                    .setAutoCancel(true);
            assert notificationManager != null;
            notificationManager.notify(i, builder.build());

        }

    }


    boolean issameday(Date d1) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(d1);

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }
}