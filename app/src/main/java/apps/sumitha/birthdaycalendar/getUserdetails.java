package apps.sumitha.birthdaycalendar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import java.util.Calendar;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class getUserdetails extends AppCompatActivity {
    private Button btn_save;
    private EditText username;
    private EditText dob;

    public static final String MY_PREFS_NAME = "MyDB";
    public String birthday = null;
    SharedPreferences.Editor editor;
    Date mybirthday;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_userdetails);

        getSupportActionBar().setTitle("Home");
      //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String restoredText = prefs.getString("Username", null);
        if (restoredText != null) {
            startActivity(new Intent(this, Home.class));
            finish();
        }

        username = findViewById(R.id.edittext_username);
        dob = findViewById(R.id.edittext_dob);
        btn_save = findViewById(R.id.btn_save);

        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int a = validatefields();

                if (a == -1) {
                    return;
                }
                savefields();
                finish();
                startActivity(new Intent(getUserdetails.this, Home.class));
            }
        });


        dob.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("InlinedApi")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //To show current date in the datepicker
                @SuppressLint({"NewApi", "LocalSuppress"}) final Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(getUserdetails.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        mybirthday = new GregorianCalendar(selectedyear, selectedmonth, selectedday).getTime();

                        birthday = "" + selectedday + "/" + (selectedmonth + 1) + "/" + selectedyear + "";

                        dob.setText(birthday);


                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });


    }

    private Date parsedate(String birthday) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Date Date = null;
        try {
            Date = df.parse(birthday);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Date;
    }

    private int validatefields() {
        String usernamecurl = username.getText().toString();
        View parentLayout = findViewById(R.id.mylayout);

        String dobcurl = dob.getText().toString();


        if (TextUtils.isEmpty(usernamecurl)) {
            Snackbar.make(parentLayout, "Enter your name...", Snackbar.LENGTH_LONG).show();
            return -1;
        }

        if (TextUtils.isEmpty(dobcurl)) {
            Snackbar.make(parentLayout, "Select Date of Birth", Snackbar.LENGTH_LONG).show();
            return -1;
        }

        if (validatedate(mybirthday)) {
            Snackbar.make(parentLayout, "Not Possible ", Snackbar.LENGTH_LONG).show();

            return -1;
        }


        return 1;

    }

    private boolean validatedate(Date date) {


        assert date != null;
        if (date.before(new Date())) {
            return false;

        } else
            return true;

    }


    private void savefields() {
        String usernamecurl = username.getText().toString();
      //  Date birthdate = parsedate(birthday);

        editor.putString("Username", usernamecurl);
        editor.putString("Userdob", birthday);
        editor.apply();

    }


}
