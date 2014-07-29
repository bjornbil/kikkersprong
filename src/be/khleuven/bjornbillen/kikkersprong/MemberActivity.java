package be.khleuven.bjornbillen.kikkersprong;

import com.example.kikkersprong.R;
import com.example.kikkersprong.R.layout;
import com.example.kikkersprong.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MemberActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.member, menu);
		return true;
	}

}
