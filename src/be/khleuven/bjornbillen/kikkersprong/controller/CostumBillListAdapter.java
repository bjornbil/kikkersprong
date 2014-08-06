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

class CustomBillListAdapter extends BaseAdapter {

    Context mContext;
    List<String> mList;

    public CustomBillListAdapter (Context context, List<String> list) {
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
        final View view = View.inflate(mContext, R.layout.costum_listview_bills, null);

        // you bind the layout with the content of your list
        // for each element of your list of notes, the adapter will create a row and affect the right title
        final TextView date= (TextView) view.findViewById(R.id.paydatelistview);
        final TextView hours= (TextView) view.findViewById(R.id.prijslistview);
        final ImageView imgview = (ImageView) view.findViewById(R.id.paystatus);
        
        date.setText(mList.get(position).split(" ")[0]);
        hours.setText(mList.get(position).split(" ")[1]);
        String betaald = mList.get(position).split(" ")[2];
        Log.d("ok",betaald);
        if (betaald.equals("true")){
        imgview.setImageResource(R.drawable.ic_ispaid);
        }
        else{
        imgview.setImageResource(R.drawable.ic_paystatus);
        }

        return view;
    }
}
