package com.hamzahrmalik.smstimeappend;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

public class MainActivity extends Activity {

	SharedPreferences pref;
	CheckBox CBuse24h, CBuseSeparator;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		pref = getSharedPreferences(Keys.PREF, Context.MODE_WORLD_READABLE);

		CBuse24h = (CheckBox) findViewById(R.id.option_24h);
		CBuseSeparator = (CheckBox) findViewById(R.id.show_separator);

		CBuse24h.setChecked(pref.getBoolean(Keys.USE_24H, false));
		CBuseSeparator.setChecked(pref.getBoolean(Keys.USE_SEPARATOR, true));
	}

	public void save(View v) {
		Editor editor = pref.edit();

		editor.putBoolean(Keys.USE_24H, CBuse24h.isChecked());
		editor.putBoolean(Keys.USE_SEPARATOR, CBuseSeparator.isChecked());

		editor.apply();
		Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show();
		finish();
	}
}
