package apps.sumitha.birthdaycalendar;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;


public class Person {
    private String name;
    private Date dob;

    public Person() {

    }

    Person(String name, Date dob1) {
        this.name = name;
        this.dob = dob1;
    }

    Date getDateindate() {
        return dob;
    }

    String getdate() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(dob);
    }

    String getname() {
        return name;
    }


    int computeDiffSRERAM() {

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


    int getAge() {
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



