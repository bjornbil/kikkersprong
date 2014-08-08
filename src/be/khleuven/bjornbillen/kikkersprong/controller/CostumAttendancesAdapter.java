package be.khleuven.bjornbillen.kikkersprong.controller;

import java.util.List;

import com.example.kikkersprong.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CostumAttendancesAdapter extends BaseAdapter {

    Context mContext;
    List<String> mList;

    public CostumAttendancesAdapter (Context context, List<String> list) {
        mList = list;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public String getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    // This method is called to draw each row of the list
    @SuppressLint("ViewHolder")
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // here you inflate the layout you want for the row
        final View view = View.inflate(mContext, R.layout.costum_listview_adminattendance, null);

        // you bind the layout with the content of your list
        // for each element of your list of notes, the adapter will create a row and affect the right title
        final TextView date= (TextView) view.findViewById(R.id.datelistview);
        final TextView aantaluur = (TextView) view.findViewById(R.id.aantaluurlistview);
        final TextView name= (TextView) view.findViewById(R.id.attendancename);
        date.setText(mList.get(position).split(" ")[0]);
        aantaluur.setText(mList.get(position).split(" ")[1]);
        name.setText(mList.get(position).split(" ")[2] + " " + mList.get(position).split(" ")[3]);

        return view;
    }
}
