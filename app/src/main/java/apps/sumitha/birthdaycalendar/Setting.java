package apps.sumitha.birthdaycalendar;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import java.nio.file.Files;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import static apps.sumitha.birthdaycalendar.getUserdetails.MY_PREFS_NAME;

public class Setting extends AppCompatActivity {
    public Button btn_pushnotifications, btn_wishme;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    public ImageButton ibsettings;

    public TextView tv_name, tv_dob;
    public int hour, min, sec;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_setting);
        final View parentLayout = findViewById(R.id.settings);

        btn_pushnotifications = findViewById(R.id.pushnotifications);

        ibsettings = findViewById(R.id.setnotificationtime);
        btn_pushnotifications.setEnabled(true);
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

        btn_wishme = findViewById(R.id.btnformybday);

        final String name = prefs.getString("Username", "");
        final String dob = prefs.getString("Userdob", "");
        tv_name = findViewById(R.id.Username);
        tv_dob = findViewById(R.id.tvdob);

        // Toast.makeText(this, ""+name+dob, Toast.LENGTH_SHORT).show();
        tv_name.setText(name);
        tv_dob.setText(dob);


        btn_pushnotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifcation();

                Snackbar.make(parentLayout, "Turned On Successfully.", Snackbar.LENGTH_LONG).show();
                btn_pushnotifications.setEnabled(false);
            }
        });

        ibsettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar mcurrentTime = Calendar.getInstance();
                final int hours = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Setting.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        hour = selectedHour;
                        min = selectedMinute;
                        editor.putInt("hour", hour);
                        editor.putInt("min", min);
                        editor.apply();

                        Snackbar.make(parentLayout, "Sucessfuly saved.", Snackbar.LENGTH_LONG).show();

                    }
                }, hours, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();


            }
        });
        btn_wishme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int age = 0, days = 0;

                Date a = parsedate(dob);
                //Toast.makeText(Setting.this, ""+dob +"\n"+a.toString(), Toast.LENGTH_SHORT).show();
                age = getAge(a);
                days = computeDiffSRERAM(a);

                if (days == 0) {

                    Snackbar.make(parentLayout, "Happy birthday ," + name, Snackbar.LENGTH_LONG).show();

                } else {
                    Snackbar.make(parentLayout, "Your " + age + " birthday is " + days + " days from now", Snackbar.LENGTH_LONG).show();

                }
            }
        });

    }


    private Date parsedate(String birthday) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date Date = null;
        try {
            Date = df.parse(birthday);
            // Toast.makeText(this, ""+Date.toString(), Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Date;
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        takemetoHome();
    }


    private void takemetoHome() {
        finish();

        startActivity(new Intent(Setting.this, Home.class));

    }


    private void notifcation() {

        int hour = prefs.getInt("hour", 8);
        int min = prefs.getInt("min", 0);


        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(getApplicationContext(), Notification_receiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()
                , AlarmManager.INTERVAL_DAY, pendingIntent);


    }


    int computeDiffSRERAM(Date dob) {

        Calendar today = Calendar.getInstance();


//birthday for this year......
        Calendar cal = Calendar.getInstance();

        cal.setTime(dob);


        // int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int thisyear = today.get(Calendar.YEAR);


//Setting Up final birthday
        Calendar bday = new GregorianCalendar();
        //  bday.setTime(dob);
        bday.set(thisyear, month, day);

        if (bday.compareTo(today) < 0) {
            bday.set(thisyear + 1, month, day);
        }


        int a = bday.compareTo(today);

        if (a != 0) {
            long msDiff = bday.getTimeInMillis() - today.getTimeInMillis();
            Long daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff);
            return daysDiff.intValue();
        } else {
            //today is your birthday
            return 0;

        }

    }


    int getAge(Date dob) {
        Date date = dob;
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        Calendar d = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        d.set(year, month, day);

        int age = today.get(Calendar.YEAR) - d.get(Calendar.YEAR) + 1;

        if (today.get(Calendar.DAY_OF_YEAR) <= d.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        return age;


    }


}
