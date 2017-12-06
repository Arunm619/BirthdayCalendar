package apps.sumitha.birthdaycalendar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Calendar;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import static apps.sumitha.birthdaycalendar.getUserdetails.MY_PREFS_NAME;


public class Addnewuser extends AppCompatActivity {
    public String birthday;
    private Button btn_save;
    private EditText username;
    private EditText dob;
    Date mybirthday;
    public int index;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_addnewuser);
        getSupportActionBar().setTitle("Add new birthday");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        username = findViewById(R.id.edittext_pname);
        dob = findViewById(R.id.edittext_pdob);
        btn_save = findViewById(R.id.btn_psave);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = prefs.edit();

        index = prefs.getInt("index", 0);

        if (index == 0) {
            ++index;
        }
        editor.putInt("index", index);
        editor.apply();


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

                DatePickerDialog mDatePicker = new DatePickerDialog(Addnewuser.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        mybirthday = new GregorianCalendar(selectedyear, selectedmonth, selectedday).getTime();
                        birthday = "" + selectedday + "/" + (selectedmonth + 1) + "/" + selectedyear + "";
                        dob.setText(birthday);
                        // Toast.makeText(Addnewuser.this, ""+birthday+"\n" +
                        //       ""+date.toString(), Toast.LENGTH_SHORT).show();


                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int a = validatefields();

                if (a == -1) {
                    return;
                }
                btn_save.setEnabled(false);


                savefields();
            }
        });


    }

    private void savefields() {
        String usernamecurl = username.getText().toString();

        Date birthdate = mybirthday;

//save

        Person person = new Person(usernamecurl, birthdate);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = prefs.edit();


        Gson gson = new Gson();
        String user_json = gson.toJson(person);
        String id = Integer.toString(index);


        editor.putString(id, user_json);
        ++index;
        editor.putInt("index", index);
        editor.apply();
        View parentLayout = findViewById(R.id.mylforadduser);
        Snackbar.make(parentLayout, "Successfully Added. You will never forget " + usernamecurl + "'s Birthday! ", Snackbar.LENGTH_INDEFINITE)
                .setAction("Yay!", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        takemetoHome();
                    }
                })

                .show();


        //  Toast.makeText(this,"olld->"+ birthday+""+"new ->"+birthdate, Toast.LENGTH_SHORT).show();
    }

    private void takemetoHome() {
        finish();

        startActivity(new Intent(Addnewuser.this, Home.class));

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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        takemetoHome();
    }


    private int validatefields() {
        String usernamecurl = username.getText().toString();
        String dobcurl = dob.getText().toString();
        View parentLayout = findViewById(R.id.mylforadduser);

        if (TextUtils.isEmpty(usernamecurl)) {
            Snackbar.make(parentLayout, "Enter the person's name...", Snackbar.LENGTH_LONG).show();
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


}
