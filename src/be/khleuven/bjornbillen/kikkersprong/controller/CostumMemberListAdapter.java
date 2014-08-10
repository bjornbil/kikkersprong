package be.khleuven.bjornbillen.kikkersprong.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import com.example.kikkersprong.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class CostumMemberListAdapter extends BaseAdapter {

	Context mContext;
	List<String> mList;
	

	public CostumMemberListAdapter(Context context, List<String> list) {
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
	public View getView(int position, View convertView, final ViewGroup parent) {

		// here you inflate the layout you want for the row
		final View view = View.inflate(mContext,
				R.layout.costum_listview_members, null);

		// you bind the layout with the content of your list
		// for each element of your list of notes, the adapter will create a row
		// and affect the right title
		final TextView naam = (TextView) view.findViewById(R.id.viewmembername);
		final TextView gebdatum = (TextView) view
				.findViewById(R.id.viewmemberdob);
		final ImageView present = (ImageView) view
				.findViewById(R.id.viewmembercheckin);
		final ImageView foto = (ImageView) view
				.findViewById(R.id.viewmemberfoto);
		final TextView textid = (TextView) view.findViewById(R.id.viewmemberid);
		final ImageButton go = (ImageButton) view
				.findViewById(R.id.gomemberpage);
		final ImageButton goqr = (ImageButton) view.findViewById(R.id.goqr);

		naam.setText(mList.get(position).split(" ")[0] + " "
				+ mList.get(position).split(" ")[1]);
		textid.setText(mList.get(position).split(" ")[2]);
		
		gebdatum.setText(mList.get(position).split(" ")[3]);
		if (mList.get(position).split(" ")[4].equals("true")) {
			present.setImageResource(R.drawable.ic_ispaid);
		} else {
			present.setImageResource(R.drawable.ic_offline);
		}
		if (mList.get(position).split(" ")[5] != null && mList.get(position).split(" ")[5].contains("http")) {
			new DownloadImageTask(foto).execute(mList.get(position).split(" ")[5]);
		}
		else {
			foto.setImageResource(R.drawable.ic_nopic);
		}
		final int memberid = Integer.parseInt(textid.getText().toString());
		textid.setText(" ");
		final String membername = naam.getText().toString();
		go.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(view.getContext(), MemberActivity.class);
				Log.d("idcostum","id costum : " + memberid);
				i.putExtra("id", memberid);
				i.putExtra("name",membername);
				parent.getContext().startActivity(i);
				((Activity) parent.getContext()).finish();

			}

		});
		goqr.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i2 = new Intent(
						Intent.ACTION_VIEW,
						Uri.parse("http://chart.apis.google.com/chart?chs=200x200&cht=qr&chld=|1&chl=id%3D"
								+ memberid
								+ "%20name%3D"
								+ membername.split(" ")[0]
								+ "%20" + membername.split(" ")[1]));
				parent.getContext().startActivity(i2);
			}
		});
		
		return view;
	}
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		  ImageView bmImage;

		  public DownloadImageTask(ImageView bmImage) {
		      this.bmImage = bmImage;
		  }

		  protected Bitmap doInBackground(String... urls) {
		      String urldisplay = urls[0];
		      Bitmap mIcon11 = null;
		      try {
		        InputStream in = new java.net.URL(urldisplay).openStream();
		        mIcon11 = BitmapFactory.decodeStream(in);
		      } catch (Exception e) {
		          Log.e("Error", e.getMessage());
		          e.printStackTrace();
		      }
		      return mIcon11;
		  }

		  protected void onPostExecute(Bitmap result) {
		      bmImage.setImageBitmap(result);
		  }
		}
	
}
