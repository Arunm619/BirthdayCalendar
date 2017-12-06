package apps.sumitha.birthdaycalendar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static apps.sumitha.birthdaycalendar.getUserdetails.MY_PREFS_NAME;
import static java.lang.Math.abs;
import static java.lang.Math.negateExact;

public class Home extends AppCompatActivity {
    public FloatingActionButton fab;

    public CardView totalcard;
    public Button btn_addfriend;
    ProgressDialog mProgress;
    public int index;
    ArrayList<Person> personArrayList;
    ListView listView;
    CardView cvlistview;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        // getActionBar().setDisplayHomeAsUpEnabled(true);
        // HashMap<String, String> hash = null;
        getSupportActionBar().setTitle("Home");
        listView = findViewById(R.id.list);
        totalcard = findViewById(R.id.cardnosaved);
        fab = findViewById(R.id.fab);

        cvlistview = findViewById(R.id.cvlistview);
        btn_addfriend = findViewById(R.id.adduserbtn);


        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        index = prefs.getInt("index", 0);
        if (index == 0) {
            totalcard.setVisibility(View.VISIBLE);

            listView.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
            cvlistview.setVisibility(View.GONE);
        }
        mProgress = new ProgressDialog(Home.this);

        mProgress.setMessage("Loading birthdays");
        mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgress.setIndeterminate(true);
        mProgress.setProgress(0);
        mProgress.setCancelable(false);

        new TestAsync().execute();


        btn_addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Addnewuser.class);
                startActivity(intent);
                finish();

            }
        });

        //  View parentLayout = findViewById(R.id.home);
        // Date firstDate1 = new Date(2011, 10, 22);
        //Snackbar.make(parentLayout, "days until=" + getdays(firstDate1) + ":", Snackbar.LENGTH_LONG).show();


  /*
       String name= prefs.getString("Username","");
     String dob =  prefs.getString("Userdob", "");

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Snackbar.make(parentLayout, "index=" + index + ":" + name + (dateFormat.format(parsedate(dob))), Snackbar.LENGTH_LONG).show();
*/


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(Home.this, Addnewuser.class);
                startActivity(intent);
                finish();
            }
        });
    }


    @SuppressLint("StaticFieldLeak")
    class TestAsync extends AsyncTask<Void, Integer, String> {
        //  String TAG = getClass().getSimpleName();

        protected void onPreExecute() {
            mProgress.show();


            //    Log.d(TAG + " PreExceute","On pre Exceute......");
        }

        protected String doInBackground(Void... arg0) {
//        Log.d(TAG + " DoINBackGround","On doInBackground...");
            personArrayList = new ArrayList<>();

            for (int i = 1; i < index; i++) {
                Gson gson = new Gson();
                String user_json = prefs.getString(Integer.toString(i), "");
                Person person = gson.fromJson(user_json, Person.class);
                //Snackbar.make(parentLayout, "index=" + index + ":" + person.getname() + person.getdate(), Snackbar.LENGTH_LONG).show();
                personArrayList.add(person);
            }

            return "You are at PostExecute";
        }

        protected void onProgressUpdate(Integer... a) {
            super.onProgressUpdate(a);
            //      Log.d(TAG + " onProgressUpdate", "You are in progress update ... " + a[0]);
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            CustomAdapter adapter = new CustomAdapter(personArrayList, getApplicationContext());

            adapter.notifyDataSetChanged();

            listView.setAdapter(adapter);
            mProgress.dismiss();
            // Log.d(TAG + " onPostExecute", "" + result);
        }
    }

    @Override
    public void onDestroy() {
        // Remove adapter refference from list
        listView.setAdapter(null);
        super.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                alert();
                break;
            case R.id.mydetails:
                Intent aboutIntent = new Intent(Home.this, Setting.class);
                //  Toast.makeText(this, "mydetails clicked", Toast.LENGTH_SHORT).show();

                startActivity(aboutIntent);

                //case R.id.exitMenuItem:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void alert() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(Home.this);
        builder1.setTitle(" Birthday Calendar");
        builder1.setMessage(" Developed By Sumitha" +
                "\n BCA III year \n" +
                " Version - 1.0 \n" +
                " GNU General Public License v3");

        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "okay",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
