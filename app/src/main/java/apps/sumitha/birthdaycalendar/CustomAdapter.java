package apps.sumitha.birthdaycalendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by root on 21/11/17.
 */


public class CustomAdapter extends ArrayAdapter<Person> implements View.OnClickListener {

    private ArrayList<Person> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtdob;
        TextView txtage;
        TextView txtdaysleftuntil;
        //ImageView bday;
    }

    public CustomAdapter(ArrayList<Person> data, Context context) {
        super(context, R.layout.singlerowitem, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public void onClick(View v) {

        int position = (Integer) v.getTag();
        Object object = getItem(position);
        Person dataModel = (Person) object;

        switch (v.getId()) {
            case R.id.name:
                Snackbar.make(v, "Release date " + dataModel.getname(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                break;
        }
    }

    private int lastPosition = -1;

    @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Person dataModel = getItem(position);
        /* Check if an existing view is being reused, otherwise inflate the view */
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.singlerowitem, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.txtdob = (TextView) convertView.findViewById(R.id.dob);

            viewHolder.txtage = (TextView) convertView.findViewById(R.id.age);
            viewHolder.txtdaysleftuntil = (TextView) convertView.findViewById(R.id.numberofdaysleft);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        lastPosition = position;

        assert dataModel != null;
        viewHolder.txtName.setText(dataModel.getname());
        viewHolder.txtdob.setText(dataModel.getdate());

        viewHolder.txtage.setText("" + (dataModel.getAge()));
        viewHolder.txtdaysleftuntil.setText("" + dataModel.computeDiffSRERAM());

        // Return the completed view to render on screen
        return result;
    }
}
